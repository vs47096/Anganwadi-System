package com.example.vinamra.anganwadi_helpers;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DR_Reg extends AppCompatActivity {
EditText dr_name,dateofcoming,orgname,childname,childname1;
ImageView add;
Spinner spinner,spinner1;
Button submit;
int i=0;
String child_data="";
LinearLayout linear,linear1;
    Adapter adapter;
    String url="https://aproject2018.000webhostapp.com/module_jeetu/Doctor_reg.php";
ArrayList<String> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr__reg);
        dr_name= (EditText) findViewById(R.id.name);
        dateofcoming= (EditText) findViewById(R.id.date);
        orgname= (EditText) findViewById(R.id.org);
        childname= (EditText) findViewById(R.id.childname);
        spinner= (Spinner) findViewById(R.id.spinner);
        add= (ImageView) findViewById(R.id.add);
        submit= (Button) findViewById(R.id.submit);
        linear= (LinearLayout) findViewById(R.id.linear);
        linear1= (LinearLayout) findViewById(R.id.linear1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDetails())
                    callServer();
            }
        });
        items.add("Select");
        items.add("BCG");
        items.add("DPT-1");
        items.add("DPT-2");
        items.add("DPT-3");
        items.add("DPT-1(Booster)");
        items.add("Vitamin A(1ml)");
        items.add("DPT-2(Booster)");
        items.add("Vitamin A(2ml)");

         adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
        spinner.setAdapter((SpinnerAdapter) adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != 0) {
                    if (childname1.getText().toString().trim().isEmpty()) {
                        childname1.setError("enter valid value");
                    } else if (spinner1.getSelectedItem().equals("Select"))
                        Toast.makeText(DR_Reg.this, "Select One Vactine", Toast.LENGTH_LONG).show();
                    else {
                        child_data +=  childname1.getText().toString() + "-->" + spinner1.getSelectedItem()+"\n";
                        callAddView();
                    }
                }else{
                    if (childname.getText().toString().trim().isEmpty()) {
                        childname.setError("enter valid value");
                    } else if (spinner.getSelectedItem().equals("Select"))
                        Toast.makeText(DR_Reg.this, "Select One Vactine", Toast.LENGTH_LONG).show();
                    else {
                        child_data +=  childname.getText().toString() + "-->" + spinner.getSelectedItem()+"\n";
                        callAddView();
                    }
                }
            }
        });
        dateofcoming.setOnClickListener(new View.OnClickListener() {
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

                        dateofcoming.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(DR_Reg.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
       // linear.addView(linear1);
        setContentView(linear);
    }

    private void callAddView() {
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        childname1=new EditText(this);
        childname1.setWidth(400);
        childname1.setHint(" Child Name");
        childname1.setTop(20);
        childname1.setPadding(5,20,5,5);
        childname1.setBackgroundResource(R.drawable.edittext_background);
        childname1.setId(i++);
        spinner1=new Spinner(this);
        spinner1.setMinimumWidth(260);
        spinner1.setPadding(20,25,5,5);
        spinner1.setId(i++);
        spinner1.setAdapter((SpinnerAdapter) adapter);
        linearLayout.addView(childname1);
        linearLayout.addView(spinner1);
     //   child_data+="\n"+childname1.getText().toString()+"->"+spinner1.getSelectedItem();
        TableRow.LayoutParams params=new TableRow.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,30,0,5);
        linearLayout.setLayoutParams(params);
       linear1.addView(linearLayout);
    }

    private void callServer() {
       //child_data+=childname.getText().toString()+"->"+spinner.getSelectedItem();
      //  for(int l=1;l<i;l++)
           child_data+=childname1.getText().toString()+"-->"+spinner1.getSelectedItem();
      //  Toast.makeText(DR_Reg.this,child_data,Toast.LENGTH_LONG).show();
        final ProgressDialog loading=ProgressDialog.show(DR_Reg.this,"Saving Details....","please wait",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //circleLoadingView.stopOk();
                //   circleLoadingView.setVisibility(View.GONE);
                //  Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                if(response.equalsIgnoreCase("1")) {
                    Toast.makeText(DR_Reg.this, "Data Uploaded Successfully", Toast.LENGTH_LONG).show();
                    dr_name.setEnabled(false);
                    orgname.setEnabled(false);
                    dateofcoming.setEnabled(false);
                    spinner1.setVisibility(View.GONE);
                    childname1.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    childname.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                }
                else
                    Toast.makeText(DR_Reg.this,"Error !!!!TRY AGAIN",Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  circleLoadingView.stopFailure();
                //  circleLoadingView.setVisibility(View.GONE);
                loading.dismiss();
                Toast.makeText(DR_Reg.this,"Error ,......."+error,Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dname",dr_name.getText().toString());
                params.put("org",orgname.getText().toString());
                params.put("date",dateofcoming.getText().toString());
                params.put("childdata",child_data);
                return params;
            }
        };
        MySingleton.getInstance(DR_Reg.this).addToRequestQueue(stringRequest);
    }

    private boolean checkDetails() {
        if(isName()||isorg()||isDate())
            return  true;
        else  return  false;
    }
    
    public  boolean isName(){
        if(dr_name.getText().toString().trim().isEmpty()){
            dr_name.setError("Enter valid Value");
            return false;
        }else  return  true;
    }


    public  boolean isorg(){
        if(orgname.getText().toString().trim().isEmpty()){
            orgname.setError("Enter valid Value");
            return false;
        }else  return  true;
    }


    public  boolean isDate(){
        if(dateofcoming.getText().toString().trim().isEmpty()){
            dateofcoming.setError("Enter valid Value");
            return false;
        }else  return  true;
    }
}
