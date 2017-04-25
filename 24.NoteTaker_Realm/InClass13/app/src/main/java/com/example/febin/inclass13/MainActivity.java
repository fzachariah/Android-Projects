package com.example.febin.inclass13;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    EditText etNotes;
    Spinner spPriority;
    List<NotesTable> notesList;

    Realm realm;
    RealmResults<NotesTable> notes;

    RecyclerView listView;
    HashMap<String,Integer> priorityMap = new HashMap<String, Integer>(){
        {
            put("High",100);
            put("Medium",50);
            put("Low",1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Note Keeper");
        listView =(RecyclerView) findViewById(R.id.listviewNotes);
        notesList = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        notes=realm.where(NotesTable.class).findAll();
        List<NotesTable> pendingNotes1 = new ArrayList<>();
        RealmResults<NotesTable> notesPend1=realm.where(NotesTable.class).equalTo("status","pending").findAll().sort("priority",Sort.DESCENDING);
        for (NotesTable note:notesPend1) {
            if(note.getStatus().equals("pending"))
                pendingNotes1.add(note);
        }

        List<NotesTable> completedNotes1 = new ArrayList<>();
        RealmResults<NotesTable> notesCom1=realm.where(NotesTable.class).equalTo("status","completed").findAll().sort("priority", Sort.DESCENDING);
        for (NotesTable note:notesCom1) {
            if(note.getStatus().equals("completed"))
                completedNotes1.add(note);
        }

        for(int i=0;i<completedNotes1.size();i++){
            pendingNotes1.add(completedNotes1.get(i));
        }
        FillAppsList((ArrayList<NotesTable>) pendingNotes1);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

        etNotes = (EditText)findViewById(R.id.etNote);
        listView =(RecyclerView) findViewById(R.id.listviewNotes);

        spPriority =(Spinner)findViewById(R.id.spPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_map, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(adapter);

        notes.addChangeListener(new RealmChangeListener<RealmResults<NotesTable>>() {
            @Override
            public void onChange(RealmResults<NotesTable> element) {
                List<NotesTable> pendingNotes1 = new ArrayList<>();
                RealmResults<NotesTable> notesPend1=realm.where(NotesTable.class).equalTo("status","pending").findAll().sort("priority",Sort.DESCENDING);
                for (NotesTable note:notesPend1) {
                    if(note.getStatus().equals("pending"))
                        pendingNotes1.add(note);
                }

                List<NotesTable> completedNotes1 = new ArrayList<>();
                RealmResults<NotesTable> notesCom1=realm.where(NotesTable.class).equalTo("status","completed").findAll().sort("priority", Sort.DESCENDING);
                for (NotesTable note:notesCom1) {
                    if(note.getStatus().equals("completed"))
                        completedNotes1.add(note);
                }

                for(int i=0;i<completedNotes1.size();i++){
                    pendingNotes1.add(completedNotes1.get(i));
                }

                FillAppsList((ArrayList<NotesTable>) pendingNotes1);
            }
        });




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsNullorEmpty(etNotes.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please Add a Note",Toast.LENGTH_SHORT).show();
                }
                else {
                    final String priority = spPriority.getSelectedItem().toString();
                    if(priority.equals("Priority")){
                        Toast.makeText(getApplicationContext(),"Please set priority",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            NotesTable note = bgRealm.createObject(NotesTable.class);
                            final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                            note.setId(uuid);
                            note.setTaskNote(etNotes.getText().toString());
                            note.setPriority(priorityMap.get(priority));
                            note.setStatus("pending");
                            note.setCreatedTime(Calendar.getInstance().getTime());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.d("Sucess","hoooray");
                            Toast.makeText(MainActivity.this,"Note Saved",Toast.LENGTH_SHORT).show();
                            etNotes.setText("");
                            //refreshData();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            // Transaction failed and was automatically canceled.
                        }
                    });

                    FillAppsList(notesList);

                }

            }
        });
    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }


    public void FillAppsList(final List<NotesTable> notesList){

        Log.d("demo7","Adapter called");
        Log.d("demo7",notesList.toString());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
        NoteAdapter adapter = new NoteAdapter(this,notesList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.show_all:

                List<NotesTable> pendingNotes2 = new ArrayList<>();
                RealmResults<NotesTable> notesPend2=realm.where(NotesTable.class).equalTo("status","pending").findAll().sort("priority",Sort.DESCENDING);
                for (NotesTable note:notesPend2) {
                    if(note.getStatus().equals("pending"))
                        pendingNotes2.add(note);
                }

                List<NotesTable> completedNotes2 = new ArrayList<>();
                RealmResults<NotesTable> notesCom2=realm.where(NotesTable.class).equalTo("status","completed").findAll().sort("priority", Sort.DESCENDING);
                for (NotesTable note:notesCom2) {
                    if(note.getStatus().equals("completed"))
                        completedNotes2.add(note);
                }

                for(int i=0;i<completedNotes2.size();i++){
                    pendingNotes2.add(completedNotes2.get(i));
                }

                FillAppsList((ArrayList<NotesTable>) pendingNotes2);

                break;
            case R.id.show_completed:
                List<NotesTable> completedNotes = new ArrayList<>();
                RealmResults<NotesTable> notesCom=realm.where(NotesTable.class).equalTo("status","completed").findAll().sort("priority",Sort.DESCENDING);;
                for (NotesTable note:notesCom) {
                    if(note.getStatus().equals("completed"))
                        completedNotes.add(note);
                }
                FillAppsList(completedNotes);
                break;
            case R.id.show_pending:
                List<NotesTable> pendingNotes = new ArrayList<>();
                RealmResults<NotesTable> notesPend=realm.where(NotesTable.class).equalTo("status","pending").findAll().sort("priority",Sort.DESCENDING);
                for (NotesTable note:notesPend) {
                    if(note.getStatus().equals("pending"))
                        pendingNotes.add(note);
                }
                FillAppsList(pendingNotes);
                break;
            case R.id.sort_by_priority:

                List<NotesTable> pendingNotes1 = new ArrayList<>();
                RealmResults<NotesTable> notesPend1=realm.where(NotesTable.class).equalTo("status","pending").findAll().sort("priority",Sort.DESCENDING);
                for (NotesTable note:notesPend1) {
                    if(note.getStatus().equals("pending"))
                        pendingNotes1.add(note);
                }

                List<NotesTable> completedNotes1 = new ArrayList<>();
                RealmResults<NotesTable> notesCom1=realm.where(NotesTable.class).equalTo("status","completed").findAll().sort("priority", Sort.DESCENDING);
                for (NotesTable note:notesCom1) {
                    if(note.getStatus().equals("completed"))
                        completedNotes1.add(note);
                }

                for(int i=0;i<completedNotes1.size();i++){
                    pendingNotes1.add(completedNotes1.get(i));
                }

                FillAppsList((ArrayList<NotesTable>) pendingNotes1);
                break;
            case R.id.sort_by_time:

                RealmResults<NotesTable> notesSample=realm.where(NotesTable.class).findAll();
                notesSample = notesSample.sort("createdTime", Sort.DESCENDING);
                List<NotesTable> notes=new ArrayList<>();
                for(NotesTable note: notesSample)
                {
                    Log.d("Noted: ",note.toString());
                    if(note.getId()!=null)
                        notes.add(note);
                }
                notesList=notes;
                FillAppsList(notesList);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
