package delta2.system.common.execmd;

import delta2.system.common.execmd.ICmdParams;

public class ParamsOnOff implements ICmdParams {
    boolean isOn;

    public boolean GetIsOn(){
        return isOn;
    }
    public ParamsOnOff(String val){
        if (val.equals("on"))
            isOn = true;
        else if (val.equals("off"))
            isOn = false;
        else
            throw new IllegalArgumentException();
    }
}