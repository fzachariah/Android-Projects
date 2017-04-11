package com.example.febin.cloudtest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by febin on 18/03/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>  {

    public MyAdapterListener onClickListener;
    private List<AppInfo> appInfoList;
    Context context;
    AppInfo tempAppInfo;

    HashMap<String,AppInfo> stringAppInfoHashMap=new HashMap<>();


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewAppName;
        public TextView textViewPrice;
        public ImageView imageView;
        public ImageButton imageButton;


        public MyViewHolder(View view) {
            super(view);
            textViewAppName = (TextView) view.findViewById(R.id.textViewApp);
            textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
            imageButton=(ImageButton)view.findViewById(R.id.imageViewStar);
            imageView =(ImageView)view.findViewById(R.id.imageView);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }


    public MainAdapter(List<AppInfo> newsList,Context context,MyAdapterListener listener) {
        this.appInfoList = newsList;
        this.context=context;
        onClickListener=listener;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_linearlayout, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef=mRootRef.child("favorites");
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("LogFebin",""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("LogFebin",""+"Inside");
                    AppInfo user = postSnapshot.getValue(AppInfo.class);
                    Log.d("Get Data", user.toString());
                    stringAppInfoHashMap.put(user.getAppName(),user);
                    Log.d("Here YTesting12",""+stringAppInfoHashMap.size());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Log.d("Here YTesting",""+stringAppInfoHashMap.size());
        final AppInfo appInfo = appInfoList.get(position);
        tempAppInfo=appInfo;
        holder.textViewAppName.setText(appInfo.getAppName());
        holder.textViewPrice.setText(appInfo.getPrice());
        if(appInfo.getImageURL()!=null) {
            Picasso.with(context).load(appInfo.getImageURL()).fit().centerCrop().into(holder.imageView);
        }
        else
        {
            holder.imageView.setImageDrawable(null);
        }

        if(stringAppInfoHashMap.containsKey(appInfo.getAppName()))
        {
            holder.imageButton.setImageResource(R.drawable.blackstar);
        }
        else
        {
            holder.imageButton.setImageResource(R.drawable.whitestar);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageButton imageButton1=(ImageButton)v;
                if(!stringAppInfoHashMap.containsKey(appInfo.getAppName())) {
                    new AlertDialog.Builder(context)
                            .setTitle("Add to Favorites").setCancelable(false)
                            .setMessage("Are you sure that you want to Add this item to favorites")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                     FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference();
                                    imageButton1.setImageResource(R.drawable.blackstar);

                                    holder.imageView.setDrawingCacheEnabled(true);
                                    holder.imageView.buildDrawingCache();
                                    Bitmap bitmap = holder.imageView.getDrawingCache();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    byte[] data = baos.toByteArray();
                                    StorageReference mountainsRef=storageRef.child("images/"+ appInfo.getAppName()+".PNG");
                                    UploadTask uploadTask = mountainsRef.putBytes(data);
                                    uploadTask.addOnSuccessListener((Activity) context,new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            @SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
                                            //textView.setText(downloadUrl.toString());
                                            DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference mConditionRef=mRootRef.child("favorites");
                                            appInfo.setSavedURL(downloadUrl.toString());
                                            mConditionRef.child(appInfo.getAppName()).setValue(appInfo);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                        }
                                    });

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

                        new AlertDialog.Builder(context)
                                .setTitle("Remove from Favorites").setCancelable(false)
                                .setMessage("Are you sure that you want to remove this item from favorites")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        /*SharedPreferences.Editor editor = favorites.edit();
                                        editor.remove(appInfo.getAppName());
                                        editor.commit();
                                        imageButton1.setImageResource(R.drawable.whitestar);*/
                                        DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference mConditionRef=mRootRef.child("favorites");

                                        mConditionRef.child(appInfo.getAppName()).removeValue();
                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference desertRef = storageRef.child("images/"+appInfo.getAppName()+".PNG");
                                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                            }
                                        });



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
        });




    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }


    static interface MyAdapterListener {

        //public void blockItem(View v, int position);
        //public void recyclerViewListClicked(View v, int position);

    }
}
