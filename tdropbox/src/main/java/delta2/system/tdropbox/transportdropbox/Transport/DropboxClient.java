package delta2.system.tdropbox.transportdropbox.Transport;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class DropboxClient {

    public static DbxClientV2 getClient(String ACCESS_TOKEN) {

        DbxRequestConfig config = new DbxRequestConfig("dropbox/delta-d");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        return client;
    }
}
