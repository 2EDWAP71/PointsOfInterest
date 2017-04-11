package com.example.eds.geolocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import static com.example.eds.geolocation.R.id.activity_main;



/**
 * Created by Eds on 11/04/2017.
 */

public class AddInterest extends Activity implements View.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);



    }


    public void onClick (View v){

        TextView textView = (TextView) findViewById(R.id.editText);
        textView.setOnClickListener(this);

        TextView textView1 = (TextView) findViewById(R.id.editText2);
        textView1.setOnClickListener(this);

        TextView textView2 = (TextView) findViewById(R.id.editText3);
        textView2.setOnClickListener(this);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        boolean button = false;
        String editText = "";
        String editText2 = "";
        String editText3= "";

        if (v.getId() == R.id.button){
            button = true;


        }
        bundle.putBoolean("com.example.button",button);
        bundle.putString("com.example.tx1",editText);
        bundle.putString("com.example.tx2",editText2);
        bundle.putString("com.example.tx3", editText3);


        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

}
