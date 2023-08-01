package com.example.project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountFragment extends Fragment {
    DB_DONAR db_donar;
    EditText email,firstname,lastname,state,city,pincode;
    Button saveAccount;
    boolean checkIfDataExists;
    String str_email,str_firstname,str_lastname,str_state,str_city,str_pincode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_donar = new DB_DONAR(requireActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        email = (EditText) view.findViewById(R.id.email_address);
        saveAccount = (Button) view.findViewById(R.id.saveAccount);
        firstname = (EditText) view.findViewById(R.id.first_name);
        lastname = (EditText) view.findViewById(R.id.last_name);
        state = (EditText) view.findViewById(R.id.currentState);
        city = (EditText) view.findViewById(R.id.city_name);
        pincode = (EditText) view.findViewById(R.id.pincode_num);


        Intent intent = requireActivity().getIntent();
        String str_name = intent.getStringExtra("name");
        String type = intent.getStringExtra("user_type");


        String emailFromDB = db_donar.getEmailFromDonar(str_name,type);

        if(email.getText().toString().isEmpty() || !email.getText().toString().equals(emailFromDB)){
            email.setText(emailFromDB);
        }

        if(type.equals("Donar")) {
            checkIfDataExists = db_donar.checkDataAlreadyExistsInDonarAccount(emailFromDB);
        }else{
            checkIfDataExists = db_donar.checkDataAlreadyExistsInVolunteerAccount(emailFromDB);
        }
        Toast.makeText(getActivity(),"huiiiiiiiiiiiiiiiiiiiiiiiiii", Toast.LENGTH_LONG).show();
        if(checkIfDataExists){
            setTheDetails(db_donar,email.getText().toString(),type);
        }

        saveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkINFO()){
                    if(checkIfDataExists){
                        if (!str_email.equals(emailFromDB)) {
                            if (type.equals("Donar")) {
                                db_donar.updateTheEmailInDonar(str_email, str_name);
                                Toast.makeText(getActivity(),"Success! All updated", Toast.LENGTH_LONG).show();
                                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout,new DonarHomeFramgent()).commit();
                            }else{
                                db_donar.updateTheEmailInVolunteer(str_email,str_name);
                                Toast.makeText(getActivity(),"Success! All updated", Toast.LENGTH_LONG).show();
                                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout,new VolunteerHomeFragment()).commit();
                            }
                        }
                    }else {
                        addUsersInAccount(db_donar, str_name,type);
                        Toast.makeText(getActivity(), "Success! All updated", Toast.LENGTH_LONG).show();
                        if(type.equals("Donar")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new DonarHomeFramgent()).commit();
                        }else{
                            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new VolunteerHomeFragment()).commit();
                        }
                    }
                }

            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Account");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public boolean setTheDetails(DB_DONAR db_donar,String currentEmail,String type){
        List<String> l= db_donar.getallUsersFromAccount(currentEmail,type);
        firstname.setText(l.get(0));
        lastname.setText(l.get(1));
        state.setText(l.get(2));
        city.setText(l.get(3));
        pincode.setText(l.get(4));
        return true;
    }

    public boolean addUsersInAccount(DB_DONAR db_donar,String username,String type){
        str_email = email.getText().toString();
        str_firstname = firstname.getText().toString();
        str_lastname = lastname.getText().toString();
        str_state = state.getText().toString();
        str_city = city.getText().toString();
        str_pincode = pincode.getText().toString();

        if(type.equals("Donar")) {
            db_donar.addUsersInDonarAccount(username, str_firstname, str_lastname, str_email, str_state, str_city, str_pincode);
        }else{
            db_donar.addUsersInVolunteerAccount(username, str_firstname, str_lastname, str_email, str_state, str_city, str_pincode);
        }
        return true;
    }
    public boolean checkINFO(){
        str_email = email.getText().toString();
        str_firstname = firstname.getText().toString();
        str_lastname = lastname.getText().toString();
        str_state = state.getText().toString();
        str_city = city.getText().toString();
        str_pincode = pincode.getText().toString();

        if(str_firstname.isEmpty()){
            firstname.setError("Please fill field");
            firstname.requestFocus();
            return false;
        }

        if(str_lastname.isEmpty()){
            lastname.setError("Please fill field");
            lastname.requestFocus();
            return false;
        }

        checkEmailPattern(str_email);

        if(str_state.isEmpty()){
            state.setError("Please fill field");
            state.requestFocus();
            return false;
        }

        if(str_city.isEmpty()){
            city.setError("Please fill field");
            city.requestFocus();
            return false;
        }

        if(str_pincode.isEmpty()){
            pincode.setError("Please fill field");
            pincode.requestFocus();
            return false;
        }
        if(!checkPincode(str_pincode)){
            pincode.setError("Pincode must be of 6 digits");
            pincode.requestFocus();
            return false;
        }
        checkPincodepattern(str_pincode);
        return true;
    }
    public boolean checkPincodepattern(String str_pincode){
        Pattern pattern = Pattern.compile("^[1-9]{1}[0-9]{2}\\\\s{0,1}[0-9]{3}$");
        Matcher matcher = pattern.matcher(str_pincode);

        if(!matcher.matches()){
            pincode.setError("pincode contains only digits");
            pincode.requestFocus();
            return false;
        }
        return true;
    }

    public boolean checkPincode(String pincode){
        Pattern p = Pattern.compile(".{6}");
        Matcher m = p.matcher(pincode);
        return m.matches();
    }

    public boolean checkEmailPattern(String str_email){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(str_email);

        if(str_email.isEmpty()){
            email.setError("Please fill field");
            email.requestFocus();
            return false;
        }
        if(!matcher.matches()){
            email.setError("Please enter valid email");
            email.requestFocus();
            return false;
        }
        return true;
    }

}