var firebase = app_fireBase
var user = firebase.auth().currentUser;
var database = firebase.database();

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        
        ref.once('value', function(snapshot) {
            snapshot.forEach(function(childSnapshot) {
                var childKey = childSnapshot.key;
                var childData = childSnapshot.val();
                // ...
            });
        });
        
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