package com.example.rohangoyal2014.usictalmanac


import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    var enrol:TextInputEditText?=null
    var pass:TextInputEditText?=null
    var submit:TextView?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout= inflater!!.inflate(R.layout.fragment_login, container, false)
        enrol=layout.findViewById<TextInputEditText>(R.id.enrolment)
        pass=layout.findViewById<TextInputEditText>(R.id.pass)
        submit=layout.findViewById<TextView>(R.id.submit)
        submit?.setOnClickListener{
            var res=false
            res=Utilities.ValidationUtilities.empty_validator(context,enrol,getString(R.string.enrolment))
            if(res){
                return@setOnClickListener
            }
            res=Utilities.ValidationUtilities.empty_validator(context,pass,getString(R.string.password))
            if(res){
                return@setOnClickListener
            }
            else{
                //TODO:Login Process
            }
        }
        return layout
    }

}// Required empty public constructor
