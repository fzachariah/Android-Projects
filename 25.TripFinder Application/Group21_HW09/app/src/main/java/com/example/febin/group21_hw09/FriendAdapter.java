package com.example.febin.group21_hw09;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
 * Created by febin on 20/04/2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {

    public FriendAdapter.MyAdapterListener onClickListener;
    private List<UserDetails> peopleList;
    Context context;
    UserDetails tempUserDetails;
    UserDetails currrentUser;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, email;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewFriendsName);
            email = (TextView) view.findViewById(R.id.textViewEmailFriends);
            imageButton = (ImageButton) view.findViewById(R.id.imageButtonFriends);
            imageView = (ImageView) view.findViewById(R.id.imageViewFriends);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }


    public FriendAdapter(List<UserDetails> peopleList, Context context, UserDetails currentUser, FriendAdapter.MyAdapterListener listener) {
        this.peopleList = peopleList;
        this.context = context;
        onClickListener = listener;
        this.currrentUser = currentUser;


    }


    @Override
    public FriendAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_friends, parent, false);

        return new FriendAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(FriendAdapter.MyViewHolder holder, final int position) {
        final UserDetails userDetails = peopleList.get(position);
        tempUserDetails = userDetails;
        holder.name.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
        holder.email.setText(userDetails.getEmail());
        if (userDetails.getPhotoUrl() != null) {
            Picasso.with(context).load(userDetails.getPhotoUrl()).fit().into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.nopic);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Are you sure you want to remove "+userDetails.getFirstName()+" as your friend?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ArrayList<String> friendListCurrent=currrentUser.getFriendList();
                                friendListCurrent.remove(userDetails.getId());
                                currrentUser.setFriendList(friendListCurrent);
                                myRef.child(currrentUser.getId()).setValue(currrentUser);

                                ArrayList<String> friendListUser=userDetails.getFriendList();
                                friendListUser.remove(currrentUser.getId());
                                userDetails.setFriendList(friendListUser);
                                userDetails.setFriendList(friendListUser);
                                myRef.child(userDetails.getId()).setValue(userDetails);

                                Toast.makeText(context, "Connection Removed", Toast.LENGTH_SHORT).show();


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