package com.example.vinamra.anganwadi_helpers;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class child_basic extends Fragment {
EditText name,fname,dob,mname,weight;
Integer id1;
String id;
Button submit;
child_reg child_reg;
String url="https://aproject2018.000webhostapp.com/module_jeetu/child_regestration.php";
    public child_basic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_basic, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        child_reg=new child_reg();
        child_reg.next= (Button) getActivity().findViewById(R.id.next);
        name= (EditText) getView().findViewById(R.id.name);
        fname= (EditText) getView().findViewById(R.id.fname);
        mname= (EditText) getView().findViewById(R.id.mname);
        dob= (EditText) getView().findViewById(R.id.dob);
        weight= (EditText) getView().findViewById(R.id.weight);
        submit= (Button) getView().findViewById(R.id.submit);
        final Random rand=new Random();
        id1=(rand.nextInt(9999-0+1)+0);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDetails()){
                    callServer();
                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd-MM-yyyy"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        dob.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    private void callServer() {
        final ProgressDialog loading=ProgressDialog.show(getContext(),"Saving Details....","please wait",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //circleLoadingView.stopOk();
                //   circleLoadingView.setVisibility(View.GONE);
              //  Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                if(response.equalsIgnoreCase("1")) {
                    Toast.makeText(getContext(), "Data Uploaded Successfully", Toast.LENGTH_LONG).show();
                    child_img img=new child_img();
                    Bundle b=new Bundle();
                    b.putString("id",id);
                    img.setArguments(b);
                    name.setEnabled(false);
                    fname.setEnabled(false);
                    mname.setEnabled(false);
                    dob.setEnabled(false);
                    weight.setEnabled(false);
                    submit.setVisibility(View.GONE);
                    child_reg.next.setVisibility(View.VISIBLE);
                }
                else
                    Toast.makeText(getContext(),"Error !!!!TRY AGAIN",Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  circleLoadingView.stopFailure();
                //  circleLoadingView.setVisibility(View.GONE);
                loading.dismiss();
                Toast.makeText(getContext(),"Error ,......."+error,Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                id=name.getText().toString()+id1;

                /*************Setting Child Preferences*****************/
                SharedPreferences regPref = getActivity().getSharedPreferences("registration_child", Context.MODE_PRIVATE);
                SharedPreferences.Editor refPrefEditor = regPref.edit();
                refPrefEditor.putString("childId", id);

                params.put("id",id);
                params.put("name",name.getText().toString());
                params.put("fname",fname.getText().toString());
                params.put("mname",mname.getText().toString());
                params.put("dob",dob.getText().toString());
                params.put("weight",weight.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private  boolean checkDetails(){
        if(checkName()||checkFname()||checkMname()||checkDob()||checkWeight()){
            return false;
        }else return true;
    }

    private boolean checkName() {
        if(name.getText().toString().trim().isEmpty()){
            name.setError("Enter Vaild Value");
            return true;
        }else  return false;
    }

    private boolean checkMname() {
        if(mname.getText().toString().trim().isEmpty()){
            mname.setError("Enter Vaild Value");
            return true;
        }else  return false;
    }

    private boolean checkFname() {
        if(fname.getText().toString().trim().isEmpty()){
            fname.setError("Enter Vaild Value");
            return true;
        }else  return false;
    }

    private boolean checkWeight() {
        if(weight.getText().toString().trim().isEmpty()){
            weight.setError("Enter Vaild Value");
            return true;
        }else  return false;
    }

    private boolean checkDob() {
        if(dob.getText().toString().trim().isEmpty()){
            dob.setError("Enter Vaild Value");
            return true;
        }else  return false;
    }
}
