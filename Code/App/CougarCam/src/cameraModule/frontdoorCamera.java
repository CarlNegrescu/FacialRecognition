package cameraModule;

import interfaces.ICamera;
import utils.Resource;

public class frontdoorCamera implements ICamera
{
  @Override
  public Resource.Result init()
  {
    return null;
  }

  @Override
  public Resource.Result openCamera(String windowName)
  {
    return null;
  }

  @Override
  public Resource.Result closeCamera()
  {
    return null;
  }

  @Override
  public Resource.Result takePicture()
  {
    return null;
  }
}
