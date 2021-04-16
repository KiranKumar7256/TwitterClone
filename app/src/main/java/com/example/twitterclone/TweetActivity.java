package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TweetActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edttweet;
    Button sendbtn,userstweetbtn;
    ListView tweetsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        setTitle("Tweets");

        edttweet= findViewById(R.id.edttweet);
        sendbtn= findViewById(R.id.sendbtn);
        userstweetbtn= findViewById(R.id.userstweetbtn);
        tweetsListView= findViewById(R.id.tweetslistview);

        sendbtn.setOnClickListener(this);
        userstweetbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendbtn:
                if(edttweet.getText().toString().equals("")){
                    FancyToast.makeText(TweetActivity.this,"Please add the tweet first!",FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                }
                else {
                    ParseObject parseObject = new ParseObject("MyTweet");
                    parseObject.put("tweet", edttweet.getText().toString());
                    parseObject.put("user", ParseUser.getCurrentUser().getUsername());
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(TweetActivity.this, "Tweet sent!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                edttweet.setText("");
                            } else {
                                FancyToast.makeText(TweetActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        }
                    });
                    dialog.dismiss();
                }
                break;
            case R.id.userstweetbtn:

                final ArrayList<HashMap<String ,String >> tweetlist=new ArrayList<>();
                final SimpleAdapter adapter=new SimpleAdapter(this,tweetlist, android.R.layout.simple_list_item_2,new  String[] {"tweetUsername","tweetValue"},new int[] {android.R.id.text1,android.R.id.text2});
                try {
                    ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("MyTweet");
                    parseQuery.whereContainedIn("user",ParseUser.getCurrentUser().getList("fanOf"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects.size()>0 && e==null){
                                for(ParseObject tweetObject: objects){
                                    HashMap<String,String> userTweet=new HashMap<>();
                                    userTweet.put("tweetUsername",tweetObject.getString("user"));
                                    userTweet.put("tweetValue",tweetObject.getString("tweet"));
                                    tweetlist.add(userTweet);
                                }
                                tweetsListView.setAdapter(adapter);
                            }
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    public void rootLayoutTapped(View view){
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}