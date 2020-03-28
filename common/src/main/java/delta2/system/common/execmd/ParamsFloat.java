package delta2.system.common.execmd;

public class ParamsFloat implements ICmdParams {
    float value;

    public float GetValue(){
        return value;
    }

    public ParamsFloat(String val){
        value = Float.valueOf(val);
    }
}