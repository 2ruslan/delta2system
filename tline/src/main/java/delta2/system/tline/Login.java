package delta2.system.tline;

import android.content.Context;
import android.content.Intent;

import delta2.system.common.Constants;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.IError;
import delta2.system.tline.Preferences.PreferencesHelper;

public class Login {
    IError errorHandler;
    Context context;
    ILoginned loginedHandler;

    public Login(Context c, IError e){
        errorHandler = e;
        context = c;
    }

    public void SetLoginedHandler(ILoginned h){
        loginedHandler = h;
    }

    public void Check(){
        if ( PreferencesHelper.getToken() == null || PreferencesHelper.getToken().equals(""))
            try {
                LoginActivity.init(
                        new IAcnivityCallback() {
                            @Override
                            public void OnActivityCallback(Intent intent) {
                                loginedHandler.OnLogin(intent.getBooleanExtra(Constants._LOGIN_AND_START, false));
                                LoginActivity.destroy();
                            }
                        }
                );
                context.startActivity(new Intent(context, LoginActivity.class));
            }
            catch (Exception ex){
                errorHandler.OnError(ex);
                loginedHandler.OnLogin(false);
            }
            finally {

            }
        else
            loginedHandler.OnLogin(true);
    }

    public interface ILoginned{
        void OnLogin(boolean IsOk);
    }
}
