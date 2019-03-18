import app from 'firebase/app';
import 'firebase/auth';

const config = {
    apiKey: "AIzaSyA2e2s8fVJ60scbvPgZTINKBSx4Ki1m8D4",
    authDomain: "bucket-list-swe.firebaseapp.com",
    databaseURL: "https://bucket-list-swe.firebaseio.com",
    projectId: "bucket-list-swe",
    storageBucket: "bucket-list-swe.appspot.com",
    messagingSenderId: "296389021106"
  };

  class Firebase {
    constructor() {
      app.initializeApp(config);
      this.auth = app.auth();
    }

    doCreateUserWithEmailAndPassword = (email, password) =>
      this.auth.createUserWithEmailAndPassword(email, password);
    
    doSignInWithEmailAndPassword = (email, password) =>
      this.auth.signInWithEmailAndPassword(email, password);

    doSignOut = () => this.auth.signOut();

    doPasswordReset = email => this.auth.sendPasswordResetEmail(email);

    doPasswordUpdate = password =>
      this.auth.currentUser.updatePassword(password);
  }
  
  export default Firebase;