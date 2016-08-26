package com.example.antonio.bdreplace;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Source source;
    EditText etQuery,etDev;
    TextView tvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        source =  new Source(getApplication());
        etQuery=(EditText)findViewById(R.id.etQuery);
        etDev=(EditText)findViewById(R.id.etDev);
        tvEvents=(TextView)findViewById(R.id.tvEvents);
    }

    public void showEvents(View view){
        Cursor allEvents =source.GetAllEvents();
        String Event="";
        if(allEvents.moveToFirst()){
            do{
                Event+="Query: "+allEvents.getString(1)+"\nDEV: "+String.valueOf(allEvents.getInt(6))+"\n";
            }while(allEvents.moveToNext());
        }
        tvEvents.setText(Event);
    }
    public void AddEvents(View view){
        String Query = etQuery.getText().toString();
        int dev = Integer.parseInt(etDev.getText().toString());
        source.AddNewEvent(Query,dev);
    }
}
