var firebase = app_fireBase
var user = firebase.auth().currentUser;
const database = firebase.database();
var storageRef = firebase.storage().ref();

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

    var isPhoto = false;
    if(file){
        isPhoto = true;
    }

    var postToBucketList = {
        title: postTitle,
        originalCreator: userName,
        postCreator: user.uid,
        date : postDate,
        description: postDescription,
        score: 0,
        liked: null,
        timestamp: parseInt(Date.now()/1000),
        key: postToBucketListKey,
        completed: false,
        timeCompleted: 0,
        hasPhoto: isPhoto
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

    if(file){
        
        console.log("File is here");
        var imageRef = storageRef.child(userName + '/' + postToBucketListKey.toString() + '.jpg');
        
        imageRef.put(file).then(function(snapshot) {
            console.log('Uploaded a blob or file!');
            window.location.replace("profile.html");
          });

    }else{
        console.log("File is not here");
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