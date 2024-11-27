/**
 * @brief point of entry for the main program
 * 
 * @authors Carl Negrescu, Ben Pilande, and Bryan Gomez
 * 
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
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    DataAccess dao = new DataAccess();
    MainUI cameraInterface = new MainUI(dao);
    cameraInterface.startApp();
  }
}
