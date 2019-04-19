var app_fireBase = {};
(function(){
        // Initialize Firebase
        var config = {
            apiKey: "AIzaSyA2e2s8fVJ60scbvPgZTINKBSx4Ki1m8D4",
            authDomain: "bucket-list-swe.firebaseapp.com",
            databaseURL: "https://bucket-list-swe.firebaseio.com",
            projectId: "bucket-list-swe",
            storageBucket: "bucket-list-swe.appspot.com",
            messagingSenderId: "296389021106"
        };
        firebase.initializeApp(config);
        app_fireBase =firebase;
})()