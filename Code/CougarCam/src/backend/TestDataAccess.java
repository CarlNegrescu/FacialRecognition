package backend;

import utils.Resource;
import org.opencv.imgcodecs.*;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;

public class TestDataAccess implements IDataAccess
{
  
 private List<Resource> users = new ArrayList<Resource>();
 
 /// Tester adder method, 
 public Resource.Result addUser(Resource inputUser)
 {
//   Resource face = new Resource();
//   Mat image = Imgcodecs.imread("C:/test/carl_negrescu.jpg");
//   face.userEncode = image;
//   face.firstName = "Carl";
//   face.lastName = "Negrescu";
   users.add(inputUser);
   System.out.println("User added");
   return Resource.Result.RESULT_OK; 
 }
  
  public Resource.Result deleteUser(int id)
  {
    return Resource.Result.RESULT_OK;
  }
  
  public Resource.Result updateUser(Resource inputUser)
  {
    return Resource.Result.RESULT_OK;
  }
 
  
  public Resource getUser(int id){ return null;}
  
  
  
  public List<Resource> getUsers()
  {
    return users;
  }
  
  
}
