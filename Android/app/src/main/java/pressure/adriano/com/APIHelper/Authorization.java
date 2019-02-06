package pressure.adriano.com.APIHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

import pressure.adriano.com.Activities.LoginActivity;
import pressure.adriano.com.Activities.MainActivity;
import pressure.adriano.com.R;

import static pressure.adriano.com.Activities.LoginActivity.googleSignInClient;
import static pressure.adriano.com.Helpers.Util.ClearPreference;
import static pressure.adriano.com.Helpers.Util.CreateBasicSnack;
import static pressure.adriano.com.Helpers.Util.ReadPreference;
import static pressure.adriano.com.Helpers.Util.WritePreference;

public class Authorization {

    public static void GoogleAuthorize(final Context context, String googleId){

        final String logLocation = "API.AUTH.GOOGLEAUTH";

        String url = context.getString(R.string.APIEndpoint) + "/GoogleAuthorization.php";

        Map<String, String> data = new HashMap<>();
        data.put("googleId", googleId);

        JsonObjectRequest getDataRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        boolean displayError = false;

                        try {
                            String error = response.getString("Error");
                            if (!error.equals("null") && !error.equals("")) {
                                displayError = true;
                                Log.e(logLocation, error);
                            }else{
                                String token = response.getString("token");
                                WritePreference(context, "authorizationToken", token);
                            }
                        }catch (JSONException jex){
                            displayError = true;
                            Log.e(logLocation, jex.getMessage());
                        }catch (Exception ex){
                            displayError = true;
                            Log.e(logLocation, ex.getMessage());
                        }finally {
                            if(displayError){
                                CreateBasicSnack("Unable to retrieve the data!", null, context);
                                googleSignInClient.signOut();
                            }
                            else {
                                Intent mainIntent = new Intent(context, MainActivity.class);
                                ((Activity)context).finish();
                                context.startActivity(mainIntent);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        googleSignInClient.signOut();
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

    public static void Logout(final Context context){

        final String logLocation = "API.AUTH.LOGOUT";

        String url = context.getString(R.string.APIEndpoint) + "/Logout.php";

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
                                displayError = true;
                                Log.e(logLocation, error);
                            }else{
                                ClearPreference(context, "authorizationToken");
                            }
                        }catch (JSONException jex){
                            displayError = true;
                            Log.e(logLocation, jex.getMessage());
                        }catch (Exception ex){
                            displayError = true;
                            Log.e(logLocation, ex.getMessage());
                        }finally {
                            googleSignInClient.signOut();
                            Intent mainIntent = new Intent(context, LoginActivity.class);
                            ((Activity)context).finish();
                            context.startActivity(mainIntent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        googleSignInClient.signOut();
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
