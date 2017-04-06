package com.example.febin.midterm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<Product> cartArrayList=new ArrayList<>();

    ListView listView;
    TextView textViewSum;

    CartAdapter cartAdapter;
    float sum;

    Button buttonCancel;
    Button buttonCheckout;

    DatabaseDataManager databaseDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        listView=(ListView)findViewById(R.id.listViewCart);

        databaseDataManager=new DatabaseDataManager(this);

        cartArrayList= (ArrayList<Product>) getIntent().getSerializableExtra("DATA_PASSED");
        Log.d("TEst here",""+cartArrayList.size());

        sum=0.0f;
        for(int i=0;i<cartArrayList.size();i++)
        {
            sum=sum+Float.parseFloat(cartArrayList.get(i).getPrice());

        }
        textViewSum=(TextView)findViewById(R.id.textViewTotal);
        textViewSum.setText("$"+(sum));

        buttonCancel=(Button)findViewById(R.id.buttonCancel);
        buttonCheckout=(Button)findViewById(R.id.buttonCheckOut);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putExtra("Data",cartArrayList);
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart=new Cart();
                cart.setCartList(cartArrayList);
                databaseDataManager.saveNote(cart);
                cartArrayList.clear();
                cartAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Ordered Successfully",Toast.LENGTH_SHORT).show();
                textViewSum.setText("");

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                cartArrayList.remove(position);
                sum=0.0f;
                for(int i=0;i<cartArrayList.size();i++)
                {
                    sum=sum+Float.parseFloat(cartArrayList.get(i).getPrice());

                }
                textViewSum=(TextView)findViewById(R.id.textViewTotal);
                textViewSum.setText("$"+(sum));
                cartAdapter.notifyDataSetChanged();
                return false;
            }
        });

         cartAdapter=new CartAdapter(this,R.layout.list_row,cartArrayList);
        listView.setAdapter(cartAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                return true;
            case R.id.history:
                setTitle("History");

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
