package com.example.febin.group21_hw07;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by febin on 03/03/2017.
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyViewHolder> {

    private List<Episode> episodeList;
    Context context;
    Episode episodeTemp;
    public MyAdapterListener onClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, date;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewTitle);
            date = (TextView) view.findViewById(R.id.textViewPosted);
            imageButton=(ImageButton)view.findViewById(R.id.imageButton);
            imageView =(ImageView)view.findViewById(R.id.imageView);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClickListener.recyclerViewListClicked(v,this.getLayoutPosition());
        }
    }


    public EpisodeAdapter(List<Episode> episodeList,Context context,MyAdapterListener listener) {
        this.episodeList = episodeList;
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
        Episode episode = episodeList.get(position);
        episodeTemp=episode;
        holder.title.setText(episode.getTitle());
        String date=episode.getReleaseDate();
        date=date.substring(0,16);
        holder.date.setText("posted: "+date);
        if(episode.getImageURL()!=null) {
            Picasso.with(context).load(episode.getImageURL()).fit().centerCrop().into(holder.imageView);
        }
        else
        {
            holder.imageView.setImageDrawable(null);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.playClick(v,position);

            }
        });




    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }


    static interface MyAdapterListener {

        public void playClick(View v, int position);
        public void recyclerViewListClicked(View v, int position);

    }



}
