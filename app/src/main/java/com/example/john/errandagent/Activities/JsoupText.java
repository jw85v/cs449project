package com.example.john.errandagent.Activities;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.john.errandagent.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupText extends AppCompatActivity {

    TextView webTxt;
    EditText searchTxt;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_jsoup_text);

//        webTxt = findViewById(R.id.testText);
//        searchTxt = findViewById(R.id.searchText);
//        searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Soupy().execute();
            }
        });
    }

    private void GetPrices() {

    }

    public class Soupy extends AsyncTask<Void,Void,Void>{
        String words;
        String childen;
        String price;
        Elements help;
        String search = searchTxt.getText().toString().replace(' ','+');
        String[] priceArray;
        @Override
        protected Void doInBackground(Void... voids) {
            Document doc;
            Connection.Response response;
            try {
                response = Jsoup.connect("https://www.amazon.com/").method(Connection.Method.GET).execute();
                doc = Jsoup.connect("https://www.walmart.com/search/?query=" + search).cookies(response.cookies()).get();
                words = doc.text();
                price = doc.getElementsByClass("search-result-productprice gridview enable-2price-2").text().replace("Current Price", "").replace("$", "");
                //priceArray =  price.split("\\s+");

            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void avoid)
        {
            super.onPostExecute(avoid);
            webTxt.setText(words);
        }
    }
}
