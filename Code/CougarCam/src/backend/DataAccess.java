/*
 * @brief DataBase Access Object, to Update, Add, Delete rows, and return whole tables
 * 
 * @author Carl Negrescu
 * 
 * @date 11/16/2024
 */
package backend;

import backend.IDataAccess;
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

  public DataAccess()
  {
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

  /*
   * @brief Adds a user to the database
   * 
   * @param Resource object with all the users details 
   */
  public Resource.Result addUser(Resource inputUser)
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    try
    {
      PreparedStatement statement = connection.prepareStatement(sql);
      System.out.println(statement);
      statement.setString(1, inputUser.firstName);
      statement.setString(2, inputUser.lastName);
      byte[] blob = convertToBlob(inputUser.userEncode);
      statement.setBytes(3, blob);
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

  public Resource.Result deleteUser(int id) 
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    String query = "DELETE FROM user WHERE id = ?";
    try
    {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, id);
      int rowsAffected = statement.executeUpdate();
      
      if (rowsAffected > 0)
      {
        System.out.println("User with ID " + id + " was deleted");
      }
      
      System.out.println("No user was deleted");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      result = Resource.Result.RESULT_FAILED;
    }
    return result;
  }

  public Resource.Result updateUser(Resource inputUser) 
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    String query = "UPDATE users SET first_name = ?, last_name = ?, facial_encoding = ? WHERE id = ?";
    byte[] featureBytes = convertToBlob(inputUser.userEncode);
    try
    {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, inputUser.firstName);
      statement.setString(2, inputUser.lastName);
      statement.setBytes(3, featureBytes);
      statement.setInt(4,  inputUser.id);
      
      int rowsAffected = statement.executeUpdate();
      
      if (rowsAffected > 0)
        System.out.println("User with ID " + inputUser.id + " was deleted");
      
      System.out.println("No user was deleted");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return result;
  }


  public Resource getUser(int id) 
  {
    Resource.Result result = Resource.Result.RESULT_OK;
    String query = "SELECT FROM user WHERE id = ?";
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

  /*
   * @brief gets all the users in the database and returns it in a Array list
   * 
   * @return List of Mat objects 
   */
  public List<Resource> getUsers()
  {
    List<Resource> listUsers = new ArrayList<Resource>();
    Resource user = new Resource();

    try 
    {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(queryTable);

      while (resultSet.next())
      {
        user.id = resultSet.getInt("id");
        user.firstName = resultSet.getString("first_name");
        user.lastName = resultSet.getString("last_name");
        
        byte[] blob = resultSet.getBytes("face_features"); ///< converting to a byte array to convert then to a Mat Object/// getBytes
//        byte[] faceFeaturesBytes = blob.getBytes(1, (int) blob.length());
//        try
//        {
//        	user.userEncode = convertToMat(blob);
//        }
//        catch (Exception e)
//        {
//        	e.printStackTrace();
//        }
        System.out.println("Added One User!");
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

  private byte[] convertToBlob(Mat face)
  {
    int size = (int)(face.total() * face.elemSize());
    byte[] byteArray = new byte[size];
    face.get(0,0, byteArray);
    
    return byteArray;
  }
//  
//  private Mat convertToMat(byte[] byteArray) throws Exception
//  {
//	  //Mat face = new Mat(1, byteArray.length, CvType.CV_32F);
//	  //face = Imgcodecs.imdecode(new MatOfByte(byteArray), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
//	  BufferedImage img;
//	  img = ImageIO.read(new ByteArrayInputStream(byteArray));
//	  Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
//	  mat.put(0, 0, ((DataBufferByte) img.getRaster().getDataBuffer()).getData());
//	  return mat;
//
//
//
//
//
//  }
}
