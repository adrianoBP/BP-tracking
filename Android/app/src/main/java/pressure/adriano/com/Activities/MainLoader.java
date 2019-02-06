package pressure.adriano.com.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.util.Strings;

import pressure.adriano.com.R;

import static pressure.adriano.com.Helpers.Util.ReadPreference;

public class MainLoader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loader);

        // TODO: Start loading

        if(!Strings.isEmptyOrWhitespace(ReadPreference(this, "authorizationToken"))){

            Intent mainIntent = new Intent(this, MainActivity.class);
            finish();
            startActivity(mainIntent);

        }else{

            Intent loginIntent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(loginIntent);

        }
    }
}
