package com.example.febin.group21_hw08;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CityWeatherActivity extends AppCompatActivity implements FetchData.IData  {

    private final static String CITY="cityname";
    private final static String COUNTRY="countryname";
    private final static String TOKEN="UHjW8AhcU3OPO15F1Gciv7reaymj1lpN";
    private final static String KEY_PASS="key";


    private GridAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager ;

    ProgressDialog progressDialog;

    String cityName;
    String country;
    String key;

    SharedPreferences currentCity;

    TextView textViewHeading;
    TextView textViewHeadLine;
    TextView textViewDate;
    TextView textViewTemperature;
    TextView textViewDayPhrase;
    ImageView imageViewDay;
    TextView textViewNightPhrase;
    ImageView imageViewNight;

    TextView textViewMoreDetails;
    TextView textViewExtended;
    RecyclerView recyclerView;
    int current=0;
    HashMap<String,Boolean> keyArrayList= new HashMap<>();

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("cities");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentCity = getSharedPreferences("currentCity",
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_city_weather);
        setTitle("Weather App");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action)));

        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        cityName=getIntent().getExtras().getString(CITY);
        country=getIntent().getExtras().getString(COUNTRY);
        key=getIntent().getExtras().getString(KEY_PASS);
        Log.d("Log:10",cityName);
        Log.d("Log:11",country);
        Log.d("Log:12",key);

        textViewHeading=(TextView)findViewById(R.id.textViewHeading);
        textViewHeadLine=(TextView)findViewById(R.id.textViewHeadLine);
        textViewDate=(TextView)findViewById(R.id.textViewDate);
        textViewTemperature=(TextView)findViewById(R.id.textViewTemperature);
        textViewDayPhrase=(TextView)findViewById(R.id.textViewDayStats);
        textViewNightPhrase=(TextView)findViewById(R.id.textViewNightStats);
        imageViewDay=(ImageView)findViewById(R.id.imageViewDay);
        imageViewNight=(ImageView)findViewById(R.id.imageViewNight);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        textViewMoreDetails=(TextView)findViewById(R.id.textViewDetails);
        textViewExtended=(TextView)findViewById(R.id.textViewExtended);

        textViewMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(""+textViewMoreDetails.getTag()));
                startActivity(browserIntent);
            }
        });

        textViewExtended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(""+textViewExtended.getTag()));
                startActivity(browserIntent);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Log:firebase", String.valueOf(dataSnapshot.getChildrenCount()));
                keyArrayList.clear();
                keyArrayList=new HashMap<String, Boolean>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    SavedCity savedCity=ds.getValue(SavedCity.class);
                    keyArrayList.put(savedCity.getKey(),savedCity.isFavorites());
                }
                Log.d("Log:firebase:",""+keyArrayList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String url="http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+key+"?apikey="+TOKEN;
        new FetchData(CityWeatherActivity.this).execute(url,"4");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cityweather, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveCity:
                String url = "http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey="+TOKEN;
                new FetchData(CityWeatherActivity.this).execute(url,"5");
                return true;

            case R.id.setAsCurrent:

                if(currentCity.contains("Key"))
                {
                    if(currentCity.getString("Key","").equals(key)) {
                        SharedPreferences.Editor editor = currentCity.edit();
                        editor.putString("Key", key);
                        editor.putString("City", cityName);
                        editor.putString("Country", country);
                        editor.commit();
                        ToastMessage("Current City Updated");
                    }
                    else
                    {
                        SharedPreferences.Editor editor = currentCity.edit();
                        editor.putString("Key", key);
                        editor.putString("City", cityName);
                        editor.putString("Country", country);
                        editor.commit();
                        ToastMessage("Current City Saved");
                    }
                }
                else
                {
                    SharedPreferences.Editor editor = currentCity.edit();
                    editor.putString("Key",key);
                    editor.putString("City",cityName);
                    editor.putString("Country",country);
                    editor.commit();
                    ToastMessage("Current City Saved");
                }

                return true;

            case R.id.settingsCity:

                Intent intent=new Intent(CityWeatherActivity.this,SettingsActivity.class);
                intent.putExtra("Activity",1);
                intent.putExtra(CITY,cityName);
                intent.putExtra(COUNTRY,country);
                intent.putExtra(KEY_PASS,key);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCity(ArrayList<String> dataStringArrayList) {
        SavedCity savedCity=new SavedCity();
        savedCity.setKey(key);
        savedCity.setCountry(country);
        savedCity.setCityName(cityName);
        savedCity.setFavorites(false);
        savedCity.setTemperature(dataStringArrayList.get(4) + " C");
        Date date = new Date();
        long epoch = date.getTime();
        savedCity.setTime(""+epoch);
        myRef.child(key).setValue(savedCity);

    }


    @Override
    public void setUpData(String data, String identifier) {

        Log.d("Log:data,",data);
        parseData(data,identifier);

}

    @Override
    public Context getContext() {
        return this;
    }

    private void parseData(String data,String identifier) {

        if(identifier.equals("4"))
        {
            textViewHeading.setText("Daily forecast for "+cityName+", "+country);
            String heading=JSONParser.getHeading(data);
            textViewHeadLine.setText(heading);
            String foreCastDay=JSONParser.getDate(data);
            textViewDate.setText("Forecast on "+foreCastDay);
            String temperature=JSONParser.getTemp(data,currentCity.getString("tempType",""));
            textViewTemperature.setText(temperature);

            ArrayList<String> stringArrayList=JSONParser.getNightDay(data);
            Log.d("Log:check",stringArrayList.toString());
            String dayIcon=stringArrayList.get(0).trim();
            String dayPhrase=stringArrayList.get(1);

            if(dayIcon.length()==1)
            {
                dayIcon="0"+dayIcon;
            }
            dayIcon="http://developer.accuweather.com/sites/default/files/"+dayIcon+"-s.png";

            Picasso.with(CityWeatherActivity.this).load(dayIcon.trim()).fit().into(imageViewDay);
            textViewDayPhrase.setText(dayPhrase);

            String nightIcon=stringArrayList.get(2).trim();
            String nightPhrase=stringArrayList.get(3);

            if(nightIcon.length()==1)
            {
                nightIcon="0"+nightIcon;
            }
            nightIcon="http://developer.accuweather.com/sites/default/files/"+nightIcon+"-s.png";
            Picasso.with(CityWeatherActivity.this).load(nightIcon.trim()).fit().into(imageViewNight);
            textViewNightPhrase.setText(nightPhrase);
            String urlMore=JSONParser.getURLMore(data);
            String urlExtended=JSONParser.getURLExtended(data);
            textViewMoreDetails.setTag(urlMore);
            textViewExtended.setTag(urlExtended);
            final ArrayList<DayDetails> dayDetailses=JSONParser.getFiveDayDetails(data);
            mAdapter = new GridAdapter(dayDetailses, getApplicationContext(), new GridAdapter.GridAdapterListener() {
                @Override
                public void recyclerViewGridClicked(View v, int layoutPosition) {
                    DayDetails dayDetails=dayDetailses.get(layoutPosition);
                    Log.d("Log:Important",dayDetails.toString());
                    String val="";
                    SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
                    SimpleDateFormat dateFormat1=new SimpleDateFormat("MMMM dd, yyyy");
                    try {
                        Date date=dateFormat.parse(dayDetails.getDate());
                        val=dateFormat1.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    textViewDate.setText("Forecast on "+val);
                    if(currentCity.getString("tempType","").equals("C"))
                    {
                        String maximumVal=Util.fToC(dayDetails.getMaximumTemp());
                        String minimumVal=Util.fToC(dayDetails.getMinimumTemp());
                        String temp="Temperature: "+maximumVal+(char) 0x00B0 +"/"+minimumVal+(char) 0x00B0;
                        textViewTemperature.setText(temp);
                    }
                    else
                    {
                        String maximumVal=(dayDetails.getMaximumTemp());
                        String minimumVal=(dayDetails.getMinimumTemp());
                        String temp="Temperature: "+maximumVal+(char) 0x00B0 +"/"+minimumVal+(char) 0x00B0;
                        textViewTemperature.setText(temp);
                    }

                    String dayIcon=dayDetails.getDayIcon().trim();
                    if(dayIcon.length()==1)
                    {
                        dayIcon="0"+dayIcon;
                    }
                    dayIcon="http://developer.accuweather.com/sites/default/files/"+dayIcon+"-s.png";

                    Picasso.with(CityWeatherActivity.this).load(dayIcon.trim()).fit().into(imageViewDay);
                    textViewDayPhrase.setText(dayDetails.getDayPhrase());

                    String nightIcon=dayDetails.getNightIcon();
                    String nightPhrase=dayDetails.getNightPhrase();

                    if(nightIcon.length()==1)
                    {
                        nightIcon="0"+nightIcon;
                    }
                    nightIcon="http://developer.accuweather.com/sites/default/files/"+nightIcon+"-s.png";
                    Picasso.with(CityWeatherActivity.this).load(nightIcon.trim()).fit().into(imageViewNight);
                    textViewNightPhrase.setText(nightPhrase);
                    String urlMore=dayDetails.getMobileLink();
                    textViewMoreDetails.setTag(urlMore);

                }
            });

            mLayoutManager = new LinearLayoutManager(CityWeatherActivity.this,LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);


            progressDialog.dismiss();
        }
        else if(identifier.equals("5"))
        {
            Log.d("Log:data", data);
            ArrayList<String> dataStringArrayList=JSONParser.currentLocationData(data);
            if(!keyArrayList.containsKey(key)) {
                saveCity(dataStringArrayList);
                ToastMessage("City Saved");
            }else
            {
                SavedCity savedCity=new SavedCity();
                savedCity.setKey(key);
                savedCity.setCountry(country);
                savedCity.setCityName(cityName);
                savedCity.setTemperature(dataStringArrayList.get(4) + " C");
                Date date = new Date();
                long epoch = date.getTime();
                savedCity.setTime(""+epoch);
                if(keyArrayList.get(key))
                {
                    savedCity.setFavorites(true);
                }
                else
                {
                    savedCity.setFavorites(false);
                }
                myRef.child(key).setValue(savedCity);
                ToastMessage("City Updated");
            }
        }
        else if(identifier.equals("6"))
        {
            Log.d("Log:data", data);
            ArrayList<String> dataStringArrayList=JSONParser.currentLocationData(data);
            saveCity(dataStringArrayList);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(CityWeatherActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
