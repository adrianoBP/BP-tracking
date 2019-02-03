package pressure.adriano.com;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
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


public class MainActivity extends AppCompatActivity {

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
        views = Arrays.asList(vAddEntry);

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
