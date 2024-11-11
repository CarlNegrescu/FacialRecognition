package cougarCam;

import cameramodule.Camera;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import utils.Resource;
import java.lang.Thread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;



public class Main
{
  public static void main(String[] args)
  {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    VideoCapture capture = new VideoCapture(0);
    Mat frame = new Mat();
    while (true)
    {
      capture.read(frame);
      HighGui.imshow(null, frame);
      if(HighGui.waitKey(1) == 'q')
        break;
      
    }
  }
}