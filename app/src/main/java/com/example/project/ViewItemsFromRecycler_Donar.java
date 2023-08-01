package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewItemsFromRecycler_Donar extends AppCompatActivity {

    ImageView setImage;
    ImageView backpress;
    EditText restaurantName;
    EditText phone;
    EditText feedcount;
    Button saveAccount;

    String str_name,str_feed_count;
    int id;

    DB_DONAR db_donar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items_from_recycler_donar);

        db_donar = new DB_DONAR(this);

        Toolbar toolbar = findViewById(R.id.viewBackpress);
        setSupportActionBar(toolbar);

        backpress = findViewById(R.id.backPress);
        setImage = findViewById(R.id.backgroundImage);
        restaurantName = findViewById(R.id.setRestaurant);
        phone = findViewById(R.id.phoneNum);
        feedcount = findViewById(R.id.feedCount);
        saveAccount = findViewById(R.id.saveAccount);

        Intent intent = getIntent();
        str_name = intent.getStringExtra("name");
        str_feed_count = intent.getStringExtra("id");
        id = Integer.parseInt(str_feed_count);

        restaurantName.setText(db_donar.getRestaurantNameFromDonar(str_name));
        phone.setText(db_donar.getPhoneNumber(str_name));
        feedcount.setText(String.valueOf(db_donar.getFeedCount(id)));

        Bitmap bitmap = db_donar.getImageFromDB(id);
        setImage.setImageBitmap(bitmap);

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewItemsFromRecycler_Donar.this,"Saved Successfully", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}