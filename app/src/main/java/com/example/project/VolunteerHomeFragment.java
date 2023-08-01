package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class VolunteerHomeFragment extends Fragment implements RecyclerViewInterface {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<HandelRecyclerView> userlist;
    Adapter adapter;
    DB_DONAR db_donar;
    String restaurant_name,emailFromDB;
    String vol_pincode;
    String donar_username;
    String str_name ,str_type;
    int id;
    private int pos = 0;
    boolean checkIfDataExists;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_donar = new DB_DONAR(requireActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volunteer_home, container, false);
        View view1 = inflater.inflate(R.layout.blank_volunteer, container, false);
        View view2 = inflater.inflate(R.layout.blank2_volunteer, container, false);
        View view3 = inflater.inflate(R.layout.custom_recycler_view, container, false);

        Intent intent = requireActivity().getIntent();
        str_name = intent.getStringExtra("name");
        str_type = intent.getStringExtra("user_type");
        imageView = view3.findViewById(R.id.imageVieww);

        emailFromDB = db_donar.getEmailFromDonar(str_name,str_type);

        if(str_type.equals("Donar")) {
            checkIfDataExists = db_donar.checkDataAlreadyExistsInDonarAccount(emailFromDB);
        }else{
            checkIfDataExists = db_donar.checkDataAlreadyExistsInVolunteerAccount(emailFromDB);
        }

        if(!checkIfDataExists){
            return view1;
        }

        vol_pincode = db_donar.getPincodeFromAccount(str_name,str_type);
        boolean checkIffoodtableisEmpty = db_donar.chechIfFoodTableisEmpty();

        if(!checkIffoodtableisEmpty) {
            boolean checkIfPincodeMatches = db_donar.checkIfPincodematches(vol_pincode);

            if (checkIfPincodeMatches) {
                List<List<String>> l = db_donar.getUsersFromFoodTable(vol_pincode);
                if (l.isEmpty()) {
                    Toast.makeText(getActivity(), "empty", Toast.LENGTH_LONG).show();
                } else {
                    userlist = new ArrayList<>();

                    Toast.makeText(getActivity(), String.valueOf(l.size()), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < l.size(); i++) {
                        donar_username = db_donar.getUsernameFromDonar(l.get(i).get(4));
                        userlist.add(new HandelRecyclerView(str_name,str_type,i, db_donar, R.drawable.profile_image,
                                R.drawable.delete, l.get(i).get(0), donar_username, l.get(i).get(3)));
                    }

                }
                intitRecyclerview(view);
            } else {
                Toast.makeText(getActivity(), "nooooooooooooooooooo", Toast.LENGTH_LONG).show();
            }
        }else{
            return view2;
        }
        return view;
    }


    public void intitRecyclerview(View view){
        recyclerView = view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(userlist,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Volunteer");
    }

    public void setPosition(int pos){
        this.pos = pos;
    }
    public int getPosition() {
        return pos;
    }

    @Override
    public void setItemOnClick(int position) {
        Intent intent = new Intent(requireActivity(), ViewItemsFromRecycler_Volunteer.class);
        intent.putExtra("name", str_name);
        setPosition((position+1));
        id = db_donar.getFoodId(getPosition(),restaurant_name);

        intent.putExtra("id",String.valueOf(id));
        startActivity(intent);
    }

    @Override
    public void onClickDeleteItem(int position) {
        userlist.remove(position);
        adapter.notifyItemRemoved(position);
    }
}