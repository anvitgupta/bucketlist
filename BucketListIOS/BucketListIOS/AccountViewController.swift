//
//  AccountViewController.swift
//  BucketListIOS
//
//  Created by Cavan Briody on 4/1/19.
//  Copyright © 2019 Cavan Briody. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseDatabase

class AccountViewController: UIViewController {

    
    @IBOutlet weak var incompleteContainer: UIView!
    @IBOutlet weak var completeContainer: UIView!
    @IBOutlet weak var seg: UISegmentedControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    
    @IBAction func didTapLogout(_ sender: Any) {
        
        try! Auth.auth().signOut()
        
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "ViewController") as! ViewController
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    @IBAction func displayContainer(_ sender: Any) {
        if(seg.selectedSegmentIndex == 0){
            self.incompleteContainer.alpha = 1
            self.completeContainer.alpha = 0
        } else {
            self.incompleteContainer.alpha = 0
            self.completeContainer.alpha = 1
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
