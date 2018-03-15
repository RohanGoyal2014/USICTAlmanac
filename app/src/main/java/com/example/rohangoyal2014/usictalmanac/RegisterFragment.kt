package com.example.rohangoyal2014.usictalmanac


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_register.*
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    var submit:TextView?=null
    var fname:TextInputEditText?=null
    var lname:TextInputEditText?=null
    var enrol:TextInputEditText?=null
    var email:TextInputEditText?=null
    var other:TextInputEditText?=null
    var pass:TextInputEditText?=null
    var conf_pass:TextInputEditText?=null
    var spinner:Spinner?=null
    var arr:ArrayList<Pair<TextInputEditText,String>>?=ArrayList<Pair<TextInputEditText,String>>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout= inflater!!.inflate(R.layout.fragment_register, container, false)
        submit=layout.findViewById<TextView>(R.id.submit)
        fname=layout.findViewById<TextInputEditText>(R.id.fname)
        lname=layout.findViewById<TextInputEditText>(R.id.lname)
        enrol=layout.findViewById<TextInputEditText>(R.id.enrolment)
        email=layout.findViewById<TextInputEditText>(R.id.email)
        other=layout.findViewById<TextInputEditText>(R.id.other_text)
        pass=layout.findViewById<TextInputEditText>(R.id.pass)
        conf_pass=layout.findViewById<TextInputEditText>(R.id.conf_pass)
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
            }
        }
        return layout
    }

}// Required empty public constructor
