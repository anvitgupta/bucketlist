var firebase = app_fireBase
var user = firebase.auth().currentUser;
var storage = firebase.storage();

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        
        document.getElementById('sign-out').addEventListener('click', signOut);
        document.getElementById("userProfilePic").src = user.photoURL
        document.getElementById("userName").textContent = user.displayName
        document.getElementById("userID").textContent = '@' + user.displayName.replace(/\s/g, '') 
        
        // Get following & follower
        document.getElementById("userFollowers").textContent = '2 Follows'
        document.getElementById("userFollowing").textContent = '2 Following'
        
        user.providerData.forEach(function (profile) {
            console.log("Sign-in provider: " + profile.providerId);
            console.log("  Provider-specific UID: " + profile.uid);
            console.log("  Name: " + profile.displayName);
            console.log("  Email: " + profile.email);
            console.log("  Photo URL: " + profile.photoURL);
            });
        
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
                    console.log(appendPhotos)

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