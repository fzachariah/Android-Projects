package com.example.febin.inclass07_group21;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> staticAdapter;

    ListView listView;
    DatabaseDataManager databaseDataManager;
    ArrayList<Notes> notesArrayListFinal=new ArrayList<>();
    EditText editText;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Note Keeper");

        spinner=(Spinner)findViewById(R.id.spinner);
        staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.priority_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(staticAdapter);

        databaseDataManager=new DatabaseDataManager(this);
        editText=(EditText)findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.listView);

        notesArrayListFinal=(ArrayList<Notes>)databaseDataManager.getAllNews();
        Collections.sort(notesArrayListFinal);
        notesArrayListFinal=arrange(notesArrayListFinal);
        notesAdapter=new NotesAdapter(this,R.layout.row_list,notesArrayListFinal);
        listView.setAdapter(notesAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setMessage("Do You Really want to delete this item")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                databaseDataManager.deleteNews(notesArrayListFinal.get(position));
                                notesArrayListFinal.remove(position);
                                notesAdapter=new NotesAdapter(MainActivity.this,R.layout.row_list,notesArrayListFinal);
                                listView.setAdapter(notesAdapter);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });




    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all:
                Collections.sort(notesArrayListFinal);
                notesArrayListFinal=arrange(notesArrayListFinal);
                notesAdapter=new NotesAdapter(MainActivity.this,R.layout.row_list,notesArrayListFinal);
                listView.setAdapter(notesAdapter);

                return true;
            case R.id.complete:

                notesArrayListFinal= (ArrayList<Notes>) databaseDataManager.getAllNews();
                ArrayList<Notes> arrayList=new ArrayList<>();
                for(int i=0;i<notesArrayListFinal.size();i++)
                {
                    Notes notes=notesArrayListFinal.get(i);
                    if(notes.getStatus()==1)
                    {
                        arrayList.add(notes);
                    }
                }
                notesAdapter=new NotesAdapter(MainActivity.this,R.layout.row_list,arrayList);
                listView.setAdapter(notesAdapter);

                return true;

            case R.id.pending:

                notesArrayListFinal= (ArrayList<Notes>) databaseDataManager.getAllNews();
                ArrayList<Notes> arrayList1=new ArrayList<>();
                for(int i=0;i<notesArrayListFinal.size();i++)
                {
                    Notes notes=notesArrayListFinal.get(i);
                    if(notes.getStatus()==0)
                    {
                        arrayList1.add(notes);
                    }
                }
                notesAdapter=new NotesAdapter(MainActivity.this,R.layout.row_list,arrayList1);
                listView.setAdapter(notesAdapter);

                return true;

            case R.id.time:

                return true;
            case R.id.priority:

                Collections.sort(notesArrayListFinal);
                notesArrayListFinal=arrange(notesArrayListFinal);
                notesAdapter=new NotesAdapter(MainActivity.this,R.layout.row_list,notesArrayListFinal);
                listView.setAdapter(notesAdapter);

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void add(View view)
    {

        String note=editText.getText().toString().trim();
        String priority=spinner.getSelectedItem().toString();

        if(note.length()==0 ||priority.equals("Priority"))
        {
            Toast.makeText(getApplicationContext(),"Please enter Values Correctly",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Notes notes=new Notes();
            notes.setNote(note);
            notes.setStatus(0);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            notes.setUpdateTime(reportDate);
            if(priority.equals("High"))
            {
                notes.setPriority(100);
            }
            else if(priority.equals("Medium"))
            {
                notes.setPriority(50);
            }
            else if(priority.equals("Low"))
            {
                notes.setPriority(1);
            }
            long id=databaseDataManager.saveNote(notes);
            notes.setId(id);
            notesArrayListFinal.add(notes);
            Collections.sort(notesArrayListFinal);
            notesArrayListFinal=arrange(notesArrayListFinal);
            editText.setText("");
            spinner.setSelection(0);
            notesAdapter=new NotesAdapter(this,R.layout.row_list,notesArrayListFinal);
            listView.setAdapter(notesAdapter);



        }

    }
    public ArrayList<Notes> arrange(ArrayList<Notes> notes)
    {
        ArrayList<Notes> notesArrayList=new ArrayList<>();
        ArrayList<Notes> notesArrayListComplete=new ArrayList<>();
        ArrayList<Notes> notesArrayListPending=new ArrayList<>();
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
