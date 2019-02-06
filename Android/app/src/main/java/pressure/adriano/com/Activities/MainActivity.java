package pressure.adriano.com.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

import pressure.adriano.com.R;

import static pressure.adriano.com.APIHelper.Authorization.Logout;


public class MainActivity extends AppCompatActivity {

    // TODO: Google auth / session
    // TODO: language pack (from Bituci)
    // TODO: Graph filter + select date range

    View vAddEntry, vGraph;
    LinearLayout cMainLayout;
    List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Views init
        vAddEntry = getLayoutInflater().inflate(R.layout.layout_add_entry, null);
        vGraph = getLayoutInflater().inflate(R.layout.layout_graph, null);
        views = Arrays.asList(vAddEntry, vGraph);

        // Items Init
        cMainLayout = findViewById(R.id.cMainLayout);

        // Start main view
        new AddEntry(this, vAddEntry);
        changeView(vAddEntry);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.iAddEntry:
                new AddEntry(this, vAddEntry);
                changeView(vAddEntry);
                return true;
            case R.id.iGraph:
                new Graph(this, vGraph);
                changeView(vGraph);
                return true;
            case R.id.iLogout:
                new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Do you really want to log out?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Logout(MainActivity.this);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.ic_logout)
                    .show();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);

        return true;
    }

    private void changeView(View view) {  // Delete all views and add specified one
        for (View v : views) {
            cMainLayout.removeView(v);
        }
        try {
            cMainLayout.addView(view);
        } catch (Exception ex) {
            Log.e("MAIN.CHANGEVIEW", ex.getMessage());
        }
    }
}
