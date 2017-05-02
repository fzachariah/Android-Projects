package com.example.febin.group21_hw09;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextReTypePassword;
    Button buttonSignUp;
    Button buttonBack;

    ProgressDialog progressDialog;


    Spinner genderSpinner;
    ArrayAdapter<CharSequence> staticAdapter;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");

    public SignUpFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getActivity());

        genderSpinner=(Spinner)getActivity().findViewById(R.id.spinnerGender);
        staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.gender_arrays,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(staticAdapter);

        editTextFirstName=(EditText)getActivity().findViewById(R.id.editTextFirstName);
        editTextLastName=(EditText)getActivity().findViewById(R.id.editTextLastName);
        editTextEmail=(EditText)getActivity().findViewById(R.id.editTextEmailSignUp);
        editTextPassword=(EditText)getActivity().findViewById(R.id.editTextPasswordSign);
        editTextReTypePassword=(EditText)getActivity().findViewById(R.id.editTextReType);
        buttonSignUp=(Button)getActivity().findViewById(R.id.buttonSignSign);
        buttonBack=(Button)getActivity().findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backPress();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpClicked();
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void signUpClicked() {

        final String firstName=editTextFirstName.getText().toString().trim();
        final String lastName=editTextLastName.getText().toString().trim();
        final String password= editTextPassword.getText().toString().trim();
        String reType=editTextReTypePassword.getText().toString().trim();
        final String email=editTextEmail.getText().toString().trim();
        final String gender=genderSpinner.getSelectedItem().toString().trim();

        if(firstName.length()==0 || lastName.length()==0 || password.length()==0 ||reType.length()==0||email.length()==0 ||gender.equals("Select Gender"))
        {
            toastMessage("Please Enter Correctly");
        }
        else if(!email.contains("@"))
        {
            toastMessage("Please Enter a Valid Email Address");
        }
        else if(!password.equals(reType))
        {
            toastMessage("Passwords are not matching!");
        }
        else
        {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Log.d("SignUpFragment:", "createUserWithEmail:onComplete:" + task.isSuccessful());
                    if(task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            UserDetails userDetails=new UserDetails();
                            userDetails.setEmail(email);
                            userDetails.setFirstName(firstName);
                            userDetails.setLastName(lastName);
                            userDetails.setPassword(password);
                            userDetails.setGender(gender);
                            userDetails.setId(user.getUid());
                            myRef.child(userDetails.getId()).setValue(userDetails);
                            toastMessage("Successfully Registered");
                            progressDialog.dismiss();
                            mListener.successSignUp(user,userDetails);
                        }

                    }
                    else
                    {
                        Log.d("Exception: ",""+task.getException());
                        toastMessage("Already Registered!");
                    }


                }
            });
        }

    }

    public void toastMessage(String message)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toastmessage,
                (ViewGroup)getActivity(). findViewById(R.id.linearLayoutContainer));

        TextView text = (TextView) layout.findViewById(R.id.textViewToast);
        text.setText(message);

        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void successSignUp(FirebaseUser firebaseUser,UserDetails userDetails);

        void backPress();
    }


}
