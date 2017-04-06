package com.example.febin.inclass07_group21;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by febin on 27/02/2017.
 */

public class NotesAdapter extends ArrayAdapter<Notes> {


    int mResource;
    Context mContext;
    List<Notes> mData;

    DatabaseDataManager databaseDataManager;


    public NotesAdapter(Context context, int resource, List<Notes> objects) {
        super(context, resource, objects);

        this.mContext=context;
        this.mData=objects;
        this.mResource=resource;


    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        databaseDataManager=new DatabaseDataManager(mContext);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(mResource,parent,false);
        }
        convertView.setLongClickable(true);

        final Notes notes=mData.get(position);
        LinearLayout linearLayout=(LinearLayout)convertView.findViewById(R.id.linearLayout);
        TextView textViewNote=(TextView)convertView.findViewById(R.id.textViewNote);
        TextView textViewPriority=(TextView)convertView.findViewById(R.id.textViewPriority);
        TextView textViewTime=(TextView)convertView.findViewById(R.id.textViewTime);
        CheckBox checkBox=(CheckBox) convertView.findViewById(R.id.checkBox2);

        PrettyTime p = new PrettyTime();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date="";
        try {
            Date d=df.parse(notes.getUpdateTime());
            date=""+p.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        textViewNote.setText(""+notes.getNote());
        textViewTime.setText(date);
        if(notes.getPriority()==100) {
            textViewPriority.setText("High Priority");
        }
        else if(notes.getPriority()==50) {
            textViewPriority.setText("Medium Priority");
        }

        else if(notes.getPriority()==1) {
            textViewPriority.setText("Low Priority");
        }

        if(notes.getStatus()==1)
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Log.d("Inside",""+notes.getId());
                    notes.setStatus(1);
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date today = Calendar.getInstance().getTime();
                    String reportDate = df.format(today);
                    notes.setUpdateTime(reportDate);
                    boolean value=databaseDataManager.updateNote(notes);
                    Log.d("Inside",""+value);
                    Collections.sort(mData, new Comparator<Notes>() {
                        @Override
                        public int compare(Notes p1, Notes p2) {
                            return p1.getStatus()- p2.getStatus();
                        }

                    });
                    notifyDataSetChanged();


                }
                else {

                                    notes.setStatus(0);
                                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                    Date today = Calendar.getInstance().getTime();
                                    String reportDate = df.format(today);
                                    notes.setUpdateTime(reportDate);
                                    databaseDataManager.updateNote(notes);
                                    notifyDataSetChanged();




                }
            }
        });



        return convertView;
    }

    public List<Notes> arrange(List<Notes> notes)
    {
        ArrayList<Notes> notesArrayList=new ArrayList<>();
        List<Notes> notesArrayListComplete=new ArrayList<>();
        List<Notes> notesArrayListPending=new ArrayList<>();
        for(int i=0;i<notes.size();i++)
        {
            Notes notes1=notes.get(i);
            if( notes1.getStatus()==0)
            {
                notesArrayListPending.add(notes1);
            }
            else
            {
                notesArrayListComplete.add(notes1);
            }

        }

        for(int j=0;j<notesArrayListComplete.size();j++)
        {
            notesArrayListPending.add(notesArrayListComplete.get(j));
        }


        return  notesArrayListPending;

    }
}
