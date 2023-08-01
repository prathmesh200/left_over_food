package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity_Volunteer extends AppCompatActivity {
    EditText name,password,email,phone,con_pass;
    TextView logNow;
    Button regBtn;
    DB_DONAR ForDonar;

    String str_name,str_pass,str_email,str_con_pass,str_phone;

//    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_volunteer);

        Objects.requireNonNull(getSupportActionBar()).hide();
        ForDonar = new DB_DONAR(this);

        name = findViewById(R.id.username_input);
        password = findViewById(R.id.pass);
        email = findViewById(R.id.iemail);
        regBtn = findViewById(R.id.registernow);
        logNow = findViewById(R.id.loginnow);
        con_pass = findViewById(R.id.compass);
        phone = findViewById(R.id.phone);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please wait....");
//        progressDialog.setCanceledOnTouchOutside(false);


        logNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });
    }

    private void Validation(){
        str_name = name.getText().toString();
        str_email = email.getText().toString();
        str_phone = phone.getText().toString();
        str_pass = password.getText().toString();
        str_con_pass = con_pass.getText().toString();

        if(str_name.isEmpty()){
            name.setError("Please fill field");
            name.requestFocus();

        }
        if(str_phone.isEmpty()){
            phone.setError("Please fill field");
            phone.requestFocus();
            return;
        }
        if(!numberCheck(str_phone)){
            phone.setError("Invalid Mobile no.");
            phone.requestFocus();
            return;
        }
        if(str_email.isEmpty()){
            email.setError("Please fill field");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            email.setError("Please enter valid email");
            email.requestFocus();
            return;
        }
        if(str_pass.isEmpty()){
            password.setError("Please fill field");
            password.requestFocus();
            return;
        }
        else if(!passwordValidation(str_pass)){
            password.setError("Enter maximum 8 digits");
            password.requestFocus();
            return;
        }
        if(str_con_pass.isEmpty()){
            password.setError("Please fill field");
            password.requestFocus();
            return;
        }else if(!passCheck(str_pass,str_con_pass)){
            con_pass.setError("password not matched");
            con_pass.requestFocus();
            return;
        }

        boolean checkuserForVolunteer = ForDonar.checkUsernameInVolunteer(str_name);
        boolean checkuserForDonar = ForDonar.checkUsernameInDonar(str_name);
        boolean checkEmailForUserInDonar = ForDonar.checkIfEmailExistsInDonar(str_email);
        boolean checkEmailForUserInVolunteer = ForDonar.checkIfEmailExistsInVolunteer(str_email);

        if(!checkuserForVolunteer && !checkuserForDonar){
            if(checkEmailForUserInDonar || checkEmailForUserInVolunteer){
                Toast.makeText(getApplicationContext(),"Email is already registered!",Toast.LENGTH_LONG).show();
                email.requestFocus();
            }else {
                boolean insert = ForDonar.addUsersInVolunteer(str_name, str_pass, str_email, str_phone);
                if (insert) {
                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed!", Toast.LENGTH_LONG).show();
                }
            }
        }else{
//            Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_LONG).show();
            name.setError("username already exist");
            name.requestFocus();
        }
    }

    //    private void checkifUserNameExists(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        reference.child(str_name).addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    if(snapshot.exists()){
//                        String db_user = snapshot.child("name").getValue(String.class);
//                        if(str_name.equals(db_user)){
//                            name.setError("UserName already exists");
//                            name.requestFocus();
//                            return;
//                        }
//                    }
//                }
//                createAccount();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(RegisterActivity_Donor.this,"Incorrect" ,Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//    private boolean passCheck(String str_pass,String str_con_pass){
//        if(str_pass.equals(str_con_pass)){
//            return true;
//        }
//        return false;
//    }
//    private void createAccount(){
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();
//
//        sendDataToDataBase();
//    }
//
//    private void sendDataToDataBase(){
//        String regTime = " " +System.currentTimeMillis();
//        HashMap<String,Object> data = new HashMap<>();
//
//        data.put("name",str_name);
//        data.put("email",str_email);
//        data.put("password",str_pass);
//        data.put("phone",str_phone);
//
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        reference.child(str_name).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                name.requestFocus();
//                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//
//
//            }
//        });
//    }
    private boolean passCheck(String str_pass,String str_con_pass){
        if(str_pass.equals(str_con_pass)){
            return true;
        }
        return false;
    }
    private boolean passwordValidation(String str_pass){
        Pattern p = Pattern.compile(".{8}");
        Matcher m = p.matcher(str_pass);
        return m.matches();
    }

    private boolean numberCheck(String str_phone){
        Pattern p =Pattern.compile("[0-9]{10}");
        Matcher m = p.matcher(str_phone);
        return m.matches();
    }

}