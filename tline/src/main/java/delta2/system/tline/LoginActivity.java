package delta2.system.tline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.LoginListener;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.linecorp.linesdk.widget.LoginButton;

import java.util.Arrays;

import delta2.system.common.Constants;
import delta2.system.common.Log.L;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.tline.Preferences.PreferencesHelper;

public class LoginActivity extends Activity {

    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
   // LoginButton loginButton;

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     //   init();
        init2();
    }

    /**/
    private static final int REQUEST_CODE = 1;
    private void init2() {
        loginButton = findViewById(R.id.tln_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    // App-to-app login
                    Intent loginIntent = LineLoginApi.getLoginIntent(
                            view.getContext(),
                            "1653917680",
                            new LineAuthenticationParams.Builder()
                                    .scopes(Arrays.asList(Scope.PROFILE, Scope.FRIEND, Scope.MESSAGE))
                                    // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                                    .build());
                    startActivityForResult(loginIntent, REQUEST_CODE);

                } catch (Exception e) {
                    L.log.error("", e);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            L.log.error ("ERROR", "Unsupported Request");
            return;
        }

        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);

        switch (result.getResponseCode()) {

            case SUCCESS:
                String accessToken = result.getLineCredential().getAccessToken().getTokenString();
                PreferencesHelper.setToken(accessToken);

                PreferencesHelper.setUserID(result.getLineProfile().getUserId());

                callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, true));
                break;

            case CANCEL:
                callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, false));
                L.log.debug("ERROR", "LINE Login Canceled by user.");
                break;

            default:
                callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, false));
                L.log.debug("ERROR", "Login FAILED!");
                L.log.debug("ERROR", result.getErrorData().toString());
        }
    }

    /**/
/*
    private void init() {
        loginButton = findViewById(R.id.line_login_btn);

        loginButton.setChannelId("1653917680");

        loginButton.enableLineAppAuthentication(true);

        loginButton.setAuthenticationParams(new LineAuthenticationParams.Builder()
                .scopes(Arrays.asList(Scope.PROFILE, Scope.MESSAGE, Scope.FRIEND))
                // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                .build()
        );
        loginButton.setLoginDelegate(loginDelegate);
        loginButton.addLoginListener(new LoginListener() {
            @Override
            public void onLoginSuccess(@NonNull LineLoginResult result) {
                String accessToken = result.getLineCredential().getAccessToken().getTokenString();
                PreferencesHelper.setToken(accessToken);

                PreferencesHelper.setUserID(result.getLineProfile().getUserId());

                callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, true));

            }

            @Override
            public void onLoginFailure(@Nullable LineLoginResult result) {
                callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, false));
            }
        });
    }
*/
    static IAcnivityCallback callback;

    public static void init(IAcnivityCallback c){
        callback = c;
    }

    public static void destroy(){
        callback = null;
    }


}
