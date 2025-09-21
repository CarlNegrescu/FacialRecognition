/**
 * @brief Manages the core facial recognition process by coordinating the camera and recognizer threads.
 * <p>
 * This class acts as a central controller. It initializes the camera and facial recognition modules
 * and uses {@link ArrayBlockingQueue} to pass data between them. It also contains logic to
 * determine if a user is authenticated based on a confidence threshold of repeated recognitions.
 *
 * @author Carl Negrescu
 * @date 11/15/2024
 */
package cougarCam;

import backend.FacialRec;
import backend.IDataAccess;
import cameramodule.Camera;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import utils.Resource;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

import backend.TestDataAccess;

public class DoorManager implements Runnable
{
  public static final ArrayBlockingQueue<Mat> faceQueue = new ArrayBlockingQueue<Mat>(5);
  public static final ArrayBlockingQueue<Resource> completedQueue = new ArrayBlockingQueue<Resource>(5);

  private FacialRec   	_recognizer;
  private Camera      	_camera;
  private Thread      	_doorThread;
  private Boolean     	_cont = true;
  private Resource    	_result;
  private IDataAccess 	_dataAccessObject;
  private VideoCapture  _videoCapture;
  private int 			_validFace;
  private int 			_nonValidFace;

  /**
   * Constructs a DoorManager instance.
   *
   * @param dao The data access object for retrieving user data for recognition.
   */
  public DoorManager(IDataAccess dao)
  {
    System.out.println("In DoorManagerConstructor");
    _dataAccessObject = dao;
    System.out.println("Creating VideoCapture Object");
    _videoCapture= new VideoCapture(0); 
    _camera = new Camera(faceQueue);
    _recognizer = new FacialRec(faceQueue, completedQueue, _dataAccessObject);
    System.out.println("Done with DoorManager Constructor");
    _validFace = 0;
    _nonValidFace = 0;
  }

  /**
   * Starts the DoorManager thread, which in turn starts the camera and facial recognition processes.
   */
  public void startDoorManager()
  {
    _doorThread = new Thread(this, "DoorManager");
    _doorThread.start();
  }
  
  /**
   * Stops the DoorManager and all associated threads (camera, recognizer) gracefully.
   *
   * @return A {@link Resource.Result} indicating the outcome of the shutdown process.
   */
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
  
  /**
   * Analyzes recognition results to provide a confident authentication decision.
   * <p>
   * To prevent false positives/negatives, this method requires 5 consecutive valid recognitions
   * to grant access or 30 consecutive invalid recognitions to deny access before showing a popup.
   *
   * @param result The {@link Resource} object containing the latest recognition result.
   * @throws InterruptedException if the thread is interrupted while waiting.
   */
  private void displayMessage(Resource result) throws InterruptedException
  {
    if (_validFace >= 5 || _nonValidFace >= 30) 
    {
      if (_validFace >= 5 && result.validFace) 
      {
        _validFace = 0;
        _nonValidFace = 0;
        showPopup("Welcome " + result.firstName + " " + result.lastName, Color.GREEN);
        stopDoorManager();
      } 
      else if (_nonValidFace >= 30 && !result.validFace) 
      {
        _nonValidFace = 0;
        _validFace = 0;
        showPopup("USER NOT RECOGNIZED! ENTRY NOT PERMITTED", Color.RED);
        stopDoorManager();
      }
    } 
    else 
    {
      if (result.validFace) 
      {
        System.out.println("Adding to ValidFace Counter, current count: " + _validFace);
        _validFace++;
      } 
      else 
      {
        System.out.println("Adding to NONValidFace Counter, current count: " + _nonValidFace);
        _nonValidFace++;
      }
    }
  }
  
  /**
   * Displays a temporary popup message to the user.
   *
   * @param message The message to be displayed.
   * @param bgColor The background color of the popup window (e.g., green for success, red for failure).
   */
  private void showPopup(String message, Color bgColor) 
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    if (bgColor != Color.GREEN)
    	frame.setSize(800, 600);
    
    else
    	frame.setSize(500, 400);
    
    frame.setLayout(new BorderLayout());
    frame.getContentPane().setBackground(bgColor);

    JLabel label = new JLabel(message, SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 20));
    label.setForeground(Color.WHITE);
    frame.add(label, BorderLayout.CENTER);

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    new Timer(3000, e -> frame.dispose()).start();
    return;
  }

  /**
   * The main execution loop for the DoorManager thread.
   * It continuously takes recognition results from the completedQueue and processes them.
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
        System.out.println("DoorManager took from Queue");
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      try 
      {
        displayMessage(_result);
      } 
      catch (InterruptedException e) 
      {
        
        e.printStackTrace();
      }
    }
  }
}
