package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText name,password;
    Button logBtn;
    TextView regNow;
    AlertDialog dialog;
    String str_name,str_pass;
    DB_DONAR ForDonar;
    public static String USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ForDonar = new DB_DONAR(this);

        name = findViewById(R.id.username_input);
        password = findViewById(R.id.pass);
        logBtn = findViewById(R.id.loginButton);
        regNow = (TextView) findViewById(R.id.registernow);

        regNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = getLayoutInflater().inflate(R.layout.activity_dialogbox,null);
                Button donor = view.findViewById(R.id.donor_btn);
                Button volunteer = view.findViewById(R.id.volunteer_btn);
                ImageView cancel_btn = view.findViewById(R.id.cancel_btn);


                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                donor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity_Donor.class);
                        startActivity(intent);
                        finish();
                    }
                });

                volunteer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity_Volunteer.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_name = name.getText().toString();
                boolean flag = Validation();

                if(flag){
                    if(!checkFromDonarTable(ForDonar) && !checkFromVolunteerTable(ForDonar)){
                        Toast.makeText(LoginActivity.this,"Kindly please register",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean Validation(){
        str_name = name.getText().toString();
        str_pass = password.getText().toString();

        if(str_name.isEmpty()){
            name.setError("Please fill field");
            name.requestFocus();
            return false;
        }

        if(str_pass.isEmpty()){
            password.setError("Please fill field");
            password.requestFocus();
            return false;
        }
        if(!passwordValidation(str_pass)){
            password.setError("Enter maximum 8 digits");
            password.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkFromVolunteerTable(DB_DONAR ForDonar){
        boolean checkuser = ForDonar.checkUsernameInVolunteer(str_name);

        if(checkuser){
            boolean checkpass = ForDonar.checkPasswordInVolunteer(str_pass);
            if(checkpass){
                Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Donar_NavigationDrawer.class);
                USER_NAME = str_name;
                intent.putExtra("name",str_name);
                intent.putExtra("user_type","Volunteer");
                startActivity(intent);
                finish();
                return true;
            }else {
                Toast.makeText(LoginActivity.this,"Incorrect password!",Toast.LENGTH_LONG).show();
                password.requestFocus();
                return true;
            }
        }
        return false;
    }

    private boolean checkFromDonarTable(DB_DONAR ForDonar){
        boolean checkuser = ForDonar.checkUsernameInDonar(str_name);

        if(checkuser){
            boolean checkpass = ForDonar.checkPasswordInDonar(str_pass);
            if(checkpass){
                Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Donar_NavigationDrawer.class);
                USER_NAME = (str_name);
                intent.putExtra("name",str_name);
                intent.putExtra("user_type","Donar");
                startActivity(intent);
                finish();
                return true;
            }else {
                Toast.makeText(LoginActivity.this,"Incorrect password!",Toast.LENGTH_LONG).show();
                password.requestFocus();
                return true;
            }
        }
        return false;
    }

//    private void checkFromDataBase(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        reference.child(str_name).addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    String db_pass = snapshot.child("password").getValue(String.class);
//                    if(str_pass.equals(db_pass)){
//                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                        intent.putExtra("name",str_name);
//
//
//                        startActivity(intent);
//                        finish();
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(),"password incorrect",Toast.LENGTH_LONG).show();
//
//                    }
//                }else{
//                    Toast.makeText(LoginActivity.this,"Please register!",Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(LoginActivity.this,"Incorrect" ,Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
    private boolean passwordValidation(String str_pass){
        Pattern p = Pattern.compile(".{8}");
        Matcher m = p.matcher(str_pass);
        return m.matches();
    }
}