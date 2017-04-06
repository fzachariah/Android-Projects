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

public class EditActivity extends AppCompatActivity {

    Spinner genreSpinner;
    SeekBar seekBarRating;
    EditText editTextName;
    EditText editTextDescription;
    EditText editTextYear;
    EditText editTextLink;
    TextView textViewRating;
    SeekBar seekBar;
    ArrayAdapter<CharSequence> staticAdapter;
    int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("Edit Movie");

        Movies movie=(Movies)getIntent().getExtras().getSerializable(MainActivity.MOVIE_OBJECT);
        selectedId=getIntent().getExtras().getInt(MainActivity.MOVIE_ORDER);

        editTextName =(EditText)findViewById(R.id.editTextName1);
        editTextName.setText(movie.getName());

        editTextDescription =(EditText)findViewById(R.id.editTextDescription1);
        editTextDescription.setText(movie.getDescription());

        editTextLink=(EditText)findViewById(R.id.editTextLink1);
        editTextLink.setText(movie.getLink());

        editTextYear=(EditText)findViewById(R.id.editTextYear1);
        editTextYear.setText(""+movie.getYear());

        genreSpinner=(Spinner)findViewById(R.id.spinnerGenre2);
        staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.genre_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(staticAdapter);
        if (!movie.getGenre().equals(null)) {
            int spinnerPosition = staticAdapter.getPosition(movie.getGenre());
            genreSpinner.setSelection(spinnerPosition);
        }
        seekBar=(SeekBar)findViewById(R.id.seekBar1);
        seekBar.setProgress(movie.getRating());
        textViewRating=(TextView)findViewById(R.id.textViewDisplay1);
        textViewRating.setText(""+movie.getRating());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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


    }
    public void saveChanges(View view)
    {
        String name=editTextName.getText().toString();
        String description=editTextDescription.getText().toString();
        String year=editTextYear.getText().toString();
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
            intent.putExtra(MainActivity.MOVIE_ORDER,selectedId);
            setResult(RESULT_OK,intent);
            finish();
            Toast.makeText(getApplicationContext(),"Movie Saved Successfully",Toast.LENGTH_SHORT).show();
        }
    }
}
