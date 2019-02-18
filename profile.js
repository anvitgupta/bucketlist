var firebase = app_fireBase
var user = firebase.auth().currentUser;
var name, email, photoUrl, uid, emailVerified;

window.onload = function() {
    var uid = null;
    firebase.auth().onAuthStateChanged(function(user){
        if(user){
            setValue("userName", user.displayName)
            setValue("userEmail",user.email)
            photoUrl = user.photoURL;
            emailVerified = user.emailVerified;
            uid = user.uid; 
            user.providerData.forEach(function (profile) {
                console.log("Sign-in provider: " + profile.providerId);
                console.log("  Provider-specific UID: " + profile.uid);
                console.log("  Name: " + profile.displayName);
                console.log("  Email: " + profile.email);
                console.log("  Photo URL: " + profile.photoURL);
              });
            
        }else{
            uid=null;
            window.location.replace("login.html");
        }
    }) 
};

function setValue(id,newvalue) {
    var s= document.getElementById(id);
    s.value = newvalue;
} 

  
