package com.example.sanja.inclass11;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    EditText fName;
    EditText lName;
    EditText email;
    EditText password;
    EditText repeatPass;
    Button cancelSignup;
    Button SignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();

        fName = (EditText)findViewById(R.id.editTextFirstName);
        lName = (EditText)findViewById(R.id.editText2LName);
        email = (EditText)findViewById(R.id.editTextEmailSignup);
        password = (EditText)findViewById(R.id.editTextPassSignup);
        repeatPass = (EditText)findViewById(R.id.editTextRepeatPass);
        cancelSignup = (Button)findViewById(R.id.buttonCancelSignup);
        SignUp = (Button)findViewById(R.id.buttonSignupSignup);

        cancelSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = fName.getText().toString();
                String lname = lName.getText().toString();
                String email1 = email.getText().toString();
                String pass1 = password.getText().toString();
                String reppass = repeatPass.getText().toString();

                if(fname.length()==0 | lname.length()==0 | email1.length()==0 | pass1.length()==0|reppass.length()==0|!(email1.contains("@")) | !(pass1.equals(reppass))){

                    Toast.makeText(SignUpActivity.this, "Enter correct details", Toast.LENGTH_SHORT).show();
                }
                else {

                    signupFunction(fname,lname,email1,pass1,reppass);

                }


            }
        });

    }

    private void signupFunction(final String fname, final String lname, String email1, String pass1, String reppass) {

        mAuth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fname+" "+lname).build();
                    user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(SignUpActivity.this, "Successfully added the user", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                }
                else{
                    Toast.makeText(SignUpActivity.this, "Error occured.Please try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
