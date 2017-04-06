package com.example.febin.group21_inclass08;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    Spinner genreSpinner;
    SeekBar seekBarRating;
    EditText editTextName;
    EditText editTextDescription;
    EditText editTextyear;
    EditText editTextLink;
    TextView textViewRating;
    Button buttonAdd;


    ArrayAdapter<CharSequence> staticAdapter;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        genreSpinner=(Spinner)getActivity().findViewById(R.id.spinnerGenre2);
        staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.genre_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(staticAdapter);
        textViewRating=(TextView)getActivity(). findViewById(R.id.textViewDisplay);
        textViewRating.setText(""+0);
        seekBarRating=(SeekBar)getActivity().findViewById(R.id.seekBar1);
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

        editTextName=(EditText)getActivity().findViewById(R.id.editTextName1);
        editTextDescription=(EditText)getActivity().findViewById(R.id.editTextDescription);
        editTextyear=(EditText)getActivity().findViewById(R.id.editTextYear1);
        editTextLink=(EditText)getActivity().findViewById(R.id.editTextLink);
        buttonAdd=(Button)getActivity().findViewById(R.id.buttonAddFrag);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovie();
            }
        });

    }

    public void addMovie()
    {
        String name=editTextName.getText().toString();
        String description=editTextDescription.getText().toString();
        String year=editTextyear.getText().toString();
        String link=editTextLink.getText().toString();
        String rating=textViewRating.getText().toString();
        String genre=genreSpinner.getSelectedItem().toString();

        if(name.length()==0 ||description.length()==0 || year.length()==0 ||link.length()==0 ||genre.equals("Select Item"))
        {
            Toast.makeText(getActivity(),"Invalid Inputs,Please Enter Correctly",Toast.LENGTH_LONG).show();
        }
        else if(Integer.parseInt(year)< 1800 || Integer.parseInt(year)>2019){

            Toast.makeText(getActivity(),"Please Enter a Valid Movie Year",Toast.LENGTH_LONG).show();
        }
        else {
            Movies movie = new Movies(name, description, genre, Integer.parseInt(rating), Integer.parseInt(year), link);
            Log.d("Printing :",movie.toString());
            Toast.makeText(getActivity(),"Movie Added",Toast.LENGTH_SHORT).show();
            mListener.movieAdded(movie);
        }

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void movieAdded(Movies movies);

    }
}
