package com.example.hyongdonjeong_juheekim_comp304lab6_ex1;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String[] contacts;
    ListView lstView;
    Intent intent;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setTitle("My Messaging App");

        TextView textView = new TextView(getApplicationContext());
        textView.setText("My Contacts");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
        textView.setPaddingRelative(50, 4, 4, 40);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/bahnschrift.ttf");
        textView.setTypeface(typeface);


        //ListView lstView = getListView();
        lstView = (ListView) findViewById(R.id.android_list);
        lstView.addHeaderView(textView);
        lstView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        lstView.setTextFilterEnabled(true);

        contacts = getResources().getStringArray(R.array.contacts);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                contacts);

        // Assign adapter to ListView
        lstView.setAdapter(adapter);
        intent = new Intent(this, MessageActivity.class);

        // ListView Item Click Listener
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String item = (String) lstView.getItemAtPosition(position);

                // Show Alert
                intent.putExtra("contactName", item);
                startActivity(intent);
            }

        });


    }

}
