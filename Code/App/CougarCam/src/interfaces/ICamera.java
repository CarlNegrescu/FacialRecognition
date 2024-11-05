package interfaces;

import utils.Resource;

interface ICamera
{
  /**
   * Initializes the Camera Module for use
   *
   * @return The success of the operation
   */
  public Resource.Result init();

  /**
   * Opens the camera for use, Uses a JFrame Panel for use
   *
   * @return The Result of the operation
   */
  public Resource.Result openCamera(String windowName);

  /**
   * Closes the camera
   *
   * @return The Result of the operation
   */
  public Resource.Result closeCamera();

  /**
   *
   * @return The Result of the operation
   */
  public Resource.Result takePicture();

}
