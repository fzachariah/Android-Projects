package com.example.febin.group21_hw09;

import android.content.Context;
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
 * Created by febin on 19/04/2017.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    public RequestAdapter.MyAdapterListener onClickListener;
    private List<UserDetails> peopleList;
    Context context;
    UserDetails tempUserDetails;
    UserDetails currentUser;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, email;
        public ImageView imageView;
        public ImageButton imageButtonAccept;
        public ImageButton imageButtonReject;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewReqName);
            email = (TextView) view.findViewById(R.id.textViewReqEmailPeople);
            imageButtonAccept = (ImageButton) view.findViewById(R.id.imageButtonAccept);
            imageButtonReject = (ImageButton) view.findViewById(R.id.imageButtonReject);
            imageView = (ImageView) view.findViewById(R.id.imageViewReq);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }


    public RequestAdapter(List<UserDetails> peopleList, Context context, UserDetails currentUser, RequestAdapter.MyAdapterListener listener) {
        this.peopleList = peopleList;
        this.context = context;
        onClickListener = listener;
        this.currentUser = currentUser;


    }


    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_request, parent, false);

        return new RequestAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RequestAdapter.MyViewHolder holder, final int position) {
        final UserDetails userDetails = peopleList.get(position);
        tempUserDetails = userDetails;
        holder.name.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
        holder.email.setText(userDetails.getEmail());
        if (userDetails.getPhotoUrl() != null) {
            Picasso.with(context).load(userDetails.getPhotoUrl()).fit().into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.nopic);
        }

        holder.imageButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> receiveList=currentUser.getReceivedList();
                receiveList.remove(userDetails.getId());
                currentUser.setReceivedList(receiveList);
                ArrayList<String> friendListCurrent=currentUser.getFriendList();
                friendListCurrent.add(userDetails.getId());
                currentUser.setFriendList(friendListCurrent);
                myRef.child(currentUser.getId()).setValue(currentUser);

                ArrayList<String> temp=userDetails.getSentList();
                temp.remove(currentUser.getId());
                userDetails.setSentList(temp);
                ArrayList<String> friendListUser=userDetails.getFriendList();
                friendListUser.add(currentUser.getId());
                userDetails.setFriendList(friendListUser);
                myRef.child(userDetails.getId()).setValue(userDetails);

                Toast.makeText(context, "Friend Request Accepted", Toast.LENGTH_SHORT).show();

            }
        });

        holder.imageButtonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> receiveList=currentUser.getReceivedList();
                receiveList.remove(userDetails.getId());
                currentUser.setReceivedList(receiveList);
                myRef.child(currentUser.getId()).setValue(currentUser);

                ArrayList<String> temp=userDetails.getSentList();
                temp.remove(currentUser.getId());
                userDetails.setSentList(temp);
                myRef.child(userDetails.getId()).setValue(userDetails);

                Toast.makeText(context, "Friend Request Rejected", Toast.LENGTH_SHORT).show();
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
