package cameramodule;
import backend.FacialRec;
import java.util.concurrent.*;
import utils.Resource;


public class CameraModule implements Runnable
{
  private FacialRec recognizer;
  private Camera camera;
  private Boolean cont = true;
  private Resource inputFace;
  private Thread cameraMThread;
  
  
  public CameraModule ()
  {
    camera = new Camera(0);
    recognizer = new FacialRec();
  }
  
  public void displayMessage(Resource result)
  {
    ///TODO: ADD THE IMPLEMENTATION OF DISPLAYING MESSAGES
  }
  
  @Override
  public void run()
  {
    inputFace = new Resource();
    camera.openCamera();
    recognizer.startFacialRec();
    while (cont)
    {
      try
      {
        inputFace = FacialRec.completedQueue.take();
        displayMessage(inputFace);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      if (!cont)
      {
        camera.closeCamera();
        recognizer.stopFacialRec();
      }
      
    }
  }
  
  public void startCameraModule()
  {
    cameraMThread = new Thread(this);
    cameraMThread.start();
  }
  
  
  public Resource.Result stopCameraModule()
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    try
    {
      cont = false;
      cameraMThread.join();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_UNABLE_TO_JOIN_THREAD;
    }
    return result;
  }
  
  
  
  
  
  
  
}
