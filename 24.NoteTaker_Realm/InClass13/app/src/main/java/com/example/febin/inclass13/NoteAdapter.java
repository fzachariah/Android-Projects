package com.example.febin.inclass13;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;




public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{

    public List<NotesTable> NoteList;
    public Context mContext;
    Realm realm=Realm.getDefaultInstance();




//    mp3Interface activity;

    public NoteAdapter(Context context, List<NotesTable> NoteList) {
        this.NoteList = NoteList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_layout, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NotesTable note = NoteList.get(position);

        holder.textViewNote.setText(note.getTaskNote());
        if(note.getPriority()==100) {
            holder.textViewPriority.setText("High Priority");
        }
        else if(note.getPriority()==50)
        {
            holder.textViewPriority.setText("Medium Priority");
        }
        else if(note.getPriority()==1)
        {
            holder.textViewPriority.setText("Low Priority");
        }


        PrettyTime p  = new PrettyTime();
        holder.textViewTime.setText(p.format(note.getCreatedTime()));

        holder.cbStatus.setChecked(note.getStatus().equals("completed"));

        holder.cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setTitle("Are you really want to mark it as pending??")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    NotesTable edit=realm.where(NotesTable.class).equalTo("id",note.getId()).findFirst();
                                    realm.beginTransaction();
                                    edit.setStatus("pending");
                                    edit.setCreatedTime(Calendar.getInstance().getTime());
                                    realm.commitTransaction();


                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.cbStatus.setChecked(true);
                                }
                            }).create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                        }
                    });
                    dialog.show();

                }
                else {

                    NotesTable edit=realm.where(NotesTable.class).equalTo("id",note.getId()).findFirst();
                    realm.beginTransaction();
                    edit.setStatus("completed");
                    edit.setCreatedTime(Calendar.getInstance().getTime());
                    realm.commitTransaction();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return NoteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView textViewNote, textViewPriority,textViewTime;
        public CheckBox cbStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewNote = (TextView)itemView.findViewById(R.id.tvNoteText);
            textViewPriority = (TextView)itemView.findViewById(R.id.tvPriority);
            cbStatus = (CheckBox)itemView.findViewById(R.id.cbStatus);
            textViewTime = (TextView)itemView.findViewById(R.id.tvUpdatedOn);

            itemView.setOnLongClickListener(this);
        }

        public boolean onLongClick(View view) {
            final int position = getAdapterPosition();
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Are you really want to delete the task??")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                NotesTable noteMain = NoteList.get(position);

                                NotesTable delete=realm.where(NotesTable.class).equalTo("id",noteMain.getId()).findFirst();
                                realm.beginTransaction();
                                delete.deleteFromRealm();
                                realm.commitTransaction();


                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).create();
                dialog.show();

            return false;
        }
    }


}

