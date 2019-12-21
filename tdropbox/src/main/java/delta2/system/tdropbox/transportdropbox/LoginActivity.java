package delta2.system.tdropbox.transportdropbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dropbox.core.android.Auth;

import delta2.system.common.Constants;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.tdropbox.R;
import delta2.system.tdropbox.transportdropbox.Preferences.DropboxPreferences;
import delta2.system.tdropbox.transportdropbox.Preferences.PreferencesHelper;

public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox_login);

        ImageView logo  = (ImageView) findViewById(R.id.ivLogo);
        logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        Intent returnIntent = new Intent();
        setResult(AppCompatActivity.RESULT_CANCELED, returnIntent);
    }

    public void onClick(View view) {
        Auth.startOAuth2Authentication(LoginActivity.this, DropboxPreferences.APP_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken();
    }

    public void getAccessToken() {
        String accessToken = Auth.getOAuth2Token(); //generate Access Token
        if (accessToken != null) {
            PreferencesHelper.setToken(accessToken);

            callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, true));

            finish();
        }

    }

    static IAcnivityCallback callback;

    public static void init(IAcnivityCallback c){
        callback = c;
    }

    public static void destroy(){
        callback = null;
    }

}
