var firebase = app_fireBase
var user = firebase.auth().currentUser;
const database = firebase.database();
const ref = firebase.storage().ref();

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        
        document.getElementById('submit').addEventListener('click', sendData);
        
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

function sendData() {

    user = firebase.auth().currentUser;
    const postActivity = document.getElementById('activity-input').value.toString();
    const postDate = document.getElementById('date-input').value.toString();
    const convertFile = document.getElementById('file-input').files[0];
    const postDescription = document.getElementById('description-input').value.toString();
    const userName = user.displayName.toString().replace(/\s/g, '');
    
    //convertFile = reader.readAsBinaryString(file);
    
    console.log(postActivity);
    console.log(postDate);
    console.log(convertFile);
    console.log(postDescription);

    // var reader = new FileReader();
    // const convertFile = reader.readAsText(file);

    // const fileName = 'AnvitGupta/11.jpg'
    // const metadata = { contentType: file.type };
    // const task = ref.child(fileName).put(convertFile);

    // task
    // .then(snapshot => snapshot.ref.getDownloadURL())
    // .then((url) => {
    // console.log("Success!");
    // })
    // .catch(console.error);

    var bucketItem = database.ref('bucket_list_items/');

    bucketItem.set({
        postActivity: {
            creator: user.displayName,
            date : postDate,
            description: postDescription,
            users_added: [userName] 
        }
    }).then(function() {
        console.log('Synchronization succeeded');
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