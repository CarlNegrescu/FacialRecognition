package cameraModule;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import interfaces.ICamera;
import utils.Resource;

public class frontdoorCamera implements ICamera
{
  @Override
  public Resource.Result init()
  {

  }

  @Override
  public Resource.Result openCamera(String windowName)
  {
    return null;
  }

  @Override
  public Resource.Result closeCamera()
  {
    return null;
  }

  @Override
  public Resource.Result takePicture()
  {
    return null;
  }
}
