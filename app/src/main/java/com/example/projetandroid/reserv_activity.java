package com.example.projetandroid;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class reserv_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private WebView webView;
    private TextView textViewDate, textViewStatus2;
    private Button btnChoose, btnReserv;
    private int day, month, year, hour, minute;
    private String myday, myMonth, myYear, myHour, myMinute, dateTime, idResto, idUtil;
    private String URL = "http://10.15.253.250/morvan/projetAndroid/insertReserv.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserv);

        Button buttonInscription = findViewById(R.id.buttonInscription);
        Button buttonSeConnecter = findViewById(R.id.buttonSeConnecter);

        View.OnClickListener ecouteur = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonInscription:
                        Intent intent1 = new Intent(reserv_activity.this, Inscription_activity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.buttonSeConnecter:
                        Intent intent2 = new Intent(reserv_activity.this, Connexion_activity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
            }
        };
        buttonInscription.setOnClickListener(ecouteur);
        buttonSeConnecter.setOnClickListener(ecouteur);

        Intent intent = getIntent();
        if (intent != null) {
            String nomResto = intent.getStringExtra("nomResto");
            idResto = String.valueOf(intent.getIntExtra("idResto", 0));

            TextView TextViewNomResto = findViewById(R.id.TextViewNomResto);
            TextView TextViewAdresseResto = findViewById(R.id.TextViewAdresseResto);
            TextView TextViewDescResto = findViewById(R.id.TextViewDescResto);
            textViewStatus2 = findViewById(R.id.textViewStatus2);
            webView = findViewById(R.id.WebViewHorairesResto);

            Integer pos = nomResto.indexOf("-");
            nomResto = nomResto.substring(0, pos - 1);

            TextViewNomResto.setText(nomResto);

            //creation de la requete http sur le serveur local, cela necessite
            OkHttpClient httpResto = new OkHttpClient();

            //prépare la requête
            Request requestRestos = new Request.Builder().url("http://10.15.253.250/morvan/projetAndroid/unResto.php?idResto=" + idResto).build();
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
                    reserv_activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // on crée un objet JSON à partir de notre réponse.
                                JSONObject jsonObjectlesRestos = new JSONObject(myResponse);
                                //on transforme cet objet JSON en array d'objet Resto sous forme JSON
                                JSONArray jsonArray = jsonObjectlesRestos.optJSONArray("unResto");
                                //on parcours cette collection d'objet Restos pour ajouter chaque Resto dans notre liste d'objet Resto


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String numAdrR = jsonObject.getString("numAdrR");
                                    if (numAdrR.equals("null")) {
                                        numAdrR = "";
                                    }
                                    String voieAdrR = jsonObject.getString("voieAdrR");
                                    if (voieAdrR.equals("null")) {
                                        voieAdrR = "";
                                    }
                                    String cpR = jsonObject.getString("cpR");
                                    if (cpR.equals("null")) {
                                        cpR = "";
                                    }
                                    String villeR = jsonObject.getString("villeR");
                                    if (villeR.equals("null")) {
                                        villeR = "";
                                    }
                                    String descR = jsonObject.getString("descR");
                                    if (descR.equals("null")) {
                                        descR = "";
                                    }
                                    String horairesR = jsonObject.getString("horairesR");
                                    if (horairesR.equals("null")) {
                                        horairesR = "";
                                    }
                                    Log.i("Restos", numAdrR + " " + voieAdrR + " " + cpR + " " + villeR + " " + descR + " " + horairesR); //message qui apparait dans la console pour vérifier
                                    TextViewAdresseResto.setText(numAdrR + " " + voieAdrR + " " + villeR + ", " + cpR);
                                    TextViewDescResto.setText(descR);
                                    webView.loadData(horairesR, "text/html", "UTF-8");
                                }

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
        textViewDate = findViewById(R.id.textViewDate);
        btnReserv = findViewById(R.id.btnReserv);
        btnReserv.setVisibility(View.INVISIBLE);
        btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(reserv_activity.this, reserv_activity.this, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = String.valueOf(year);
        myday = String.valueOf(dayOfMonth);
        if (myday.length() < 2) {
            myday = "0" + myday;
        }
        myMonth = String.valueOf(month + 1);
        if (myMonth.length() < 2) {
            myMonth = "0" + myMonth;
        }
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(reserv_activity.this, reserv_activity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = String.valueOf(hourOfDay);
        if (myHour.length() < 2) {
            myHour = "0" + myHour;
        }
        myMinute = String.valueOf(minute);
        if (myMinute.length() < 2) {
            myMinute = "0" + myMinute;
        }
        btnReserv.setVisibility(View.VISIBLE);
        textViewDate.setText(new StringBuilder().append(myYear).append("-").append(myMonth).append("-").append(myday).append(" ").append(myHour).append(":").append(myMinute));
    }

    public void reserv(View view) {
        dateTime = textViewDate.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);
                if (response.trim().equals("success")) {
                    textViewStatus2.setText("Réservation effectuée.");
                    btnReserv.setClickable(false);
                } else if (response.trim().equals("failure")) {
                    textViewStatus2.setText("Quelque chose s'est mal passé !");
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                idUtil = "";
                Map<String, String> data = new HashMap<>();
                data.put("idResto", idResto);
                data.put("idUtil", idUtil);
                data.put("dateResa", dateTime);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
