package com.example.febin.group21_hw09;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ChatFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private OnFragmentInteractionListener mListener;

    private final static String USER_DETAILS="userDetails";
    private final static String TRIP_DETAILS="tripDetails";

    LocationManager locationManager;
    Context mContext;
    ImageView imageViewTrip;
    TextView textViewTitle;
    ScrollView scrollView;

    ProgressDialog progressDialog;

    SharedPreferences favorites;

    ImageButton imageButtonSend;
    ImageButton imageButtonGallery;
    EditText editTextMessage;
    LinearLayout linearLayout;

    ImageButton imageButtonSearchLoc;
    ImageButton imageButtonViewMap;
    ImageButton imageButtonMapDelete;
    ImageButton imageButtonDirections;

    private final static int PLACE_PICKER_REQUEST = 105;
    private GoogleApiClient mGoogleApiClient;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    String userId;
    String tripId;
    String message="";

    UserDetails userDetails;
    TripDetails mainTrip;

    ArrayList<String> membersId=new ArrayList<>();
    ArrayList<UserDetails> tripMembers=new ArrayList<>();

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");
    private DatabaseReference tripRef = mDatabase.child("Trips");

    public ChatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId=(String) getArguments().getSerializable(USER_DETAILS);
        tripId =(String)getArguments().getSerializable(TRIP_DETAILS);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        favorites = getActivity().getSharedPreferences("tripDetails",
                Context.MODE_PRIVATE);
        mContext=getActivity();
        progressDialog=new ProgressDialog(mContext);
        mGoogleApiClient = new GoogleApiClient
                .Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        Log.d("Checking Members",""+membersId.toString());
        imageViewTrip=(ImageView)getActivity().findViewById(R.id.imageViewTripChat);
        textViewTitle=(TextView)getActivity().findViewById(R.id.textViewChatTripName);
        editTextMessage=(EditText)getActivity().findViewById(R.id.editTextNewMessage);
        imageButtonSend=(ImageButton)getActivity().findViewById(R.id.btnSend);
        imageButtonGallery=(ImageButton)getActivity().findViewById(R.id.btnGallery);
        linearLayout=(LinearLayout) getActivity().findViewById(R.id.linearLayoutMessages);
        scrollView=(ScrollView)getActivity().findViewById(R.id.scrollView);
        imageButtonSearchLoc=(ImageButton)getActivity().findViewById(R.id.imageButtonSearch);
        imageButtonViewMap=(ImageButton)getActivity().findViewById(R.id.imageButtonViewMap);
        imageButtonMapDelete=(ImageButton)getActivity().findViewById(R.id.imageButtonDeleteLoc);
        imageButtonDirections=(ImageButton)getActivity().findViewById(R.id.imageButtonNavigate);
        scrollView.fullScroll(View.FOCUS_DOWN);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserDetails user = postSnapshot.getValue(UserDetails.class);
                    if(user.getId().equals(userId))
                    {
                        Log.d("Inside Check",user.toString());
                        userDetails=user;
                        setMessage();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tripMembers.clear();
                tripMembers=new ArrayList<UserDetails>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserDetails user = postSnapshot.getValue(UserDetails.class);
                        tripMembers.add(user);
                }
                Log.d("TRip Members",""+tripMembers.toString());
                Log.d("Check size ",""+tripMembers.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TripDetails tripDetails=postSnapshot.getValue(TripDetails.class);
                    if(tripDetails.getTripId().equals(tripId))
                    {
                        mainTrip=tripDetails;
                        Log.d("Main Test1",mainTrip.toString());
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editTextMessage.getText().toString().trim();
                if(message.length()>0)
                {
                    sendMessage(message,0);
                }
            }
        });

        imageButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ChatFragment.this.startActivityForResult(intent,103);

            }
        });

        imageButtonSearchLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Do you want to add more locations for your trip?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               addLocation();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            }
        });

        imageButtonMapDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLocations();
            }
        });

        imageButtonViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewRoundTrip();

            }
        });

        imageButtonDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

    }

    private void openMap() {

        SharedPreferences.Editor editor = favorites.edit();
        editor.putBoolean("Imp",true);
        editor.commit();
        String cityName="";
        ArrayList<LocationDetails> locationDetailsArrayList=mainTrip.getLocationDetailsArrayList();
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (locationDetailsArrayList.size() > 0) {

                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                130);
                    }
                }

                Location location = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
                if (location != null) {

                    Log.d("Inside", "LOC " + location.getLatitude() + "<>" + location.getLongitude());
                    Geocoder geocoder = new Geocoder(mContext,
                            Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cityName = addresses.get(0).getAddressLine(0);

                }
                String navUrl = "";
                for (int i = 0; i < locationDetailsArrayList.size(); i++) {
                    navUrl = navUrl + "+to:" + locationDetailsArrayList.get(i).getLocationName();
                }
                navUrl = navUrl + "+to:" + cityName;
                Log.d("demo", "NAV URL " + navUrl.toString());

                final String uri = "http://maps.google.com/maps?daddr=" + cityName + navUrl;
                final Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");

                startActivity(intent);
            } else {
                Toast.makeText(mContext, "No Locations are added to Trip", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(mContext, "Please Enable GPS", Toast.LENGTH_LONG).show();
        }
    }

    private void viewRoundTrip() {

        SharedPreferences.Editor editor = favorites.edit();
        editor.putBoolean("Imp",true);
        editor.commit();

        ArrayList<LocationDetails> locationDetailsArrayList=mainTrip.getLocationDetailsArrayList();
        if(locationDetailsArrayList.size()>0) {

            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("Location", locationDetailsArrayList);
            startActivityForResult(intent, 250);
        }
        else
        {
            Toast.makeText(mContext,"No Locations are added to Trip",Toast.LENGTH_LONG).show();
        }

    }

    private void deleteLocations() {

        ArrayList<LocationDetails> locationDetailsArrayList=mainTrip.getLocationDetailsArrayList();
        if(locationDetailsArrayList.size()>0) {
            CharSequence[] ar = new String[locationDetailsArrayList.size()];
            final boolean[] checkedItems = new boolean[locationDetailsArrayList.size()];

            int i = 0;
            for (LocationDetails locationDetails : locationDetailsArrayList) {
                ar[i] = locationDetails.getLocationName();
                i++;
            }

            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setTitle("Select Locations to Delete").setMultiChoiceItems(ar, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                checkedItems[which] = true;

                            } else {
                                checkedItems[which] = false;

                            }
                        }
                    })
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            //Delete Location from main Trip
                            boolean change=false;
                            ArrayList<LocationDetails> locationDetailsArrayList = mainTrip.getLocationDetailsArrayList();
                            ArrayList<LocationDetails> actualList = new ArrayList<LocationDetails>();
                            int k = 0;
                            for (LocationDetails locationDetails : locationDetailsArrayList) {
                                if (!checkedItems[k]) {
                                    actualList.add(locationDetails);

                                }
                                else
                                {
                                    change=true;
                                }
                                k++;
                            }
                            mainTrip.setLocationDetailsArrayList(actualList);
                            tripRef.child(mainTrip.getTripId()).setValue(mainTrip);

                            //Delete Location from friendsList

                            ArrayList<String> membersTemp = mainTrip.getMembers();
                            Log.d("DeleteLocation123", "" + membersTemp.size());
                            for (int i = 0; i < tripMembers.size(); i++) {
                                UserDetails userDetails = tripMembers.get(i);
                                Log.d("Main Test12", "" + userDetails.getLastName());
                                if (membersTemp.contains(userDetails.getId())) {
                                    ArrayList<TripDetails> tempTrips1 = userDetails.getTripList();
                                    Log.d("Main Test1234", "" + userDetails.getTripList().size());
                                    TripDetails mainTrip1 = new TripDetails();
                                    for (int j = 0; j < tempTrips1.size(); j++) {
                                        if (tempTrips1.get(j).getTripId().equals(tripId)) {
                                            mainTrip1 = tempTrips1.get(j);
                                            break;
                                        }
                                    }
                                    tempTrips1.remove(mainTrip1);
                                    ArrayList<LocationDetails> locationDetailsArrayList1 = mainTrip1.getLocationDetailsArrayList();
                                    ArrayList<LocationDetails> actualListForMembers = new ArrayList<LocationDetails>();
                                    actualListForMembers.clear();
                                    int p = 0;
                                    for (LocationDetails locationDetails : locationDetailsArrayList1) {
                                        if (!checkedItems[p]) {
                                            actualListForMembers.add(locationDetails);
                                        }
                                        p++;
                                    }

                                    mainTrip1.setLocationDetailsArrayList(actualListForMembers);
                                    tempTrips1.add(mainTrip1);
                                    userDetails.setTripList(tempTrips1);
                                    myRef.child(userDetails.getId()).setValue(userDetails);
                                    if(change) {
                                        Toast.makeText(mContext, "Location Deleted from this Trip", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            dialog.show();
        }
        else
        {
            Toast.makeText(mContext,"No Location Added to this Trip",Toast.LENGTH_SHORT).show();
        }
    }

    private void addLocation() {

        SharedPreferences.Editor editor = favorites.edit();
        editor.putBoolean("Imp",true);
        editor.commit();

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 121: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Log.d("demo","not granted (permission)");
                }
                return;
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==103 &&resultCode==Activity.RESULT_OK &&data!=null &&data.getData()!=null) {
            try {

                SharedPreferences.Editor editor = favorites.edit();
                editor.putBoolean("Imp",true);
                editor.commit();
                Log.d("Image status","Ok");
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();
                final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                StorageReference imageRef=storageRef.child("messagePictures/"+uuid +".JPEG");
                UploadTask uploadTask = imageRef.putBytes(data1);
                uploadTask.addOnSuccessListener(getActivity(),new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
                        Log.d("God",downloadUrl.toString());
                        sendMessage(downloadUrl.toString(),1);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

            }
            catch (Exception e)
            {

            }
        }

        else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                progressDialog.show();
                Place place = PlacePicker.getPlace(data, mContext);

                if(place!=null) {
                    Log.d("Test1234567", "" + place.getName());
                }

                LocationDetails locationDetails=new LocationDetails();
                locationDetails.setId(""+place.getId());
                locationDetails.setLocationName(""+place.getName());
                LatLng latLng=place.getLatLng();
                locationDetails.setLat(""+latLng.latitude);
                locationDetails.setLongt(""+latLng.longitude);
                Log.d("Location12345",""+locationDetails.toString());
                ArrayList<LocationDetails> locationDetailses=mainTrip.getLocationDetailsArrayList();
                locationDetailses.add(locationDetails);
                tripRef.child(mainTrip.getTripId()).setValue(mainTrip);

                ArrayList<TripDetails>  tempTrips=userDetails.getTripList();
                TripDetails mainTripX=new TripDetails();
                for(int i=0;i<tempTrips.size();i++)
                {
                    if(tempTrips.get(i).getTripId().equals(tripId))
                    {
                        mainTripX=tempTrips.get(i);
                        break;
                    }
                }
                tempTrips.remove(mainTripX);
                ArrayList<LocationDetails> locationDetailsesX=mainTripX.getLocationDetailsArrayList();
                locationDetailsesX.add(locationDetails);
                mainTripX.setLocationDetailsArrayList(locationDetailsesX);
                tempTrips.add(mainTripX);
                userDetails.setTripList(tempTrips);
                myRef.child(userDetails.getId()).setValue(userDetails);

                ArrayList<String > membersTemp=mainTrip.getMembers();
                Log.d("Main Test12",""+membersTemp.size());
                for(int i=0;i<tripMembers.size();i++)
                {
                    UserDetails userDetails=tripMembers.get(i);
                    Log.d("Main Test12",""+userDetails.getLastName());
                    Log.d("MainTest123",""+userDetails.getId());
                    Log.d("MainTest123",""+membersTemp.toString());

                    if(membersTemp.contains(userDetails.getId())&& !userDetails.getId().equals(userId)) {
                        ArrayList<TripDetails> tempTrips1 = userDetails.getTripList();
                        Log.d("Main Test12", "" + userDetails.getTripList().size());
                        TripDetails mainTrip1 = new TripDetails();
                        for (int j = 0; j < tempTrips1.size(); j++) {
                            if (tempTrips1.get(j).getTripId().equals(tripId)) {
                                mainTrip1 = tempTrips1.get(j);
                                break;
                            }
                        }
                        tempTrips1.remove(mainTrip1);
                        ArrayList<LocationDetails> locationDetailses1 = mainTrip1.getLocationDetailsArrayList();
                        locationDetailses1.add(locationDetails);
                        mainTrip1.setLocationDetailsArrayList(locationDetailses1);
                        tempTrips1.add(mainTrip1);
                        userDetails.setTripList(tempTrips1);
                        myRef.child(userDetails.getId()).setValue(userDetails);
                    }
                }
                progressDialog.dismiss();
                String toastMsg = String.format("Place Added: %s", place.getName());
                Toast.makeText(mContext, toastMsg, Toast.LENGTH_LONG).show();

            }

        }

        else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("Inside","Check");
            }

        }


    }


    private void sendMessage(String message,int type) {

        MessageDetails message1=new MessageDetails();
        Date date=new Date();
        long epoch=date.getTime();
        message1.setSenderId(userId);
        message1.setSenderName(userDetails.getFirstName()+" "+userDetails.getLastName());
        message1.setType(type);
        message1.setMessage(message);
        message1.setTime(epoch);
        Log.d("check12345",""+message1.toString());

        ArrayList<MessageDetails> tempMessage=mainTrip.getMessageDetailsArrayList();
        if(tempMessage.size()==0)
        {
            tempMessage=new ArrayList<>();
        }
        tempMessage.add(message1);
        mainTrip.setMessageDetailsArrayList(tempMessage);
        tripRef.child(mainTrip.getTripId()).setValue(mainTrip);

        ArrayList<TripDetails>  tempTrips=userDetails.getTripList();
        TripDetails mainTripX=new TripDetails();
        for(int i=0;i<tempTrips.size();i++)
        {
            if(tempTrips.get(i).getTripId().equals(tripId))
            {
                mainTripX=tempTrips.get(i);
                break;
            }
        }
        tempTrips.remove(mainTripX);
        ArrayList<MessageDetails> messageList=mainTripX.getMessageDetailsArrayList();
        messageList.add(message1);
        mainTripX.setMessageDetailsArrayList(messageList);
        tempTrips.add(mainTripX);
        userDetails.setTripList(tempTrips);
        myRef.child(userDetails.getId()).setValue(userDetails);

        ArrayList<String > membersTemp=mainTrip.getMembers();
        Log.d("Main Test12",""+membersTemp.size());
        for(int i=0;i<tripMembers.size();i++)
        {
            UserDetails userDetails=tripMembers.get(i);
            Log.d("Main Test12",""+userDetails.getLastName());
            Log.d("MainTest123",""+userDetails.getId());
            Log.d("MainTest123",""+membersTemp.toString());

            if(membersTemp.contains(userDetails.getId())&& !userDetails.getId().equals(userId)) {
                ArrayList<TripDetails> tempTrips1 = userDetails.getTripList();
                Log.d("Main Test12", "" + userDetails.getTripList().size());
                TripDetails mainTrip1 = new TripDetails();
                for (int j = 0; j < tempTrips1.size(); j++) {
                    if (tempTrips1.get(j).getTripId().equals(tripId)) {
                        mainTrip1 = tempTrips1.get(j);
                        break;
                    }
                }
                tempTrips1.remove(mainTrip1);
                ArrayList<MessageDetails> messageList1 = mainTrip1.getMessageDetailsArrayList();
                messageList1.add(message1);
                mainTrip1.setMessageDetailsArrayList(messageList1);
                tempTrips1.add(mainTrip1);
                userDetails.setTripList(tempTrips1);
                myRef.child(userDetails.getId()).setValue(userDetails);
            }
        }

        editTextMessage.setText("");
        Toast.makeText(mContext,"Message Sent",Toast.LENGTH_SHORT).show();
    }

    private void setMessage() {

        linearLayout.removeAllViews();
        scrollView.fullScroll(View.FOCUS_DOWN);
        ArrayList<TripDetails> allTrips=userDetails.getTripList();
        TripDetails mainTrip=new TripDetails();
        for(int i=0;i<allTrips.size();i++)
        {
            if(allTrips.get(i).getTripId().equals(tripId))
            {
                mainTrip=allTrips.get(i);
                break;
            }
        }

        Picasso.with(mContext).load(mainTrip.getPhotoURL()).fit().into(imageViewTrip);
        textViewTitle.setText(""+mainTrip.getTitle());

        ArrayList<MessageDetails> messageList=mainTrip.getMessageDetailsArrayList();
        if(messageList.size()>0)
        {
            for(int i=0;i<messageList.size();i++)
            {
                final MessageDetails messageDetails=messageList.get(i);
                if(messageDetails.getType()==0)
                {
                    if(messageDetails.getSenderId().equals(userId)) {
                        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout linearLayout1 = (LinearLayout) inflator.inflate(R.layout.message_layout, null);
                        linearLayout.addView(linearLayout1);
                        TextView txtViiewMessage = (TextView) linearLayout1.findViewById(R.id.txtMessage);
                        txtViiewMessage.setText(messageDetails.getMessage());
                        TextView txtViewName = (TextView) linearLayout1.findViewById(R.id.txtViewSenderNameIm1);
                        txtViewName.setText(messageDetails.getSenderName());

                        TextView txtVieTime = (TextView) linearLayout1.findViewById(R.id.txtViewTIme);
                        PrettyTime prettyTime = new PrettyTime();
                        txtVieTime.setText(prettyTime.format(new Date(Long.parseLong("" + messageDetails.getTime()))));
                        final ImageButton button = (ImageButton) linearLayout1.findViewById(R.id.imageButtonDelete);
                        button.setTag(messageDetails);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ImageButton button=(ImageButton)v;
                                final MessageDetails message=(MessageDetails) button.getTag();
                                Log.d("Very Important",message.toString());
                                ArrayList<TripDetails> tripDetailses=userDetails.getTripList();
                                TripDetails tripDetails=new TripDetails();
                                for(int i=0;i<tripDetailses.size();i++)
                                {
                                    if(tripId.equals(tripDetailses.get(i).getTripId()))
                                    {
                                        tripDetails=tripDetailses.get(i);
                                        break;
                                    }
                                }
                                ArrayList<MessageDetails> messageDetailses=tripDetails.getMessageDetailsArrayList();
                                messageDetailses.remove(message);
                                tripDetailses.remove(tripDetails);
                                tripDetails.setMessageDetailsArrayList(messageDetailses);
                                tripDetailses.add(tripDetails);
                                userDetails.setTripList(tripDetailses);
                                myRef.child(userDetails.getId()).setValue(userDetails);

                            }
                        });
                    }
                    else
                    {
                        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout linearLayout1 = (LinearLayout) inflator.inflate(R.layout.message_layout_others, null);
                        linearLayout.addView(linearLayout1);

                        TextView txtViiewMessage = (TextView) linearLayout1.findViewById(R.id.txtMessage);
                        txtViiewMessage.setText(messageDetails.getMessage());
                        TextView txtViewName = (TextView) linearLayout1.findViewById(R.id.txtViewSenderNameIm1);
                        txtViewName.setText(messageDetails.getSenderName());

                        TextView txtVieTime = (TextView) linearLayout1.findViewById(R.id.txtViewTIme);
                        PrettyTime prettyTime = new PrettyTime();
                        txtVieTime.setText(prettyTime.format(new Date(Long.parseLong("" + messageDetails.getTime()))));
                        final ImageButton button = (ImageButton) linearLayout1.findViewById(R.id.imageButtonDelete);
                        button.setTag(messageDetails);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageButton button=(ImageButton)v;
                                final MessageDetails message=(MessageDetails) button.getTag();
                                Log.d("Very Important",message.toString());
                                ArrayList<TripDetails> tripDetailses=userDetails.getTripList();
                                TripDetails tripDetails=new TripDetails();
                                for(int i=0;i<tripDetailses.size();i++)
                                {
                                    if(tripId.equals(tripDetailses.get(i).getTripId()))
                                    {
                                         tripDetails=tripDetailses.get(i);
                                        break;
                                    }
                                }
                                ArrayList<MessageDetails> messageDetailses=tripDetails.getMessageDetailsArrayList();
                                messageDetailses.remove(message);
                                tripDetailses.remove(tripDetails);
                                tripDetails.setMessageDetailsArrayList(messageDetailses);
                                tripDetailses.add(tripDetails);
                                userDetails.setTripList(tripDetailses);
                                myRef.child(userDetails.getId()).setValue(userDetails);
                            }
                        });
                    }
                }


                else if(messageDetails.getType()==1)
                {
                    if(messageDetails.getSenderId().equals(userId)) {
                        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout linearLayout1 = (LinearLayout) inflator.inflate(R.layout.image_layout, null);
                        linearLayout.addView(linearLayout1);
                        ImageView imageView=(ImageView)linearLayout1.findViewById(R.id.imageViewMesage1);
                        Picasso.with(mContext).load(messageDetails.getMessage()).fit().into(imageView);
                        TextView txtViewName = (TextView) linearLayout1.findViewById(R.id.txtViewSenderNameIm1);
                        txtViewName.setText(messageDetails.getSenderName());

                        TextView txtVieTime = (TextView) linearLayout1.findViewById(R.id.txtViewTimeImage);
                        PrettyTime prettyTime = new PrettyTime();
                        txtVieTime.setText(prettyTime.format(new Date(Long.parseLong("" + messageDetails.getTime()))));
                        final ImageButton button = (ImageButton) linearLayout1.findViewById(R.id.imageButtonDeleteIm);
                        button.setTag(messageDetails);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ImageButton button=(ImageButton)v;
                                final MessageDetails message=(MessageDetails) button.getTag();
                                Log.d("Very Important",message.toString());
                                ArrayList<TripDetails> tripDetailses=userDetails.getTripList();
                                TripDetails tripDetails=new TripDetails();
                                for(int i=0;i<tripDetailses.size();i++)
                                {
                                    if(tripId.equals(tripDetailses.get(i).getTripId()))
                                    {
                                        tripDetails=tripDetailses.get(i);
                                        break;
                                    }
                                }
                                ArrayList<MessageDetails> messageDetailses=tripDetails.getMessageDetailsArrayList();
                                messageDetailses.remove(message);
                                tripDetailses.remove(tripDetails);
                                tripDetails.setMessageDetailsArrayList(messageDetailses);
                                tripDetailses.add(tripDetails);
                                userDetails.setTripList(tripDetailses);
                                myRef.child(userDetails.getId()).setValue(userDetails);

                            }
                        });
                    }
                    else
                    {
                        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout linearLayout1 = (LinearLayout) inflator.inflate(R.layout.image_layout_others, null);
                        linearLayout.addView(linearLayout1);
                        ImageView imageView=(ImageView)linearLayout1.findViewById(R.id.imageViewMesage1);
                        Picasso.with(mContext).load(messageDetails.getMessage()).fit().into(imageView);
                        TextView txtViewName = (TextView) linearLayout1.findViewById(R.id.txtViewSenderNameIm1);
                        txtViewName.setText(messageDetails.getSenderName());

                        TextView txtVieTime = (TextView) linearLayout1.findViewById(R.id.txtViewTimeImage1);
                        PrettyTime prettyTime = new PrettyTime();
                        txtVieTime.setText(prettyTime.format(new Date(Long.parseLong("" + messageDetails.getTime()))));
                        final ImageButton button = (ImageButton) linearLayout1.findViewById(R.id.imageButtonDeleteIm1);
                        button.setTag(messageDetails);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageButton button=(ImageButton)v;
                                final MessageDetails message=(MessageDetails) button.getTag();
                                Log.d("Very Important",message.toString());
                                ArrayList<TripDetails> tripDetailses=userDetails.getTripList();
                                TripDetails tripDetails=new TripDetails();
                                for(int i=0;i<tripDetailses.size();i++)
                                {
                                    if(tripId.equals(tripDetailses.get(i).getTripId()))
                                    {
                                        tripDetails=tripDetailses.get(i);
                                        break;
                                    }
                                }
                                ArrayList<MessageDetails> messageDetailses=tripDetails.getMessageDetailsArrayList();
                                messageDetailses.remove(message);
                                tripDetailses.remove(tripDetails);
                                tripDetails.setMessageDetailsArrayList(messageDetailses);
                                tripDetailses.add(tripDetails);
                                userDetails.setTripList(tripDetailses);
                                myRef.child(userDetails.getId()).setValue(userDetails);
                            }
                        });
                    }
                }
            }
        }
        else
        {

        }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public interface OnFragmentInteractionListener {

    }



    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


}
