/*
 * @brief Camera implementation class, opens the camera, detects face in the frame, sends cropped Face vector to Facial Recognition
 * 
 * @author Carl Negrescu
 * @date 11/15/2024
 */
package cameramodule;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import utils.Resource;
import java.lang.Thread;
import backend.FacialRec;
import java.util.concurrent.*;
//import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class Camera implements Runnable
{
  private VideoCapture _camera;
  private Mat _frame;
  private boolean cont = true;
  private CascadeClassifier _cascade;
  private MatOfRect _faceDetections;
  private Mat _cropFace;
  private BlockingQueue<Mat> _faceQueue;
  private Thread cameraThread;
  
  
  /**
   * @brief Creates a Camera object
   * @param index the Camera index, the default is 0
   */
  public Camera (int index, BlockingQueue<Mat> faceQueue)
  {
    System.out.println("In Camera Constructor");
    _faceQueue = faceQueue;
	  _camera = new VideoCapture(0);
	  System.out.println("VideoCapture Created");
	  _frame = new Mat();
	  _cascade = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_default.xml");
	  _faceDetections = new MatOfRect();
  }

  /**
   * @brief implements the java thread library function
   *        And will open and start detecting faces in the frame, when that happens it will send that to facial recognition for processing
   */
  @Override
  public void run()
  {
    while (cont)
    {
      _camera.read(_frame);
      if (_frame.empty())
      {
        System.out.println("Frame is empty");
        break;
      }
//      Imgproc.cvtColor(_frame, _frame, Imgproc.COLOR_RGB2GRAY);
//      Imgproc.equalizeHist(_frame, _frame);
      _cascade.detectMultiScale(_frame, _faceDetections);
      for (Rect rect : _faceDetections.toArray())
      {
        Imgproc.rectangle(_frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        _cropFace = new Mat(_frame, rect);
        processFaceForRecognition(_cropFace); ///< Sending the processed face for recognition
      }
      
      HighGui.imshow("CameraFeed", _frame);
      if (HighGui.waitKey(30) == 'q')
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
    cameraThread = new Thread(this);
    cameraThread.start();
  }

  public Resource.Result closeCamera()
  {
    Resource.Result result = Resource.Result.RESULT_OK;

    try
    {
      cont = false;
      cameraThread.join(500);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_UNABLE_TO_JOIN_THREAD;
    }
    return result;
  }

  public Resource.Result takePicture()
  {
    return null;
  }
  
  private void processFaceForRecognition(Mat face)
  {
    try
    {
      _faceQueue.put(face);
      System.out.println("Face added for Recogntion");
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      System.out.println("Failed to add queue");
    }
  }
  
}
