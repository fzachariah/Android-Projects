package com.example.febin.group21_hw08;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements FetchData.IData {

    RelativeLayout  relativeLayoutOne;
    LinearLayout linearLayoutDownList;
    ProgressBar progressBarLoad;

    private final static String CITY="cityname";
    private final static String COUNTRY="countryname";
    private final static String KEY_PASS="key";

    String citySearch;
    String countrySerach;

    private final static String TOKEN="UHjW8AhcU3OPO15F1Gciv7reaymj1lpN";

    boolean check=false;

    SharedPreferences currentCity;

    TextView textViewCityName;
    TextView textViewWeatherStatus;
    ImageView imageView;
    TextView textViewTemperature;
    TextView textViewUpdatedTime;

    TextView textViewNotSet;
    Button buttonNotSet;

    EditText editTextCity;
    EditText editTextCountry;

    TextView textViewSavedMessage;
    TextView textViewSavedMessage1;


    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("cities");

    ArrayList<SavedCity> savedCities=new ArrayList<>();

    RecyclerView recyclerView;
    private MainAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather App");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action)));

        currentCity = getSharedPreferences("currentCity",
                Context.MODE_PRIVATE);
        if(!currentCity.contains("tempType"))
        {
            SharedPreferences.Editor editor = currentCity.edit();
            editor.putString("tempType","C");
            editor.commit();
        }

        editTextCity=(EditText)findViewById(R.id.editTextCityName);
        editTextCountry=(EditText)findViewById(R.id.editTextCountry);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        progressBarLoad=(ProgressBar)findViewById(R.id.progressBarLoad);
        progressBarLoad.setVisibility(View.GONE);

        relativeLayoutOne=(RelativeLayout)findViewById(R.id.relativeLayoutFrameOne);
        relativeLayoutOne.setVisibility(View.GONE);
        linearLayoutDownList=(LinearLayout)findViewById(R.id.linearlayoutList);
        linearLayoutDownList.setVisibility(View.GONE);
        textViewCityName=(TextView)findViewById(R.id.textViewCityName);
        textViewTemperature=(TextView)findViewById(R.id.textViewTemperature);
        textViewWeatherStatus=(TextView)findViewById(R.id.textViewWeather);
        textViewUpdatedTime=(TextView)findViewById(R.id.textViewUpdated);
        imageView=(ImageView)findViewById(R.id.imageView);

        textViewSavedMessage=(TextView)findViewById(R.id.textViewSavedMessaage);
        textViewSavedMessage1=(TextView)findViewById(R.id.textViewSavedMessaage1);

        textViewNotSet=(TextView)findViewById(R.id.textViewNotSet);
        buttonNotSet=(Button)findViewById(R.id.buttonSet);


        if(currentCity.contains("Key"))
        {
            progressBarLoad.setVisibility(View.VISIBLE);
            check=true;
            String key=currentCity.getString("Key","");
            String url = "http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey="+TOKEN;
            new FetchData(MainActivity.this).execute(url,"2");
        }
        else
        {
            textViewNotSet.setVisibility(View.VISIBLE);
            buttonNotSet.setVisibility(View.VISIBLE);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                savedCities=new ArrayList<SavedCity>();
                Log.d("Log:main",""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    SavedCity savedCity=postSnapshot.getValue(SavedCity.class);
                    savedCities.add(savedCity);

                }
                if(savedCities.size()>0) {
                    Collections.sort(savedCities, new Comparator<SavedCity>() {
                        @Override
                        public int compare(SavedCity abc1, SavedCity abc2) {
                            int b1 = abc1.isFavorites() ? 1 : 0;
                            int b2 = abc2.isFavorites() ? 1 : 0;

                            return b2 - b1;
                        }
                    });
                    mAdapter = new MainAdapter(savedCities, MainActivity.this, new MainAdapter.MyAdapterListener() {

                    });

                    mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    textViewSavedMessage.setVisibility(View.GONE);
                    textViewSavedMessage1.setVisibility(View.GONE);
                    linearLayoutDownList.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(mAdapter);
                }
                else
                {
                    linearLayoutDownList.setVisibility(View.GONE);
                    textViewSavedMessage1.setVisibility(View.VISIBLE);
                    textViewSavedMessage.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_row, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                intent.putExtra("Activity",0);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setUpData(String data, String identifier) {

        Log.d("Log:identifier",identifier);
        parseData(data,identifier);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void clickSetCurrentCity(View view)
    {

        final AlertDialog alertDialog1;
        Log.d("Log:","Reached Inside Function");
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage("Enter City Details");
        LinearLayout linearLayout=new LinearLayout(MainActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText editTextCity = new EditText(MainActivity.this);
        editTextCity.setHint("Enter your City");
        final EditText editTextCountry = new EditText(MainActivity.this);
        editTextCountry.setHint("Enter your Country");
        linearLayout.addView(editTextCity);
        linearLayout.addView(editTextCountry);

        final Button buttonSet=new Button(MainActivity.this);
        buttonSet.setText("Set");
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        buttonSet.setLayoutParams(param);

        final Button buttonCancel=new Button(MainActivity.this);
        buttonCancel.setText("Cancel");
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        buttonCancel.setLayoutParams(param1);


        LinearLayout linearLayoutHorizontal=new LinearLayout(MainActivity.this);
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHorizontal.addView(buttonCancel);
        linearLayoutHorizontal.addView(buttonSet);
        linearLayout.addView(linearLayoutHorizontal);

        alertDialog.setView(linearLayout);
        alertDialog1=alertDialog.create();
        alertDialog1.setCancelable(false);
        alertDialog1.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cityName=editTextCity.getText().toString().trim();
                final String country=editTextCountry.getText().toString().trim();
                if(cityName.length()==0 ||country.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter the values Correctly",Toast.LENGTH_LONG).show();
                }
                else
                {
                    alertDialog1.dismiss();
                    progressBarLoad.setVisibility(View.VISIBLE);
                    String url="http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey="+TOKEN+"&q="+cityName;
                    new FetchData(MainActivity.this).execute(url,"1");


                }

            }
        });


    }



    private void parseData(String data,String identifier) {

        Log.d("Log:IdentifierParse",identifier.trim());

        if(identifier.equals("1")) {
            String key = JSONParser.setLocation(data);
            Log.d("Log:I", key);
            if (key.equals("error")) {
                ToastMessage("City not Found");
                progressBarLoad.setVisibility(View.GONE);
            } else {
                String cityName=JSONParser.getCity(data);
                String country=JSONParser.getCountry(data);
                SharedPreferences.Editor editor = currentCity.edit();
                editor.putString("Key",key);
                editor.putString("City",cityName);
                editor.putString("Country",country);
                editor.commit();
                editor.commit();
                String url = "http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey="+TOKEN;
                new FetchData(MainActivity.this).execute(url,"2");


            }


        }
        else if(identifier.equals("2"))
        {
            Log.d("Log:2", data);
            ArrayList<String> dataStringArrayList=JSONParser.currentLocationData(data);
            if(dataStringArrayList.size()==5) {
                String cityName = currentCity.getString("City", "");
                String country = currentCity.getString("Country", "");
                textViewCityName.setText(cityName + "," + country);
                textViewWeatherStatus.setText("" + dataStringArrayList.get(2));
                if(currentCity.getString("tempType","").equals("C"))
                {
                    String temp="Temperature: " + dataStringArrayList.get(4)+(char) 0x00B0+" C";
                    textViewTemperature.setText(temp);
                }
                else
                {
                    String val=dataStringArrayList.get(4);
                     val=Util.cToF(val);
                    String temp="Temperature: " + val+ (char) 0x00B0+" F";
                    Log.d("checking here",temp);
                    textViewTemperature.setText(temp);
                }

                PrettyTime p = new PrettyTime();
                Log.d("Log:4",dataStringArrayList.get(0));
                textViewUpdatedTime.setText("Updated "+p.format(new Date(Long.parseLong(dataStringArrayList.get(0))*1000)));
                String val = dataStringArrayList.get(3);
                String imageUrl = "http://developer.accuweather.com/sites/default/files/";
                if (val.length() == 1) {
                    imageUrl = imageUrl + "0" + val + "-s.png";
                } else {
                    imageUrl = imageUrl + val + "-s.png";
                }

                Picasso.with(MainActivity.this).load(imageUrl.trim()).fit().into(imageView);
                Log.d("Log:3", dataStringArrayList.toString());
                textViewNotSet.setVisibility(View.GONE);
                buttonNotSet.setVisibility(View.GONE);
                relativeLayoutOne.setVisibility(View.VISIBLE);
                progressBarLoad.setVisibility(View.GONE);
                if(!check) {
                    ToastMessage("Current City Details Saved");
                }

            }

            else
            {
                ToastMessage("Error Occurred in Fetching data");
            }
        }
        else if(identifier.trim().equals("3")) {
            String key = JSONParser.setLocation(data);
            Log.d("Log:Inside", key);
            if (key.equals("error")) {
                ToastMessage("City not Found");
            } else {
                citySearch=JSONParser.getCity(data);
                countrySerach=JSONParser.getCountry(data);
                Intent intent=new Intent(MainActivity.this,CityWeatherActivity.class);
                intent.putExtra(CITY,citySearch);
                intent.putExtra(COUNTRY,countrySerach);
                intent.putExtra(KEY_PASS,key);
                startActivity(intent);
            }


        }

    }

    public void clickSearchCity(View view)
    {
        citySearch=editTextCity.getText().toString().trim();
        countrySerach=editTextCountry.getText().toString().trim();
        if(citySearch.length()==0||countrySerach.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Please Enter the values Correctly",Toast.LENGTH_LONG).show();
        }
        else
        {
            String url="http://dataservice.accuweather.com/locations/v1/"+countrySerach+"/search?apikey="+TOKEN+"&q="+citySearch;
            new FetchData(MainActivity.this).execute(url,"3");

        }


    }

    public void ToastMessage(String message)
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toastmessage,
                (ViewGroup) findViewById(R.id.linearLayoutContainer));

        TextView text = (TextView) layout.findViewById(R.id.textViewToast);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
