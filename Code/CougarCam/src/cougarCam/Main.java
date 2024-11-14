package cougarCam;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import cameramodule.Camera;


public class Main
{
  public static void main(String[] args)
  {
	  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	  System.out.println("Starting Camera");
	  Camera cam = new Camera(0);
	  cam.openCamera();

  }
//    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    VideoCapture _camera = new VideoCapture(0);
//    Mat _frame = new Mat();
//    CascadeClassifier _cascade = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_alt.xml");
//    MatOfRect _faceDetections = new MatOfRect();
//    Mat _cropFace = new Mat();
//    
//    
//    
////    
//      while (true)
//      {
//    	_camera.read(_frame);
////
//      if (_frame.empty())
//      {
//        System.out.println("Frame is empty");
//        break;
//      }
//      _cascade.detectMultiScale(_frame, _faceDetections);
//
//      for (Rect rect : _faceDetections.toArray())
//      {
//        Imgproc.rectangle(_frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
//        _cropFace = new Mat(_frame, rect);
//        ///send to Facial Rec
//        ///Then Delete object and repeat
//      }
//    	HighGui.imshow("CameraFeed", _frame);
//    	if (HighGui.waitKey(1) == 'q') 
//    	{
//    		_camera.release();
//    		break;
//    	}
//      }
//      HighGui.destroyAllWindows();
//    
//  }
}