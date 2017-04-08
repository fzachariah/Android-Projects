package com.example.febin.group21_hw08;

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

    private List<DayDetails> dayDetailses;
    Context context;

    public GridAdapterListener onClickListener;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewGridDate);
            imageView = (ImageView) view.findViewById(R.id.imageViewMain);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onClickListener.recyclerViewGridClicked(v,this.getLayoutPosition());

        }
    }


    public GridAdapter(List<DayDetails> dayDetailses, Context context, GridAdapterListener listener) {
        this.dayDetailses = dayDetailses;
        this.context = context;
        onClickListener = listener;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DayDetails dayDetails = dayDetailses.get(position);
        if (dayDetails.getDayIcon() != null) {
            String url=dayDetails.getDayIcon().trim();
            if(url.length()==1)
            {
                url="0"+url;
            }
            String url1="http://developer.accuweather.com/sites/default/files/"+url+"-s.png";
            Picasso.with(context).load(url1).fit().centerCrop().into(holder.imageView);
        } else {
            holder.imageView.setImageDrawable(null);
        }
        String value=dayDetails.getDate().toString().trim();
        int i=0;

        String month="";
        String date="";
        String year="";
        for(String temp:value.split(" "))
        {
            if (i==0)
            {
                month=temp;
            }
            else if(i==1)
            {
                date=temp;
                date=date.replace(",","");
                int result=Integer.parseInt(date);
                if(result==1)
                {
                    date="1st";
                }
                else if(result==2)
                {
                    date="2nd";
                }
                else if(result==3)
                {
                    date="3rd";
                }
                else if(result==21)
                {
                    date="21st";
                }
                else if(result==22)
                {
                    date="22nd";
                }
                else if(result==23)
                {
                    date="23rd";
                }
                else if(result==31)
                {
                    date="31st";
                }
                else
                {
                    date=result+"th";
                }

            }
            else
            {
                year = temp;
            }
            i++;
        }
        value=""+date+" "+month+"'"+year;
        holder.title.setText(value);
        Log.d("Test1234:",holder.title.getText().toString());

    }

    @Override
    public int getItemCount() {
        return dayDetailses.size();
    }


    static interface GridAdapterListener {


        public void recyclerViewGridClicked(View v, int layoutPosition);
    }
}
