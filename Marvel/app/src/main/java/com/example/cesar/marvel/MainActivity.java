package com.example.cesar.marvel;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesar.marvel.adapters.ituneArrayAdapter;
import com.example.cesar.marvel.pojo.itune;

public class MainActivity extends Activity {

    private ArrayAdapter<String> arrayAdapter;

    private ListView listView;
    private ituneArrayAdapter ituneArrayAdapter;
    private EditText artistEdit;
    private ImageButton btnNext, btnBack;
    private int indexH;

    private RequestQueue nQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        // artistEdit = (EditText) findViewById(R.id.editText);

        //search();
        indexH = 0;
        marvelAdap(indexH);
    }

    public void marvelAdap(int i){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);
        // new MarvelJson(adapter).execute();
        nQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString(i), adapter);
    }
    // Async task
    /*public class ProcesaJson extends AsyncTask<String, Integer, ArrayList<itune>> {
        private ituneArrayAdapter adapter;
        public ProcesaJson(ituneArrayAdapter adapter){
            this.adapter = adapter;
        }
        @Override
        protected ArrayList<itune> doInBackground(String... urls) {
            // New json
            Json json = new Json();
            // Response from the service as a string
            String jsonString = json.serviceCall(urls[0]);
            ArrayList<itune> arrayList = new ArrayList<>();
            // Give data to the array list. Only the collection name
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject dato = jsonArray.getJSONObject(i);
                    itune ituneObj = new itune();
                    ituneObj.collectionName = dato.getString("collectionName");
                    ituneObj.trackName = dato.getString("trackName");
                    ituneObj.trackPrice = dato.getDouble("trackPrice");
                    arrayList.add(ituneObj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return arrayList;
        }

        // Create new method to give the data to the adapter
        @Override
        protected void onPostExecute(ArrayList<itune> strings) {
            adapter.clear();
            adapter.addAll(strings);
            adapter.notifyDataSetChanged();
        }
    }

    public void search(){
        String artist = artistEdit.getText().toString();
        artist = artist.replace(" ", "+");

        // Array adapter to give data to the list from te service
        ituneArrayAdapter = new ituneArrayAdapter(this,
                R.layout.itunes_layout, new ArrayList<itune>());
        // Give the adapter to the list
        listView.setAdapter(ituneArrayAdapter);
        new ProcesaJson(ituneArrayAdapter).execute("https://itunes.apple.com/search?term=" + artist);
    }*/

    /*public void searchArtist(View view){
        search();
    }*/

    public void goBack(View view){
        indexH -= 100;
        marvelAdap(indexH);
    }

    public  void goNext(View view){
        indexH += 100;
        marvelAdap(indexH);
    }

    private final String LOG_TAG = "MARVEL";

    private static char[] HEXCodes = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private void jsonMarvel(String url, final ArrayAdapter<String> adapter){
        adapter.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        adapter.add(jsonObject.getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        nQueue.add(request);
    }

    // Metodo para obtener el string de marvel
    private String getMarvelString(int index){

        // Conexion con marvel
        String ts = Long.toString(System.currentTimeMillis() / 1000);
        String apikey = "1681a9eefcf8fbf43de66c59727718da";
        String hash = md5(ts + "ede49375699321e3736436b53011574333433f40" + "1681a9eefcf8fbf43de66c59727718da");
        ArrayList<String> arrayList = new ArrayList<>();

        //Conexión con el getway de marvel
        final String CHARACTER_BASE_URL = "http://gateway.marvel.com/v1/public/characters";
        // Configuración de la petición
        String characterJsonStr = null;
        final String TIMESTAMP = "ts";
        final String API_KEY = "apikey";
        final String HASH = "hash";
        final String ORDER = "orderBy";

        Uri builtUri;
        builtUri = Uri.parse(CHARACTER_BASE_URL+"?").buildUpon()
                .appendQueryParameter(TIMESTAMP, ts)
                .appendQueryParameter(API_KEY, apikey)
                .appendQueryParameter(HASH, hash)
                .appendQueryParameter(ORDER, "name")
                .appendQueryParameter("limit", "100")
                .appendQueryParameter("offset", index+"")
                .build();

        //Ejecución de la conexión
            return (builtUri.toString());
    }

    /*
    Investiga y reporta qué es md5:
    MD5 es una función hash que produce un hash de 128 bits. En un principio fue diseñado y
    utilizado como una función criptográfica, pero debido a sus vulverabilidades puede ser
    vulnerado por fuerza bruta, por ello hoy en día es utilizado como un "checksum" que es
    para verificar la integridad de los datos y así verificar que un archivo no haya sido
    modificado.
    */
    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            String hash = new String(hexEncode(digest.digest()));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /*
        Investiga y reporta qué hace esta aplicación
        Códifica un texto en hexadecimal
    */
    public static String hexEncode(byte[] bytes) {
        char[] result = new char[bytes.length*2];
        int b;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            b = bytes[i] & 0xff;
            result[j++] = HEXCodes[b >> 4];
            result[j++] = HEXCodes[b & 0xf];
        }
        return new String(result);
    }

}