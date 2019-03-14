package com.versionone.tm.timemanager.view;
import android.content.Intent;
import android.database.Cursor;
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

public class Register_Activity extends AppCompatActivity {
    private EditText editText,editText2,editText3;
    private Button okButton,Back;
    private DBManager dbMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        dbMgr= new DBManager(Register_Activity.this,"tm_tb",null,1);



/*
        //test
        dbMgr.open();
        Cursor c = dbMgr.query1("#111112");
        while (c.moveToNext()){
            //  int id  = c.getColumnIndex("id");

            String  name = c.getString(2);
            String  psd = c.getString(3);
            String  email =c.getString(1);
            Log.i("++++++++++","name is "+name +"  psd: "+psd+"    email:  "+email);
        }
        dbMgr.close();*/





        editText = (EditText)findViewById(R.id.Email);
        editText2 = (EditText)findViewById(R.id.UserName);
        editText3 = (EditText)findViewById(R.id.newPassword);
        okButton = (Button)findViewById(R.id.Submit);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Email = editText.getText().toString();
                String UserName = editText2.getText().toString();
                String password = editText3.getText().toString();
                UserController uc = new UserController();//实例化UserController类；
                if(TextUtils.isEmpty(Email)||TextUtils.isEmpty(UserName)||TextUtils.isEmpty(password)){
                    Toast.makeText(Register_Activity.this,"Form cannot be empty!!!",(int)2000).show();
                }
                else {
                    uc.Registered(dbMgr,Email,UserName,password);
                    Toast.makeText(Register_Activity.this,"Successfully!",(int)2000).show();
                    editText.setText("");
                    editText2.setText("");
                    editText3.setText("");
                }
            }
        });
        Back = (Button)findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sss2 = new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(sss2);
            }
        });



    }
}
