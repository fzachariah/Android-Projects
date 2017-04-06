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
 * Created by febin on 05/03/2017.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    private List<Episode> episodeList;
    Context context;

    public GridAdapterListener onClickListener;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewGridTitle);
            imageView = (ImageView) view.findViewById(R.id.imageViewMain);
            imageButton=(ImageButton)view.findViewById(R.id.imageButtonGridPlay);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onClickListener.recyclerViewGridClicked(v,this.getLayoutPosition());

        }
    }


    public GridAdapter(List<Episode> episodeList, Context context, GridAdapter.GridAdapterListener listener) {
        this.episodeList = episodeList;
        this.context = context;
        onClickListener = listener;


    }

    @Override
    public GridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new GridAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GridAdapter.MyViewHolder holder, final int position) {
        Episode episode = episodeList.get(position);
        if (episode.getImageURL() != null) {
            Picasso.with(context).load(episode.getImageURL()).fit().centerCrop().into(holder.imageView);
        } else {
            holder.imageView.setImageDrawable(null);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.playClickGrid(v, position);

            }
        });
        String value=episode.getTitle().toString().trim();
        Log.d("Test123:",""+episode.getTitle().length());
        holder.title.setText(value);
        Log.d("Test1234:",holder.title.getText().toString());

    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }


    static interface GridAdapterListener {

        void playClickGrid(View v, int position);
        public void recyclerViewGridClicked(View v, int position);

    }
}
