package pressure.adriano.com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static pressure.adriano.com.APIHelper.Pressure.SendPressureData;
import static pressure.adriano.com.Util.HideKeyboard;

public class AddEntry extends AppCompatActivity {

    // Class elements
    Context context;
    View view;

    // View elements
    EditText etSystole, etDiastole;
    Button bSendData;

    public AddEntry(final Context context, View view){

        this.context = context;
        this.view = view;

        // Components init
        etSystole = view.findViewById(R.id.etSystole);
        etDiastole = view.findViewById(R.id.etDiastole);
        bSendData = view.findViewById(R.id.bSendData);


        // Listeners init
        bSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSystole.getText().toString().isEmpty() && !etDiastole.getText().toString().isEmpty()) {
                    HideKeyboard(((Activity)context).getCurrentFocus(), context);
                    SendPressureData(etSystole.getText().toString(), etDiastole.getText().toString(), context);
                }
            }
        });

    }
}
