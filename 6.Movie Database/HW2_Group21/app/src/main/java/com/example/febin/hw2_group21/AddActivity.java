package com.example.febin.hw2_group21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    Spinner genreSpinner;
    SeekBar seekBarRating;
    EditText editTextName;
    EditText editTextDescription;
    EditText editTextyear;
    EditText editTextLink;
    TextView textViewRating;
    ArrayAdapter<CharSequence> staticAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Add Movies");
        genreSpinner=(Spinner)findViewById(R.id.spinnerGenre2);
        staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.genre_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(staticAdapter);
        textViewRating=(TextView) findViewById(R.id.textViewDisplay);
        textViewRating.setText(""+0);
        seekBarRating=(SeekBar)findViewById(R.id.seekBar1);
        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewRating.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        editTextName=(EditText)findViewById(R.id.editTextName1);
        editTextDescription=(EditText)findViewById(R.id.editTextDescription);
        editTextyear=(EditText)findViewById(R.id.editTextYear1);
        editTextLink=(EditText)findViewById(R.id.editTextLink);


    }

    public void addMovie(View view)
    {
        String name=editTextName.getText().toString();
        String description=editTextDescription.getText().toString();
        String year=editTextyear.getText().toString();
        String link=editTextLink.getText().toString();
        String rating=textViewRating.getText().toString();
        String genre=genreSpinner.getSelectedItem().toString();

        if(name.length()==0 ||description.length()==0 || year.length()==0 ||link.length()==0 ||genre.equals("Select Item"))
        {
            Toast.makeText(getApplicationContext(),"Invalid Inputs,Please Enter Correctly",Toast.LENGTH_LONG).show();
        }
        else {
            Movies movie = new Movies(name, description, genre, Integer.parseInt(rating), Integer.parseInt(year), link);
            Intent intent=new Intent();
            intent.putExtra(MainActivity.MOVIE_OBJECT,movie);
            setResult(RESULT_OK,intent);
            finish();
            Toast.makeText(getApplicationContext(),"Movie Added",Toast.LENGTH_SHORT).show();
        }

    }
}
