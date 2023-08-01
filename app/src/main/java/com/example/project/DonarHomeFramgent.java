package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class DonarHomeFramgent extends Fragment implements RecyclerViewInterface{

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<HandelRecyclerView> userlist;
    Adapter adapter;
    DB_DONAR db_donar;
    String restaurant_name;
    String str_name ,type;
    int id;
    private int pos = 0;

    DonarHomeFramgent(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_donar = new DB_DONAR(requireActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donar_home, container, false);

        Intent intent = requireActivity().getIntent();
        str_name = intent.getStringExtra("name");
        type = intent.getStringExtra("user_type");

        restaurant_name = db_donar.getRestaurantNameFromDonar(str_name);

        boolean checkRestaurantExistsInAddedlist = db_donar.checkRestaurantexistsinFoodTable(restaurant_name);

        if(checkRestaurantExistsInAddedlist) {
            List<List<String>> l = db_donar.makeItemsVisibleByDonar(restaurant_name);

            if(l.isEmpty()){
                Toast.makeText(getActivity(),"No food added", Toast.LENGTH_LONG).show();
            }else{
                userlist = new ArrayList<>();

                for (int i = 0; i < l.size(); i++){
                    userlist.add(new HandelRecyclerView(str_name,type,i,db_donar,R.drawable.profile_image,
                            R.drawable.delete,l.get(i).get(0),str_name,l.get(i).get(3)));
                }
            }
            intitRecyclerview(view);
        }else{
            Toast.makeText(getActivity(),"No Food added yet", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    public void setPosition(int pos){
        this.pos = pos;
    }
    public int getPosition() {
        return pos;
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
        getActivity().setTitle("Donar");
    }

    @Override
    public void setItemOnClick(int position) {
        Intent intent = new Intent(requireActivity(), ViewItemsFromRecycler_Donar.class);
        intent.putExtra("name", str_name);
        setPosition((position+1));
        id = db_donar.getFoodId(getPosition(),restaurant_name);

        intent.putExtra("id",String.valueOf(id));
        startActivity(intent);
    }

    @Override
    public void onClickDeleteItem(int position) {
        int index = position+1;
        setPosition(index);
        id = db_donar.getFoodId(getPosition(),restaurant_name);
        db_donar.deleteItemsFromRecyclerView(id);
        Toast.makeText(getActivity(),String.valueOf(id), Toast.LENGTH_LONG).show();
        userlist.remove(position);
        adapter.notifyItemRemoved(position);
    }
}