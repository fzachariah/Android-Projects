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

public class SimilarAdapter extends ArrayAdapter<Game> {


    int mResource;
    Context mContext;
    List<Game> mData;


    public SimilarAdapter(Context context, int resource, List<Game> objects) {
        super(context, resource, objects);

        this.mContext=context;
        this.mData=objects;
        this.mResource=resource;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(mResource,parent,false);
        }

        Game game=mData.get(position);
        StringBuilder value = new StringBuilder();
        value.append(game.getTitle());
        if(game.getReleaseDate()!=null &&game.getReleaseDate().length()>0) {
            String temp = game.getReleaseDate();
            temp = temp.substring(temp.lastIndexOf("/") + 1);
            value.append(" Released in " + temp + ".");
        }
        else
        {
            value.append(" Released in ");
        }
        if(game.getPlatform()!=null&&game.getPlatform().length()>0) {
            value.append("Platform: " + game.getPlatform());
        }
        else
        {
            value.append("Platform: ");
        }
        TextView textViewContent=(TextView)convertView.findViewById(R.id.textViewDisplay);
        textViewContent.setText(""+value.toString());



        return  convertView;
    }
}
