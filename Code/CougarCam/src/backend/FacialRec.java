/*
 * @brief Facial Recognition Class, responisble for taking in faces from the message queue and 
 * accessing the database to find any matching faces
 * 
 * @author Carl Negrescu
 * @date 11/16/2024
 */
package backend;

import org.opencv.core.*;
import org.opencv.objdetect.FaceRecognizerSF;
import utils.Resource;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import backend.IDataAccess;

public class FacialRec implements Runnable
{
  private static final double COSINE_SIMILAR_THREASHOLD = 0.050; ///0.363 /// Good camera is required ! Otherwise lower the threshold
  private static final double L2NORM_SIMILAR_THRESHOLD  = 1.128; ///1.128
  private BlockingQueue<Mat> _faceQueue;
  private BlockingQueue<Resource> _completedQueue;
  private IDataAccess _dataObject;
  private FaceRecognizerSF faceRecognizer;
  private Thread facRecThread;
  private Boolean cont = true;
  private Mat inputFace;
  private List<Resource> users;
  
  public FacialRec(BlockingQueue<Mat> faceQueue, BlockingQueue<Resource> completedQueue, IDataAccess dataObject)
  {
    System.out.println("In FacialRec Constructor");
    _dataObject     = dataObject;
    _faceQueue      = faceQueue;
    _completedQueue = completedQueue;
    faceRecognizer  = FaceRecognizerSF.create("resources/face_recognition_sface_2021dec.onnx", "");
  }
  
  @Override
  public void run(  )
  {
    
    while (cont)
    {
      try
      {
        inputFace = _faceQueue.take();
        _completedQueue.put(recognizeFace(inputFace)); /// Here is the final average Mat
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
    Resource result             = new Resource();
    Resource dataBaseResource   = new Resource();
    users                       = new ArrayList<Resource>();
    users                       = _dataObject.getUsers();
    Iterator<Resource> iter     = users.iterator();
    Mat faceRecognizeFeatures   = new Mat();
    Mat faceDBFeatures          = new Mat();
    
    faceRecognizer.feature(face, faceRecognizeFeatures);
    faceRecognizeFeatures = faceRecognizeFeatures.clone();
    
    while (iter.hasNext())
    {
      dataBaseResource = iter.next();
      faceRecognizer.feature(dataBaseResource.userEncode, faceDBFeatures);
      faceDBFeatures = faceDBFeatures.clone();
      ///<getting the cosine similarity
      double cosineMatch = faceRecognizer.match(faceRecognizeFeatures, faceDBFeatures, FaceRecognizerSF.FR_COSINE);
      ///<getting the l2norm
      double l2Match = faceRecognizer.match(faceRecognizeFeatures, faceDBFeatures, FaceRecognizerSF.FR_NORM_L2);
      System.out.printf("CosineMatch: %.4f Threashold: %.4f%n", cosineMatch, COSINE_SIMILAR_THREASHOLD);
      System.out.printf("L2Match: %.4f Threashold: %.4f%n", l2Match, L2NORM_SIMILAR_THRESHOLD);
      
      if(cosineMatch >= COSINE_SIMILAR_THREASHOLD && l2Match >= L2NORM_SIMILAR_THRESHOLD)
      {
        result.validFace      = true;
        result.firstName      = dataBaseResource.firstName;
        result.lastName       = dataBaseResource.lastName;
        result.id             = dataBaseResource.id;
        result.user           = Resource.Result.RESULT_USER_RECOGNIZED;
        result.userEncode     = dataBaseResource.userEncode;
        System.out.println("Face Recognized");
        break;
      }
      else
      {
        result.validFace  = false;
        result.firstName  = null;
        result.lastName   = null;
        result.user       = Resource.Result.RESULT_USER_NOT_RECOGNIZED;
        result.id         = 0;
        System.out.println("Face NOT Recognized");
      }
      
    }
        
    return result;
  }
  
  public void startFacialRec()
  {
    facRecThread = new Thread(this, "Facial Thread");
    facRecThread.start();
  }
  
  public Resource.Result stopFacialRec()
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    try
    {
      cont = false;
      facRecThread.join(500);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_UNABLE_TO_JOIN_THREAD;
    }
    return result;
    
  }
  
  
}
