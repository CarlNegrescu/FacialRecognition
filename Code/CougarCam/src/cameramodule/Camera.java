/**
 * @brief Manages all camera operations, including video capture and face detection.
 * <p>
 * This class runs on a dedicated thread to continuously read frames from a webcam.
 * For each frame, it uses an OpenCV {@link CascadeClassifier} to detect faces.
 * Any detected faces are cropped and placed into a {@link BlockingQueue} for the
 * facial recognition module to process.
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
  
  private final Object        lock;
  private VideoCapture        _camera;
  private Mat                 _frame;
  private boolean             cont = true;
  private CascadeClassifier   _cascade;
  private MatOfRect           _faceDetections;
  private Mat                 _cropFace;
  private BlockingQueue<Mat>  _faceQueue;
  private Thread              cameraThread;
  private Boolean             isRunning = false;
 
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
   * The main execution loop for the camera thread.
   * <p>
   * Continuously reads frames from the camera, performs face detection,
   * displays the video feed with detected faces highlighted, and sends
   * cropped faces to the recognition queue.
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
   * Starts the camera thread to begin capturing video.
   *
   * @param camera The {@link VideoCapture} object to use for reading frames.
   * @return A {@link Resource.Result} indicating the outcome of the operation.
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

 /**
  * Stops the camera thread gracefully and releases resources.
  *
  * @return A {@link Resource.Result} indicating the outcome of the shutdown.
  */
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
  
  /**
   * Places a cropped face image onto the processing queue.
   *
   * @param face The {@link Mat} object representing the cropped face.
   */
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
  
  /**
   * Releases all associated OpenCV resources to prevent memory leaks.
   */
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
