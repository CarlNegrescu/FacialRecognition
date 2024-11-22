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
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import utils.Resource;
import java.lang.Thread;
import java.util.concurrent.*;

public class Camera implements Runnable
{
  private final Object lock;
  private VideoCapture _camera;
  private Mat _frame;
  private boolean cont = true;
  private CascadeClassifier _cascade;
  private MatOfRect _faceDetections;
  private Mat _cropFace;
  private BlockingQueue<Mat> _faceQueue;
  private Thread cameraThread;
  private Boolean isRunning = false;
  
  
  
  /**
   * @brief Creates a Camera object
   * @param index the Camera index, the default is 0
   */
  public Camera (BlockingQueue<Mat> faceQueue)
  {
    lock = new Object();
    System.out.println("In Camera Constructor");
    _faceQueue       = faceQueue;
	  System.out.println("VideoCapture Created");
	  _frame           = new Mat();
	  _cascade         = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_default.xml");
	  _faceDetections  = new MatOfRect();
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
      _cascade.detectMultiScale(_frame, _faceDetections);
      for (Rect rect : _faceDetections.toArray())
      {
        Imgproc.rectangle(_frame, new Point(rect.x, rect.y), 
                          new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        _cropFace = new Mat(_frame, rect);
        processFaceForRecognition(_cropFace); ///< Sending the processed face for recognition
        _cropFace.release();
      }
      HighGui.imshow("CameraView", _frame);
      HighGui.waitKey(1);
      
      }
    HighGui.destroyAllWindows();
    releaseResources();
  }

  /**
   * @brief Starts the thread running the camera module

   * @return none
   */
 public Resource.Result openCamera(VideoCapture camera)
  {
   Resource.Result result = Resource.Result.RESULT_OK;
   synchronized (lock)
   {
     if (isRunning)
     {
       System.out.println("Camera is already running");
       result = Resource.Result.RESULT_ALREADY_OPEN;
     }
      _camera = camera;
      isRunning = true;
   }
   cameraThread = new Thread(this, "Camera Thread");
   cameraThread.start();
   return result;
  }

  public Resource.Result closeCamera()
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    synchronized (lock)
    {
      if(!isRunning)
      {
        System.out.println("Camera is already Closed");
        return Resource.Result.RESULT_ALREADY_CLOSED;
      }
      cont = false;
      isRunning = false;
    }
    
    try
    {
      if(cameraThread != null)
      {
        System.out.println("Camera Joined Successfully");
        cameraThread.join();
        result = Resource.Result.RESULT_JOINED_THREAD;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_UNABLE_TO_JOIN_THREAD;
    }
    return result;
  }
  
  private void processFaceForRecognition(Mat face)
  {
    try
    {
      _faceQueue.put(face.clone());
      System.out.println("Face added for Recogntion");
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      System.out.println("Failed to add queue");
    }
  }
  
  private void releaseResources() 
  {
	if (_camera != null && _camera.isOpened()) 
	{
	  _camera.release();
	  System.out.println("Camera resources released.");
	}
	if (_frame != null) 
	{
	_frame.release();
	}
	System.out.println("All resources released.");
  }

}
