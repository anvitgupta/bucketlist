//
//  LoginViewController.swift
//  BucketListIOS
//
//  Created by Cavan Briody on 3/13/19.
//  Copyright © 2019 Cavan Briody. All rights reserved.
//

import UIKit
import FirebaseAuth

class LogInViewController: UIViewController {

    
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    
    @IBOutlet weak var errorMessage: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    
    @IBAction func didTapCancel(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func didTapLogin(_ sender: Any) {
        Auth.auth().signIn(withEmail: self.email.text!, password: self.password.text!, completion: { (user, error) in
            
            if(error == nil){
                //self.databaseRef.child("user").child(user!.uid).child("email").setValue(self.email.text!)
                // TO DO: ensure user actually exists
                //self.performSegue(withIdentifier: "MainTabController", sender: nil)
                let mainTabController = self.storyboard?.instantiateViewController(withIdentifier: "MainTabController") as! MainTabController
                
                self.present(mainTabController, animated: true, completion: nil)
            } else {
                self.errorMessage.text = error?.localizedDescription
            }
            
            //if(user != nil) {
            
             //} else {
             //   print(error)
             //}
            
        })
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
