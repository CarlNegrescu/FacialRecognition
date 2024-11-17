/*
 * @brief DataBase Access Object
 * 
 * @author Carl Negrescu
 * 
 * @date 11/16/2024
 */
package backend;

import backend.IDataAccess;
import utils.Resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;


public class DataAccess implements IDataAccess 
{
  private String url = "jdbc::sqlite::C:/PCAssignments/SECodingProject/dev_carl/Code/facesDataBase.db";
  private String sql = "INSERT INTO Users (FirstName, LastName, FacialEncoding) VALUES (?, ?, ?)";
  Connection connection;
  
  public DataAccess()
  {
    
  }
  
 public Resource.Result addUser(Resource inputUser ) {}
  
  public Resource.Result deleteUser() {}
  
  public Resource.Result updateUser() {}
  
  public void listUsers() {}
  
  public void getUser() {}
  
  /*
   * @brief gets all the users in the database and returns it in a Array list
   * 
   * @return List of Mat objects 
   */
  public List<Resource> getUsers() {}
  
  

}
