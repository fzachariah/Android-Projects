package edu.uncc.notekeeper_firebase;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/*
Assignment InClass10
MainActivity.java
Sai Yesaswy Mylavarapu, Harish Pendyala, Febin Zachariah, Danjie Gu
 */

public class MainActivity extends AppCompatActivity {
    Button btnAdd;
    EditText etNotes;
    Spinner spPriority;
    List<Note> notesList;
    Note note;

    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childref;

    RecyclerView listView;
    HashMap<String,String> priorityMap = new HashMap<String, String>(){
        {
            put("High","High priority");
            put("Medium","Medium priority");
            put("Low","Low priority");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Note Keeper");

        childref = dref.child("Notes");

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

        etNotes = (EditText)findViewById(R.id.etNote);
        listView =(RecyclerView) findViewById(R.id.listviewNotes);

        spPriority =(Spinner)findViewById(R.id.spPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_map, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(adapter);

        notesList = new ArrayList<>();

        childref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo5", String.valueOf(dataSnapshot.getChildrenCount()));
                ArrayList<Note> notesList1= new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Note note = ds.getValue(Note.class);
                    notesList1.add(note);
                    Log.d("demo5",note.toString());
                }
                Log.d("demo6",notesList1.toString());
                notesList = notesList1;
                FillAppsList(sortList((ArrayList<Note>) notesList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("demo","notes cannot be fetched");
            }
        });

        FillAppsList(sortList((ArrayList<Note>) notesList));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsNullorEmpty(etNotes.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please Add a Note",Toast.LENGTH_SHORT).show();
                }
                else {
                    String priority = spPriority.getSelectedItem().toString();
                    if(priority.equals("Priority")){
                        Toast.makeText(getApplicationContext(),"Please set priority",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    note = new Note();
                    note.setTaskNote(etNotes.getText().toString());
                    note.setPriority(priorityMap.get(priority));
                    note.setStatus("pending");
                    note.setCreatedTime(Calendar.getInstance().getTime());

                    Log.d("demo",note.toString());

                    DatabaseReference newRef = childref.push();
                    note.setId(String.valueOf(newRef));

                    newRef.setValue(note);

                    FillAppsList(notesList);

                }

            }
        });
    }

    public ArrayList<Note> sortList(ArrayList<Note> notelist){
        ArrayList<Note> list1,list2;
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        for(int i=0;i<notelist.size();i++){
            if(notelist.get(i).getStatus().equals("completed")){
                list1.add(notelist.get(i));
            }
            else if(notelist.get(i).getStatus().equals("pending")){
                list2.add(notelist.get(i));
            }
        }

        Collections.sort(list1,new CompareByPriority());
        Collections.sort(list2,new CompareByPriority());

        for(int i=0;i<list1.size();i++){
            list2.add(list1.get(i));
        }

        Log.d("demo9",list2.toString());

        return list2;
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
                FillAppsList(sortList((ArrayList<Note>) notesList));
                break;
            case R.id.show_completed:
                List<Note> completedNotes = new ArrayList<>();
                for (Note note:notesList) {
                    if(note.getStatus().equals("completed"))
                        completedNotes.add(note);
                }
                FillAppsList(completedNotes);
                break;
            case R.id.show_pending:
                List<Note> pendingNotes = new ArrayList<>();
                for (Note note:notesList) {
                    if(note.getStatus().equals("pending"))
                        pendingNotes.add(note);
                }
                FillAppsList(pendingNotes);
                break;
            case R.id.sort_by_priority:
                FillAppsList(sortList((ArrayList<Note>) notesList));
                break;
            case R.id.sort_by_time:
                Collections.sort(notesList,new CompareByTime());
                FillAppsList(notesList);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void FillAppsList(final List<Note> notesList){
        //this.notesList = notesList;
        Log.d("demo7","Adapter called");
        Log.d("demo7",notesList.toString());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
        NoteAdapter adapter = new NoteAdapter(this,notesList);
        listView.setAdapter(adapter);
    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }


    private class CompareByStatus implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return (o1.getStatus().equals(o2.getStatus()))?0:(o1.getStatus().equals("pending"))?-1:1 ;
        }
    }

    private class CompareByPriority implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            int i=0,j = 0;
            if(o1.getPriority().equals("Low priority")){
                i=1;
            }
            else if(o1.getPriority().equals("Medium priority")){
                i=5;
            }
            else if(o1.getPriority().equals("High priority")){
                i=10;
            }

            if(o2.getPriority().equals("Low priority")){
                j=1;
            }
            else if(o2.getPriority().equals("Medium priority")){
                j=5;
            }
            else if(o2.getPriority().equals("High priority")){
                j=10;
            }

            return (j-i);
            //return (o1.getPriority().equals(o2.getPriority()))?0:(o1.getPriority().equals("Low priority"))?-1:(o1.getPriority().equals("Medium priority"))?-1:1 ;
        }
    }

    private class CompareByTime implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return (o1.getCreatedTime()==o2.getCreatedTime())?0:(o1.getCreatedTime().after(o2.getCreatedTime()))?-1:1 ;
        }
    }

}
