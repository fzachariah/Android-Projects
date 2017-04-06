package com.example.febin.group21_hw06;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by febin on 20/02/2017.
 */

public class AppAdapter extends ArrayAdapter<AppInfo> {

    int mResource;
    Context mContext;
    List<AppInfo> mData;
    SharedPreferences favorites;


    public AppAdapter(Context context, int resource, List<AppInfo> objects) {
        super(context, resource, objects);

        this.mContext=context;
        this.mData=objects;
        this.mResource=resource;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        favorites=mContext.getSharedPreferences("myFavorites",
                Context.MODE_PRIVATE);

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(mResource,parent,false);
        }

        final AppInfo appInfo=mData.get(position);

        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);
        TextView textViewAppName=(TextView)convertView.findViewById(R.id.textViewApp);
        TextView textViewPrice=(TextView)convertView.findViewById(R.id.textViewPrice);
        ImageButton imageButton=(ImageButton)convertView.findViewById(R.id.imageViewStar);

        textViewAppName.setText(appInfo.getAppName());
        textViewPrice.setText(appInfo.getPrice());
        Picasso.with(mContext).load(appInfo.getImageURL()).fit().centerCrop().into(imageView);
        if(!favorites.contains(appInfo.getAppName())) {
            imageButton.setImageResource(R.drawable.whitestar);
        }
        else
        {
            String devName = favorites.getString(appInfo.getAppName(),"");
            if(appInfo.getDevName().equals(devName))
            imageButton.setImageResource(R.drawable.blackstar);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageButton imageButton1=(ImageButton)v;
                if(!favorites.contains(appInfo.getAppName())) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Add to Favorites").setCancelable(false)
                            .setMessage("Are you sure that you want to Add this item to favorites")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = favorites.edit();
                                    editor.putString(appInfo.getAppName(), appInfo.getDevName());
                                    editor.commit();
                                    imageButton1.setImageResource(R.drawable.blackstar);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else
                {
                    String devName = favorites.getString(appInfo.getAppName(),"");
                    if(appInfo.getDevName().equals(devName))
                    {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Remove from Favorites").setCancelable(false)
                                .setMessage("Are you sure that you want to remove this item from favorites")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor editor = favorites.edit();
                                        editor.remove(appInfo.getAppName());
                                        editor.commit();
                                        imageButton1.setImageResource(R.drawable.whitestar);


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                }



            }
        });

        return convertView;
    }
}
