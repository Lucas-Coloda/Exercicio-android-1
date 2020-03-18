package com.example.exercicio_android_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    // URLs
    private static final String ENDPOINT = "http://www.transparencia.gov.br/api-de-dados/";
    private static final String BOLSA_FAMILIA_MUNICIPIO = "bolsa-familia-por-municipio";

    // URI rquest params
    private static final String PARAM_CODIGO_MUNICIPIO = "codigoIbge";
    private static final Integer CODIGO_PR_CURITIBA = 4106902;
    private static final String PARAM_MES_ANO = "mesAno";
    private static final String PARAM_PAGINA = "pagina";

    // URI utils
    private static final String URI_INITIALIATOR = "?";
    private static final String URI_EQUALS_TO = "=";
    private static final String URI_AND = "&";

    // View objects
    private TextView maiotPgt;
    private TextView menorPgt;
    private TextView totalPago;
    private TextView mediaBeneficiados;

    // Other vars
    private ArrayList<JSONObject> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.data = new ArrayList<JSONObject>();

        // View Objects
        maiotPgt = findViewById(R.id.maiorPgt);
        menorPgt = findViewById(R.id.menorPgt);
        totalPago = findViewById(R.id.totalPago);
        mediaBeneficiados = findViewById(R.id.mediaBeneficiados);

        // Get API data
        carregarDados();
    }

    private void carregarDados() {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

        Date date = new Date();

        Integer currentMonth = Integer.valueOf(monthFormat.format(date.getTime()));
        Integer currentYear = Integer.valueOf(yearFormat.format(date.getTime()));

        for (int i = 0; i < 12; i++) {
            String month = String.format("%02d", (((i + currentMonth) % 12) + 1));
            String year = Long.valueOf(month) > currentMonth ? String.valueOf(currentYear - 1) : String.valueOf(currentYear);

            String url = ENDPOINT + BOLSA_FAMILIA_MUNICIPIO;
            url += URI_INITIALIATOR + PARAM_CODIGO_MUNICIPIO + URI_EQUALS_TO + CODIGO_PR_CURITIBA;
            url += URI_AND + PARAM_MES_ANO + URI_EQUALS_TO + year + month;
            url += URI_AND + PARAM_PAGINA + URI_EQUALS_TO + 1;
            generateRequest(url);
        }

        this.showData();
    }

    // View suport methods
    private void showData() {
        try {
            // Metodo syncrono do volley nao se adaptou ao resultado da busca na API
            TimeUnit.SECONDS.sleep(15);
            Log.i("ii", this.data.size()+"");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Services
    public void generateRequest(String url) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, successResponse(), errorResponse());
        APISingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<JSONArray> successResponse() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    try {
                        JSONObject res = response.getJSONObject(0);

                        JSONObject dado = new JSONObject();
                        dado.put("data", res.getString("dataReferencia"));
                        dado.put("valor", res.getString("valor"));
                        dado.put("beneficiados", res.getString("quantidadeBeneficiados"));

                        Log.i("dado", response.toString());
                        Log.i("dado", res.toString());
                        data.add(dado);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("INFO", response.toString());
                }
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
