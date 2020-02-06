package com.evans.multiimageupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private Toolbar homeToolbar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        NavController mNavController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupWithNavController(bottomNav, mNavController);

        homeToolbar.setTitle("Home");
        setSupportActionBar(homeToolbar);
    }

    private void initViews() {
        bottomNav = findViewById(R.id.bottom_nav);
        homeToolbar = findViewById(R.id.homeToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionAdd) {
            toAddHouse();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toAddHouse() {
        startActivity(new Intent(this, AddHouseActivity.class));
    }
}
