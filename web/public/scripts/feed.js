var firebase = app_fireBase
var user = firebase.auth().currentUser;
var database = firebase.database();
var storageRef = firebase.storage();

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        
        const userName = user.displayName.toString().replace(/\s/g, '');
        var appendPosts = document.getElementById("populate_feed");
        var emptyLine = document.createElement('br');

        database.ref('/bucket_list_items').orderByChild('timestamp').once('value').then(function(snapshot){
            snapshot.forEach(function(childNodes){

                if(childNodes.val().hasPhoto){
                    
                    var img = document.createElement('img');
                    
                    refPath = storageRef.ref(childNodes.val().originalCreator + '/' + childNodes.val().key + '.jpg');
                    (function(pid) {
                        refPath.getDownloadURL().then(function(url) {
                            
                            img.src = url;
                            img.style.width = '50%';
                            img.style.height = '50%';
                            img.id = "bucket-img";
                            img.style.marginTop = '2px';
                            img.style.marginBottom = '5px';
                            img.style.marginLeft = '5px';
                            img.style.marginRight = '5px';

                        }).catch(function(error) {
                            console.log(error);
                        });
                    })();

                    appendPosts.appendChild(img);
                }

                var post = document.createElement('div');
                post.className = "feed_post";
                var str = "Date:" + childNodes.val().date + "<br/>" + "Title: " + childNodes.val().title + "<br/>" + 
                "Description: " + childNodes.val().description + "<br/>" + "Creator: " + childNodes.val().originalCreator;
                post.innerHTML = str;
                appendPosts.appendChild(post);

                if(childNodes.val().originalCreator != userName){
                    
                    var btnBuck = document.createElement("BUTTON"); 
                    btnBuck.innerHTML = "Buck It!";
                    btnBuck.addEventListener('click',function(){
                        addToList(childNodes.val(),user);
                    })

                    appendPosts.appendChild(btnBuck);
                
                }else{

                    var btnComplete = document.createElement("BUTTON");
                    btnComplete.innerHTML = "Complete!"
                    btnComplete.addEventListener('click',function(){
                        markComplete(childNodes.val());
                    })

                    appendPosts.appendChild(btnComplete);
                }

                var post = document.createElement('p');
                post.className = "feed_post";
                appendPosts.appendChild(post);
            })
        });
    
    }else{
        uid=null;
        window.location.replace("login.html");
    }
}) 

function addToList(list, user){

    console.log(list);
    console.log(list.date);

    var postToBucketListKey = firebase.database().ref().child('bucket_list_items').push().key;
    var postToUserKey = firebase.database().ref().child('users/' + user.uid + '/personal_list').push().key;

    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();
    today = yyyy + '-' + mm + '-' + dd;

    var postToBucketList = {
        title: list.title,
        originalCreator: list.originalCreator,
        postCreator: user.uid,
        date : today,
        description: list.description,
        score: 0,
        liked: null,
        timestamp: parseInt(Date.now()/1000),
        key: postToBucketListKey,
        completed: false,
        timeCompleted: 0
    }

    var updates = {};
    updates['/users/' + user.uid + '/personal_list/' + postToUserKey] = postToBucketListKey;
    updates['/bucket_list_items/' + postToBucketListKey] = postToBucketList;
    
    firebase.database().ref().update(updates)
    .then(function(){
        console.log('Synchronization succeeded');
    })
    .catch(function(error) {
        console.log('Synchronization failed');
    });

    window.location.replace("profile.html");
}

function markComplete(list){

    var postToBucketList = {
        title: list.title,
        originalCreator: list.originalCreator,
        postCreator: list.postCreator,
        date : list.date,
        description: list.description,
        score: list.score,
        timestamp: list.timestamp,
        key: list.key,
        completed: true,
        timeCompleted: parseInt(Date.now()/1000)
    }

    console.log(postToBucketList);

    var updates = {};
    updates['/bucket_list_items/' + list.key] = postToBucketList;
    
    firebase.database().ref().update(updates)
    .then(function(){
        console.log('Synchronization succeeded');
    })
    .catch(function(error) {
        console.log('Synchronization failed');
    });
    
    window.location.replace("profile.html");
}

function signOut() {
    firebase.auth().signOut()
    .then(function() {
        window.location.replace("login.html");
    })
    .catch(function(error) {
        console.log(error)
  });
}

function setValue(id,newvalue) {
    var s= document.getElementById(id);
    s.value = newvalue;
} 