package com.example.febin.group21_hw09;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by febin on 21/04/2017.
 */

public class JoinedAdapter extends RecyclerView.Adapter<JoinedAdapter.MyViewHolder> {

    private final ArrayList<TripDetails> allTrips;
    public JoinedAdapter.MyAdapterListener onClickListener;
    private List<TripDetails> peopleList;
    Context context;
    TripDetails tempUserDetails;
    UserDetails currrentUser;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");
    private DatabaseReference tripRef = mDatabase.child("Trips");


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, email;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewTripTitle);
            email = (TextView) view.findViewById(R.id.textViewTripLocation);
            imageButton = (ImageButton) view.findViewById(R.id.imageButtonExit);
            imageView = (ImageView) view.findViewById(R.id.imageViewJoinTrip);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Log.d("Chat Test",""+currrentUser.toString());
            Log.d("Chat Test Trip",peopleList.get(this.getLayoutPosition()).toString());
            onClickListener.chatFunction(currrentUser,peopleList.get(this.getLayoutPosition()));


        }
    }


    public JoinedAdapter(List<TripDetails> peopleList, Context context, UserDetails currentUser, ArrayList<TripDetails> allTrips, MyAdapterListener listener) {
        this.peopleList = peopleList;
        this.context = context;
        onClickListener = listener;
        this.currrentUser = currentUser;
        this.allTrips=allTrips;


    }


    @Override
    public JoinedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_joinedtrips, parent, false);

        return new JoinedAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(JoinedAdapter.MyViewHolder holder, final int position) {
        final TripDetails tripDetails = peopleList.get(position);
        tempUserDetails = tripDetails;
        holder.name.setText(""+tripDetails.getTitle());
       // holder.email.setText(tripDetails.getLocation());
        if (tripDetails.getPhotoURL() != null) {
            Picasso.with(context).load(tripDetails.getPhotoURL()).fit().into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.nopic);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Are you sure you want to exit from "+tripDetails.getTitle())
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ArrayList<TripDetails>tripDetailsTemp=currrentUser.getTripList();
                                tripDetailsTemp.remove(tripDetails);
                                currrentUser.setTripList(tripDetailsTemp);
                                myRef.child(currrentUser.getId()).setValue(currrentUser);

                                Log.d("Test12345678",""+allTrips.size());
                                for(int i=0;i<allTrips.size();i++)
                                {

                                    if(tripDetails.getTripId().equals(allTrips.get(i).getTripId()))
                                    {
                                        final TripDetails temp=allTrips.get(i);
                                        ArrayList<String > membersTemp=temp.getMembers();
                                        membersTemp.remove(currrentUser.getId());
                                        Log.d("Test12345",""+(membersTemp==null));
                                        Log.d("Test123",""+membersTemp.size());
                                        if(membersTemp.size()==0)
                                        {
                                            StorageReference imageRef=storageRef.child("tripPhoto/"+temp.getTripId()+".JPEG");
                                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    tripRef.child(temp.getTripId()).removeValue();
                                                    Toast.makeText(context,"Deleting Trip,Last Person to exit",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            temp.setMembers(membersTemp);
                                            tripRef.child(temp.getTripId()).setValue(temp);
                                        }
                                    }
                                }

                                Toast.makeText(context,"Exited from Trip",Toast.LENGTH_SHORT).show();


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

        void chatFunction(UserDetails currrentUser, TripDetails tripDetails);
    }
}

