package delta2.system.delta2system;

public class InfoData {
    private static int send = 0;
    private static int receive = 0;

    public static void PlusSend(){
        send++;
    }

    public static void PlusReceive(){
        receive++;
    }

    public static int GetSend(){
        return send;
    }

    public static int GetReceive(){
        return receive;
    }
}
