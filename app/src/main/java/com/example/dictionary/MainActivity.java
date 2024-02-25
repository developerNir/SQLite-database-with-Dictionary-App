package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dictionary.DictionaryClass.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText searchView;
    DatabaseHelper databaseHelper;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    TextToSpeech textToSpeech;
    Animation animation,myanim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.search_View);
        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.up_anim);
        myanim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.down_anim);
        searchView.startAnimation(animation);



        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });



//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                textToSpeech.setLanguage(new Locale("bn_IN"));
//                textToSpeech.speak("এখানে অনুসন্ধান করুন", TextToSpeech.QUEUE_FLUSH, null, null);
//            }
//        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                if (i==0){
                    searchView.startAnimation(myanim);
                    searchView.setVisibility(View.VISIBLE);
                }else {
                    searchView.startAnimation(animation);
                    searchView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                Log.d("onS", "onScroll: "+i+" "+i1+" "+i2);
            }
        });

        databaseHelper = new DatabaseHelper(this);
//        Cursor cursor =databaseHelper.sortData("Zoo");

        // search function
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("myText", "beforeTextChanged: "+charSequence+" "+i+" "+" "+i1+" "+i2);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d("myText", "onTextChanged: "+i+"  "+charSequence+" "+i1+" "+i2);
                Cursor cursor = databaseHelper.sortData(searchView.getText().toString());
                loadData(cursor);


                }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("myText", "afterTextChanged: "+editable);
            }
        });

        Cursor cursor = databaseHelper.getAllData();
        loadData(cursor);




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

    public void loadData(Cursor cursor){

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


}