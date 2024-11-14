package utils;

public class Resource 
{
  public String firstName;
  public String lastName;
  public Boolean validFace;
  public int id;
  
	public enum Result
	  {
	    RESULT_OK,
	    RESULT_FAILED,
	    RESULT_INVALID_DATA,
	    RESULT_INVALID_FILE,
	    RESULT_INVALID_DIRECTORY,
	    RESULT_INVALID_FORMAT,
	    RESULT_INVALID_FILE_FORMAT,
	    RESULT_UNABLE_TO_JOIN_THREAD
	  }
	
	public Resource()
	{
	  firstName = "";
	  lastName = "";
	  id = 0;
	  validFace = false;
	}
	
}
