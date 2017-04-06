package com.example.febin.hw2_group21;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movies> moviesList=new ArrayList<>();
    final static int REQ_CODE_ADD=100;
    final static int REQ_CODE_EDIT=101;
    final static  String MOVIE_OBJECT="Movie";
    final static  String MOVIE_ORDER="Order";
    final static  String MOVIE_LIST="List";
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    CharSequence items[];
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CODE_ADD)
        {
            if(resultCode==RESULT_OK)
            {
                Movies movie=(Movies)data.getExtras().getSerializable(MOVIE_OBJECT);
                moviesList.add(movie);
            }
        }
        if(requestCode==REQ_CODE_EDIT)
        {
            if(resultCode==RESULT_OK)
            {
                Movies movie=(Movies)data.getExtras().getSerializable(MOVIE_OBJECT);
                int value=data.getExtras().getInt(MOVIE_ORDER);
                moviesList.add(value,movie);
                moviesList.remove(value+1);
            }
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(),"No changes done",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Favorite Movies");
    }
    public void addMovies(View view)
    {
        Intent intent=new Intent(MainActivity.this,AddActivity.class);
        startActivityForResult(intent,REQ_CODE_ADD);
    }
    public void editMovies(View view)
    {
        items=new CharSequence[moviesList.size()];
        for(int i=0;i<moviesList.size();i++)
        {
            items[i]=moviesList.get(i).getName().toString();
        }
        if(items.length==0)
        {
            Toast.makeText(getApplicationContext(),"No Movie Present",Toast.LENGTH_SHORT).show();
        }else {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick a Movie")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, EditActivity.class);
                            intent.putExtra(MOVIE_OBJECT, moviesList.get(which));
                            intent.putExtra(MOVIE_ORDER, which);
                            startActivityForResult(intent, REQ_CODE_EDIT);
                        }
                    });
            alertDialog = builder.create();
            alertDialog.show();
        }
    }
    public void deleteMovies(View view)
    {
        items=new CharSequence[moviesList.size()];
        for(int i=0;i<moviesList.size();i++)
        {
            items[i]=moviesList.get(i).getName().toString();
        }
        if(items.length==0)
        {
            Toast.makeText(getApplicationContext(),"No Movie Present",Toast.LENGTH_SHORT).show();
        }
        else {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick a Movie")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Movies movies=(Movies)moviesList.get(which);
                            Toast.makeText(getApplicationContext(),""+movies.getName()+" deleted",Toast.LENGTH_SHORT).show();
                            moviesList.remove(which);
                        }
                    });
            alertDialog = builder.create();
            alertDialog.show();
        }
    }
    public void listMoviesYear(View view)
    {
        if(moviesList.size()==0)
        {
            Toast.makeText(getApplicationContext(),"No Movies Presnt",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent("com.example.febin.hw2_group21.intent.action.YEAR");
            intent.putExtra(MOVIE_LIST, moviesList);
            startActivity(intent);
        }
    }
    public void listMoviesRating(View view)
    {
        if(moviesList.size()==0)
        {
            Toast.makeText(getApplicationContext(),"No Movies Present",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent("com.example.febin.hw2_group21.intent.action.RATING");
            intent.putExtra(MOVIE_LIST, moviesList);
            startActivity(intent);
        }
    }

}
