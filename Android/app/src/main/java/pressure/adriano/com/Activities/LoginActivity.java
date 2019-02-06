package pressure.adriano.com.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import pressure.adriano.com.R;

import static pressure.adriano.com.APIHelper.Authorization.GoogleAuthorize;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    public static GoogleSignInClient googleSignInClient;
    SignInButton sibSignIn;
    Integer RC_SIGN_IN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // Link elements
        sibSignIn = findViewById(R.id.sibSignIn);
        sibSignIn.setSize(SignInButton.SIZE_STANDARD);

        // Elements listeners
        sibSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }

    private void SignIn(){
        googleSignInClient.signOut();
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            HandleSignInResult(task);
        }
    }

    private void HandleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleAuthorize(this, account.getId());
//            // Signed in successfully, show authenticated UI.
//            String email = account.getEmail();
//            String email1 = account.getDisplayName();
//            Account email2 = account.getAccount();
//            String email3 = account.getId();
//            String email4 = account.getIdToken();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LOGIN.RESULT", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
