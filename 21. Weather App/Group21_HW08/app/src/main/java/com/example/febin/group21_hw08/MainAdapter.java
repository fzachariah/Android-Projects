package com.example.febin.group21_hw08;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;

/**
 * Created by febin on 18/03/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>  {

    public MyAdapterListener onClickListener;
    private List<SavedCity> savedCityList;
    Context context;
    SavedCity tempSavedCity;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView city,temperature,time;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            city = (TextView) view.findViewById(R.id.textViewCityRecycler);
            temperature = (TextView) view.findViewById(R.id.textViewTempRecycler);
            time=(TextView) view.findViewById(R.id.textViewTimeRecycler);
            imageButton=(ImageButton)view.findViewById(R.id.imageButton);
            view.setOnLongClickListener(this);


        }


        @Override
        public boolean onLongClick(View v) {
            Log.d("Log: clicklong",""+this.getLayoutPosition());
            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
            final DatabaseReference myRef = mDatabase.child("cities");
            SavedCity savedCity=savedCityList.get(this.getLayoutPosition());
            myRef.child(savedCity.getKey()).removeValue();
            savedCityList.remove(this.getLayoutPosition());
            notifyDataSetChanged();
            Toast.makeText(context,"Removed from Saved Cities",Toast.LENGTH_LONG).show();

            return true;
        }
    }


    public MainAdapter(List<SavedCity> savedCityList,Context context,MyAdapterListener listener) {
        this.savedCityList = savedCityList;
        this.context=context;
        onClickListener=listener;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_main, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
         DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        final DatabaseReference myRef = mDatabase.child("cities");
        SharedPreferences currentCity = context.getSharedPreferences("currentCity",Context.MODE_PRIVATE);
        Log.d("Log:Temp","TEst"+currentCity.contains("City"));
        try{
            Log.d("Log:Temp",currentCity.getString("tempType",""));
        }catch (Exception e)
        {
            Log.d("Log:Temp",e.toString());
        }




        final SavedCity savedCity = savedCityList.get(position);
        tempSavedCity=savedCity;
        holder.city.setText(savedCity.getCityName()+", "+savedCity.getCountry());
        if(savedCity.isFavorites())
        {
            holder.imageButton.setImageResource(R.drawable.star_gold);
        }
        else {
            holder.imageButton.setImageResource(R.drawable.star_gray);
        }
        String val="";
        Log.d("Log:Temp",currentCity.getString("typeTemp",""));
        if(currentCity.getString("tempType","").equals("C"))
        {
            val=savedCity.getTemperature();
            val=val.replaceAll("C","");
            val=val+(char) 0x00B0+" C";
        }
        else
        {
            val=savedCity.getTemperature();
            val=val.replaceAll("C","");
            val=Util.cToF(val);
            val=val+(char) 0x00B0+" F";
        }
        holder.temperature.setText("Temperature: "+val);
        PrettyTime p = new PrettyTime();
        holder.time.setText("Last Updated: "+p.format(new Date(Long.parseLong(savedCity.getTime()))));


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!savedCity.isFavorites()) {
                    savedCity.setFavorites(true);
                    myRef.child(savedCity.getKey()).setValue(savedCity);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    savedCity.setFavorites(false);
                    myRef.child(savedCity.getKey()).setValue(savedCity);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return savedCityList.size();
    }


    static interface MyAdapterListener {
        /*public void blockItem(View v, int position);*/
        //public void recyclerViewListClicked(View v, int position);

    }
}
