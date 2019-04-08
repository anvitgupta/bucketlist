var firebase = app_fireBase
var user = firebase.auth().currentUser;
var database = firebase.database();

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        
        const userName = user.displayName.toString().replace(/\s/g, '');
        var appendPosts = document.getElementById("populate_feed");

        database.ref('/bucket_list_items').once('value').then(function(snapshot){
            snapshot.forEach(function(childNodes){
                if(childNodes.val().creator != userName){
                    var post = document.createElement('div');
                    post.className = "feed_post";
                    var str = "Date:" + childNodes.val().date + "<br/>" + "Title: " + childNodes.val().title + "<br/>" + 
                    "Description: " + childNodes.val().description + "<br/>" + "Creator: " + childNodes.val().creator;
                    post.innerHTML = str;

                    var btnBuck = document.createElement("BUTTON"); 
                    btnBuck.innerHTML = "Buck It!";
                    // btnBuck.setAttribute('onclick', 'addToList()');

                    var btnComplete = document.createElement("BUTTON");
                    btnComplete.innerHTML = "Complete!"

                    appendPosts.appendChild(btnBuck);
                    appendPosts.appendChild(btnComplete);
                    appendPosts.appendChild(post);
                }
            })
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

function addToList(){
    window.location.replace("login.html");
}