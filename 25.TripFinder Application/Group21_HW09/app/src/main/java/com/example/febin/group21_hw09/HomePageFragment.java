package com.example.febin.group21_hw09;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.UUID;

public class HomePageFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private OnFragmentInteractionListener mListener;
    UserDetails userDetails;
    SharedPreferences favorites;
    String userId;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;

    TextView textViewDispName;
    TextView textViewGender;
    ImageView imageViewDp;
    TextView textViewEmail;
    ImageButton imageButtonUpdateDetails;
    ImageButton imageButtonProfile;
    ProgressDialog progressDialog;
    Button buttonFriendList;
    Button buttonPeopleList;
    Button buttonRequestsList;
    Button buttonTripsList;
    Button buttonJoinedTrips;
    Button buttonCreateTrip;
    LinearLayout linearLayoutInflate;
    LinearLayout linearLayoutRecycler;
    TextView textViewMessage;

    String tempMessage;

    RecyclerView recyclerView;
    private PeopleAdapter peopleAdapter;
    private RequestAdapter requestAdapter;
    private FriendAdapter friendAdapter;
    private JoinedAdapter joinedAdapter;
    private AvailableAdapter availableAdapter;
    RecyclerView.LayoutManager mLayoutManager ;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private final static String USER_DETAILS="userDetails";
    private final static int GALLERY=100;
    private final static int IMAGE=101;
    private final static int PLACE_PICKER_REQUEST = 105;

    boolean initialCheck=true;

    Place placeC;

    LinkedHashMap<String,UserDetails> friendsHashMap=new LinkedHashMap<>();
    ArrayList<UserDetails> registeredList=new ArrayList<>();
    ArrayList<UserDetails> friendsList=new ArrayList<>();
    ArrayList<UserDetails> requestList=new ArrayList<>();
    ArrayList<TripDetails> allTrips=new ArrayList<>();
    ArrayList<TripDetails> availableTrips=new ArrayList<>();

    String url;

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");
    private DatabaseReference tripRef = mDatabase.child("Trips");

    public HomePageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        favorites = getActivity().getSharedPreferences("tripDetails",
                Context.MODE_PRIVATE);
        userId = (String) getArguments().getSerializable(USER_DETAILS);
        mContext=getActivity();

        mGoogleApiClient = new GoogleApiClient
                .Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        textViewDispName=(TextView)getActivity().findViewById(R.id.textViewDisplayName);
        textViewGender=(TextView)getActivity().findViewById(R.id.textViewGender);
        textViewEmail=(TextView)getActivity().findViewById(R.id.textViewEMail);
        imageViewDp=(ImageView)getActivity().findViewById(R.id.imageViewProfilePic);
        imageButtonUpdateDetails=(ImageButton)getActivity().findViewById(R.id.imageButtonUpdateDetails);
        imageButtonProfile=(ImageButton) getActivity().findViewById(R.id.imageButtonProfileUpdate);

        buttonPeopleList=(Button)getActivity().findViewById(R.id.buttonPeople);
        buttonFriendList=(Button)getActivity().findViewById(R.id.buttonFriends);
        buttonRequestsList=(Button)getActivity().findViewById(R.id.buttonRequests);
        buttonTripsList=(Button)getActivity().findViewById(R.id.buttonTRips);
        buttonCreateTrip=(Button)getActivity().findViewById(R.id.buttonCreate);
        buttonJoinedTrips=(Button)getActivity().findViewById(R.id.buttonJoined);
        recyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerView);
        textViewMessage=(TextView)getActivity().findViewById(R.id.textViewNoMessage);

        linearLayoutInflate=(LinearLayout)getActivity().findViewById(R.id.linearLayoutInflate);
        linearLayoutRecycler=(LinearLayout)getActivity().findViewById(R.id.linearLayoutHomeContainer);
        linearLayoutInflate.setVisibility(View.GONE);
        textViewMessage.setVisibility(View.GONE);

        progressDialog=new ProgressDialog(mContext);
        progressDialog.show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserDetails user = postSnapshot.getValue(UserDetails.class);
                    if(user.getId().equals(userId))
                    {
                        Log.d("Inside Check",user.toString());
                        userDetails=user;
                        setData();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tripRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                allTrips.clear();
                allTrips=new ArrayList<TripDetails>();
                Log.d("Printing value",""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    TripDetails tripDetails=postSnapshot.getValue(TripDetails.class);
                    allTrips.add(tripDetails);
                }
                Log.d("Printing valueT",""+allTrips.size());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Inside Second One","test "+userDetails.friendList.size());
                HashSet<String> checkSet=new HashSet<String>();
                HashSet<String> requestSet=new HashSet<String>();
                for(int i=0;i<userDetails.receivedList.size();i++)
                {
                    checkSet.add(userDetails.receivedList.get(i));
                    requestSet.add(userDetails.getReceivedList().get(i));
                }

                HashSet<String> friendSet=new HashSet<String>();
                for(int i=0;i<userDetails.getFriendList().size();i++)
                {
                    friendSet.add(userDetails.getFriendList().get(i));
                }
                registeredList.clear();
                registeredList=new ArrayList<UserDetails>();
                friendsList.clear();
                friendsList=new ArrayList<UserDetails>();
                requestList.clear();
                requestList=new ArrayList<UserDetails>();
                friendsHashMap.clear();
                friendsHashMap=new LinkedHashMap<String, UserDetails>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserDetails user = postSnapshot.getValue(UserDetails.class);
                    if(!user.getId().equals(userId)&&!userDetails.getSentList().contains(user.getId())&&!checkSet.contains(user.getId())&&!friendSet.contains(user.getId()))
                    {
                        registeredList.add(user);
                    }
                    else if(friendSet.contains(user.getId()))
                    {
                        friendsList.add(user);
                        friendsHashMap.put(user.getId(),user);
                    }
                    else if(requestSet.contains(user.getId()))
                    {
                        requestList.add(user);
                    }

                }
                if(peopleAdapter!=null)
                {
                    dispPeopleList();
                }
                else if(friendAdapter!=null)
                {
                    dispFriends();
                }
                else if(requestAdapter!=null)
                {
                    dispRequests();
                }
                else if(joinedAdapter!=null)
                {
                    Log.d("TestError","Error");
                    dispJoinedTrips();
                }
                else if(availableAdapter!=null)
                {
                    dispPossibleTrips();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        imageViewDp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(userDetails.getPhotoUrl()!=null &&userDetails.getPhotoUrl().length()>0) {
                    deleteImage();
                }
                else
                {
                    toastMessage("No Profile Picture");
                }
                return true;
            }
        });

        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });

        imageButtonUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileDetails();
            }
        });

        buttonPeopleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutInflate.setVisibility(View.GONE);
                linearLayoutRecycler.setVisibility(View.VISIBLE);
                dispPeopleList();
            }
        });

        buttonRequestsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutInflate.setVisibility(View.GONE);
                linearLayoutRecycler.setVisibility(View.VISIBLE);
                dispRequests();
            }
        });

        buttonFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutInflate.setVisibility(View.GONE);
                linearLayoutRecycler.setVisibility(View.VISIBLE);
                dispFriends();
            }
        });

        buttonCreateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutInflate.setVisibility(View.VISIBLE);
                linearLayoutRecycler.setVisibility(View.GONE);
                createTrip();

            }
        });

        buttonJoinedTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutInflate.setVisibility(View.GONE);
                linearLayoutRecycler.setVisibility(View.VISIBLE);
                dispJoinedTrips();
            }
        });

        buttonTripsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayoutInflate.setVisibility(View.GONE);
                linearLayoutRecycler.setVisibility(View.VISIBLE);
                dispPossibleTrips();

            }
        });
    }

    private void deleteImage() {


        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to delete your profile picture?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        userDetails.setPhotoUrl("");
                        myRef.child(userDetails.getId()).setValue(userDetails);
                        toastMessage("Profile Picture Deleted");

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    private void dispPossibleTrips() {

        peopleAdapter=null;
        requestAdapter=null;
        friendAdapter=null;
        joinedAdapter=null;

        buttonJoinedTrips.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonTripsList.setBackgroundColor(mContext.getResources().getColor(R.color.whiteColor));
        buttonCreateTrip.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonFriendList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonPeopleList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonRequestsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));

        SharedPreferences.Editor editor = favorites.edit();
        editor.clear();
        editor.commit();


        HashSet<String> temp=new HashSet<>();
        temp.clear();
        availableTrips.clear();
        availableTrips=new ArrayList<TripDetails>();
        ArrayList<TripDetails> tripDetailsTemp=userDetails.getTripList();
        ArrayList<String> friendList=userDetails.getFriendList();
        for(int i=0;i<tripDetailsTemp.size();i++)
        {
            temp.add(tripDetailsTemp.get(i).getTripId());
        }
        ArrayList<TripDetails> tempList=new ArrayList<>();
        for(int i=0;i<allTrips.size();i++)
        {
            if(!temp.contains(allTrips.get(i).getTripId()))
            {
                tempList.add(allTrips.get(i));
            }
        }
        for(int i=0;i<tempList.size();i++)
        {
            if(friendList.contains(tempList.get(i).getCreatedBy())||tempList.get(i).getCreatedBy().equals(userDetails.getId()))
            {
                availableTrips.add(tempList.get(i));
            }
        }

        if(availableTrips.size()==0)
        {
            textViewMessage.setVisibility(View.VISIBLE);
        }
        else
        {
            textViewMessage.setVisibility(View.GONE);
        }

        availableAdapter=new AvailableAdapter(availableTrips, getActivity(), userDetails, allTrips, new AvailableAdapter.MyAdapterListener() {
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(availableAdapter);

    }

    private void dispJoinedTrips() {

        Log.d("TestError123","Error");

        peopleAdapter=null;
        requestAdapter=null;
        friendAdapter=null;
        availableAdapter=null;

        buttonJoinedTrips.setBackgroundColor(mContext.getResources().getColor(R.color.whiteColor));
        buttonTripsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonCreateTrip.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonFriendList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonPeopleList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonRequestsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));

        SharedPreferences.Editor editor = favorites.edit();
        editor.clear();
        editor.commit();

        ArrayList<TripDetails> tripDetailsList=userDetails.getTripList();

        if(tripDetailsList.size()==0)
        {
            textViewMessage.setVisibility(View.VISIBLE);
        }
        else
        {
            textViewMessage.setVisibility(View.GONE);
        }

        joinedAdapter=new JoinedAdapter(tripDetailsList, getActivity(), userDetails,allTrips, new JoinedAdapter.MyAdapterListener() {
            @Override
            public void chatFunction(UserDetails currrentUser, TripDetails tripDetails) {

                mListener.chatActvitiyCalled(currrentUser,tripDetails);

            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(joinedAdapter);


    }

    private void dispFriends() {

        peopleAdapter=null;
        requestAdapter=null;
        joinedAdapter=null;
        availableAdapter=null;

        buttonJoinedTrips.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonTripsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonCreateTrip.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonFriendList.setBackgroundColor(mContext.getResources().getColor(R.color.whiteColor));
        buttonPeopleList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonRequestsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));

        SharedPreferences.Editor editor = favorites.edit();
        editor.clear();
        editor.commit();

        if(friendsList.size()==0)
        {
            textViewMessage.setVisibility(View.VISIBLE);
        }
        else
        {
            textViewMessage.setVisibility(View.GONE);
        }
        friendAdapter=new FriendAdapter(friendsList, getActivity(), userDetails, new FriendAdapter.MyAdapterListener() {

        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendAdapter);
    }

    private void dispRequests() {

        SharedPreferences.Editor editor = favorites.edit();
        editor.clear();
        editor.commit();
        Log.d("Inside Request",""+userDetails.getReceivedList().size());
        peopleAdapter=null;
        friendAdapter=null;
        joinedAdapter=null;
        availableAdapter=null;

        buttonJoinedTrips.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonTripsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonCreateTrip.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonFriendList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonPeopleList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonRequestsList.setBackgroundColor(mContext.getResources().getColor(R.color.whiteColor));

        if(requestList.size()==0)
        {
            textViewMessage.setVisibility(View.VISIBLE);
        }
        else
        {
            textViewMessage.setVisibility(View.GONE);
        }

        requestAdapter = new RequestAdapter(requestList, getActivity(), userDetails,new RequestAdapter.MyAdapterListener() {

        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(requestAdapter);

    }
    private void dispPeopleList() {

        SharedPreferences.Editor editor = favorites.edit();
        editor.clear();
        editor.commit();

        requestAdapter=null;
        friendAdapter=null;
        joinedAdapter=null;
        availableAdapter=null;

        buttonJoinedTrips.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonTripsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonCreateTrip.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonFriendList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonPeopleList.setBackgroundColor(mContext.getResources().getColor(R.color.whiteColor));
        buttonRequestsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));

        if(registeredList.size()==0)
        {
            textViewMessage.setVisibility(View.VISIBLE);
        }
        else
        {
            textViewMessage.setVisibility(View.GONE);
        }

        peopleAdapter = new PeopleAdapter(registeredList, getActivity(), userDetails,new PeopleAdapter.MyAdapterListener() {
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(peopleAdapter);

    }

    private void createTrip() {

        peopleAdapter=null;
        requestAdapter=null;
        friendAdapter=null;
        joinedAdapter=null;
        availableAdapter=null;

        buttonJoinedTrips.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonTripsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonCreateTrip.setBackgroundColor(mContext.getResources().getColor(R.color.whiteColor));
        buttonFriendList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonPeopleList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));
        buttonRequestsList.setBackgroundColor(mContext.getResources().getColor(R.color.brownColor));

        textViewMessage.setVisibility(View.GONE);
        linearLayoutInflate.removeAllViews();
        LayoutInflater inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflator.inflate(R.layout.layout_create_trip,null);

        linearLayoutInflate.addView(linearLayout);
        ImageButton buttonImagePicker=(ImageButton) linearLayoutInflate.findViewById(R.id.imageButtonGallery);
        final EditText editTextTitle=(EditText)linearLayoutInflate.findViewById(R.id.editTextTripTitle);
        ImageButton imageButtonFriendsAdd=(ImageButton)linearLayoutInflate.findViewById(R.id.imageButtonTripPeople);
        ImageButton imageButtonAddLocation=(ImageButton)linearLayoutInflate.findViewById(R.id.imageButtonMap);
        Button buttonAdd=(Button)linearLayoutInflate.findViewById(R.id.buttonAddTrip);

        imageButtonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
        buttonImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = favorites.edit();
                editor.putBoolean("Imp",true);
                editor.commit();
                Intent  intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE);

            }
        });
        Log.d("Printing here",""+ friendsHashMap.size());
        final boolean[] checkedItems=new boolean[friendsHashMap.size()];
        imageButtonFriendsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Printing",""+ friendsHashMap.size());
                CharSequence[] ar = new String[friendsHashMap.size()];

                int i=0;
                for (UserDetails userDetails : friendsHashMap.values()) {
                    ar[i]=userDetails.getFirstName()+" "+userDetails.getLastName();
                    i++;
                }
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Select Friends").setMultiChoiceItems(ar, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked)
                                {
                                    checkedItems[which]=true;

                                }
                                else
                                {
                                    checkedItems[which]=false;

                                }
                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Check boolean ",""+checkedItems[0]+" "+checkedItems[1]);
                            }
                        }).create();
                dialog.show();

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(mContext);
                final String title=editTextTitle.getText().toString().trim();
                String photoUrl=tempMessage;
                Place placeSub=placeC;
                if(title.length()==0  )
                {
                    Toast.makeText(mContext,"Please Enter Title",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(photoUrl==null ||photoUrl.length()==0)
                {
                    Toast.makeText(mContext,"Please Select an Image",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(placeSub==null)
                {
                    Toast.makeText(mContext,"Please Select a Location",Toast.LENGTH_SHORT).show();
                    return;
                }
                    progressDialog.show();
                    byte [] data1=Base64.decode(photoUrl,Base64.DEFAULT);
                    final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    StorageReference imageRef=storageRef.child("tripPhoto/"+uuid+".JPEG");
                    UploadTask uploadTask = imageRef.putBytes(data1);
                    uploadTask.addOnSuccessListener(getActivity(),new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.show();

                            @SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
                            String storageUrl=downloadUrl.toString();
                            TripDetails tripDetails=new TripDetails();
                            LocationDetails locationDetails=new LocationDetails();
                            locationDetails.setId(""+placeC.getId());
                            locationDetails.setLocationName(""+placeC.getName());
                            LatLng latLng=placeC.getLatLng();
                            locationDetails.setLat(""+latLng.latitude);
                            locationDetails.setLongt(""+latLng.longitude);
                            ArrayList<LocationDetails> locationDetailses=tripDetails.getLocationDetailsArrayList();
                            locationDetailses.add(locationDetails);
                            tripDetails.setLocationDetailsArrayList(locationDetailses);
                            tripDetails.setTripId(uuid);
                            tripDetails.setCreatedBy(userDetails.getId());
                            tripDetails.setPhotoURL(storageUrl);
                            tripDetails.setTitle(title);
                            int i=0;
                            for (UserDetails userDetails : friendsHashMap.values()) {
                                if(checkedItems[i])
                                {
                                    tripDetails.members.add(userDetails.getId());
                                }
                                i++;
                            }
                            tripDetails.members.add(userDetails.getId());
                            tripRef.child(tripDetails.getTripId()).setValue(tripDetails);
                            int j=0;
                            for (UserDetails userDetails : friendsHashMap.values()) {
                                if(checkedItems[j])
                                {
                                    userDetails.tripList.add(tripDetails);
                                    myRef.child(userDetails.getId()).setValue(userDetails);
                                }
                                j++;
                            }
                            userDetails.tripList.add(tripDetails);
                            myRef.child(userDetails.getId()).setValue(userDetails);
                            Toast.makeText(mContext,"Trip Added Successfully",Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = favorites.edit();
                            editor.clear();
                            editor.commit();

                            editor.putString("Done","Done");
                            editor.commit();
                            progressDialog.dismiss();
                            createTrip();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
            }
        });
    }

    private void updateProfileDetails() {

        Log.d("Log:","Reached Inside Function");
        final AlertDialog alertDialog1;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Update Profile Details");
        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText editTextFirstName = new EditText(getActivity());
        editTextFirstName.setText(userDetails.getFirstName());
        final EditText editTextLastName = new EditText(getActivity());
        editTextLastName.setText(userDetails.getLastName());
        final Spinner genderSpinner=new Spinner(getActivity());
        ArrayAdapter<CharSequence> staticAdapter= ArrayAdapter
                .createFromResource(getActivity(), R.array.gender_arrays,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(staticAdapter);
        if(userDetails.getGender()!=null&&userDetails.getGender().equals("Male"))
        {
            genderSpinner.setSelection(1);
        }
        else if(userDetails.getGender()!=null&&userDetails.getGender().equals("Female"))
        {
            genderSpinner.setSelection(2);
        }
        else
        {
            genderSpinner.setSelection(0);
        }

        linearLayout.addView(editTextFirstName);
        linearLayout.addView(editTextLastName);
        linearLayout.addView(genderSpinner);

        Button buttonSet=new Button(getActivity());
        buttonSet.setText("Update");
        buttonSet.setBackgroundColor(getResources().getColor(R.color.actionbar));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        buttonSet.setLayoutParams(param);

        final Button buttonCancel=new Button(getActivity());
        buttonCancel.setText("Cancel");
        buttonCancel.setBackgroundColor(getResources().getColor(R.color.actionbar));
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        buttonCancel.setLayoutParams(param1);


        LinearLayout linearLayoutHorizontal=new LinearLayout(getActivity());
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHorizontal.addView(buttonCancel);
        linearLayoutHorizontal.addView(buttonSet);
        linearLayout.addView(linearLayoutHorizontal);

        alertDialog.setView(linearLayout);
        alertDialog1=alertDialog.create();
        alertDialog1.setCancelable(false);
        alertDialog1.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fName=editTextFirstName.getText().toString().trim();
                final String lName=editTextLastName.getText().toString().trim();
                final String gender=genderSpinner.getSelectedItem().toString().trim();
                if(fName.length()==0 ||lName.length()==0 ||gender.contains("Select Gender"))
                {
                    toastMessage("Please Enter Values Correctly!");
                }
                else
                {

                    userDetails.setFirstName(fName);
                    userDetails.setLastName(lName);
                    userDetails.setGender(gender);
                    myRef.child(userDetails.getId()).setValue(userDetails);
                    alertDialog1.dismiss();



                }

            }
        });

    }
    private void updateImage() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to update your profile picture?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,GALLERY);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 &&resultCode==Activity.RESULT_OK &&data!=null &&data.getData()!=null) {
            try {
                progressDialog.show();
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();
                StorageReference imageRef=storageRef.child("profilePictures/"+ userDetails.getId()+".JPEG");
                UploadTask uploadTask = imageRef.putBytes(data1);
                uploadTask.addOnSuccessListener(getActivity(),new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
                        userDetails.setPhotoUrl(downloadUrl.toString().trim());
                        myRef.child(userDetails.getId()).setValue(userDetails);
                        Log.d("check",""+userDetails.toString());
                        Picasso.with(mContext).load(userDetails.getPhotoUrl()).fit().into(imageViewDp);
                        progressDialog.dismiss();

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
        else if(requestCode==IMAGE &&resultCode==Activity.RESULT_OK &&data!=null &&data.getData()!=null) {
            try {

                Uri uri = data.getData();
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();
                String temp= Base64.encodeToString(data1, Base64.DEFAULT);
                tempMessage=temp;

            }
            catch (Exception e)
            {

            }
        }

        else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, mContext);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(mContext, toastMsg, Toast.LENGTH_LONG).show();
                if(placeC!=null) {
                    Log.d("Test12345", "" + placeC.getName());
                }
                placeC=place;

            }
        }

    }

    public void setData()
    {
        if(userDetails!=null)
        {
            textViewDispName.setText(userDetails.getFirstName()+" "+userDetails.getLastName());
            textViewEmail.setText(userDetails.getEmail());
            if(userDetails.getGender()==null||userDetails.getGender().length()==0)
            {
                textViewGender.setText("Gender: ");
            }
            else
            {
                textViewGender.setText("Gender: "+userDetails.getGender());
            }
           // Log.d("ProfilePicture",""+userDetails.getPhotoUrl().length());
            if(userDetails.getPhotoUrl()!=null &&userDetails.getPhotoUrl().length()!=0 )
            {
                Log.d("Inside Photo",""+userDetails.getPhotoUrl());
                Picasso.with(mContext).load(userDetails.getPhotoUrl()).fit().centerInside().into(imageViewDp);
            }
            else
            {
                imageViewDp.setImageResource(R.drawable.nopic);
            }
        }
        if(initialCheck)
        {
            Log.d("Test123456",""+friendsList.size());
            dispFriends();
            initialCheck=false;
            SharedPreferences.Editor editor = favorites.edit();
            editor.clear();
            editor.clear();
            editor.putString("AddTrip","1");
            editor.commit();

        }

        progressDialog.dismiss();

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
        toast.setDuration(Toast.LENGTH_SHORT);
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

        void chatActvitiyCalled(UserDetails currrentUser, TripDetails tripDetails);
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
