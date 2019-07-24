package com.example.tictactoe;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class display extends AppCompatActivity {

    DatabaseHandler db;
    ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        list= findViewById(R.id.list);
        db = new DatabaseHandler(this);

        ArrayList<String> arrayList = new ArrayList<>();
        Cursor data = db.displayScore();

        if (data.getCount()==0){
            Toast.makeText(display.this,"Add something ti be displayed",Toast.LENGTH_SHORT).show();
        }else {
            while (data.moveToNext()){
                arrayList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
                list.setAdapter(listAdapter);
            }
        }


    }
}
