package cougarCam;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import cougarCam.DoorManager;
import backend.DataAccess;
import backend.TestDataAccess;
import utils.Resource;
import org.opencv.videoio.VideoCapture;
import frontend.MainUI;

public class Main
{
  public static void main(String[] args)
  {
	  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	  System.out.println("Starting Camera");
	  
	  TestDataAccess test_dao = new TestDataAccess();
	  DataAccess dao = new DataAccess();
	  
	  Mat image = Imgcodecs.imread("C:/test/carl_negrescu.jpg");
	  Resource user = new Resource();
	   user.userEncode = image;
	   user.firstName = "Carl";
	   user.lastName = "Negrescu";
	   
	  //dao.addUser(user);
	  //test_dao.addUser(user);
	  MainUI cameraInterface = new MainUI(test_dao);
	  cameraInterface.startApp();
  }
}