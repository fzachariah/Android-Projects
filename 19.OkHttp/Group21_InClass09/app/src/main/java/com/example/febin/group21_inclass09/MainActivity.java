package com.example.febin.group21_inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    SharedPreferences appDetails;

    EditText editTextEmailLogin;
    EditText editTextPasswordLogin;

    EditText editTextEmailSignUp;
    EditText editTextPasswordSignUp;
    EditText editTextFirstName;
    EditText editTextLastName;
    boolean check=true;

    Button buttonLogin;
    Button buttonSignUp;


    public void successFull()
    {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, " Operation Successful",
                        Toast.LENGTH_LONG).show();
            }
        });
        Intent intent=new Intent(MainActivity.this,MainScreen.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDetails = getSharedPreferences("userSession",
                Context.MODE_PRIVATE);
        setTitle("Chit Chat");
        editTextEmailLogin=(EditText) findViewById(R.id.editTextEmail);
        editTextEmailSignUp=(EditText) findViewById(R.id.editTextEmailSignup);

        editTextPasswordLogin=(EditText)findViewById(R.id.editTextPassword);
        editTextPasswordSignUp=(EditText)findViewById(R.id.editTextAPasswordSign);

        editTextFirstName=(EditText)findViewById(R.id.editTextFirstName);
        editTextLastName=(EditText)findViewById(R.id.editTextLastName);

        buttonLogin=(Button)findViewById(R.id.buttonLogin);
        buttonSignUp=(Button)findViewById(R.id.buttonSignUp);

        if(appDetails.contains("email")&& appDetails.contains("token"))
        {
            callLogin();
        }

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName=editTextFirstName.getText().toString();
                String lastName=editTextLastName.getText().toString();
                final String email=editTextEmailSignUp.getText().toString();
                 final String password=editTextPasswordSignUp.getText().toString();

                if(firstName.length()==0 || lastName.length()==0 ||email.length()==0||password.length()==0||!email.contains("@"))
                {
                    Toast.makeText(getApplicationContext(),"Enter values Correctly",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody formBody = new FormBody.Builder()
                            .add("email", email)
                            .add("password",password)
                            .add("fname",firstName)
                            .add("lname",lastName)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://52.90.79.130:8080/Groups/api/signUp")
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(okhttp3.Call call, Response response) throws IOException {

                           // Log.d("testing here chck",""+response.body().string());
                            String val=response.body().string().toString();
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(val);
                                String status=jsonObject.getString("status");
                                        if(status.equals("1"))
                                        {
                                            String token=jsonObject.getString("data");

                                            SharedPreferences.Editor editor = appDetails.edit();
                                            editor.putString("token", token);
                                            editor.putString("email",email);
                                            editor.putString("password",password);
                                            editor.commit();
                                            check=true;
                                            Log.d("Test",appDetails.getString("token","0"));
                                            successFull();
                                        }
                                        else
                                        {
                                            check=false;

                                        }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        }
                    });
                }


            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=editTextEmailLogin.getText().toString();
                final String password=editTextPasswordLogin.getText().toString();

                if(email.length()==0 ||password.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Enter values Correctly",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    OkHttpClient okHttpClient=new OkHttpClient();

                    RequestBody formBody = new FormBody.Builder()
                            .add("email", email)
                            .add("password",password)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://52.90.79.130:8080/Groups/api/login")
                            .post(formBody)
                            .build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(okhttp3.Call call, Response response) throws IOException {

                            // Log.d("testing here chck",""+response.body().string());
                            String val=response.body().string().toString();
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(val);
                                String status=jsonObject.getString("status");
                                if(status.equals("1"))
                                {
                                    String token=jsonObject.getString("data");

                                    SharedPreferences.Editor editor = appDetails.edit();
                                    editor.putString("token", token);
                                    editor.putString("email",email);
                                    editor.putString("password",password);
                                    editor.commit();
                                    check=true;
                                    Log.d("Test",appDetails.getString("token","0"));
                                    successFull();
                                }
                                else
                                {
                                    check=false;

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });




                }

            }
        });

    }
    public void callLogin() {
        final String email = appDetails.getString("email", null);

        final String password = appDetails.getString("password", null);

        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter values Correctly", Toast.LENGTH_SHORT).show();
        } else {

            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url("http://52.90.79.130:8080/Groups/api/login")
                    .post(formBody)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {

                    // Log.d("testing here chck",""+response.body().string());
                    String val = response.body().string().toString();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(val);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            String token = jsonObject.getString("data");

                            SharedPreferences.Editor editor = appDetails.edit();
                            editor.putString("token", token);
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();
                            check = true;
                            Log.d("Test", appDetails.getString("token", "0"));
                            successFull();
                        } else {
                            check = false;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}
