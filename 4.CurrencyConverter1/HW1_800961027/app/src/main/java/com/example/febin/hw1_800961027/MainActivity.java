package com.example.febin.hw1_800961027;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupInitial;
    private RadioGroup radioGroupFinal;
    private EditText input;
    private Button convert;
    private RadioButton radioButtonInitial;
    private RadioButton radioButtonFinal;
    private TextView textViewOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroupInitial =(RadioGroup)findViewById(R.id.radioGroup);
        radioGroupFinal =(RadioGroup)findViewById(R.id.radioGroup3);
        convert=(Button)findViewById(R.id.buttonConvert);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int initialSelectedId = radioGroupInitial.getCheckedRadioButtonId();
                int finalSelectedId = radioGroupFinal.getCheckedRadioButtonId();
                if(initialSelectedId!=-1 &&finalSelectedId !=-1) {

                    input=(EditText)findViewById(R.id.editTextInput);
                    String value=input.getText().toString();
                    if(value==null || value.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Input,Enter a currency amount on the provided input box",Toast.LENGTH_LONG).show();
                    }
                    else {

                        radioButtonInitial = (RadioButton) findViewById(initialSelectedId);
                        radioButtonFinal = (RadioButton) findViewById(finalSelectedId);
                        String initialValue = radioButtonInitial.getText().toString();
                        String finalValue = radioButtonFinal.getText().toString();
                        Log.d("Initial Currency", initialValue);
                        Log.d("Final Currency", "" + finalValue);
                        textViewOutput=(TextView)findViewById(R.id.textAnswer);
                        DecimalFormat df = new DecimalFormat("#.00");
                        if(initialValue.equals("AUD"))
                        {
                            double result=Double.parseDouble(value)/1.34;
                            if(finalValue.equals("USD"))
                            {
                                String formatted = df.format(result);
                                textViewOutput.setText(""+formatted);
                            }
                            else if(finalValue.equals("GBP"))
                            {
                                String formatted = df.format(result*0.83);
                                textViewOutput.setText(formatted);
                            }
                        }
                        else if(initialValue.equals("CAD"))
                        {
                            double result=Double.parseDouble(value)/1.32;

                            if(finalValue.equals("USD"))
                            {
                                String formatted = df.format(result);
                                textViewOutput.setText(""+formatted);
                            }
                            else if(finalValue.equals("GBP"))
                            {
                                String formatted = df.format(result*0.83);
                                textViewOutput.setText(formatted);
                            }
                        }
                        else if(initialValue.equals("INR"))
                        {
                            double result=Double.parseDouble(value)/68.14;

                            if(finalValue.equals("USD"))
                            {
                                String formatted = df.format(result);
                                textViewOutput.setText(""+formatted);
                            }
                            else if(finalValue.equals("GBP"))
                            {
                                String formatted = df.format(result*0.83);
                                textViewOutput.setText(formatted);
                            }
                        }



                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Select the currencies from the radio buttons for conversion",Toast.LENGTH_LONG).show();
                }





            }
        });
    }

    public  void clearData(View view)
    {
        textViewOutput=(TextView)findViewById(R.id.textAnswer);
        textViewOutput.setText("");
        input=(EditText)findViewById(R.id.editTextInput);
        input.setText("");
        radioGroupInitial =(RadioGroup)findViewById(R.id.radioGroup);
        radioGroupFinal =(RadioGroup)findViewById(R.id.radioGroup3);
        radioGroupFinal.clearCheck();
        radioGroupInitial.clearCheck();
    }
}
