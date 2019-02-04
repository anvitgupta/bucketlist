from flask import Flask
import pyrebase

app = Flask(__name__)

@app.route('/')
def display_users():
    output = []
    config = {
            "apiKey": "AIzaSyA2e2s8fVJ60scbvPgZTINKBSx4Ki1m8D4",
            "authDomain": "bucket-list-swe.firebaseapp.com",
            "databaseURL": "https://bucket-list-swe.firebaseio.com",
            "storageBucket": "bucket-list-swe.appspot.com"
            }
    
    firebase = pyrebase.initialize_app(config)
    
    db = firebase.database()
    
    bucket_list_items = db.child("bucket_list_items").get()
    
    output.append("[Bucket List]")
    for item in bucket_list_items.each():
        output.append(item.val()["description"] + " - added by: ")
        added = []
        for user in item.val()["added_by"]:
            added.append(user)
        output[-1] += ", ".join(added)

    return "<br/>".join(output)
