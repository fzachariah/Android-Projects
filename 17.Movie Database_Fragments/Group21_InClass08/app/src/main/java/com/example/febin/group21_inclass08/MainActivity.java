package com.example.febin.group21_inclass08;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener,AddFragment.OnFragmentInteractionListener,EditFragment.OnFragmentInteractionListener,YearFragment.OnFragmentInteractionListener,RatingFragment.OnFragmentInteractionListener {

    ArrayList<Movies> moviesList=new ArrayList<>();
    final String DATA="data";
    final static  String MOVIE_ORDER="Order";
    final static  String MOVIE_LIST="List";

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    CharSequence items[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Favorite Movies");
        getFragmentManager().beginTransaction().add(R.id.container,new BlankFragment(),"fragment1").commit();
    }

    @Override
    public void userAction(int type) {
        Log.d("Printing Type",""+type);
        if(type==0)
        {
            setTitle("Add Movie");
            getFragmentManager().beginTransaction().replace(R.id.container,new AddFragment(),"add").addToBackStack(null).commit();
        }

        else if(type==1)
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
                                Log.d("Clicked on item",moviesList.get(which).toString());
                                EditFragment editFragment=new EditFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(DATA,moviesList.get(which));
                                bundle.putInt(MOVIE_ORDER,which);
                                editFragment.setArguments(bundle);
                                setTitle("Edit Movie");
                                getFragmentManager().beginTransaction().replace(R.id.container,editFragment,"edit").addToBackStack(null).commit();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
            }
        }

        else if(type==2)
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


        else if(type==3)
        {
            //year
            if(moviesList.size()==0)
            {
                Toast.makeText(getApplicationContext(),"No Movies Present",Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("Inside correct","Test");
                YearFragment yearFragment=new YearFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(MOVIE_LIST,moviesList);
                yearFragment.setArguments(bundle);
                setTitle("Movies by Year");
                getFragmentManager().beginTransaction().replace(R.id.container,yearFragment,"year").addToBackStack(null).commit();
            }
        }

        else if(type==4)
        {
            //rating
            Log.d("Inside correct","Test");
            if(moviesList.size()==0)
            {
                Toast.makeText(getApplicationContext(),"No Movies Present",Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("Inside correct","TestRating");
                RatingFragment ratingFragment=new RatingFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(MOVIE_LIST,moviesList);
                ratingFragment.setArguments(bundle);
                setTitle("Movies by Rating");
                getFragmentManager().beginTransaction().replace(R.id.container,ratingFragment,"rating").addToBackStack(null).commit();
            }
        }


    }

    @Override
    public void movieAdded(Movies movies) {
        moviesList.add(movies);
        getFragmentManager().popBackStack();
        setTitle("My Favorite Movies");
        Log.d("Movie Added:",movies.toString());
    }


    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount()>0)
        {
            getFragmentManager().popBackStack();
            setTitle("My Favorite Movies");
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void movieEdited(int which, Movies movies) {
        moviesList.add(which,movies);
        moviesList.remove(which+1);
        setTitle("My Favorite Movies");
        getFragmentManager().popBackStack();
        Log.d("Movie Added:",movies.toString());
    }

    @Override
    public void finishYear() {

        getFragmentManager().popBackStack();
        setTitle("My Favorite Movies");
    }

    @Override
    public void finishRating() {

        getFragmentManager().popBackStack();
        setTitle("My Favorite Movies");

    }
}
