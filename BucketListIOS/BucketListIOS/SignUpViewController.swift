//
//  SignUpViewController.swift
//  BucketListIOS
//
//  Created by Cavan Briody on 3/13/19.
//  Copyright © 2019 Cavan Briody. All rights reserved.
//

import UIKit
import FirebaseDatabase
import FirebaseAuth

class SignUpViewController: UIViewController {

    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    
    @IBOutlet weak var signUp: UIButton!
    
    var databaseRef = Database.database().reference()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        signUp.isEnabled = false
        // Do any additional setup after loading the view.
    }
    
    @IBAction func didTapSignUp(_ sender: UIButton) {
        
        signUp.isEnabled = false
        
        Auth.auth().createUser(withEmail: email.text!, password: password.text!, completion: { (user, error) in
            
            if(error != nil){
                if(error!._code == 17999){
                    //errorMessage = "Invalid email Address"
                } else {
                    //errorMessage = error?.localizedDescription
                }
            } else {
                //errorMessage = "Registered Successfully"
                Auth.auth().signIn(withEmail: self.email.text!, password: self.password.text!, completion: { (user, error) in
                    
                    
                        //self.databaseRef.child("user_profiles").child(user!.uid).child("email").setValue(self.email.text!)
                    if(user != nil) {
                        let mainTabController = self.storyboard?.instantiateViewController(withIdentifier: "MainTabController") as! MainTabController
                        
                        self.present(mainTabController, animated: true, completion: nil)
                    } else {
                        print(error)
                    }
                    
                })}
            }
    )
    
    }
    
    @IBAction func didTapCancel(_ sender: UIButton) {
        
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func textDidChange(_ sender: UITextField) {
        
        if(!email.text!.isEmpty && !password.text!.isEmpty) {
            signUp.isEnabled = true
        } else {
            signUp.isEnabled = false
        }
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
