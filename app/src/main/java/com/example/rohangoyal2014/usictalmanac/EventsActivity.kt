package com.example.rohangoyal2014.usictalmanac

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AlertDialogLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.Toast

class EventsActivity : AppCompatActivity() {

    var responseWaitView: View?=null
    var resendButton:Button?=null
    var doneButton:Button?=null
    var toolbar: Toolbar?=null
    var recyclerView:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        responseWaitView=findViewById<View>(R.id.response_wait_view)
        resendButton=findViewById<Button>(R.id.resend)
        doneButton=findViewById<Button>(R.id.done)
        toolbar=findViewById<Toolbar>(R.id.toolbar)
        recyclerView=findViewById<RecyclerView>(R.id.recycler_view)
        setSupportActionBar(toolbar)

        if(Utilities.FirebaseUtilites.mAuth.currentUser?.isEmailVerified==false){
            AlertDialog.Builder(this).setTitle("Verify Email")
                    .setMessage("We are sending you a verification Email")
                    .setPositiveButton("OKAY",DialogInterface.OnClickListener{
                        _, i ->
                        sendVerificationMail()
                    }).setCancelable(false).create().show()
        }
        else{
            val linearLayoutManager=LinearLayoutManager(this)
            recyclerView?.layoutManager=linearLayoutManager
            recyclerView?.setHasFixedSize(true)
            val list=ArrayList<EventsModel>()
            list.add(EventsModel("Dance","Let us enjoy this event of dance in USICT","","","","",""))
            list.add(EventsModel("Music","Let us enjoy this event of Music in USICT.Let us enjoy this event of Music in USICT.Let us enjoy this event of Music in USICT","","","","",""))
            val adapter=EventsAdapter(list)
            recyclerView?.adapter=adapter
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
                            startActivity(Intent(this,EventsActivity::class.java))
                            finish()
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
