var firebase = app_fireBase
var user = firebase.auth().currentUser;

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        
        document.getElementById("userProfilePic").src = user.photoURL
        document.getElementById("userName").textContent = user.displayName
        document.getElementById("userID").textContent = '@' + user.displayName.replace(/\s/g, '') 
        document.getElementById("userFollowers").textContent = '2 Follows'
        document.getElementById("userFollowing").textContent = '2 Following'
        
        user.providerData.forEach(function (profile) {
            console.log("Sign-in provider: " + profile.providerId);
            console.log("  Provider-specific UID: " + profile.uid);
            console.log("  Name: " + profile.displayName);
            console.log("  Email: " + profile.email);
            console.log("  Photo URL: " + profile.photoURL);
            });

        document.getElementById('sign-out').addEventListener('click', signOut);      

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