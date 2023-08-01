package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class GiveAwayFood extends AppCompatActivity {
    String str_name = "null",str_type= "null",emailFromDB = "null";
    DB_DONAR db_donar;
    ImageView cover;
    ImageView imageview;
    ImageView backpress;
    TextView textView;
    EditText foodTitle,foodDescription,feedCount;
    Button submitBtn;
    ImageView getImage;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_away_food);
        db_donar = new DB_DONAR(this);

        Toolbar toolbar = findViewById(R.id.toolbar_backpressed);
        setSupportActionBar(toolbar);

        textView = findViewById(R.id.toolbar_title);
        cover = findViewById(R.id.setImage);
        imageview = findViewById(R.id.imageBtn);
        backpress = findViewById(R.id.backPress);
        foodTitle = findViewById(R.id.giveAway_food_title);
        foodDescription = findViewById(R.id.description);
        feedCount = findViewById(R.id.giveAway_feed_count);
        submitBtn = findViewById(R.id.submitBtn);

        textView.setText("Give Food");

        Intent intent = getIntent();
        str_name = intent.getStringExtra("name");
        str_type = intent.getStringExtra("user_type");
        emailFromDB = db_donar.getEmailFromDonar(str_name,str_type);

        boolean checkIfDataExists = db_donar.checkDataAlreadyExistsInDonarAccount(emailFromDB);
        submitBtn.setEnabled(true);
        if(!checkIfDataExists){
            Toast.makeText(GiveAwayFood.this,"Please fill your profile first", Toast.LENGTH_LONG).show();
            submitBtn.setEnabled(false);
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setAddedItemsIntoDB()){
                    Intent intent1 = new Intent();

                    Toast.makeText(GiveAwayFood.this,"Successfully Added!", Toast.LENGTH_LONG).show();
//                    replaceFragment(new DonarHomeFramgent());
                    onBackPressed();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new DonarHomeFramgent()).commit();
                }else{
                    Toast.makeText(GiveAwayFood.this,"can't add", Toast.LENGTH_LONG).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new DonarHomeFramgent()).commit();
                }
            }
        });

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(GiveAwayFood.this)
                        .crop(22f,20f)
                        .start(10);
            }
        });

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean setAddedItemsIntoDB(){
        String restaurant_name, food_title, food_description,feed_count,email,state,pincode,city;

        restaurant_name = db_donar.getRestaurantNameFromDonar(str_name);
        email = db_donar.getEmailFromDonar(str_name,str_type);
        state = db_donar.getStateFromDB(str_name);
        pincode = db_donar.getPincodeFromAccount(str_name,str_type);
        city = db_donar.getCityFromDB(str_name);
        food_title = foodTitle.getText().toString();
        food_description = foodDescription.getText().toString();
        feed_count = feedCount.getText().toString();
        byte [] img = compressImage(ConverionofImage());

        Toast.makeText(this, String.valueOf(img.length), Toast.LENGTH_LONG).show();
        if(img == null){
            Toast.makeText(this,"nullllllllllllllllllllllllllllllllGiveAWAY", Toast.LENGTH_LONG).show();
        }

        boolean added = db_donar.addDOonarFoodInFoodTable(img,restaurant_name,food_title,food_description,feed_count,email,state,pincode,city);
        return added;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        cover.setImageURI(uri);
        if(uri != null){
            setImageUri(uri);
        }

        imageview.setVisibility(View.INVISIBLE);
    }

    private byte[] compressImage(byte[] bytes){
        byte[] bytes1 = bytes;
        Toast.makeText(this, String.valueOf(bytes.length), Toast.LENGTH_LONG).show();
        while (bytes1.length > 400000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes1,0,bytes1.length);
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, 512,nh,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resize.compress(Bitmap.CompressFormat.PNG,100,stream);
            bytes1 = stream.toByteArray();
        }
        return bytes1;
    }

    private byte[] ConverionofImage(){
        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            if(getUri() != null) {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), getUri());
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();
                return byteArray;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void setImageUri(Uri uri){
        this.uri = uri;
    }

    private Uri getUri(){
        return uri;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        name = editText.getText().toString();
//        title = textView.getText().toString();
//        savedInstanceState.putString(KEY_NAME,name);
//        savedInstanceState.putString(KEY_TITLE,title);
//        super.onSaveInstanceState(savedInstanceState);
//        // etc.
//    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//
////        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
////        name = preferences.getString(KEY_NAME,"");
////        title = preferences.getString(KEY_TITLE,"");
//        name = savedInstanceState.getString(KEY_NAME);
//        title = savedInstanceState.getString(KEY_TITLE);
//        editText.setText(name);
//        textView.setText(title);
//
//    }

//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//
//        // Store values between instances here
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI
//
//        name = editText.getText().toString();
//        title = textView.getText().toString();
//
//        editor.putString(KEY_NAME, name); // value to store
//        editor.putString(KEY_TITLE, title); // value to store
//        editor.apply();
//    }

}