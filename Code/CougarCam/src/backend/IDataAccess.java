/**
 * @brief Interface for the data access layer, defining a contract for all data access objects.
 * <p>
 * This interface outlines the standard CRUD (Create, Read, Update, Delete) operations
 * required for managing user data in the application's persistence layer.
 *
 * @author Carl Negrescu
 * 
 */
package backend;
import utils.Resource;
import org.opencv.core.*;
import java.util.List;

public interface IDataAccess 
{
  /**
   * Adds a new user to the data store.
   *
   * @param inputUser A {@link Resource} object containing the details of the user to be added.
   * @return A {@link Resource.Result} indicating the success or failure of the operation.
   */
  public Resource.Result addUser(Resource inputUser);
  
  /**
   * Deletes a user from the data store based on their first name.
   *
   * @param firstName The first name of the user to be deleted.
   * @return A {@link Resource.Result} indicating the success or failure of the operation.
   */
  public Resource.Result deleteUser(String firstName);
  
  /**
   * Updates an existing user's information in the data store.
   *
   * @param inputUser A {@link Resource} object containing the new user details.
   * @param firstName The original first name of the user to identify them for the update.
   * @return A {@link Resource.Result} indicating the success or failure of the operation.
   */
  public Resource.Result updateUser(Resource inputUser, String firstName);
  
  /**
   * Retrieves a single user from the data store by their unique ID.
   *
   * @param id The unique identifier of the user.
   * @return A {@link Resource} object containing the user's details, or null if not found.
   */
  public Resource getUser(int id);
  
  /**
   * Retrieves a list of all users from the data store.
   *
   * @return A {@link List} of {@link Resource} objects, each representing a user.
   */
  public List<Resource> getUsers();
}
