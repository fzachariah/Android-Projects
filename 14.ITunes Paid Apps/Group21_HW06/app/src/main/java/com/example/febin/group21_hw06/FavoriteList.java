package com.example.febin.group21_hw06;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    ListView listView;
    SharedPreferences favorites;
    final String DATA="Data";
    ArrayList<AppInfo> listFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        setTitle("Favorite List");
        listFinal=(ArrayList<AppInfo>)getIntent().getSerializableExtra(DATA);
        listView=(ListView)findViewById(R.id.listViewFavorites);
        favorites = getSharedPreferences("myFavorites",
                Context.MODE_PRIVATE);
        FavoriteAdapter favoriteAdapter=new FavoriteAdapter(this,R.layout.row_linearlayout,listFinal);
        listView.setAdapter(favoriteAdapter);

    }

}
