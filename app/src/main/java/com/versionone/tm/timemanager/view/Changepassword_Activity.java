package com.versionone.tm.timemanager.view;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.versionone.tm.timemanager.Controller.UserController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.R;

public class Changepassword_Activity extends AppCompatActivity {
    private EditText Email,UserName,newPassword;
    private Button Submit,Back1;
    UserController uc = new UserController();
    DBManager dbMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword_activity);
        dbMgr= new DBManager(Changepassword_Activity.this,"tm_tb",null,1);
        //初始化控件
        Email = (EditText)findViewById(R.id.Email);
        UserName = (EditText)findViewById(R.id.UserName);
        newPassword = (EditText)findViewById(R.id.newPassword);
        Submit = (Button) findViewById(R.id.Submit);
        Back1 = (Button) findViewById(R.id.Back1);
        //给Submit设置事件
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email_value = Email.getText().toString();
                String UserName_value = UserName.getText().toString();
                String newPassword_value = newPassword.getText().toString();
                Log.i("***********"," -1");
                if(TextUtils.isEmpty(Email_value)||TextUtils.isEmpty(UserName_value)||TextUtils.isEmpty(newPassword_value)){
                    Toast.makeText(Changepassword_Activity.this,"Form cannot be empty!!!",(int)2000).show();
                }
                else if (uc.ChangePassword(Email_value,UserName_value,newPassword_value,dbMgr)){
                    Log.i("***********"," 0");
                    Toast.makeText(Changepassword_Activity.this,"Successfully!",(int)2000).show();
                    Email.setText("");
                    UserName.setText("");
                    newPassword.setText("");
                }
                else {
                    Toast.makeText(Changepassword_Activity.this,"ERROR!",(int)2000).show();
                }
            }
        });

        Back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ssss1 = new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(ssss1);
            }
        });

    }
}
