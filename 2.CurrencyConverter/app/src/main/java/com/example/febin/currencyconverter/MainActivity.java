package com.example.febin.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public void convert(View view)
    {
        EditText editText=(EditText)findViewById(R.id.DollarField);
        Log.i("Value",editText.getText().toString());
        Double dollarAmount= Double.parseDouble(editText.getText().toString());
        Double poundAmount=dollarAmount*0.65;
        Toast.makeText(getApplicationContext(),"Amount is "+poundAmount,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
