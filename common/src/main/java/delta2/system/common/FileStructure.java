package delta2.system.common;

import android.content.Context;
import android.os.Environment;

public class FileStructure {

    private static Context context;

    public static String GetLogPathDir(){
        return Environment.getExternalStorageDirectory() + "/delta2system/logs/";
    }

}


/*
if (Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState()))
                        dir =  String.format("%s/", Environment.getExternalStorageDirectory () );
                    else
                        dir =  String.format("%s/", getWorkDir() );

 */