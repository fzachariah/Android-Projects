package com.example.febin.group21_hw09;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,SignUpFragment.OnFragmentInteractionListener,HomePageFragment.OnFragmentInteractionListener ,GoogleApiClient.OnConnectionFailedListener,ChatFragment.OnFragmentInteractionListener{

    private final static String USER_DETAILS="userDetails";
    private final static String TRIP_DETAILS="tripDetails";

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    SharedPreferences favorites;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");
    HashMap<String,UserDetails> userDetailsHashMap=new HashMap<>();

    boolean isGoogle=false;

    String firstName;
    String lastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favorites = getSharedPreferences("tripDetails",
                Context.MODE_PRIVATE);
        setTitle("TripMate");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserDetails user = postSnapshot.getValue(UserDetails.class);
                    Log.d("DataPoint1", user.toString());
                    userDetailsHashMap.put(user.getId(),user);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        Log.d("DataPoint2",""+userDetailsHashMap.size());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
        Log.d("DataPoint3",""+userDetailsHashMap.size());
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d("DataPoint4",""+userDetailsHashMap.size());
                if (user != null) {
                    UserDetails userDetails=userDetailsHashMap.get(user.getUid());
                    if(userDetails==null&&isGoogle)
                    {
                        Log.d("important",""+user.getUid());
                        userDetails=new UserDetails();
                        userDetails.setId(user.getUid());
                        userDetails.setEmail(user.getEmail());
                        userDetails.setLastName(lastName);
                        userDetails.setFirstName(firstName);
                        myRef.child(user.getUid()).setValue(userDetails);

                    }
                    if(!favorites.contains("Imp")) {

                        if(userDetails!=null) {
                            if (userDetails.getPassword() == null || userDetails.getPassword().length() == 0) {
                                isGoogle = true;
                                Log.d("DataPointInside",""+isGoogle);
                            }
                        }
                        Log.d("Checking the status", "lets see");
                        HomePageFragment homePageFragment = new HomePageFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(USER_DETAILS, user.getUid());
                        homePageFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.container, homePageFragment, "homeFragment").commit();
                    }
                    else
                    {
                        SharedPreferences.Editor editor = favorites.edit();
                        editor.remove("Imp");
                        editor.commit();
                    }


                } else {
                    LoginFragment loginFragment=new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container, loginFragment, "loginFragment").commit();
                    Log.d("LogData:", "Initial");
                }

            }
        };


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("222222222222222","reached");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInResult account = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(account);
            } else {

            }
        }

        else
        {
            Log.d("222222222222222","reachedActivity");
            super.onActivityResult(requestCode, resultCode, data);
        }


    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("test123:", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);

        } else {

        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("Test123", "firebaseAuthWithGoogle:" + acct.getId());
        String temp=acct.getDisplayName();
        firstName=temp.substring(0,temp.indexOf(" "));
        lastName=temp.substring(temp.indexOf(" ")).trim();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Test", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            toastMessage("Please Try Again");
                        }
                        else
                        {

                        }

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }



    @Override
    public void clickSign() {

        Log.d("CheckClick","positive");
        SignUpFragment signUpFragment=new SignUpFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, signUpFragment, "signFragment1").commit();

    }



    @Override
    public void sendMessage() {
        isGoogle=true;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void successSignUp(FirebaseUser firebaseUser, UserDetails userDetails) {
        Log.d("Success Login",userDetails.toString());
        if(firebaseUser!=null)
        {
            HomePageFragment homePageFragment=new HomePageFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(USER_DETAILS, userDetails.getId());
            homePageFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.container, homePageFragment, "homeFragment").commit();
        }
    }

    @Override
    public void onBackPressed() {

        SharedPreferences.Editor editor = favorites.edit();
        editor.clear();
        editor.commit();

        if(getFragmentManager().getBackStackEntryCount()>0)
        {
            Log.d("Popped Out","Test"+getFragmentManager().getBackStackEntryCount());
            getFragmentManager().popBackStack();
            Log.d("Popped Out","Test");
        }
        else {
            super.onBackPressed();
        }


    }

    @Override
    public void backPress() {

        LoginFragment loginFragment=new LoginFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, loginFragment, "loginFragment").commit();
        Log.d("LogData:", "Initial");
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_itemLogout:

                if(getFragmentManager().getBackStackEntryCount()>0)
                {
                    Log.d("Popped Out","Test"+getFragmentManager().getBackStackEntryCount());
                    getFragmentManager().popBackStack();
                    Log.d("Popped Out","Test");
                }
                if(!isGoogle) {
                    FirebaseAuth.getInstance().signOut();
                    LoginFragment loginFragment = new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container, loginFragment, "loginFragment1").commit();
                    Log.d("Logout:", "onAuthStateChanged:signed_out");
                }
                else
                {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                            FirebaseAuth.getInstance().signOut();
                            LoginFragment loginFragment = new LoginFragment();
                            getFragmentManager().beginTransaction().replace(R.id.container, loginFragment, "loginFragment1").commit();
                            Log.d("Logout:", "onAuthStateChanged:signed_out");
                            isGoogle=false;

                        }
                    });
                }
                SharedPreferences.Editor editor = favorites.edit();
                editor.clear();
                editor.commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void toastMessage(String message)
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toastmessage,
                (ViewGroup) findViewById(R.id.linearLayoutContainer));

        TextView text = (TextView) layout.findViewById(R.id.textViewToast);
        text.setText(message);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void chatActvitiyCalled(UserDetails currrentUser, TripDetails tripDetails) {

        Log.d("Reached Chat",currrentUser.toString());
        Log.d("Reached Chat1",tripDetails.toString());
        ChatFragment chatFragment=new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_DETAILS, currrentUser.getId());
        bundle.putSerializable(TRIP_DETAILS, tripDetails.getTripId());
        chatFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, chatFragment, "ChatFragment").addToBackStack(null).commit();
    }







}
