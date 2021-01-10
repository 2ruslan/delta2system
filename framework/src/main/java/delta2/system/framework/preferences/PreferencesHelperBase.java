package delta2.system.framework.preferences;

import android.content.Context;

public class PreferencesHelperBase {
    protected static Context context;
    public static void SetContext(Context c){
        context = c;
    }

}
