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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtemail,edtusername,edtpassword;
    Button signupbtn;
    TextView clicktxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //    ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser user=ParseUser.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(this,TwitterActivity.class));
            finish();
        }

        edtemail= findViewById(R.id.edtemail);
        edtusername= findViewById(R.id.edtusername);
        edtpassword= findViewById(R.id.edtpassword);
        signupbtn= findViewById(R.id.signupbtn);
        clicktxt= findViewById(R.id.clicktxt);

        edtpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(signupbtn);
                }
                return false;
            }
        });

        signupbtn.setOnClickListener(this);
        clicktxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupbtn:

                ParseUser user=new ParseUser();
                user.setEmail(edtemail.getText().toString());
                user.setUsername(edtusername.getText().toString());
                user.setPassword(edtpassword.getText().toString());

                ProgressDialog dialog=new ProgressDialog(this);
                dialog.setMessage("Signing Up...");
                dialog.show();
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(MainActivity.this,"SignUp Successful!",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();
                            startActivity(new Intent(MainActivity.this,TwitterActivity.class));
                            finish();
                        }
                        else {
                            FancyToast.makeText(MainActivity.this, e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });
                dialog.dismiss();
                break;
            case R.id.clicktxt:
                startActivity(new Intent(this,LoginActivity.class));
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