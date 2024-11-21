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

public class CameraUi implements Runnable
{
  private final Object lock;
  private VideoCapture _camera;
  private Mat _frame;
  private boolean cont = true;
  private CascadeClassifier _cascade;
  private MatOfRect _faceDetections;
  private Mat _cropFace;
  private Thread cameraThread;
  private Boolean isRunning = false;
  
  
  
  /**
   * @brief Creates a Camera object
   * @param index the Camera index, the default is 0
   */
  public CameraUi ()
  {
    lock = new Object();
    System.out.println("In Camera UI Constructor");
    System.out.println("Camera UI VideoCapture Created");
    _frame           = new Mat();
    _cascade         = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_default.xml");
    _faceDetections  = new MatOfRect();
  }

  /**
   * @brief implements the java thread library function
   *        And will open and start detecting faces in the frame, When the User pressing the key to capture the frame, a cropped face is sent to the database to be added
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
        ///if take pic is pressed
        /// save cropped face
        ///exit loop
        /// send to Manager Class
        _cropFace.release();
      }
      HighGui.imshow("CameraView", _frame);
      if (HighGui.waitKey(30) == 'q')
      {
        System.out.println("Exit Key detected");
      break;
      }
    }
  
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
   cameraThread = new Thread(this, "CameraUI Thread");
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
        result = Resource.Result.RESULT_ALREADY_CLOSED;
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
  
  
  private void releaseResources() {
    if (_camera != null && _camera.isOpened()) {
        _camera.release();
        System.out.println("Camera resources released.");
    }
    if (_frame != null) {
        _frame.release();
    }
    HighGui.destroyAllWindows();
    System.out.println("All resources released.");
}
  
}
