package pressure.adriano.com.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.util.Strings;

import pressure.adriano.com.R;

import static pressure.adriano.com.APIHelper.Pressure.ValidateToken;
import static pressure.adriano.com.Helpers.Util.Init;
import static pressure.adriano.com.Helpers.Util.ReadPreference;

public class MainLoader extends AppCompatActivity {

    // Elements
    LottieAnimationView avMainLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loader);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        // TODO: Start loading animatino

        // Link items
        avMainLoader = findViewById(R.id.avMainLoader);

        // Add events
        avMainLoader.setImageAssetsFolder("assets");
        avMainLoader.setAnimation("loadingAnimation.json");
        avMainLoader.loop(true);
        avMainLoader.playAnimation();


        Init(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Strings.isEmptyOrWhitespace(ReadPreference(MainLoader.this, "authorizationToken"))){

                    ValidateToken(MainLoader.this, true);

        //            Intent mainIntent = new Intent(this, MainActivity.class);
        //            finish();
        //            startActivity(mainIntent);

                }else{

                    Intent loginIntent = new Intent(MainLoader.this, LoginActivity.class);
                    finish();
                    startActivity(loginIntent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                }
            }
        }, 1500);



    }
}
