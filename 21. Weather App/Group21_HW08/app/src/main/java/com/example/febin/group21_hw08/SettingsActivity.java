package com.example.febin.group21_hw08;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    int val;

    private final static String CITY="cityname";
    private final static String COUNTRY="countryname";
    private final static String KEY_PASS="key";

    String cityName;
    String country;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(android.R.style.ThemeOverlay_Material_Dark_ActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cityName=getIntent().getExtras().getString(CITY);
        country=getIntent().getExtras().getString(COUNTRY);
        key=getIntent().getExtras().getString(KEY_PASS);
        val=getIntent().getExtras().getInt("Activity");
        setTitle(Html.fromHtml("<font color=\"black\">" + "Preferences" + "</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grid)));
        Fragment fragment=new SettingsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.relativeLayoutContainer, new SettingsFragment(),"container")
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements FetchData.IData
    {

        SharedPreferences currentCity;
        private final static String TOKEN="UHjW8AhcU3OPO15F1Gciv7reaymj1lpN";
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            currentCity = getActivity().getSharedPreferences("currentCity",
                    Context.MODE_PRIVATE);
            addPreferencesFromResource(R.xml.user_settings);

            Preference myPrefTemp = (Preference) findPreference("tempKey");
            Preference myPrefCity = (Preference) findPreference("cityKey");
            myPrefCity.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if(currentCity.contains("Key"))
                    {
                        alertDialogCity(true);
                    }
                    else
                    {
                        alertDialogCity(false);
                    }

                    return true;
                }
            });

           myPrefTemp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    alertTemperature();
                    return true;
                }
            });

        }

        private void alertTemperature() {
            int pos=-1;
            if(currentCity.getString("tempType","").equals("C"))
            {
                pos=0;
            }
            else
            {
                pos=1;
            }

            CharSequence[] items = {"Celsius","Fahrenheit"};
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog);
            alertDialog.setTitle("Choose Temperature Unit");
            alertDialog.setSingleChoiceItems(items, pos, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            final int finalPos = pos;
            alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    if(selectedPosition==0&& finalPos ==1)
                    {
                        SharedPreferences.Editor editor = currentCity.edit();
                        editor.putString("tempType","C");
                        editor.commit();
                        ToastMessage("Temperature Unit has been changed to"+(char) 0x00B0+"C from "+(char) 0x00B0+"F");
                    }
                    else if(selectedPosition==1&& finalPos ==0)
                    {
                        SharedPreferences.Editor editor = currentCity.edit();
                        editor.putString("tempType","F");
                        editor.commit();
                        ToastMessage("Temperature Unit has been changed to"+(char) 0x00B0+"F from "+(char) 0x00B0+"C");
                    }
                    else
                    {
                        ToastMessage("No Change in Unit");
                    }
                    dialog.dismiss();


                }
            });


            AlertDialog alert = alertDialog.create();
            alert.show();



        }

        public void alertDialogCity(boolean check)
        {
            final AlertDialog alertDialog1;
            Log.d("Log:","Reached Inside Function");
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog);
            if(check)
            {
                alertDialog.setMessage("Update City Details");
            }
            else {
                alertDialog.setMessage("Enter City Details");
            }
            LinearLayout linearLayout=new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            final EditText editTextCity = new EditText(getActivity());
            editTextCity.setTextColor(Color.BLACK);
            editTextCity.setHintTextColor(Color.BLACK);
            final EditText editTextCountry = new EditText(getActivity());
            editTextCountry.setTextColor(Color.BLACK);
            editTextCountry.setHintTextColor(Color.BLACK);
            if(check)
            {
                String cityName=currentCity.getString("City","");
                String countryName=currentCity.getString("Country","");
                editTextCity.setText(cityName);
                editTextCountry.setText(countryName);

            }
            else
            {
                editTextCity.setHint("Enter your City");
                editTextCountry.setHint("Enter your Country");
            }

            linearLayout.addView(editTextCity);
            linearLayout.addView(editTextCountry);

            final Button buttonSet=new Button(getActivity());
            if(check)
            {
                buttonSet.setText("Change");
            }
            else {
                buttonSet.setText("Set");
            }
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            buttonSet.setLayoutParams(param);

            final Button buttonCancel=new Button(getActivity());
            buttonCancel.setText("Cancel");
            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            buttonCancel.setLayoutParams(param1);


            LinearLayout linearLayoutHorizontal=new LinearLayout(getActivity());
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
                        Toast.makeText(getActivity(),"Please Enter the values Correctly",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String url="http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey="+TOKEN+"&q="+cityName;
                        new FetchData(SettingsFragment.this).execute(url,"1");
                        alertDialog1.dismiss();



                    }

                }
            });
        }

        @Override
        public void setUpData(String data, String identifier) {

            Log.d("Log:Preference",data);
            parseData(data,identifier);

        }
        private void parseData(String data,String identifier) {

            Log.d("Log:Preference", identifier.trim());

            if (identifier.equals("1")) {
                String key = JSONParser.setLocation(data);
                Log.d("Log:I", key);
                if (key.equals("error")) {
                    Toast.makeText(getActivity(),"City Not Found",Toast.LENGTH_LONG).show();
                    ToastMessage("City Not Found");
                } else {
                    String cityName = JSONParser.getCity(data);
                    String country = JSONParser.getCountry(data);
                    SharedPreferences.Editor editor = currentCity.edit();
                    editor.putString("Key", key);
                    editor.putString("City", cityName);
                    editor.putString("Country", country);
                    editor.commit();
                    editor.commit();
                    ToastMessage("Current City Details Saved");

                }
            }
        }
        public void ToastMessage(String message)
        {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastmessage,
                    (ViewGroup) getActivity().findViewById(R.id.linearLayoutContainer));

            TextView text = (TextView) layout.findViewById(R.id.textViewToast);
            text.setText(message);

            Toast toast = new Toast(getActivity());
            toast.setGravity(Gravity.BOTTOM, 0, 10);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view =  super.onCreateView(inflater, container, savedInstanceState);
           //view.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            return  view;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(val==0)
        {
            Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent=new Intent(SettingsActivity.this,CityWeatherActivity.class);
            intent.putExtra(CITY, cityName);
            intent.putExtra(COUNTRY, country);
            intent.putExtra(KEY_PASS, key);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
