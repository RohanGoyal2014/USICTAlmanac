package com.example.rohangoyal2014.usictalmanac

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AlertDialogLayout
import android.view.View
import android.widget.Button
import android.widget.Toast

class EventsActivity : AppCompatActivity() {

    var responseWaitView: View?=null
    var resendButton:Button?=null
    var doneButton:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        responseWaitView=findViewById<View>(R.id.response_wait_view)
        resendButton=findViewById<Button>(R.id.resend)
        doneButton=findViewById<Button>(R.id.done)

        if(Utilities.FirebaseUtilites.mAuth.currentUser?.isEmailVerified==false){
            AlertDialog.Builder(this).setTitle("Verify Email")
                    .setMessage("We are sending you a verification Email")
                    .setPositiveButton("OKAY",DialogInterface.OnClickListener{
                        _, i ->
                        sendVerificationMail()
                    }).setCancelable(false).create().show()
        }
        resendButton?.setOnClickListener{
            sendVerificationMail()
        }
        doneButton?.setOnClickListener{
            checkIfDoneOrNot()
        }
    }

    private fun checkIfDoneOrNot(){
        Utilities.FirebaseUtilites.mAuth.currentUser?.reload()
                ?.addOnCompleteListener{
                    task ->
                    if(task.isSuccessful){
                        if(Utilities.FirebaseUtilites.mAuth.currentUser?.isEmailVerified==true){
                            hideValidationBox()
                        } else{
                            Toast.makeText(this,"Not verified",Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun sendVerificationMail(){
        Utilities.FirebaseUtilites.mAuth.currentUser?.sendEmailVerification()
                ?.addOnCompleteListener{
                    task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Verification Email Sent",Toast.LENGTH_SHORT).show()
                        showValidationBox()
                    } else{
                        Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private  fun showValidationBox(){
        responseWaitView?.visibility=View.VISIBLE
    }

    private fun hideValidationBox(){
        responseWaitView?.visibility=View.GONE
    }
}
