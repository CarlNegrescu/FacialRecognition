package cameramodule;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import utils.Resource;
import java.lang.Thread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class Camera extends Thread
{
  private VideoCapture _camera;
  private Mat _frame;
  private boolean cont = true;
  private CascadeClassifier _cascade;
  private MatOfRect _faceDetections;
  private Mat _cropFace;

  /**
   * @brief Creates a Camera object
   * @param index the Camera index, the default is 0
   */
  public Camera (int index)
  {
	  _camera = new VideoCapture(0);
	  _frame = new Mat();
	  _cascade = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_alt.xml");
	  System.out.print("Created the Classifer");
	  _faceDetections = new MatOfRect();
	  _cropFace = new Mat();

   
  }
  
  
  public void runCamera(VideoCapture cap)
  {
	  while (cont)
	  {
		  _camera.read(_frame);
		

		  if (_frame.empty())
		  {
			  System.out.println("Frame is empty");
			  break;
		  }
//		  Imgproc.cvtColor(_frame, _frame, Imgproc.COLOR_RGB2GRAY);
//		  Imgproc.equalizeHist(_frame, _frame);
		  _cascade.detectMultiScale(_frame, _faceDetections);
		  
		  for (Rect rect : _faceDetections.toArray())
		  {
			  Imgproc.rectangle(_frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
			  _cropFace = new Mat(_frame, rect);
			  ///send to Facial Rec
			  ///Then Delete object and repeat
		  }
		  HighGui.imshow("CameraFeed", _frame);
		  if (HighGui.waitKey(MAX_PRIORITY) == 'q')
		  {
			  _camera.release();
			  break;
		  }
	  }
	  HighGui.destroyAllWindows();
	  }

  /**
   * @brief implements the java thread library function
   */
  public void run()
  {
    while (cont)
    {
      _camera.read(_frame);
      System.out.println("Read in Frame");
      if (_frame.empty())
      {
        System.out.println("Frame is empty");
        break;
      }
//      Imgproc.cvtColor(_frame, _frame, Imgproc.COLOR_RGB2GRAY);
//      Imgproc.equalizeHist(_frame, _frame);
      _cascade.detectMultiScale(_frame, _faceDetections);
      System.out.println("Detected Face");
      for (Rect rect : _faceDetections.toArray())
      {
        Imgproc.rectangle(_frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        _cropFace = new Mat(_frame, rect);
        ///send to Facial Rec
        ///Then Delete object and repeat
      }
      HighGui.imshow("CameraFeed", _frame);
      if (HighGui.waitKey(MAX_PRIORITY) == 'q')
	  {
    	  System.out.println("Exit Key detected");
		  _camera.release();
		  break;
	  }
    }
    HighGui.destroyAllWindows();
    System.out.println("Destroying Windows");
  }

  /**
   * @brief Starts the thread running the camera module

   * @return none
   */
 public void openCamera()
  {
    this.start();
  }

  public Resource.Result closeCamera()
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    cont = false;
    try
    {
      this.join();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_FAILED;
    }
    return result;
  }

  public Resource.Result takePicture()
  {
    return null;
  }
}
