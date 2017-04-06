package com.example.febin.group21_inclass08;

import android.app.Activity;
import android.app.Fragment;

import android.os.Bundle;
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


public class EditFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    final String DATA="data";
    final static  String MOVIE_ORDER="Order";

    Spinner genreSpinner;
    SeekBar seekBarRating;
    EditText editTextName;
    EditText editTextDescription;
    EditText editTextYear;
    EditText editTextLink;
    TextView textViewRating;
    SeekBar seekBar;
    ArrayAdapter<CharSequence> staticAdapter;
    Button buttonSave;

    Movies movie;
    int order;

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        movie = (Movies) getArguments().getSerializable(DATA);
        order=(Integer)getArguments().getInt(MOVIE_ORDER);
        return inflater.inflate(R.layout.fragment_edit, container, false);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editTextName =(EditText)getActivity().findViewById(R.id.editTextName1);
        editTextName.setText(movie.getName());

        editTextDescription =(EditText)getActivity().findViewById(R.id.editTextDescription1);
        editTextDescription.setText(movie.getDescription());

        editTextLink=(EditText)getActivity().findViewById(R.id.editTextLink1);
        editTextLink.setText(movie.getLink());

        editTextYear=(EditText)getActivity().findViewById(R.id.editTextYear1);
        editTextYear.setText(""+movie.getYear());

        genreSpinner=(Spinner)getActivity().findViewById(R.id.spinnerGenre2);
        staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.genre_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(staticAdapter);
        if (!movie.getGenre().equals(null)) {
            int spinnerPosition = staticAdapter.getPosition(movie.getGenre());
            genreSpinner.setSelection(spinnerPosition);
        }
        seekBar=(SeekBar)getActivity().findViewById(R.id.seekBar1);
        seekBar.setProgress(movie.getRating());
        textViewRating=(TextView)getActivity().findViewById(R.id.textViewDisplay1);
        textViewRating.setText(""+movie.getRating());
        buttonSave=(Button)getActivity().findViewById(R.id.buttonSave);
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
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovie();
            }
        });


    }

    public void saveMovie()
    {
        String name=editTextName.getText().toString();
        String description=editTextDescription.getText().toString();
        String year=editTextYear.getText().toString();
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

            Toast.makeText(getActivity(),"Movie Saved Successfully",Toast.LENGTH_SHORT).show();
            mListener.movieEdited(order,movie);
        }

    }

    public interface OnFragmentInteractionListener {
        public void movieEdited(int which,Movies movies);
    }
}
