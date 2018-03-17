package com.example.rohangoyal2014.usictalmanac


import android.app.AlertDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.victor.loading.newton.NewtonCradleLoading
import kotlinx.android.synthetic.main.fragment_register.*
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    companion object {
        val TAG="RegisterFragment"
    }

    var submit:TextView?=null
    var fname:TextInputEditText?=null
    var lname:TextInputEditText?=null
    var enrol:TextInputEditText?=null
    var email:TextInputEditText?=null
    var other:TextInputEditText?=null
    var pass:TextInputEditText?=null
    var conf_pass:TextInputEditText?=null
    var spinner:Spinner?=null
    var courseRadioGroup:RadioGroup?=null
    var branchRadioGroup:RadioGroup?=null
    var progressView:View?=null
    var NewtonLoader:NewtonCradleLoading?=null
    var cardView:CardView?=null
    var tabLayout:TabLayout?=null
    var arr:ArrayList<Pair<TextInputEditText,String>>?=ArrayList<Pair<TextInputEditText,String>>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout= inflater!!.inflate(R.layout.fragment_register, container, false)
        tabLayout=activity.findViewById<TabLayout>(R.id.tab_layout)
        submit=layout.findViewById<TextView>(R.id.submit)
        fname=layout.findViewById<TextInputEditText>(R.id.fname)
        lname=layout.findViewById<TextInputEditText>(R.id.lname)
        enrol=layout.findViewById<TextInputEditText>(R.id.enrolment)
        email=layout.findViewById<TextInputEditText>(R.id.email)
        other=layout.findViewById<TextInputEditText>(R.id.other_text)
        pass=layout.findViewById<TextInputEditText>(R.id.pass)
        conf_pass=layout.findViewById<TextInputEditText>(R.id.conf_pass)
        courseRadioGroup=layout.findViewById<RadioGroup>(R.id.course_group)
        branchRadioGroup=layout.findViewById<RadioGroup>(R.id.branch_group)
        progressView=layout.findViewById<View>(R.id.progress_1)
        NewtonLoader=layout.findViewById(R.id.newton_cradle_loading)
        cardView=layout.findViewById<CardView>(R.id.registration_card)
        spinner=layout.findViewById<Spinner>(R.id.year_spinner)
        val spinnerAdapter=Utilities.getSpinnerData(context)
        spinner?.adapter=spinnerAdapter

        arr?.add(Pair(fname!!,getString(R.string.fname)))
        arr?.add(Pair(lname!!,getString(R.string.lname)))
        arr?.add(Pair(enrol!!,getString(R.string.enrolment)))
        arr?.add(Pair(email!!,getString(R.string.email)))
        arr?.add(Pair(pass!!,getString(R.string.password)))
        arr?.add(Pair(conf_pass!!,getString(R.string.conf_pass)))

        submit?.setOnClickListener {
            startProgress()
            var res=false
            for (i:Pair<TextInputEditText,String> in arr!!){
                res=Utilities.ValidationUtilities.empty_validator(context,i.first,i.second);
                if(res){
                    break
                }
            }
            if(res){
                return@setOnClickListener
            }
            if(enrol?.text.toString().length<9){
                Toast.makeText(context,"Incorrect Enrolment Number",Toast.LENGTH_SHORT).show()
            }
            else if(pass?.text.toString().length<8){
                Toast.makeText(context,"Password must be atleast 8 characters long",Toast.LENGTH_SHORT).show()
            }
            else if(!pass?.text.toString().equals(conf_pass?.text.toString())){
                Toast.makeText(context,"The passwords need to match",Toast.LENGTH_SHORT).show()
            }
            else if (!mca.isChecked && !btech.isChecked){
                Toast.makeText(context,"Please select the course",Toast.LENGTH_SHORT).show()
            }
            else if(!cse.isChecked && !ece.isChecked && !it_course.isChecked && !other_option.isChecked){
                Toast.makeText(context,"Please select a branch",Toast.LENGTH_SHORT).show()
            }
            else if(other_option.isChecked && other_text.text.toString().trim().isEmpty()){
                Toast.makeText(context,"Please mention your branch under other region",Toast.LENGTH_SHORT).show()
            }
            else {
                createUser()
            }
            endProgress()
        }
        return layout
    }

    private fun startProgress(){
        tabLayout?.visibility=View.GONE
        progressView?.visibility=View.VISIBLE
        cardView?.visibility=View.GONE
        NewtonLoader?.start()
    }

    private fun endProgress(){
        tabLayout?.visibility=View.VISIBLE
        progressView?.visibility=View.GONE
        cardView?.visibility=View.VISIBLE
        NewtonLoader?.stop()
    }

    private fun createUser(){
        Utilities.FirebaseUtilites.mAuth.createUserWithEmailAndPassword(email?.text.toString().trim(),pass?.text.toString().trim())
                .addOnCompleteListener{
                    task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        Log.d(TAG,"Sucess")
                        addToDatabase(Utilities.FirebaseUtilites.mAuth.currentUser)
                        updateUI(Utilities.FirebaseUtilites.mAuth.currentUser)
                    } else {
                        Log.e(TAG,"Failed",task.exception)
                        if(task.exception.toString().contains("FirebaseAuthUserCollisionException")){
                            Toast.makeText(context,"This user already exists",Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(context,getString(R.string.error),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }

    private fun updateUI(user:FirebaseUser?){
        //Authenticated Successfully
        if(user!= null){
            startActivity(Intent(context,EventsActivity::class.java))
            activity.finish()
        } else{
            Toast.makeText(context,getString(R.string.error),Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToDatabase(user:FirebaseUser?){
        if(user!=null){
            val displayName:String= fname?.text.toString().trim()+" "+lname?.text.toString().trim()
            val enrolment=enrol?.text.toString().trim()
            val year=spinner?.selectedItem.toString()
            var course:String=""
            if(courseRadioGroup?.checkedRadioButtonId==R.id.mca){
                course=getString(R.string.mca)
            } else {
                course=getString(R.string.btechmtech)
            }
            var branch:String=""
            if(branchRadioGroup?.checkedRadioButtonId==R.id.cse){
                branch=getString(R.string.cse)
            } else if(branchRadioGroup?.checkedRadioButtonId==R.id.it_course){
                branch=getString(R.string.it)
            } else if(branchRadioGroup?.checkedRadioButtonId==R.id.ece){
                branch=getString(R.string.ece)
            } else{
                branch=other?.text.toString().trim()
            }
            val userModel:UserModel=UserModel(displayName,enrolment,course,year,branch)
            FirebaseDatabase.getInstance().reference.child("users").child(user.uid).setValue(userModel)
        }
    }

}// Required empty public constructor
