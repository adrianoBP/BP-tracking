package pressure.adriano.com.APIHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pressure.adriano.com.Activities.Graph;
import pressure.adriano.com.Activities.LoginActivity;
import pressure.adriano.com.Activities.MainActivity;
import pressure.adriano.com.Classes.PressureEntry;
import pressure.adriano.com.Helpers.Util;
import pressure.adriano.com.R;

import static pressure.adriano.com.Helpers.Util.CreateBasicSnack;
import static pressure.adriano.com.Helpers.Util.ReadPreference;
import static pressure.adriano.com.Helpers.Util.StartMainActivity;

public class Pressure {

    public static void SendPressureData(PressureEntry pressureEntry, final Context context){

        final String logLocation = "API.PRESSURE.SEND";

        Map<String, String> data = new HashMap<>();
        data.put("systole", pressureEntry.getSystole().toString());
        data.put("diastole", pressureEntry.getDiastole().toString());
        data.put("bpm", pressureEntry.getBpm().toString());
        data.put("token", ReadPreference(context, "authorizationToken"));

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
                                CreateBasicSnack("Data sent correctly!", null, context);
                                Log.d(logLocation, "Data sent correctly!");
                            }
                        } catch (JSONException jex) {
                            displayError = true;
                            Log.e(logLocation, jex.getMessage());
                        } catch (Exception ex) {
                            displayError = true;
                            Log.e(logLocation, ex.getMessage());
                        }finally {
                            if(displayError){ CreateBasicSnack("Unable to send the data!", null, context); }
                        }

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
                            CreateBasicSnack("Unable to send the data!", null, context);
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sendDataRequest);
    }

    public static void GetPressureData(final Context context, final View view){

        final String logLocation = "API.PRESSURE.GET";

        @SuppressLint("SimpleDateFormat") final SimpleDateFormat responseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String url = context.getString(R.string.APIEndpoint) + "/GetMeasurements.php";

        Map<String, String> data = new HashMap<>();
        data.put("token", ReadPreference(context, "authorizationToken"));

        JsonObjectRequest getDataRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        boolean displayError = false;
                        List<PressureEntry> pressureEntries = new ArrayList<>();

                        try {
                            String error = response.getString("Error");
                            if (!error.equals("null") && !error.equals("")) {
                                displayError = true;
                                Log.e(logLocation, error);
                            }else{
                                JSONArray responseData = response.getJSONArray("Data");
                                for(int i = 0; i < responseData.length(); i++){

                                    JSONObject pressureOBJ = responseData.getJSONObject(i);

                                    PressureEntry pressureEntry = new PressureEntry(
                                            pressureOBJ.getString("id"),
                                            pressureOBJ.getInt("systole"),
                                            pressureOBJ.getInt("diastole"),
                                            pressureOBJ.getInt("bpm"),
                                            responseDateFormat.parse(pressureOBJ.getString("createTime"))
                                    );
                                    pressureEntries.add(pressureEntry);
                                }
                            }
                        }catch (ParseException pex){
                            displayError = true;
                            Log.e(logLocation, pex.getMessage());
                        }catch (JSONException jex){
                            displayError = true;
                            Log.e(logLocation, jex.getMessage());
                        }catch (Exception ex){
                            displayError = true;
                            Log.e(logLocation, ex.getMessage());
                        }finally {
                            if(displayError){ CreateBasicSnack("Unable to retrieve the data!", null, context); }
                            else{
                                if(pressureEntries.size() > 0) {
                                    Collections.reverse(pressureEntries);
                                    Graph.AddGraphData(pressureEntries, context, view);
                                }else{
                                    Log.e(logLocation, "No data available!");
                                    CreateBasicSnack("No data available!", null, context);
                                }
                            }
                        }
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
                            CreateBasicSnack("Unable to retrieve the data!", null, context);
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(getDataRequest);

    }

    public static void ValidateToken(final Context context, final boolean startActivity){

        final String logLocation = "API.PRESSURE.VALIDATE";

        String url = context.getString(R.string.APIEndpoint) + "/ValidateToken.php";

        Map<String, String> data = new HashMap<>();
        data.put("token", ReadPreference(context, "authorizationToken"));

        JsonObjectRequest getDataRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        boolean displayError = false;

                        try {
                            String error = response.getString("Error");
                            if (!error.equals("null") && !error.equals("")) {

                                Intent loginIntent = new Intent(context, LoginActivity.class);
//                                ((Activity)context).finish();
                                context.startActivity(loginIntent);

                            }else{
                                if(startActivity) {
                                    StartMainActivity(context);
                                }
                            }
                        }catch (JSONException jex){
                            displayError = true;
                            Log.e(logLocation, "JEX - " + jex.getMessage());
                        }catch (Exception ex){
                            displayError = true;
                            Log.e(logLocation, ex.getMessage());
                        }finally {
                            if(displayError){ CreateBasicSnack("Unable to retrieve the data!", null, context); }
                        }
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
                            CreateBasicSnack("Unable to retrieve the data!", null, context);
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(getDataRequest);

    }



}
