package delta2.system.tline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.LoginListener;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.linecorp.linesdk.widget.LoginButton;

import java.util.Arrays;

import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.tline.Preferences.PreferencesHelper;

public class LoginActivity extends Activity {

    private static final int REQUEST_CODE = 1;

    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
    LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }


    private void init() {
        loginButton = findViewById(R.id.line_login_btn);

        loginButton.setChannelId("1653917680");

        loginButton.enableLineAppAuthentication(true);

        loginButton.setAuthenticationParams(new LineAuthenticationParams.Builder()
                .scopes(Arrays.asList(Scope.PROFILE))
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

            }

            @Override
            public void onLoginFailure(@Nullable LineLoginResult result) {
                // bad
            }
        });
    }

    static IAcnivityCallback callback;

    public static void init(IAcnivityCallback c){
        callback = c;
    }

    public static void destroy(){
        callback = null;
    }


}
