package com.example.febin.midterm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by febin on 20/03/2017.
 */

public class CartAdapter extends ArrayAdapter<Product> {

    int mResource;
    Context mContext;
    List<Product> mData;



    public CartAdapter(Context context, int resource, List<Product> objects) {
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

        final Product appInfo=mData.get(position);

        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageViewCart);
        TextView textViewAppName=(TextView)convertView.findViewById(R.id.textViewTitleCart);
        TextView textViewPrice=(TextView)convertView.findViewById(R.id.textViewPriceCart);


        textViewAppName.setText(appInfo.getTitle());
        textViewPrice.setText(appInfo.getPrice());
        Picasso.with(mContext).load(appInfo.getImageURLTwo()).fit().centerCrop().into(imageView);


        return convertView;
    }
}
