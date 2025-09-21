/**
 * @brief helper class, creating an easy to use return types, and a grouping of variables for faces 
 * 
 * @author Carl Negrescu
 * @date 11/16/2024
 */
package utils;
import org.opencv.core.*;

public class Resource 
{
  public String   firstName;
  public String   lastName;
  public Boolean  validFace;
  public int      id;
  public Result   user;
  public Mat      userEncode;
  
  /**
   * @brief Defines a set of standardized status codes for the outcomes of various operations.
   * <p>
   * This enum provides a type-safe way to represent results from tasks such as
   * file handling, database interactions, thread management, and facial recognition.
   */
  public enum Result
  {
      /** Indicates that the operation completed successfully. */
      RESULT_OK,
      /** Indicates that a generic failure occurred during the operation. */
      RESULT_FAILED,
      /** Indicates that the data provided for an operation was invalid. */
      RESULT_INVALID_DATA,
      /** Indicates that a specified file is invalid or could not be found. */
      RESULT_INVALID_FILE,
      /** Indicates that a specified directory is invalid or could not be found. */
      RESULT_INVALID_DIRECTORY,
      /** Indicates that the format of some data is invalid. */
      RESULT_INVALID_FORMAT,
      /** Indicates that the format of a file (e.g., not an image) is invalid. */
      RESULT_INVALID_FILE_FORMAT,
      /** Indicates that a thread could not be successfully joined. */
      RESULT_UNABLE_TO_JOIN_THREAD,
      /** Indicates that a thread was successfully joined. */
      RESULT_JOINED_THREAD,
      /** Indicates an attempt to close a resource that was already closed. */
      RESULT_ALREADY_CLOSED,
      /** Indicates an attempt to open a resource that was already open. */
      RESULT_ALREADY_OPEN,
      /** Indicates that a user's face was successfully recognized. */
      RESULT_USER_RECOGNIZED,
      /** Indicates that a user's face was not recognized. */
      RESULT_USER_NOT_RECOGNIZED
  }
	
	public Resource()
	{
	  firstName = "";
	  lastName = "";
	  id = 0;
	  validFace = false;
	}
	
}
