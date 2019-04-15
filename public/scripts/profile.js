var firebase = app_fireBase
var user = firebase.auth().currentUser;
var storage = firebase.storage();
var database = firebase.database();

firebase.auth().onAuthStateChanged(function(user){
    if(user){

        database.ref().orderByChild('/users').equalTo(user.uid).once('value',snapshot => {
            if(snapshot.exists()){
                console.log("Exists");
            }else{
                console.log(user.uid);
                console.log("Does not exists");

                var postData = {
                    email: user.email,
                    username: user.displayName.replace(/\s/g, ''),
                    personal_list: {Hash: "Hash"}
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
        
        // Get number of posts
        var numberPosts = 10;
        var appendPhotos = document.getElementById("photos");

        for (var i = 1; i <= numberPosts; i++) {
            photoId = i + 1;
            refPath = storage.ref(user.displayName.replace(/\s/g, '') + '/' + i.toString() + '.jpg');
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
            })(photoId);
        }
        
    }else{
        uid=null;
        window.location.replace("login.html");
    }
}) 

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