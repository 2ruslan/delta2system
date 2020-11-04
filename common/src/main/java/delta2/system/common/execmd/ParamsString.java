package delta2.system.common.execmd;

public class ParamsString implements ICmdParams {
    String value;

    public String GetValue(){
        return value;
    }

    public ParamsString(String val){
        value = val;
    }
}