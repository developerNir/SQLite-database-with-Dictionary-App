package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dictionary.DictionaryClass.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    DatabaseHelper databaseHelper;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });

        databaseHelper = new DatabaseHelper(this);

        Cursor cursor = databaseHelper.getAllData();

        if (cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String word = cursor.getString(1);
                String meaning = cursor.getString(2);
                String parsOfSpeech = cursor.getString(3);
                String example = cursor.getString(4);

                hashMap = new HashMap<>();
                hashMap.put("id", ""+id);
                hashMap.put("word", word);
                hashMap.put("meaning", meaning);
                hashMap.put("parsOfSpeech", parsOfSpeech);
                hashMap.put("example", example);

                arrayList.add(hashMap);


            }

            listView.setAdapter(new MyAdapter());


        }


    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view1 = layoutInflater.inflate(R.layout.word_card, null ,false);

            TextView wordTv = view1.findViewById(R.id.wordTv);
            ImageButton speckButton = view1.findViewById(R.id.speckButton);
            TextView meaningTv = view1.findViewById(R.id.meaningTv);
            TextView exampleTv = view1.findViewById(R.id.exampleTv);

            hashMap = arrayList.get(i);
            String word = hashMap.get("word");
            String meaning = hashMap.get("meaning");
            String speech = hashMap.get("parsOfSpeech");
            String sentences = hashMap.get("example");

            wordTv.setText(word+"  ("+speech+")");
            meaningTv.setText(meaning);

            exampleTv.setText(sentences);

            speckButton.setOnClickListener(view2 -> {
                textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
            });

            return view1;
        }
    }


}