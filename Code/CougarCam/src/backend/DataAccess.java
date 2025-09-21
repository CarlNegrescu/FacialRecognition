/**
 * @brief Data Access Object (DAO) for handling all interactions with the SQLite database.
 * <p>
 * This class implements the {@link IDataAccess} interface and provides concrete
 * methods for adding, deleting, updating, and retrieving user data from the
 * `facesDataBase.db` file. It also handles serialization of OpenCV {@link Mat} objects
 * for storage as BLOBs.
 *
 * @author Carl Negrescu
 * @date 11/16/2024
 */
package backend;

import backend.IDataAccess;
import backend.SerializeData;
import backend.SerializeData.SerializedMat;
import utils.Resource;
import java.sql.Statement;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


public class DataAccess implements IDataAccess 
{
  private String url        = "jdbc:sqlite:./resources/facesDataBase.db";
  private String sql        = "INSERT INTO users (first_name, last_name, face_features) VALUES (?, ?, ?)";
  private String queryTable = "SELECT ROWID as id, first_name, last_name, face_features FROM users";
  Connection connection;
  SerializeData serializeData;

  /**
   * Constructs the DataAccess object and establishes a connection to the database.
   */
  public DataAccess()
  {
    serializeData = new SerializeData(); 
    try
    {
      connection = DriverManager.getConnection(url);
      System.out.println("Connected to the Database");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Resource.Result addUser(Resource inputUser)
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    try
    {
      PreparedStatement statement = connection.prepareStatement(sql);
      System.out.println(statement);
      statement.setString(1, inputUser.firstName);
      statement.setString(2, inputUser.lastName);
      
      SerializedMat smat = serializeData.serializeFromMat(inputUser.userEncode);
      statement.setBytes(3, smat.getBytes());
      
      System.out.println(statement);
      System.out.println("Executing Update");
      statement.executeUpdate();
      System.out.println("Updated Database");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_FAILED;
    }

    return result;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Resource.Result deleteUser(String firstName) 
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    System.out.println("IN DELETE USER");
    String query = "DELETE FROM users WHERE first_name = ?";
    try
    {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, firstName);
      int rowsAffected = statement.executeUpdate();
      
      if (rowsAffected > 0)
      {
        System.out.println("User with ID " + firstName + " was deleted");
      }
      else
      {
        System.out.println("No user was deleted");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_FAILED;
    }
    return result;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Resource.Result updateUser(Resource inputUser, String firstName) 
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    String query = "UPDATE users SET first_name = ?, last_name = ?, face_features = ? WHERE first_name = ?";
    SerializedMat smat = serializeData.serializeFromMat(inputUser.userEncode);
    try
    {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, inputUser.firstName);
      statement.setString(2, inputUser.lastName);
      statement.setBytes(3, smat.getBytes());
      statement.setString(4,  firstName);
      System.out.println("Sending to DataBase" + statement);
      int rowsAffected = statement.executeUpdate();
      
      if (rowsAffected > 0)
        System.out.println("User with ID " + inputUser.id + " was deleted");
      else
        System.out.println("No user was deleted");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Resource getUser(int id) 
  {
    String query = "SELECT * FROM users WHERE id = ?";
    Resource user = new Resource();
    try
    {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery(query);
      user.id = resultSet.getInt("id");
      user.firstName = resultSet.getString("first_name");
      user.lastName = resultSet.getString("last_name");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    
    return user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Resource> getUsers()
  {
    List<Resource> listUsers = new ArrayList<Resource>();
    
    SerializedMat smat = new SerializedMat();
    try 
    {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(queryTable);

      while (resultSet.next())
      {
        Resource user = new Resource();
        user.id = resultSet.getInt("id");
        user.firstName = resultSet.getString("first_name");
        user.lastName = resultSet.getString("last_name");
        
        byte[] inputByte = resultSet.getBytes("face_features"); ///< converting to a byte array to convert then to a Mat Object/// getBytes
        smat.setBytes(inputByte);
        user.userEncode = SerializeData.DeserializeToMat(smat);
        System.out.println("Added One User for processing");
        listUsers.add(user);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    System.out.println(listUsers);
    return listUsers;
  }
}
