package com.example.android_assignment03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.assignment_03.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle prevState) {
        super.onCreate(prevState);
        setContentView(R.layout.activity_main);
       RecyclerView rcv;
       rcv = findViewById(R.id.rcv);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        rcv.setLayoutManager(lin);
        ArrayList<String> head=new ArrayList<String>();
        ArrayList<String> levels=new ArrayList<String>();
        ArrayList<String> information=new ArrayList<String>();
        ArrayList<String> id=new ArrayList<String>();
        ArrayList<String> url=new ArrayList<String>();


        try {
            String json="";

            InputStream ipS = getResources().openRawResource(R.raw.mydata);
            byte data[] = new byte[ipS.available()];
            while (ipS.read(data) != -1)
            {
                //doNothing
            }
            json=new String(data);

            try {
                JSONObject jsonObject=new JSONObject(json);
                JSONArray obj=jsonObject.getJSONArray("books");
                for(int i=0;i<obj.length();i++)
                {
                    JSONObject j_obj=obj.getJSONObject(i);
                    head.add(j_obj.getString("title"));
                    levels.add(j_obj.getString("level"));
                    information.add(j_obj.getString("info"));
                    id.add(j_obj.getString("cover"));
                    url.add(j_obj.getString("url"));

                }
                    myRecycler adp=new myRecycler(this,head,levels,information,id,url);
                       rcv.setAdapter(adp);

            }
            catch (JSONException e)
            {
                return;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
