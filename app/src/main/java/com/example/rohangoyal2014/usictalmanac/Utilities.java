package com.example.rohangoyal2014.usictalmanac;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by RohanGoyal2014 on 3/15/2018.
 */

public class Utilities {
    public static class ValidationUtilities{
        public static boolean empty_validator(Context context, TextInputEditText editText, String s){
            if(editText.getText().toString().trim().isEmpty()){
                String message=s+" can not be blank";
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
    }
    public static ArrayAdapter<String> getSpinnerData(Context context){
        ArrayList<String> arrayList=new ArrayList<>();
        for(int i=0;i<6;++i){
            arrayList.add(String.valueOf(i+1));
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,arrayList);
        return  arrayAdapter;
    }
    public static class FirebaseUtilites{
        public static FirebaseAuth mAuth;
    }
}
