package com.otnam.kasirqiu;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.material.navigation.NavigationView;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ImageView topDrawerIcon;
    NavigationView navigationView;
    FrameLayout hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportActionBar().hide();

        drawerLayout = findViewById(R.id.drawerLayoutID);
        navigationView = findViewById(R.id.navigationViewID);
        topDrawerIcon = findViewById(R.id.topDrawerIcon);
        hand = findViewById(R.id.hand);
        navigationDrawer();
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    private void navigationDrawer(){
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        topDrawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                break;
            case R.id.nav_kelolaProduk:
                startActivity(new Intent(getApplicationContext(), KelolaProduk.class));
                break;
            case R.id.nav_hostory:
                break;
            case R.id.nav_setting:
                startActivity(new Intent(getApplicationContext(), Setting.class));
                break;
        }
        return true;
    }

}