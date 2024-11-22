/*
 * @brief helper class, creating an easy to use return types, and a grouping of variables for faces 
 * 
 * @author Carl Negrescu
 * @date 11/16/2024
 */
package utils;
import org.opencv.core.*;

public class Resource 
{
  public String firstName;
  public String lastName;
  public Boolean validFace;
  public int id;
  public Result user;
  public Mat userEncode;
  
	public enum Result
	{
	    RESULT_OK,
	    RESULT_FAILED,
	    RESULT_INVALID_DATA,
	    RESULT_INVALID_FILE,
	    RESULT_INVALID_DIRECTORY,
	    RESULT_INVALID_FORMAT,
	    RESULT_INVALID_FILE_FORMAT,
	    RESULT_UNABLE_TO_JOIN_THREAD,
	    RESULT_JOINED_THREAD,
	    RESULT_ALREADY_CLOSED,
	    RESULT_ALREADY_OPEN,
	    RESULT_USER_RECOGNIZED,
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
