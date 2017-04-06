package com.example.febin.inclass2a;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonSubmit;
    EditText editTextWeight;
    EditText editTextFeet;
    EditText editTextInches;

    TextView textViewResultBMI;
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");

        editTextWeight=(EditText)findViewById(R.id.editTextWeight);
        editTextFeet=(EditText)findViewById(R.id.editTextFeet);
        editTextInches =(EditText)findViewById(R.id.editTextInches);

        buttonSubmit =(Button)findViewById(R.id.buttonCalculate);
        textViewResult =(TextView)findViewById(R.id.textViewResultTwo);
        textViewResultBMI=(TextView)findViewById(R.id.textViewResultOne);




        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String weightString=editTextWeight.getText().toString();

                String feetString=editTextFeet.getText().toString();

                String inchesString=editTextInches.getText().toString();

                if(weightString.length()==0 || feetString.length()==0 || inchesString.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Inputs",Toast.LENGTH_LONG).show();
                }
                else {
                    double weight=Double.parseDouble(weightString);
                    double feet=Double.parseDouble(feetString);
                    double inches=Double.parseDouble(inchesString);
                    double heightInches = (feet * 12) + inches;
                    double BMI = (weight * 703) / (Math.pow(heightInches, 2));
                    BMI=(double)Math.round(BMI * 10d) / 10d;
                    textViewResultBMI.setText("Your BMI:" + BMI);

                    if (BMI <= 18.5) {
                        textViewResult.setText("You are Underweight");
                    } else if (BMI > 18.5 && BMI <= 24.9) {
                        textViewResult.setText("You are Normalweight");
                    } else if (BMI >= 25 && BMI <= 29.9) {
                        textViewResult.setText("You are Overweight");
                    } else if (BMI >= 30.0) {
                        textViewResult.setText("You are Obesity");
                    }
                    Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
