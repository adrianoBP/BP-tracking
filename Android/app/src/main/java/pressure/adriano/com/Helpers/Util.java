package pressure.adriano.com.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import pressure.adriano.com.R;

import static pressure.adriano.com.Activities.LoginActivity.googleSignInClient;

public class Util {

    public static void HideKeyboard(View view, Context context){
        if(view != null){
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void CreateBasicSnack(String message, Integer length, Context context){
        length = length != null ? length : 3000;
        Snackbar.make(((Activity)context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(((Activity)context).getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    static void Init(Toolbar toolbar, Context context){
        // Override default toolbar icon
//        Drawable drawable =  context.getDrawable(R.mipmap.ic_add);
//        toolbar.setOverflowIcon(drawable);
    }

    public static Integer CalculateAverage(String field){

        String[] values = field.split(",");
        Integer average = 0, counter = values.length;

        for (String value: values) {
            if(!value.isEmpty())
                average += Integer.parseInt(value.trim());
            else
                counter--;
        }

        return average / counter;
    }

    //region SharedPreferences
    public static String ReadPreference(Context context, String preferenceName){
        if(!CheckPreference(context, preferenceName)){ return null; }
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferenceDestination), Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, "");
    }
    public static void WritePreference(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferenceDestination), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }
    public static void ClearPreference(Context context, String preferenceName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferenceDestination), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, "");
        editor.apply();
    }
    public static boolean CheckPreference(Context context, String preferenceName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferenceDestination), Context.MODE_PRIVATE);
        return sharedPreferences.contains(preferenceName);
    }
    //endregion

    public static void TryLogout(){
        if(googleSignInClient != null){
            googleSignInClient.signOut();
        }
    }
}
