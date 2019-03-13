//
//  ViewController.swift
//  BucketListIOS
//
//  Created by Cavan Briody on 2/10/19.
//  Copyright © 2019 Cavan Briody. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth
import GoogleSignIn
import FBSDKLoginKit
import FBSDKCoreKit


//class LoginViewController: UIViewController, GIDSignInUIDelegate, FBSDKLoginButtonDelegate {

class ViewController: UIViewController {
   
    override func viewDidLoad() {
        super.viewDidLoad()
    }

/*
    func loginButton(_ loginButton: FBSDKLoginButton!, didCompleteWith result: FBSDKLoginManagerLoginResult!, error: Error!) {
        print ("User logged in")
    }
    
    func loginButtonDidLogOut(_ loginButton: FBSDKLoginButton!) {
        print("User did log out")
    }
    
    var loginButton: FBSDKLoginButton = FBSDKLoginButton()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        loginButton.center = self.view.center
        loginButton.readPermissions = ["public_profile", "email", "user_friends"]
        loginButton.delegate = self
        self.view!.addSubview(loginButton)
        
        //GIDSignIn.sharedInstance().uiDelegate = self
        //GIDSignIn.sharedInstance().signIn()
        
        let mainTabController = storyboard?.instantiateViewController(withIdentifier: "MainTabController") as! MainTabController
        
        self.present(mainTabController, animated: true, completion: nil)
        
        
        //let gSignIn = GIDSignInButton(frame: CGRect(x: 0, y: 0, width: 230, height: 48))
        //gSignIn.center = view.center
        //view.addSubview(gSignIn)
    }
    
    
    
//    @IBAction func loginTapped(_ sender: UIButton) {
//
//        let mainTabController = storyboard?.instantiateViewController(withIdentifier: "MainTabController") as! MainTabController
//
//        present(mainTabController, animated: true, completion: nil)
//    }
//
 */
}

