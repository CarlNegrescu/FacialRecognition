package cougarCam;

import org.opencv.core.*;
import cougarCam.DoorManager;
import backend.TestDataAccess;
import utils.Resource;
import frontend.CameraUI;

public class Main
{
	public static void main(String[] args)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Starting System");
		CameraUI cam = new CameraUI();
		cam.startApp();
		//	  System.out.println("Starting Camera");
		//	  TestDataAccess dao = new TestDataAccess();
		//	  Resource user = new Resource();
		//	  dao.addUser(user);
		//	  DoorManager dmanager = new DoorManager(dao);
		//	  dmanager.startDoorManager();
	}
}