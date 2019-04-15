var firebase = app_fireBase
var user = firebase.auth().currentUser;
const database = firebase.database();
var storageRef = firebase.storage().ref();
var loadImage = new Image();

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        
        document.getElementById('submit').addEventListener('click', sendData);
    }else{
        
        uid=null;
        window.location.replace("login.html");
    }
}) 

function sendData() {

    user = firebase.auth().currentUser;
    const postTitle = document.getElementById('activity-input').value.toString();
    const postDate = document.getElementById('date-input').value.toString();
    const postDescription = document.getElementById('description-input').value.toString();
    const userName = user.displayName.toString().replace(/\s/g, '');
    const file = document.getElementById("file-input").files[0];

    var postToBucketListKey = firebase.database().ref().child('bucket_list_items').push().key;
    var postToUserKey = firebase.database().ref().child('users/' + user.uid + '/personal_list').push().key;

    var postToBucketList = {
        title: postTitle,
        creator: userName,
        date : postDate,
        description: postDescription,
        score: 0,
        timestamp: Date.now(),
        key: postToBucketListKey,
        completed: false
    }

    if(file){
        var reader  = new FileReader();
        var imageRef = storageRef.child(userName + '/' + postToBucketListKey.toString() + '.jpg');
        
        imageRef.put(file).then(function(snapshot) {
            console.log('Uploaded a blob or file!');
          });
    }

    var updates = {};
    updates['/users/' + user.uid + '/personal_list/' + postToUserKey] = postToBucketListKey;
    updates['/bucket_list_items/' + postToBucketListKey] = postToBucketList;
    
    firebase.database().ref().update(updates)
    .then(function(){
        console.log('Synchronization succeeded');
        //window.location.replace("profile.html");
    })
    .catch(function(error) {
        console.log('Synchronization failed');
    });
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