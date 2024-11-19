/*
 * @brief Manager Class of the CameraModule and the Facial Recognition class
 * 
 * @author Carl Negrescu
 * @data 11/15/2024
 */
package cougarCam;

import backend.FacialRec;
import backend.IDataAccess;
import cameramodule.Camera;
import java.util.concurrent.ArrayBlockingQueue;
import utils.Resource;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

import backend.TestDataAccess;

public class DoorManager implements Runnable
{
  public static final ArrayBlockingQueue<Mat> faceQueue = new ArrayBlockingQueue<Mat>(5);
  public static final ArrayBlockingQueue<Resource> completedQueue = new ArrayBlockingQueue<Resource>(5);
  
  private FacialRec   _recognizer;
  private Camera      _camera;
  private Thread      _doorThread;
  private Boolean     _cont = true;
  private Resource    _result;
  private IDataAccess _dataAccessObject;
  private VideoCapture  _videoCapture;
  
  /*
   * @brief Constructor creates the camera and facial Recognition objects
   * 
   */
  public DoorManager(IDataAccess dao)
  {
    System.out.println("In DoorManagerConstructor");
    _dataAccessObject = dao;
    _videoCapture= new VideoCapture(0);
    _camera = new Camera(faceQueue);
    _recognizer = new FacialRec(faceQueue, completedQueue, _dataAccessObject);
    System.out.println("Done with DoorManager Constructor");
  }
  
  /// @brief starts the doorManager thread
  public void startDoorManager()
  {
    _doorThread = new Thread(this, "DoorManager");
    _doorThread.start();
  }
  
  public Resource.Result stopDoorManager()
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    result = _recognizer.stopFacialRec();
    result = _camera.closeCamera();
   if (result != Resource.Result.RESULT_OK)
   {
     return result;
   }
    try
    {
      _cont = false;
      if (_doorThread != null)
      {
        _doorThread.join();
      }
      System.out.println("Door Thread Stopped");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_UNABLE_TO_JOIN_THREAD;
    }
    
    if(_videoCapture.isOpened())
    {
      _videoCapture.release();
      System.out.println("VideoCapture released");
    }
    
    return result;
  }
  
  private void displayMessage(Resource result)
  {
    if (result.validFace)
    {
      System.out.println("User Recognized");
    }
    else
    {
      System.out.println("Who are you");
    }
  }
  
  
  /*
   * @brief point of entry of the thread, responsible of managing the output of the facialRecognition object
   */
  @Override
  public void run()
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    result = _camera.openCamera(_videoCapture);
    _recognizer.startFacialRec();
    while (_cont)
    {
        try
        {
          _result = completedQueue.take();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        displayMessage(_result);
    }
  }
}
