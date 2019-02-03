package pressure.adriano.com.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
}
