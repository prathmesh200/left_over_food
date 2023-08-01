package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;

    private List<HandelRecyclerView> userList;
    public Adapter(List<HandelRecyclerView>userList,RecyclerViewInterface recyclerViewInterface){
        this.userList = userList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_view,parent,false);
        return new ViewHolder(view,recyclerViewInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        DB_DONAR db_donarr = userList.get(position).getDb_donar();
        String type = userList.get(position).getType();
        int pos = userList.get(position).getI();
        String res_name = userList.get(position).getTextView1();
        String vol_name = userList.get(position).getName();
        String user_name = userList.get(position).getTextView2();
        String feed_count = userList.get(position).getTextView3();
        List<Bitmap> bmp;

        if(type.equals("Donar")) {
            bmp = db_donarr.getImageFromDB(res_name);
        }
        else{
            String pin = db_donarr.getPincodeFromAccount(vol_name,type);
            bmp = db_donarr.getImageForVolunteer(pin);
        }
        int delet = userList.get(position).getImageView2();

        holder.setData(pos,bmp,res_name,delet,user_name,feed_count);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageView imageView2;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;

        public ViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageVieww);
            imageView2 = itemView.findViewById(R.id.deleteItem);
            textView1 = itemView.findViewById(R.id.restaurantName);
            textView2 = itemView.findViewById(R.id.user_name);
            textView3 = itemView.findViewById(R.id.feed_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.setItemOnClick(pos);
                        }
                    }
                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onClickDeleteItem(pos);
                        }
                    }
                }
            });
        }

        public void setData(int i,List<Bitmap> bmp,String res_name,int deleteItem, String user_name, String feed_count) {
            imageView.setImageBitmap(bmp.get(i));
            imageView2.setImageResource(deleteItem);
            textView1.setText(res_name);
            textView2.setText(user_name);
            textView3.setText(feed_count);
        }
    }
}
