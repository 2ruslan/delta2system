package delta2.system.ttelegram.transporttelegram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import delta2.system.common.Constants;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.ttelegram.R;
import delta2.system.ttelegram.transporttelegram.Preferences.PreferencesHelper;
import delta2.system.ttelegram.transporttelegram.Transport.TelegramTransport;


public class LoginActivity extends Activity {
    public final static String _LOGIN_PARAM = "LOGIN_PARAM";
    public final static String _PARAM_PHONE = "_PARAM_PHONE";
    public final static String _PARAM_TCODE = "_PARAM_CODE_TELEGRAM";
    public final static String _PARAM_UCODE = "_PARAM_CODE_USER";
    public final static String _PARAM_PASSWORD = "_PARAM_PASSWORD";
    public final static String _PARAM_UCODE_VAL = "_PARAM_CODE_USER_VAL";
    public final static String _PARAM_PASSHINT_VAL = "_PARAM_PASSHINT_VAL";


    EditText edPhone;
    EditText edCode;
    EditText edPass;

    Button btNext1;
    Button btNext2;
    Button btPass;

    LinearLayout llPhone;
    LinearLayout llCode;
    LinearLayout llCodeUser;
    LinearLayout llPassword;

    static LoginActivity _LoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _LoginActivity = this;

        setContentView(R.layout.activity_telegram_login);

        ImageView logo  = findViewById(R.id.ivLogo);
        logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        edPhone = findViewById(R.id.etPhoneNum);
        edCode = findViewById(R.id.etCode);
        edPass = findViewById(R.id.edPass);
        btNext1 = findViewById(R.id.btNext1);
        btNext2 = findViewById(R.id.btNext2);

        edPhone.setText(PreferencesHelper.getPhoneNum());

        llPhone = findViewById(R.id.llPhone);
        llCode = findViewById(R.id.llCode);
        llCodeUser = findViewById(R.id.llCodeUser);
        llPassword = findViewById(R.id.llPassword);


        Intent intent = getIntent();
        String mode = intent.getStringExtra(_LOGIN_PARAM);


        if(mode.equals(_PARAM_PHONE)){
            llCode.setVisibility(View.GONE);
            llCodeUser.setVisibility(View.GONE);
            llPassword.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.tvRegInfo)).setText(getText(R.string.phone_need));
        }
        else if(mode.equals(_PARAM_TCODE)){
            llPhone.setVisibility(View.GONE);
            llCodeUser.setVisibility(View.GONE);
            llPassword.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.tvRegInfo)).setText(getText(R.string.code_need));
        }
        else if(mode.equals(_PARAM_UCODE)){
            llPhone.setVisibility(View.GONE);
            llCode.setVisibility(View.GONE);
            btNext1.setVisibility(View.GONE);
            btNext2.setVisibility(View.GONE);
            llPassword.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.chkCode)).setText(intent.getStringExtra(_PARAM_UCODE_VAL));
            ((TextView)findViewById(R.id.tvRegInfo)).setText(getText(R.string.code_need));
        }
        else if(mode.equals(_PARAM_PASSWORD)){
            llPhone.setVisibility(View.GONE);
            llCode.setVisibility(View.GONE);
            llCodeUser.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.tvRegInfo)).setText(intent.getStringExtra(_PARAM_PASSHINT_VAL));
        }
    }


    public void ConnectClick(View v){
        PreferencesHelper.SetPhoneNum(edPhone.getText().toString());
        PreferencesHelper.code = edCode.getText().toString();

        telegramTransport.connect();
    }

    public void PasswordClick(View v){

        PreferencesHelper.pass = edPass.getText().toString();

        telegramTransport.connect();
    }

    public static void close(){
        telegramTransport = null;

        if(_LoginActivity != null)
            _LoginActivity.finish();

    }

    static TelegramTransport telegramTransport;
    public static void SetTransport(TelegramTransport t){
        telegramTransport = t;
    }

}
