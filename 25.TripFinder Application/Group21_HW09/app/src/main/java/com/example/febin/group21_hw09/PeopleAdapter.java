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

import java.util.List;

/**
 * Created by febin on 19/04/2017.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder>  {

    public MyAdapterListener onClickListener;
    private List<UserDetails> peopleList;
    Context context;
    UserDetails tempUserDetails;
    UserDetails currrentUser;

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("TripUsers");


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, email;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewPeopleName);
            email = (TextView) view.findViewById(R.id.textViewEmailPeople);
            imageButton=(ImageButton)view.findViewById(R.id.imageButtonRequest);
            imageView =(ImageView)view.findViewById(R.id.imageViewPeople);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }


    public PeopleAdapter(List<UserDetails> peopleList,Context context,UserDetails currentUser,MyAdapterListener listener) {
        this.peopleList = peopleList;
        this.context=context;
        onClickListener=listener;
        this.currrentUser=currentUser;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_people, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final UserDetails userDetails = peopleList.get(position);
        tempUserDetails=userDetails;
        holder.name.setText(userDetails.getFirstName()+" "+userDetails.getLastName());
        holder.email.setText(userDetails.getEmail());
        if(userDetails.getPhotoUrl()!=null) {
            Picasso.with(context).load(userDetails.getPhotoUrl()).fit().into(holder.imageView);
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.nopic);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currrentUser.getSentList().add(userDetails.getId());
                userDetails.getReceivedList().add(currrentUser.getId());
                myRef.child(currrentUser.getId()).setValue(currrentUser);
                myRef.child(userDetails.getId()).setValue(userDetails);
                Toast.makeText(context,"Friend Request Sent",Toast.LENGTH_SHORT).show();
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
