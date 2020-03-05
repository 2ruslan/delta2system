package delta2.system.tline.Transport;

import android.content.Context;

import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;

public class LineTransport {
    Context context;

    private static LineApiClient lineApiClient;

    public LineTransport(Context c){
        context = c;
    }

    public void init(){
        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(context, "1653917680");
        lineApiClient = apiClientBuilder.build();
      //  lineApiClient.
    }

    public void destroy(){

    }
}
