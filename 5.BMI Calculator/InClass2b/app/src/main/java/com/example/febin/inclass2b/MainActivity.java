package com.example.febin.inclass2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weight Estimator");
        final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        final EditText et = (EditText) findViewById(R.id.editText);
        final EditText et2 = (EditText) findViewById(R.id.editText2);
        final TextView result = (TextView) findViewById(R.id.textViewResult);


        Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rid = rg.getCheckedRadioButtonId();
                String feet = et.getText().toString();
                String inch = et2.getText().toString();

                if(feet.length()==0 ||inch.length()==0 || rid==-1)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Inputs",Toast.LENGTH_LONG).show();
                }
                else {


                    Double f = Double.parseDouble(feet);
                    f = f * 12;
                    Double i = Double.parseDouble(inch);


                    if (rid == R.id.radioButton) {
                        Double bmi = 18.5;
                        double est_weight = (bmi / 703) * (Math.pow((f + i), 2));
                        est_weight = (double) Math.round(est_weight * 10d) / 10d;
                        result.setText("Your weight should be less than" + est_weight + " amount");


                    } else if (rid == R.id.radioButton2) {
                        double w1 = (18.5 / 703) * (Math.pow((f + i), 2));
                        double w2 = (24.9 / 703) * (Math.pow((f + i), 2));
                        w1 = (double) Math.round(w1 * 10d) / 10d;
                        w2 = (double) Math.round(w2 * 10d) / 10d;
                        result.setText("Your weight should be in between" + w1 + "to " + w2 + " amount");

                    } else if (rid == R.id.radioButton3) {
                        double w1 = (24.9 / 703) * (Math.pow((f + i), 2));
                        double w2 = (29.9 / 703) * (Math.pow((f + i), 2));
                        w1 = (double) Math.round(w1 * 10d) / 10d;
                        w2 = (double) Math.round(w2 * 10d) / 10d;
                        result.setText("Your weight should be in between" + w1 + "to " + w2 + " amount");

                    } else if (rid == R.id.radioButton4) {
                        double w1 = (29.9 / 703) * (Math.pow((f + i), 2));
                        w1 = (double) Math.round(w1 * 10d) / 10d;
                        result.setText("Your weight should be greater than" + w1 + " amount");

                    }
                    Toast.makeText(getApplicationContext(), "Weight Calculated", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
