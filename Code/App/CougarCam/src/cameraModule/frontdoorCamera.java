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
  private VideoCapture camera;
  private Mat frame;


  @Override
  public Resource.Result init()
  {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    camera = new VideoCapture(0);
    frame = new Mat();
    return Resource.Result.RESULT_OK;
  }

  @Override
  public Resource.Result openCamera(String windowName)
  {
    camera.open(0);
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
