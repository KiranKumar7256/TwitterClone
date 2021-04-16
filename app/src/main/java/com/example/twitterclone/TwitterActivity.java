package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<String> tUsers;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        setTitle("Twitter Activity");

        listView = findViewById(R.id.listview);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(this);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseUser twitterUser : objects) {
                        tUsers.add(twitterUser.getUsername());
                    }
                    listView.setAdapter(adapter);

                    for(String  twitterUser: tUsers){
                        if(ParseUser.getCurrentUser().getList("fanOf")!=null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                listView.setItemChecked(tUsers.indexOf(twitterUser), true);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send:
                startActivity(new Intent(this, TweetActivity.class));
                break;
            case R.id.logout:
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView= (CheckedTextView) view;

        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterActivity.this,"Followed "+tUsers.get(position), Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().add("fanOf",tUsers.get(position));
        }
        else {
            FancyToast.makeText(TwitterActivity.this,"Unfollowed "+tUsers.get(position), Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList= ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOfList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(TwitterActivity.this,"Saved", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
            }
        });
    }
}
