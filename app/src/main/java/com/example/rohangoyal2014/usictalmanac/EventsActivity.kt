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
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventsActivity : AppCompatActivity() {

    companion object {
        val TAG="EventsActivity"
    }

    var responseWaitView: View?=null
    var resendButton:Button?=null
    var doneButton:Button?=null
    var toolbar: Toolbar?=null
    var recyclerView:RecyclerView?=null
    var fabButtonNormal:com.github.clans.fab.FloatingActionButton?=null
    var fabButtonAddAdmin:com.github.clans.fab.FloatingActionButton?=null
    var fabButtonAddEvent:com.github.clans.fab.FloatingActionButton?=null
    var fabButtonViewProfile:com.github.clans.fab.FloatingActionButton?=null
    var fabMenu:com.github.clans.fab.FloatingActionMenu?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        responseWaitView=findViewById(R.id.response_wait_view)
        resendButton=findViewById(R.id.resend)
        doneButton=findViewById(R.id.done)
        toolbar=findViewById(R.id.toolbar)
        recyclerView=findViewById(R.id.recycler_view)
        fabButtonNormal=findViewById(R.id.fab_normal)
        fabButtonAddAdmin=findViewById(R.id.add_admin)
        fabButtonAddEvent=findViewById(R.id.add_event)
        fabButtonViewProfile=findViewById(R.id.user_profile)
        fabMenu=findViewById(R.id.fab_menu)
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
            recyclerView?.visibility=View.VISIBLE
            val linearLayoutManager=LinearLayoutManager(this)
            recyclerView?.layoutManager=linearLayoutManager
            recyclerView?.setHasFixedSize(true)
            val list=ArrayList<EventsModel>()
            list.add(EventsModel("Dance","Let us enjoy this event of dance in USICT","","","","",""))
            list.add(EventsModel("Music","Let us enjoy this event of Music in USICT.Let us enjoy this event of Music in USICT.Let us enjoy this event of Music in USICT","","","","",""))
            val adapter=EventsAdapter(list)
            recyclerView?.adapter=adapter
            loadAppropriateFab()
            fabButtonAddAdmin?.setOnClickListener{
                startActivity(Intent(this,AdminDashboardActivity::class.java)
                        .putExtra(Utilities.FRAGMENT_SELECT_KEY,Utilities.FRAGMENT_SELECTOR_VALUE_ADD_ADMIN)
                )
            }
            fabButtonAddEvent?.setOnClickListener{
                startActivity(Intent(this,AdminDashboardActivity::class.java)
                        .putExtra(Utilities.FRAGMENT_SELECT_KEY,Utilities.FRAGMENT_SELECTOR_VALUE_ADD_EVENT)
                )
            }
        }
        resendButton?.setOnClickListener{
            sendVerificationMail()
        }
        doneButton?.setOnClickListener{
            checkIfDoneOrNot()
        }
    }

    private fun loadAppropriateFab(){
        val progressBar=ProgressBar(this)
        progressBar.setPadding(20,60,20,60)
        var dialog=AlertDialog.Builder(this).setView(progressBar).setCancelable(false).create()
        dialog.show()
        FirebaseDatabase.getInstance().reference.child("users").child(Utilities.FirebaseUtilites.mAuth.uid)
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        Toast.makeText(this@EventsActivity,getString(R.string.error)+".Some options may not load.Try restarting the app with a proper internet connection",Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        if(p0?.value!=null){
                            val enrolment=p0.child("enrolment").value.toString()
                            FirebaseDatabase.getInstance().reference.child("admin").child(enrolment)
                                    .addListenerForSingleValueEvent(object:ValueEventListener{
                                        override fun onDataChange(p1: DataSnapshot?) {
                                            if(p1?.value==null){
                                                fabButtonNormal?.visibility=View.VISIBLE
                                            }
                                            else{
                                                fabMenu?.visibility=View.VISIBLE
                                            }
                                            dialog.dismiss()
                                        }

                                        override fun onCancelled(p0: DatabaseError?) {
                                            dialog.dismiss()
                                            Toast.makeText(this@EventsActivity,getString(R.string.error)+".Some options may not load.Try restarting the app with a proper internet connection",Toast.LENGTH_SHORT).show()
                                        }
                                    })
                        }
                    }
                })
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
