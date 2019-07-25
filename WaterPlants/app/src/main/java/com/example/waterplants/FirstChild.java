package com.example.waterplants;


import com.example.waterplants.Fragments.ExtraFragment;
import com.example.waterplants.Fragments.ProfileFragment;
import com.example.waterplants.Fragments.appFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirstChild extends AppCompatActivity  {
    private static Context context;
    private ImageView usr_img;
    private FirebaseAuth auth;
    private StorageReference mStorageRef;
    public String userID;
    private DatabaseReference  mDatabase;
    private TextView username;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_child);

        FirstChild.context=getApplicationContext();
        usr_img=(ImageView)findViewById(R.id.user_image);

        username=(TextView)findViewById(R.id.txtDashboard);
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        FirebaseUser user = auth.getCurrentUser();
        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(userID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fc_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

       // String ur=mDatabase.child(userID).child("img").getKey();
        //show.setText(ur);

        //Uri downloadURI=Uri.parse(ur);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user =dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    usr_img.setImageResource(R.drawable.def_user);
                }else{
                    Glide.with(getContext()).load(user.getImageURL()).into(usr_img);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adauga planta", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(FirstChild.this, AddFlower.class));
            }
        });

        final TabLayout tabLayout=findViewById(R.id.tb_layout);
        final ViewPager viewPager=findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new appFragment(),"Florile mele");
        viewPagerAdapter.addFragment(new ExtraFragment(),"Explorează");
        viewPagerAdapter.addFragment(new ProfileFragment(),"Profil");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        usr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabLayout.Tab tab = tabLayout.getTabAt(2);
                tab.select();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        fab.show();
                        break;

                    case 1:
                        fab.hide();
                        break;

                    case 2:
                        fab.hide();
                        break;
                    default:
                        fab.show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    public static Context getContext(){
        return FirstChild.context;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                return true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){

            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}

