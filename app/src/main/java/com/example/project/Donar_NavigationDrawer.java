package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class Donar_NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toogle;
    private NavigationView navigationView;
    DB_DONAR db_donar;
    String str_type,str_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_navigation_drawer);

        db_donar = new DB_DONAR(this);

        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);

        floatingActionButton = findViewById(R.id.add);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        Intent intent = getIntent();
        str_name = intent.getStringExtra("name");
        str_type = intent.getStringExtra("user_type");

        Toast.makeText(Donar_NavigationDrawer.this,str_type, Toast.LENGTH_LONG).show();
        if(Objects.equals(str_type, "Donar")){
            if(savedInstanceState == null) {
                replaceFragment(new DonarHomeFramgent());
            }
        }else{
            if(savedInstanceState == null) {
                replaceFragment(new VolunteerHomeFragment());
            }
        }

        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.business);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

        toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_type.equals("Donar")) {
                    showBottomDialog_Donar();
                }else{
                    showBottomDialog_Volunteer();
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                floatingActionButton.setVisibility(View.VISIBLE);
                switch(item.getItemId()){
                    case R.id.nav_home:
                        if(Objects.equals(str_type, "Donar")){
                            replaceFragment(new DonarHomeFramgent());
                        }else{
                            replaceFragment(new VolunteerHomeFragment());
                        }
                        break;
                    case R.id.nav_account:
                        replaceFragment(new AccountFragment());
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.help:
                        Uri uri = Uri.parse("https://help.olioex.com/");
                        startActivity(new Intent(Intent.ACTION_VIEW,uri));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        floatingActionButton.setVisibility(View.VISIBLE);
        switch(item.getItemId()){
            case R.id.nav_home:
                if(Objects.equals(str_type, "Donar")){
                    replaceFragment(new DonarHomeFramgent());
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new DonarHomeFramgent()).commit();
                }else{
                    replaceFragment(new VolunteerHomeFragment());
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new VolunteerHomeFragment()).commit();
                }
                break;
            case R.id.nav_setting:
                replaceFragment(new SettingsFragment());
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new SettingsFragment()).commit();
                break;
            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Download this app";
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,"Share using"));
                break;
            case R.id.nav_account:
                replaceFragment(new AccountFragment());
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new AccountFragment()).commit();
                floatingActionButton.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_Users_near_me:
                replaceFragment(new UsersNearMeFragment());
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new UsersNearMeFragment()).commit();
                break;
            case R.id.nav_about:
                replaceFragment(new AboutUsFragment());
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new AboutUsFragment()).commit();
                break;
            case R.id.nav_listing:
                replaceFragment(new MyListingsFragment());
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new MyListingsFragment()).commit();
                break;
            case R.id.help:
                Uri uri = Uri.parse("https://help.olioex.com/");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
                break;
            case R.id.nav_logout:
                finish();
                startActivity(new Intent(Donar_NavigationDrawer.this,LoginActivity.class));
                Toast.makeText(Donar_NavigationDrawer.this,"Logged Out!", Toast.LENGTH_LONG).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setCheckable(true);
        return true;
    }

    private void showBottomDialog_Volunteer(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout_volunteer);

        LinearLayout takeAway_layout = dialog.findViewById(R.id.takeAway_layout);
        LinearLayout enjoy_feast_food_layout = dialog.findViewById(R.id.enjoy_feast_food_layout);
        LinearLayout help_layout_volunteer = dialog.findViewById(R.id.help_layout_volunteer);

        takeAway_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),TakeAwayFood.class);
                intent.putExtra("name",str_name);
                intent.putExtra("user_type",str_type);
                startActivity(intent);
            }
        });

        enjoy_feast_food_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),FeastGiving.class);
                startActivity(intent);
            }
        });

        help_layout_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://help.olioex.com/article/100-what-am-i-not-allowed-to-share-on-olio");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showBottomDialog_Donar(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout_donar);

        LinearLayout giveAway_layout = dialog.findViewById(R.id.giveAway_layout);
        LinearLayout feast_food_layout = dialog.findViewById(R.id.feast_food_layout);
        LinearLayout help_layout = dialog.findViewById(R.id.help_layout);


        giveAway_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),GiveAwayFood.class);
                intent.putExtra("name",str_name);
                intent.putExtra("user_type",str_type);
                startActivity(intent);
            }
        });

        feast_food_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),FeastGiving.class);
                startActivity(intent);
            }
        });

        help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://help.olioex.com/article/100-what-am-i-not-allowed-to-share-on-olio");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}