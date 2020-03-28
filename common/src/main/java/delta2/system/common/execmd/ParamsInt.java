package delta2.system.common.execmd;

public class ParamsInt implements ICmdParams {
    int value;

    public int GetValue(){
        return value;
    }

    public ParamsInt(String val){
        value = Integer.valueOf(val);
    }
}