package backend;

import org.opencv.core.*;
import org.opencv.objdetect.FaceRecognizerSF;
import utils.Resource;
import java.lang.Thread;
import java.util.concurrent.*;

public class FacialRec implements Runnable
{
  public static final BlockingQueue<Mat> faceQueue = new LinkedBlockingQueue<>();
  public static final BlockingQueue<Resource> completedQueue = new LinkedBlockingQueue<>();
  private static final double COSINE_SIMILAR_THREASHOLD = 0.363;
  private static final double L2NORM_SIMILAR_THRESHOLD  = 1.128;
  
  private FaceRecognizerSF faceRecognizer;
  private Thread facRecThread;
  private Boolean cont = true;
  private Mat inputFace;
  
  public FacialRec()
  {
    faceRecognizer = FaceRecognizerSF.create("resources/face_recognition_sface_2021dec.onnx", "");
  }
  
  @Override
  public void run(  )
  {
    while (cont)
    {
      try
      {
        inputFace = FacialRec.faceQueue.take();
        FacialRec.completedQueue.put(recognizeFace(inputFace));
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
        System.out.println("Recognition Thread Interrupted" + e.getMessage());
      }
    }
  }
  
  private Resource recognizeFace(Mat face)
  {
    Resource result = new Resource();
    ///Pull faces from data base, and compare with the given faces?
    /// using the faces DAO
    
    Mat faceRecognizeFeatures = new Mat();
    Mat faceDBFeatures = new Mat();
    faceRecognizer.feature(face, faceRecognizeFeatures);
    faceRecognizeFeatures= faceRecognizeFeatures.clone();
    //TODO: Add the recognizer from DB
    //faceRecognizer.feature(TODO:!!FACEFROMDB!!, faceDBFeatures);
    //faceDBFeatures = faceDBFeatures.clone();
    
    ///<getting the cosine similarity
    double cosineMatch = faceRecognizer.match(faceRecognizeFeatures, faceDBFeatures, FaceRecognizerSF.FR_COSINE);
    ///<getting the l2norm
    double l2Match = faceRecognizer.match(faceRecognizeFeatures, faceDBFeatures, FaceRecognizerSF.FR_NORM_L2);
    
    if(cosineMatch >= COSINE_SIMILAR_THREASHOLD && l2Match >= L2NORM_SIMILAR_THRESHOLD)
    {
      result.validFace = true;
      //<result.firstName = whatever the name is
      //<result.lastName = lastname
      //< result.id unique identifer
      result.user = Resource.Result.RESULT_USER_RECOGNIZED;
    }
    else
    {
      result.validFace  = false;
      result.firstName  = null;
      result.lastName   = null;
      result.user       = Resource.Result.RESULT_USER_NOT_RECOGNIZED;
      result.id         = 0;
    }
     
    return result;
  }
  
  public void startFacialRec()
  {
    facRecThread = new Thread(this);
    facRecThread.start();
  }
  
  public Resource.Result stopFacialRec()
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    try
    {
      cont = false;
      facRecThread.join();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_UNABLE_TO_JOIN_THREAD;
    }
    return result;
    
  }
  
  
}
