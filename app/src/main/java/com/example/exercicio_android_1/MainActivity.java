package com.example.exercicio_android_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String ENDPOINT = "http://www.transparencia.gov.br/api-de-dados/";
    private static final String BOLSA_FAMILIA_MUNICIPIO = "bolsa-familia-por-municipio";
    private static final String PARAM_CODIGO_MUNICIPIO = "&codigoIbge=";
    private static final Integer CODIGO_PR_CURITIBA = 4106902;
    private static final String PARAM_MES_ANO = "?mesAno=";
    private static final String PARAM_PAGINA = "&pagina=";

    private TextView textView;
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textName);

        names = new ArrayList<>();
    }
    private void carregarDados() {
        for (int i = 1; i <= 12; i++) {
            String url = ENDPOINT + BOLSA_FAMILIA_MUNICIPIO;
            url += PARAM_CODIGO_MUNICIPIO + CODIGO_PR_CURITIBA;
            url += PARAM_MES_ANO + 201903;
            url += PARAM_PAGINA + 1;
            generateRequest(url);
            this.showData();
        }
    }

    private void showData() {
       Log.i("INFO", "oi");
    }

    // Event captured from view
    public void btnCarregarEvent(View v) {
        carregarDados();
    }

    // Services
    public void generateRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, successResponse(), errorResponse());
        APISingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<JSONObject> successResponse() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("INFO", response.toString());
            }
        };
    }

    public Response.ErrorListener errorResponse() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        };
    }
}