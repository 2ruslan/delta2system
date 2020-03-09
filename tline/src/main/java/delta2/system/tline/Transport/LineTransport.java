package delta2.system.tline.Transport;

import android.content.Context;

import com.linecorp.linesdk.LineAccessToken;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.linecorp.linesdk.message.MessageData;
import com.linecorp.linesdk.message.TextMessage;

import java.util.ArrayList;
import java.util.List;

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
        test();
    }

    private void test(){

        List<MessageData> data = new ArrayList<>();

        TextMessage t = new TextMessage("test");

        data.add(t);

        lineApiClient.sendMessage("ruha", data  );
    }


    public void destroy(){

    }
}
