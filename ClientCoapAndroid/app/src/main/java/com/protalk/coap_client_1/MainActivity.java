package com.protalk.coap_client_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.CoapResponse;

public class MainActivity extends AppCompatActivity {

    MyCoapGet myCoap;
    Button btnGet;
    TextView textGet;
    Button btnPut;
    EditText editPut;
    EditText edit;
    TextView textAdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCoap = new MyCoapGet("coap://[2005::ba27:ebff:fe3c:ba5a]/", getApplicationContext());

        btnGet = (Button)findViewById(R.id.btnGet);
        textGet = (TextView)findViewById(R.id.textGet);
        btnPut = (Button)findViewById(R.id.btnPut);
        editPut = (EditText)findViewById(R.id.editPayload);
        edit = (EditText)findViewById(R.id.editText);

        textAdv = (TextView)findViewById(R.id.textAdvanced);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoapResponse response = myCoap.onSensorGet(edit.getText().toString());
                textGet.setText("get : " + response.getResponseText());
                textAdv.setText(response.advanced().getType() + "-" + response.getCode());
            }
        });


        btnPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoapResponse response =
                        myCoap.onSensorPost(edit.getText().toString(), editPut.getText().toString());
                textAdv.setText(response.advanced().getType() + "-" + response.getCode());
            }
        });


    }

}
