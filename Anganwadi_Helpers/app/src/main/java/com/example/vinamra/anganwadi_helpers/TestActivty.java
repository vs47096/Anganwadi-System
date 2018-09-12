package com.example.vinamra.anganwadi_helpers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Vinamra on 3/26/2018.
 */

public class TestActivty extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initialize();
    }

    private void initialize() {
        final EditText testEditText=(EditText) findViewById(R.id.testEditText);
        Button btn=(Button)findViewById(R.id.testEditBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= String.valueOf(testEditText.getText());
                Intent i=new Intent();
                i.putExtra("message",name);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }
}
