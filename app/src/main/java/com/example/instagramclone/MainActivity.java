package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView navigBottom;
    final FragmentManager fragmentTransaction = getSupportFragmentManager();
    final Fragment doPost = new DoPost();
    final Fragment compose = new Compose();
    final Fragment profile = new Profile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigBottom = findViewById(R.id.bottom_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // click on the bottom navigation and check the item selected
        navigBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = new Fragment();
                if (item.getItemId()==R.id.Home){frag = doPost; }
                if(item.getItemId()== R.id.Composetouch) {frag = compose;}
                if(item.getItemId()==R.id.account){frag = profile;}
                fragmentTransaction.beginTransaction().replace(R.id.Placeholder, frag).commit();
                return true;
            }
        });
        navigBottom.setSelectedItemId(R.id.Home);
    }
}