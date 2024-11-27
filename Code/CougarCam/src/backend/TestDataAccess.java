/**
 * @brief testing dataAccess class when implementation of the database is not yet completed, or to ensure problems do not arise from data access
 * 
 * @author Carl Negrescu
 * 
 * @date 11/16/24
 */
package backend;

import utils.Resource;
import java.util.ArrayList;
import java.util.List;

public class TestDataAccess implements IDataAccess
{
 private List<Resource> users = new ArrayList<Resource>();
 
 /// Test adder method, 
 public Resource.Result addUser(Resource inputUser)
 {
   users.add(inputUser);
   System.out.println("User added");
   return Resource.Result.RESULT_OK; 
 }
  
  public Resource.Result deleteUser(String firstName)
  {
    return Resource.Result.RESULT_OK;
  }
  
  public Resource.Result updateUser(Resource inputUser, String firstName)
  {
    return Resource.Result.RESULT_OK;
  }
 
  
  public Resource getUser(int id){ return null;}
  
  
  
  public List<Resource> getUsers()
  {
    return users;
  }
  
  
}
