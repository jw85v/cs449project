package com.example.john.errandagent.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.john.errandagent.R;

public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final BootstrapButton login =  findViewById(R.id.loginBtn);
        BootstrapButton guest = findViewById(R.id.guestBtn);
        TextView createAccount = findViewById(R.id.createAccount);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);



        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                setGuest();
                BuildAlert("By continuing as a guest, your activity and preferences will not be saved", intent);


            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAccount = new Intent(v.getContext(), CreateAccountActivity.class);
                startActivity(createAccount);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(password.getText())){
                    login();
                }
            }
        });


    }

    public void BuildAlert(String titleMessage, final Intent intent){
        //alert dialog is initialized
        AlertDialog show = new AlertDialog.Builder(this, R.style.AlertThemeCustom)
                .setTitle(titleMessage)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LaunchPage(intent);
                    }
                }).show();
    }

    public void login(){
        SharedPreferences sharedPref = this.getSharedPreferences("AccountPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String pass = sharedPref.getString(name.getText().toString(), "");
        if(pass.equals(password.getText().toString())){
            editor.putString("CurrentUser", name.getText().toString());
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            LaunchPage(intent);
        }
        else{
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            BuildAlert("Login failed", intent);
        }

    }
    public void setGuest(){
        SharedPreferences sharedPref = this.getSharedPreferences("AccountPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CurrentUser", "g");
        editor.commit();
    }

    public void LaunchPage(Intent intent)
    {
        startActivity(intent);
    }
}
