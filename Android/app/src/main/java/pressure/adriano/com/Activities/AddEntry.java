package pressure.adriano.com.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pressure.adriano.com.Classes.PressureEntry;
import pressure.adriano.com.R;

import static pressure.adriano.com.APIHelper.Pressure.SendPressureData;
import static pressure.adriano.com.Helpers.Util.CalculateAverage;
import static pressure.adriano.com.Helpers.Util.CreateBasicSnack;
import static pressure.adriano.com.Helpers.Util.HideKeyboard;

public class AddEntry extends AppCompatActivity {

    // Class elements
    Context context;
    View view;

    // View elements
    EditText etSystole, etDiastole, etBpm;
    Button bSendData;

    public AddEntry(final Context context, View view){

        this.context = context;
        this.view = view;

        // Components init
        etSystole = view.findViewById(R.id.etSystole);
        etDiastole = view.findViewById(R.id.etDiastole);
        etBpm = view.findViewById(R.id.etBpm);
        bSendData = view.findViewById(R.id.bSendData);


        // Listeners init
        bSendData.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!etSystole.getText().toString().isEmpty() && !etDiastole.getText().toString().isEmpty() && !etBpm.getText().toString().isEmpty()) {
                    HideKeyboard(((Activity)context).getCurrentFocus(), context);

                    SendPressureData(new PressureEntry(
                            null,
                            CalculateAverage(etSystole.getText().toString()),
                            CalculateAverage(etDiastole.getText().toString()),
                            CalculateAverage(etBpm.getText().toString()),
                            null
                    ), context);

                    etSystole.setText(CalculateAverage(etSystole.getText().toString()).toString());
                    etDiastole.setText(CalculateAverage(etDiastole.getText().toString()).toString());
                    etBpm.setText(CalculateAverage(etBpm.getText().toString()).toString());

                }else{
                    CreateBasicSnack("Please make sure all the fields are not empty.", null, context);
                }
            }
        });

    }


}
