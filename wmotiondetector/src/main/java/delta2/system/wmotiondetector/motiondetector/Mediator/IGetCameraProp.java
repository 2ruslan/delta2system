package delta2.system.wmotiondetector.motiondetector.Mediator;

import delta2.system.wmotiondetector.motiondetector.Detector.CamearaProps;

public interface IGetCameraProp {
    void SendCameraProp(String prop);

    CamearaProps GetCameraProps(String prop);
}
