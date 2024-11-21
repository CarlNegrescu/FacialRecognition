package cougarCam;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

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

    CascadeClassifier _cascade = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_default.xml");
    MatOfRect faceDetections = new MatOfRect();
    TestDataAccess test_dao = new TestDataAccess();
    DataAccess dao = new DataAccess();
    Mat image = new Mat();
    List<Resource> listUsers = new ArrayList<Resource>();
    Resource face = new Resource();
    Mat cropFace = null;
    image = Imgcodecs.imread("C:/test/carl_negrescu.jpg", Imgcodecs.IMREAD_COLOR);
    _cascade.detectMultiScale(image, faceDetections);
    
    for (Rect rect : faceDetections.toArray())
    {
      Imgproc.rectangle(image, new Point(rect.x, rect.y), 
                        new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
      cropFace = new Mat(image, rect);
      HighGui.imshow("face", cropFace);
    }
    

    HighGui.waitKey();
    //Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);
    System.out.println("Image Channels: " + cropFace.channels());
    System.out.println("Image Size: " + cropFace.size());
    System.out.println("Image Type: " + cropFace.type());
    
    Resource user = new Resource();
    user.userEncode = cropFace;
    user.firstName = "Carl";
    user.lastName = "Negrescu";
    Resource updatedUser = new Resource();
    updatedUser.firstName = "Ben";
    updatedUser.lastName = "Pilande";
    updatedUser.userEncode = cropFace;
    
    //dao.addUser(user);
    dao.updateUser(user, updatedUser.firstName);
    listUsers = dao.getUsers();
    
    face = listUsers.get(0);
    
    //dao.deleteUser(face.firstName);
    System.out.println("Image Channels: " + face.userEncode.channels());
    System.out.println("Image Size: " + face.userEncode.size());
    System.out.println("Image Type: " + face.userEncode.type());
    //test_dao.addUser(user);
    MainUI cameraInterface = new MainUI(dao);
    cameraInterface.startApp();
  }
}