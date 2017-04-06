package com.example.febin.group21_inclass09;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.List;

/**
 * Created by febin on 18/03/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>  {

    public MyAdapterListener onClickListener;
    private List<Channel> newsList;
    Context context;
    Channel tempChannel;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, date;
        public Button imageButton;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewTitle);

            imageButton=(Button) view.findViewById(R.id.buttonAddActivity);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClickListener.recyclerViewListClicked(v,this.getLayoutPosition());
        }
    }


    public MainAdapter(List<Channel> newsList,Context context,MyAdapterListener listener) {
        this.newsList = newsList;
        this.context=context;
        onClickListener=listener;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Channel channel = newsList.get(position);
        tempChannel=channel;
        holder.title.setText(channel.getName());
        if(channel.getStatus()==1)
        {
            holder.imageButton.setText("Join");
        }
        else
        {
            holder.imageButton.setText("View");
        }
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(channel.getStatus()==1)
                {
                    onClickListener.blockItem(v,position,channel.getStatus());
                    channel.setStatus(0);
                    notifyDataSetChanged();
                }
                else
                {
                    onClickListener.blockItem(v,position,channel.getStatus());
                }
            }
        });







    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    static interface MyAdapterListener {

        public void blockItem(View v, int position,int status);
        public void recyclerViewListClicked(View v, int position);

    }
}
