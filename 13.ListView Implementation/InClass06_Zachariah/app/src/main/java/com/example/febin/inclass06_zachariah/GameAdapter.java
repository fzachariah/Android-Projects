package com.example.febin.inclass06_zachariah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by febin on 20/02/2017.
 */

public class GameAdapter extends ArrayAdapter<Game> {

    int mResource;
    Context mContext;
    List<Game> mData;




    public GameAdapter(Context context, int resource, List<Game> objects) {
        super(context, resource, objects);

        this.mContext=context;
        this.mData=objects;
        this.mResource=resource;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder ;
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(mResource,parent,false);
            holder = new ViewHolder();
            holder.imageView=(ImageView)convertView.findViewById(R.id.imageViewOne);
            holder.textView=(TextView)convertView.findViewById(R.id.textViewContent);
            convertView.setTag(holder);
        }
        holder=(ViewHolder)convertView.getTag();

        Game game=mData.get(position);
        StringBuilder value = new StringBuilder();
        value.append(game.getTitle());
        String temp = game.getReleaseDate();
        temp = temp.substring(temp.lastIndexOf("/") + 1);
        value.append(" Released in " + temp + ".");
        value.append("Platform: " + game.getPlatform());


        if(!game.getImageURL().equals("")) {
            Log.d("Inside Picasso",game.getImageURL());
            Picasso.with(mContext).load(game.getImageURL().trim()).resize(50,50).into(holder.imageView);
        }
        else
        {
            holder.imageView.setImageDrawable(null);
        }
        holder.textView.setText(""+value.toString());



        return  convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
