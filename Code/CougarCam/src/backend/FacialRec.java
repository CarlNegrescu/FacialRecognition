/**
 * @brief Handles the core facial recognition logic.
 * <p>
 * This class runs on a separate thread, taking detected face images (as {@link Mat} objects)
 * from a blocking queue. It uses the OpenCV {@link FaceRecognizerSF} model to compare
 * the input face against all user faces stored in the database.
 *
 * @author Carl Negrescu
 * @date 11/16/2024
 */
package backend;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
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
  private static final double COSINE_SIMILAR_THREASHOLD = 0.150; ///0.363 /// Good camera is required ! Otherwise lower the threshold
  private static final double L2NORM_SIMILAR_THRESHOLD  = .728; ///1.128
  private BlockingQueue<Mat> _faceQueue;
  private BlockingQueue<Resource> _completedQueue;
  private IDataAccess _dataObject;
  private FaceRecognizerSF faceRecognizer;
  private Thread facRecThread;
  private Boolean cont = true;
  private Mat inputFace;
  private List<Resource> users;
  
  /**
   * Constructs the FacialRec instance.
   *
   * @param faceQueue      The queue from which to take detected face images.
   * @param completedQueue The queue to put recognition results into.
   * @param dataObject     The DAO for accessing user data from the database.
   */
  public FacialRec(BlockingQueue<Mat> faceQueue, BlockingQueue<Resource> completedQueue, IDataAccess dataObject)
  {
    System.out.println("In FacialRec Constructor");
    _dataObject     = dataObject;
    _faceQueue      = faceQueue;
    _completedQueue = completedQueue;
    faceRecognizer  = FaceRecognizerSF.create("resources/face_recognition_sface_2021dec.onnx", "");
  }
  
  /**
   * The main execution loop for the facial recognition thread.
   * It continuously takes faces from the {@code _faceQueue}, recognizes them,
   * and places the result in the {@code _completedQueue}.
   */
  @Override
  public void run(  )
  {
    
    while (cont)
    {
      try
      {
        inputFace = _faceQueue.take();
        _completedQueue.put(recognizeFace(inputFace));
        System.out.println("IN FACIAL REC");
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
        System.out.println("Recognition Thread Interrupted" + e.getMessage());
      }
    }
  }
  /**
   * Compares a given face against all users in the database.
   * <p>
   * It extracts the facial features from the input face and iterates through all database users,
   * calculating the cosine similarity and L2 norm distance. If a match is found that exceeds
   * the defined thresholds, it returns a {@link Resource} object with the user's details.
   *
   * @param face A {@link Mat} object of the cropped face to be recognized.
   * @return A {@link Resource} object containing the recognition result.
   */
  private Resource recognizeFace(Mat face)
  {
    Resource result             = new Resource();
    Resource dataBaseResource   = new Resource();
    users                       = new ArrayList<Resource>();
    users                       = _dataObject.getUsers();
    Iterator<Resource> iter     = users.iterator();
    Mat faceRecognizeFeatures   = new Mat();
    Mat faceDBFeatures          = new Mat();
    Mat grayToColorMat          = new Mat();
    
    faceRecognizer.feature(face, faceRecognizeFeatures);
    faceRecognizeFeatures = faceRecognizeFeatures.clone();
    System.out.println(users);
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
  
  /**
   * Starts the facial recognition thread.
   */
  public void startFacialRec()
  {
    facRecThread = new Thread(this, "Facial Thread");
    facRecThread.start();
  }
  
  /**
   * Stops the facial recognition thread gracefully.
   *
   * @return A {@link Resource.Result} indicating the outcome of the shutdown.
   */
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
