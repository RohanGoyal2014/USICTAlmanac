package com.example.rohangoyal2014.usictalmanac


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.rohangoyal2014.usictalmanac.Utilities.FirebaseUtilites.mAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.design.widget.TabLayout
import android.support.v7.widget.CardView
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.victor.loading.newton.NewtonCradleLoading
import java.net.URL


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    companion object {
        val TAG="LoginFragment"
    }

    var enrol:TextInputEditText?=null
    var pass:TextInputEditText?=null
    var submit:TextView?=null
    var card:CardView?=null
    var progressView:View?=null
    var NewtonLoader:NewtonCradleLoading?=null
    var tabLayout: TabLayout?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout= inflater!!.inflate(R.layout.fragment_login, container, false)
        tabLayout=activity.findViewById<TabLayout>(R.id.tab_layout)
        enrol=layout.findViewById<TextInputEditText>(R.id.enrolment)
        pass=layout.findViewById<TextInputEditText>(R.id.pass)
        submit=layout.findViewById<TextView>(R.id.submit)
        card=layout.findViewById<CardView>(R.id.login_card)
        progressView=layout.findViewById(R.id.progress_2)
        NewtonLoader=layout.findViewById(R.id.newton_cradle_loading)
        submit?.setOnClickListener{
            var res=Utilities.ValidationUtilities.empty_validator(context,enrol,getString(R.string.enrolment))
            if(res){
                return@setOnClickListener
            }
            res=Utilities.ValidationUtilities.empty_validator(context,pass,getString(R.string.password))
            if(res){
                return@setOnClickListener
            }
            else{
                startLogin(enrol?.text.toString().trim(),pass?.text.toString().trim())
            }
        }
        return layout
    }

    private fun startProgress(){
        tabLayout?.visibility=View.GONE
        card?.visibility=View.GONE
        progressView?.visibility=View.VISIBLE
        NewtonLoader?.start()
    }

    private fun endProgress(){
        tabLayout?.visibility=View.VISIBLE
        progressView?.visibility=View.GONE
        NewtonLoader?.stop()
        card?.visibility=View.VISIBLE

    }

    private fun startLogin(enrol:String,pass:String){
        startProgress()
        FirebaseDatabase.getInstance().reference.child("enrolments").child(enrol).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                val mail=p0?.value
                if(mail!=null) {
                    Utilities.FirebaseUtilites.mAuth.signInWithEmailAndPassword(mail.toString(), pass)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG,"Authorization Successful")
                                    endProgress()
                                    startActivity(Intent(context,EventsActivity::class.java))
                                    activity.finish()
                                }
                                else{
                                    if(task.exception is FirebaseAuthInvalidCredentialsException){
                                        Toast.makeText(context,getString(R.string.incorrect_password),Toast.LENGTH_SHORT).show()
                                        endProgress()
                                    }
                                    else {
                                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                                        Log.e(TAG, task.exception.toString())
                                        endProgress()
                                    }
                                }
                            }
                } else{
                    Toast.makeText(context,getString(R.string.not_exists),Toast.LENGTH_SHORT).show()
                    endProgress()
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(context,getString(R.string.error),Toast.LENGTH_SHORT).show()
                endProgress()
            }
        })
    }
}
