package pressure.adriano.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static pressure.adriano.com.APIHelper.Pressure.SendPressureData;
import static pressure.adriano.com.Util.*;

public class MainActivity extends AppCompatActivity {

    EditText etSystole, etDiastole;
    Button bSendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Components init
        etSystole = findViewById(R.id.etSystole);
        etDiastole = findViewById(R.id.etDiastole);
        bSendData = findViewById(R.id.bSendData);

        // Listeners init
        bSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(MainActivity.this.getCurrentFocus(), MainActivity.this);
                SendPressureData(etSystole.getText().toString(), etDiastole.getText().toString(), MainActivity.this);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.item1:
//                Toast.makeText(this, "Item1", Toast.LENGTH_SHORT).show();
//                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        return true;
    }
}
