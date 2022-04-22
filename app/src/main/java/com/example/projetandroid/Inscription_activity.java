package com.example.projetandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Inscription_activity extends AppCompatActivity {

    private EditText editTextPseudo, editTextEmail, editTextPassword, editTextReenterPassword;
    private TextView textViewStatus;
    private Button btnRegister;
    private String URL = "http://192.168.1.99/projetAndroid/insertUser.php";
    private String pseudo, email, password, reenterPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextReenterPassword = findViewById(R.id.editTextReenterPassword);
        textViewStatus = findViewById(R.id.textViewStatus);
        btnRegister = findViewById(R.id.btnRegister);
        pseudo = email = password = reenterPassword = "";
    }

    public void save(View view) {
        pseudo = editTextPseudo.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        reenterPassword = editTextReenterPassword.getText().toString().trim();
        if(!password.equals(reenterPassword)){
            Toast.makeText(this, "Le mot de passe ne correspond pas", Toast.LENGTH_SHORT).show();
        }
        else if(!pseudo.equals("") && !email.equals("") && !password.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("res", response);
                    if (response.trim().equals("success")) {
                        textViewStatus.setText("Enregistré avec succès.");
                        btnRegister.setClickable(false);
                    } else if (response.trim().equals("failure")) {
                        textViewStatus.setText("Quelque chose s'est mal passé !");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("pseudoU", pseudo);
                    data.put("mailU", email);
                    data.put("mdpU", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    public void login(View view) {
        Intent intent = new Intent(Inscription_activity.this, Connexion_activity.class);
        startActivity(intent);
        finish();
    }
}