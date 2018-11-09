package com.versionone.tm.timemanager.view;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.versionone.tm.timemanager.Controller.UserController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.Entity.User;
import com.versionone.tm.timemanager.R;

import java.util.ArrayList;

public class Login_Activity extends AppCompatActivity {
    private EditText accountEditText,passwordEditText;
    private Button loginButton,RegisteredButton,ChangeButton;
    private DBManager dbMgr;
    private User user;
    private SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        dbMgr= new DBManager(Login_Activity.this,"tm_tb",null,1);
        db=dbMgr.getWritableDatabase();
        accountEditText = (EditText)findViewById(R.id.accountEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginButton = (Button)findViewById(R.id.loginButton);
        RegisteredButton = (Button)findViewById(R.id.RegisteredButton);
        ChangeButton = (Button)findViewById(R.id.ChangeButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = accountEditText.getText().toString();
                String pw = passwordEditText.getText().toString();
                UserController uc = new UserController();
                if (TextUtils.isEmpty(username)||TextUtils.isEmpty(pw)){
                    Toast.makeText(Login_Activity.this,"The account or password cannot be empty",(int)2000).show();
                }
                else if (uc.verifes(username,pw,dbMgr)){
                    Intent s = new Intent(getApplicationContext(),MainEvent_Activity.class);
                    s.putExtra("extra_data",username);
                    s.putExtra("extra_password",pw);
                    startActivity(s);
                }
                else {
                    Toast.makeText(Login_Activity.this,"WRONG PASSWORD!!!",(int)2000).show();

                }
            }
        });
        RegisteredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s2 = new Intent(getApplicationContext(),Register_Activity.class);
                startActivity(s2);
            }
        });
        ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s3 = new Intent(getApplicationContext(),Changepassword_Activity.class);
                startActivity(s3);

            }
        });
    }

}
