package com.example.febin.midterm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchJSONAsync.IData {

    RecyclerView recyclerView;
    private GridAdapter mAdapter;
    AlertDialog alertDialog;
    //DatabaseDataManager databaseDataManager;
    RecyclerView.LayoutManager mLayoutManager ;

    ProgressDialog progressDialog;

    ArrayList<Product> productsArrayList=new ArrayList<>();
    ArrayList<Product> cartArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Shopping App");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        new FetchJSONAsync(MainActivity.this).execute("http://52.90.79.130:8080/MidTerm/get/products");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public void setUpData(ArrayList<Product> questions) {

        Log.d("Test Here", ""+questions.get(0).toString());
        productsArrayList=questions;
        progressDialog.dismiss();

        mAdapter = new GridAdapter(productsArrayList, this, new GridAdapter.GridAdapterListener() {
            @Override
            public void ClickButton(View v, int position) {
                Log.d(" Testing ", productsArrayList.get(position).toString());
                cartArrayList.add(productsArrayList.get(position));



            }

            @Override
            public void recyclerViewGridClicked(View v, int position) {

                Log.d("Testing position",productsArrayList.get(position).toString());
                //mListener.ClickFromUnBlocked(arrayList.get(position));


            }
        });

        mLayoutManager = new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100)
        {
            if(resultCode==RESULT_OK)
            {

                ArrayList<Product> data_back=(ArrayList<Product>)data.getExtras().getSerializable("data_back");
                cartArrayList=data_back;


            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                Intent intent =new Intent(MainActivity.this,CartActivity.class);
                intent.putExtra("DATA_PASSED",cartArrayList);
                startActivityForResult(intent,100);
                return true;
            case R.id.history:

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
