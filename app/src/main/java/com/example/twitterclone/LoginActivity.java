package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtuser,edtpass;
    Button loginbtn;
    TextView gobacktxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        edtuser= findViewById(R.id.edtuser);
        edtpass= findViewById(R.id.edtpass);
        loginbtn= findViewById(R.id.loginbtn);
        gobacktxt= findViewById(R.id.gobacktxt);

        edtpass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(loginbtn);
                }
                return false;
            }
        });

        loginbtn.setOnClickListener(this);
        gobacktxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbtn:

                ProgressDialog dialog=new ProgressDialog(this);
                dialog.setMessage("Signing Up...");
                dialog.show();
                ParseUser.logInInBackground(edtuser.getText().toString(), edtpass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user!=null && e==null){
                            FancyToast.makeText(LoginActivity.this,"Welcome "+user.getUsername(),FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();
                            startActivity(new Intent(LoginActivity.this,TwitterActivity.class));
                            finish();
                        }
                        else {
                            FancyToast.makeText(LoginActivity.this, e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });
                dialog.dismiss();
                break;
            case R.id.gobacktxt:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }
    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}