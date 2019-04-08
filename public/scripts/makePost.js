var firebase = app_fireBase
var user = firebase.auth().currentUser;
const database = firebase.database();
const ref = firebase.storage().ref();
var loadImage = new Image();

firebase.auth().onAuthStateChanged(function(user){
    if(user){

        document.getElementById("file-input").onchange = function (evt){
            var tgt = evt.target || window.event.srcElement,
            files = tgt.files;

            if (FileReader && files && files.length) {
                var fr = new FileReader();
                fr.onload = function () {
                    loadImage.src = fr.result;
                }
                fr.readAsDataURL(files[0]);
                sleep(2000);
                console.log(loadImage);

            } else {
                console.log("Failed")
            }

        }
        
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

    var reader  = new FileReader();
    console.log(document.getElementById('file-input').files[0]);

    var newPostKey = firebase.database().ref().child('bucket_list_items').push().key;

    var postData = {
        title: postTitle,
        creator: userName,
        date : postDate,
        description: postDescription,
        score: 0,
        timestamp: Date.now(),
        key: newPostKey,
        completed: false
    }

    console.log(postData);

    var updates = {};
    updates['/bucket_list_items/' + newPostKey] = postData;

    firebase.database().ref().update(updates)
    .then(function(){
        console.log('Synchronization succeeded');
        window.location.replace("profile.html");
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

function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
  }

  function sendData() {

    user = firebase.auth().currentUser;
    const postTitle = document.getElementById('activity-input').value.toString();
    const postDate = document.getElementById('date-input').value.toString();
    const postDescription = document.getElementById('description-input').value.toString();
    const userName = user.displayName.toString().replace(/\s/g, '');

    var reader  = new FileReader();
    console.log(document.getElementById('file-input').files[0]);

    var newPostKey = firebase.database().ref().child('bucket_list_items').push().key;

    var postData = {
        title: postTitle,
        creator: userName,
        date : postDate,
        description: postDescription,
        score: 0,
        timestamp: Date.now(),
        key: newPostKey,
        completed: false
    }

    console.log(postData);

    var updates = {};
    updates['/bucket_list_items/' + newPostKey] = postData;

    firebase.database().ref().update(updates)
    .then(function(){
        console.log('Synchronization succeeded');
        window.location.replace("profile.html");
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

function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
  }