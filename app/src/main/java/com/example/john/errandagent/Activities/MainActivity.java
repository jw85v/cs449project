package com.example.john.errandagent.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.john.errandagent.Fragments.CalendarFragment;
import com.example.john.errandagent.Fragments.ItineraryFragment;
import com.example.john.errandagent.R;
import com.example.john.errandagent.Fragments.ShoppingListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<String, ?> jsonPreferences;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShoppingListFragment()).commit();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sharedPref = this.getSharedPreferences("AccountPref", Context.MODE_PRIVATE);
        user = sharedPref.getString("CurrentUser", "");
        MenuInflater inflate = getMenuInflater();
        if(user.equals("g")) {
            inflate.inflate(R.menu.main_menu_guest, menu);
        }
        else
            inflate.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //button select switch
        switch(item.getItemId())
        {
            case R.id.logout:
                //count is reset if reset button is clicked
                logOut();
                return true;
            case R.id.login:
                logIn();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void logOut(){
        SharedPreferences sharedPref = this.getSharedPreferences("AccountPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CurrentUser", "g");
        editor.commit();
        Intent createAccount = new Intent(this, LoginActivity.class);
        startActivity(createAccount);
    }

    private void logIn(){
        SharedPreferences sharedPref = this.getSharedPreferences("ShoppingListPref", Context.MODE_PRIVATE);
        jsonPreferences = sharedPref.getAll();
        List keys = new ArrayList(jsonPreferences.keySet());
        List<Integer> deleteKeys = new ArrayList<Integer>();
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < jsonPreferences.size(); i++) {
            String[] listInfo = keys.get(i).toString().split("SSTTOOPPSS|IITTEEMMSS|TTOOTTAALLSS|UUSSEERR");
            if (listInfo.length >= 5) {
                if (listInfo[4].equals("g")) {
                    deleteKeys.add(i);
                    editor.remove(keys.get(i).toString());
                }

            }
            else
                editor.remove(keys.get(i).toString());

        }
        editor.commit();
        Intent createAccount = new Intent(this, LoginActivity.class);
        startActivity(createAccount);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selected = null;

                    switch(menuItem.getItemId()){
//                        case R.id.home:
//                            selected = new HomeFragment();
//                            break;
                        case R.id.itinerary:
                            selected = new ItineraryFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
                            break;
                        case R.id.shopping_list:
                            selected = new ShoppingListFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
                            break;
                        case R.id.calendar:
                            selected = new CalendarFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
                            //comingSoon();
                            break;
//                        case R.id.price_compare:
//                            selected = new PriceCompareFragment();
//                            break;

                    }


                    return true;
                }
            };
    private void comingSoon(){
        AlertDialog show = new AlertDialog.Builder(this, R.style.AlertThemeCustom)
                .setTitle("Feature coming soon!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
