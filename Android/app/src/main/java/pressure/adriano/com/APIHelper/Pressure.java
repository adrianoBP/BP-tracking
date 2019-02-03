package pressure.adriano.com.APIHelper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pressure.adriano.com.R;

import static pressure.adriano.com.Util.CreateBasicSnack;

public class Pressure {

    public static void SendPressureData(String systole, String diastole, final Context context){

        final String logLocation = "API.PRESSURE.SEND";

        Map<String, String> data = new HashMap<>();
        data.put("systole", systole);
        data.put("diastole", diastole);

        String url = context.getString(R.string.APIEndpoint) + "/AddMeasurement.php";

        JsonObjectRequest sendDataRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        boolean displayError = false;

                        try {
                            String error = response.getString("Error");
                            if (!error.equals("null") && !error.equals("")) {
                                displayError = true;
                                Log.e(logLocation, error);
                            } else {
                                CreateBasicSnack("Data sent correctly!", context);
                                Log.d(logLocation, "Data sent correctly!");
                            }
                        } catch (JSONException jex) {
                            displayError = true;
                            Log.e(logLocation, jex.getMessage());

                        } catch (Exception ex) {
                            displayError = true;
                            Log.e(logLocation, ex.getMessage());
                        }

                        if(displayError){ CreateBasicSnack("Unable to send the data!", context); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Log.e(logLocation, error.getMessage());
                        }catch (Exception ex){
                            Log.e(logLocation, "Unable to display the error response - " + ex.getMessage());
                        }finally {
                            CreateBasicSnack("Unable to send the data!", context);
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sendDataRequest);
    }

}
