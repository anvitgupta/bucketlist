var firebase = app_fireBase
var database = firebase.database
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

function writeUserData(userId, name, email, imageUrl) {
    firebase.database().ref('bucket_list').set({
      username: name,
      email: email,
      profile_picture : imageUrl
    });
  }

//   function writeNewPost(uid, username, picture, title, body) {
//     // A post entry.
//     var postData = {
//       author: username,
//       uid: uid,
//       body: body,
//       title: title,
//       starCount: 0,
//       authorPic: picture
//     };
  
//     // Get a key for a new Post.
//     var newPostKey = firebase.database().ref().child('posts').push().key;
  
//     // Write the new post's data simultaneously in the posts list and the user's post list.
//     var updates = {};
//     updates['/posts/' + newPostKey] = postData;
//     updates['/user-posts/' + uid + '/' + newPostKey] = postData;
  
//     return firebase.database().ref().update(updates);
//   }
