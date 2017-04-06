package com.example.febin.midterm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by febin on 05/03/2017.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    private List<Product> episodeList;
    Context context;
    boolean aBoolean[];

    public GridAdapterListener onClickListener;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title,price,discount;
        public ImageView imageView;
        public Button imageButton;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewTitle);
            price = (TextView) view.findViewById(R.id.textViewPrice);
            discount = (TextView) view.findViewById(R.id.textViewDiscount);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageButton=(Button) view.findViewById(R.id.buttonAddCart);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onClickListener.recyclerViewGridClicked(v,this.getLayoutPosition());

        }
    }


    public GridAdapter(List<Product> episodeList, Context context, GridAdapterListener listener) {
        this.episodeList = episodeList;
        this.context = context;
        onClickListener = listener;
        aBoolean=new boolean[episodeList.size()];


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Product episode = episodeList.get(position);
        Log.d("Checking Url",episode.getImageURLTwo());
        if(!episode.isStatus()) {
            holder.imageButton.setEnabled(true);
        }
        else
        {
            holder.imageButton.setEnabled(false);
        }
        if (episode.getImageURLTwo() != null) {
            Picasso.with(context).load(episode.getImageURLTwo().trim()).resize(150,150).into(holder.imageView);
        } else {
            holder.imageView.setImageDrawable(null);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               episode.setStatus(true);
                if(episode.isStatus()) {
                    holder.imageButton.setEnabled(false);
                }
                onClickListener.ClickButton(v, position);

            }
        });
        String value=episode.getTitle().toString().trim();
        Log.d("Test123:",""+episode.getTitle().length());
        holder.title.setText(value);
        holder.price.setText(episode.getPrice()+" $");
        holder.discount.setText(episode.getDiscount()+" %");

        Log.d("Test1234:",holder.title.getText().toString());

    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }


    static interface GridAdapterListener {

        void ClickButton(View v, int position);
        public void recyclerViewGridClicked(View v, int position);

    }
}
