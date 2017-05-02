package com.example.febin.group21_hw09;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by febin on 21/04/2017.
 */

public class AvailableAdapter extends RecyclerView.Adapter<AvailableAdapter.MyViewHolder> {

    private final ArrayList<TripDetails> allTrips;
    public AvailableAdapter.MyAdapterListener onClickListener;
    private List<TripDetails> peopleList;
    Context context;
    TripDetails tempUserDetails;
    UserDetails currrentUser;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");
    private DatabaseReference tripRef = mDatabase.child("Trips");


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, email;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewAvailTitle);
            email = (TextView) view.findViewById(R.id.textViewAvailLocation);
            imageButton = (ImageButton) view.findViewById(R.id.imageButtonJoinAvail);
            imageView = (ImageView) view.findViewById(R.id.imageViewAvailTrip);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }


    public AvailableAdapter(List<TripDetails> peopleList, Context context, UserDetails currentUser, ArrayList<TripDetails> allTrips, AvailableAdapter.MyAdapterListener listener) {
        this.peopleList = peopleList;
        this.context = context;
        onClickListener = listener;
        this.currrentUser = currentUser;
        this.allTrips=allTrips;


    }


    @Override
    public AvailableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_availtrips, parent, false);

        return new AvailableAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(AvailableAdapter.MyViewHolder holder, final int position) {
        final TripDetails tripDetails = peopleList.get(position);
        tempUserDetails = tripDetails;
        holder.name.setText(""+tripDetails.getTitle());
        ArrayList<LocationDetails> locationDetailses=tripDetails.getLocationDetailsArrayList();
        String locations="";
        if(locationDetailses.size()>2) {
            for (int i = 0; i < 2; i++) {
                locations = locations + locationDetailses.get(i).getLocationName() + ", ";
            }
            locations=locations+"...";
            holder.email.setText(locations);
        }
        else if(locationDetailses.size()==2)
        {
            locations=locationDetailses.get(0).getLocationName()+", "+locationDetailses.get(1).getLocationName();
            holder.email.setText(locations);
        }
        else if(locationDetailses.size()==1)
        {
            locations=locationDetailses.get(0).getLocationName();
            holder.email.setText(locations);
        }


        if (tripDetails.getPhotoURL() != null) {
            Picasso.with(context).load(tripDetails.getPhotoURL()).fit().into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.nopic);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Are you sure you want to Join Trip "+tripDetails.getTitle())
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ArrayList<String> membersList=tripDetails.getMembers();
                                membersList.add(currrentUser.getId());
                                tripDetails.setMembers(membersList);
                                tripRef.child(tripDetails.getTripId()).setValue(tripDetails);

                                ArrayList<TripDetails>tripDetailsTemp=currrentUser.getTripList();
                                ArrayList<MessageDetails> messageList=tripDetails.getMessageDetailsArrayList();
                                messageList.clear();
                                tripDetails.setMessageDetailsArrayList(messageList);
                                tripDetailsTemp.add(tripDetails);
                                currrentUser.setTripList(tripDetailsTemp);
                                myRef.child(currrentUser.getId()).setValue(currrentUser);

                                Toast.makeText(context,"Joined Trip",Toast.LENGTH_SHORT).show();


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }


    static interface MyAdapterListener {

    }
}

