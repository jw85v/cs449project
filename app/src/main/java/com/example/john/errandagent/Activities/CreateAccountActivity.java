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

import com.example.john.errandagent.DataPersistence.SaveUserInformation;
import com.example.john.errandagent.Queries.GetUserInformation;
import com.example.john.errandagent.R;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText accountName;
    private EditText accountPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        accountName = findViewById(R.id.createNameText);
        accountPass = findViewById(R.id.createPassText);



        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button createBtn = findViewById(R.id.createAccountBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(accountName.getText()) && !TextUtils.isEmpty(accountPass.getText())){
                    createAccount();

                }
                else{
                    Intent intent = new Intent(getBaseContext(), CreateAccountActivity.class);
                    alert("Please fill out all fields!", intent);
                }
            }
        });
    }

    public void alert(String message, final Intent intent){
        AlertDialog show = new AlertDialog.Builder(this, R.style.AlertThemeCustom)
                .setTitle(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                }).show();
    }

    public void createAccount(){

        SaveUserInformation saveUserInformation = new SaveUserInformation();
        if(saveUserInformation.createUser(getBaseContext(), accountName.getText().toString(), accountPass.getText().toString())){
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            alert(accountName.getText().toString()+" created!", intent);

        }
        else{
            Intent intent = new Intent(getBaseContext(), CreateAccountActivity.class);
            alert(accountName.getText().toString() +" is already taken!", intent);
        }
    }
}
