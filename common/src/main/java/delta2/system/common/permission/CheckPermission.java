package delta2.system.common.permission;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Constants;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.IError;

public class CheckPermission implements IAcnivityCallback {
    IError errorHandler;
    Context context;
    ICheckedPermission checkedPermissionHandler;

    public CheckPermission(Context c, IError e){
        errorHandler = e;
        context = c;
    }

    public void Check(ArrayList<String> p){
        try
        {
            CheckPermissionActivity.setActivityCallback(this);

            Intent i = new Intent(context, CheckPermissionActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putStringArrayListExtra(Constants._ALL_PERMISSION, p);
            context.startActivity(i);
        }
        catch (Exception ex){
            errorHandler.OnError(ex);
        }
    }

    public void SetOnChecked(ICheckedPermission h){
        checkedPermissionHandler = h;
    }

    @Override
    public void OnActivityCallback(Intent intent) {
        if (checkedPermissionHandler != null)
            checkedPermissionHandler.OnChecked(intent.getBooleanExtra(Constants._ALL_PERMISSION, false));
    }


    public interface ICheckedPermission{
        void OnChecked(boolean IsOk);
    }
}
