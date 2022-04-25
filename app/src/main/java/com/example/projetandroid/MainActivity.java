package com.example.projetandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projetandroid.Modele.Resto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonInscription = findViewById(R.id.buttonInscription);
        Button buttonSeConnecter = findViewById(R.id.buttonSeConnecter);

        View.OnClickListener ecouteur = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonInscription:
                        Intent intent1 = new Intent(MainActivity.this, Inscription_activity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.buttonSeConnecter:
                        Intent intent2 = new Intent(MainActivity.this, Connexion_activity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
            }
        };
        buttonInscription.setOnClickListener(ecouteur);
        buttonSeConnecter.setOnClickListener(ecouteur);

        //on définit une collection de caractéristiques d un Resto
        ArrayList<String> lesRestos = new ArrayList<>();
        //on définit un objet ListView
        ListView listViewRestos;
        //on associe l'objet au widget
        listViewRestos = findViewById(R.id.listViewResto);

        //creation de la requete http sur le serveur local, cela necessite
        OkHttpClient httpResto = new OkHttpClient();

        //prépare la requête
        Request requestRestos = new Request.Builder().url("http://http://10.15.253.250/morvan/projetAndroid/listViewResto.php").build();
        //exécution de cette requête
        httpResto.newCall(requestRestos).enqueue(new Callback() {
            @Override
            //si la requête échoue affichage d'un message d'erreur dans les log
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("erreur1", e.getMessage());
            }

            @Override
            //si la requête réussie
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // on crée un objet JSON à partir de notre réponse.
                            JSONObject jsonObjectlesRestos = new JSONObject(myResponse);
                            //on transforme cet objet JSON en array d'objet Resto sous forme JSON
                            JSONArray jsonArray = jsonObjectlesRestos.optJSONArray("restos");
                            //on parcours cette collection d'objet Restos pour ajouter chaque Resto dans notre liste d'objet Resto


                            //on efface le contenu de la liste
                            lesRestos.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String nomR = jsonObject.getString("nomR");
                                String villeR = jsonObject.getString("villeR");
                                Log.i("Restos", nomR + " " + villeR); //message qui apparait dans la console pour vérifier
                                //on ajoute le Resto à la collection lesRestos
                                lesRestos.add(nomR + " - " + villeR);
                            }
                            //on affecte cette liste d'objet Restos dans la listeview pour l'afficher
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lesRestos);
                            listViewRestos.setAdapter(dataAdapter);
                            listViewRestos.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(MainActivity.this, lesRestos.get(position), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, reserv_activity.class);
                                    intent.putExtra("nomResto", lesRestos.get(position));
                                    intent.putExtra("idResto", position+1);
                                    startActivity(intent);
                                }
                            });

                        } catch (final JSONException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("erreur2", e.getMessage());
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}