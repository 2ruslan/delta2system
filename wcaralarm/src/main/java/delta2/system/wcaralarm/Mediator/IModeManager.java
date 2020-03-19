package delta2.system.wcaralarm.Mediator;

public interface IModeManager {
    void setSpeed(int speed);
    void setAcceleration(float acceleration);
    boolean isSendGps();
    boolean isSendAcc();

    void OnDestroy();
}
