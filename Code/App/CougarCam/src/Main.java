import cameraModule.Camera;



public class Main
  {
    public static void main(String[] args)
    {
        Camera faceDetect = new Camera(0);
        faceDetect.openCamera();
    }
}