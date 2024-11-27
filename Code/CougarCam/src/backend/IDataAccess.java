/**
 * @brief interface class of the data access logic
 * 
 * @author Carl Negrescu
 * 
 * @date 11/15/2024
 * 
 */
package backend;
import utils.Resource;
import org.opencv.core.*;
import java.util.List;

public interface IDataAccess 
{
  /**
   * @brief Adds a user to the database
   * 
   * @param Resource object with all the users details 
   */
  public Resource.Result addUser(Resource inputUser);
  
  public Resource.Result deleteUser(String firstName);
  
  public Resource.Result updateUser(Resource inputUser, String firstName);
  
  public Resource getUser(int id);
  
  /*
   * @brief gets all the users in the database and returns it in a Array list
   * 
   * @return List of Mat objects 
   */
  public List<Resource> getUsers();
}
