var firebase = app_fireBase
var user = firebase.auth().currentUser;
var database = firebase.database();
var storageRef = firebase.storage();
var appendPhotos = document.getElementById("photos");

firebase.auth().onAuthStateChanged(function(user){
    if(user){

        database.ref().orderByChild('/users/' + user.uid).once('value',snapshot => {
            if(snapshot.exists()){
                console.log("User exists");
            }else{
                console.log(user.uid);
                console.log("User does not exist");

                var postData = {
                    username: user.displayName.replace(/\s/g, ''),
                    email: user.email,
                    followers: 0,
                    following: 0
                }

                var updates = {};
                updates['/users/' + user.uid] = postData;

                firebase.database().ref().update(updates)
                .then(function(){
                    console.log('Synchronization succeeded');
                })
                    .catch(function(error) {
                    console.log('Synchronization failed');
                });
            }
        });
        
        document.getElementById('sign-out').addEventListener('click', signOut);
        document.getElementById("userProfilePic").src = user.photoURL
        document.getElementById("userName").textContent = user.displayName
        document.getElementById("userID").textContent = '@' + user.displayName.replace(/\s/g, '') 
        
        // Get following & follower
        document.getElementById("userFollowers").textContent = '2 Follows'
        document.getElementById("userFollowing").textContent = '2 Following'
        
        var postKeys = [];

        database.ref('/users/' + user.uid + '/personal_list').once('value').then(function(snapshot){
            snapshot.forEach(function(childNodes){
                    postKeys.push(childNodes.val());
            })
        })

        setTimeout(getPhotos, 2000, postKeys, user);

    }else{
        uid=null;
        window.location.replace("login.html");
    }
}) 

function getPhotos(postKeys, user){

    var appendPhotos = document.getElementById("photos");

    for (i = 0; i < postKeys.length; i++) {

        refPath = storageRef.ref(user.displayName.replace(/\s/g, '') + '/' + postKeys[i] + '.jpg');
        (function(pid) {
            refPath.getDownloadURL().then(function(url) {
                
                var img = document.createElement('img');
                img.src = url;
                img.style.width = '150px';
                img.style.height = '150px';
                img.style.borderRadius = '50%';
                img.id = "bucket-img";
                img.style.marginTop = '25px';
                img.style.marginBottom = '50px';
                img.style.marginLeft = '25px';
                img.style.marginRight = '25px';
                appendPhotos.appendChild(img);

            }).catch(function(error) {
                console.log(error);
            });
        })();
    }
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