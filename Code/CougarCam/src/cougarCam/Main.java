/**
 * @brief The main entry point for the Cougar Cam application.
 * <p>
 * This class is responsible for initializing the application environment,
 * including loading the native OpenCV library, instantiating the data access object (DAO),
 * and launching the main user interface.
 *
 * @authors Carl Negrescu, Ben Pilande, and Bryan Gomez
 * @date 11/14/2024
 */
package cougarCam;

import org.opencv.core.*;
import backend.DataAccess;
import frontend.MainUI;

public class Main
{
  public static void main(String[] args)
  {
    // Load the native OpenCV library required for all image processing operations.
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    // Instantiate the Data Access Object to handle database interactions.
    DataAccess dao = new DataAccess();
    // Create the main user interface and start the application.
    MainUI cameraInterface = new MainUI(dao);
    cameraInterface.startApp();
  }
}
