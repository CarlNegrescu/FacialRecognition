/**
 *  @brief Seralization and deseralization helper class to convert mat objects for entry and exit from the database
 *  
 *  @date 11/20/2024
 *  @author Carl Negrescu
 */
package backend;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


public class SerializeData 
{
  /**
   * @brief Takes in a SerializedMat Object to convert into a Mat Object
   * 
   * @param SerializedMat object
   * 
   * @return Mat Object of user's facial encodings
   */
  public static Mat DeserializeToMat(SerializedMat smat) 
  {
    byte[] byteArray = smat.getBytes();

    if (byteArray == null || byteArray.length == 0) 
    {
      System.err.println("Cannot deserialize: byte array is null or empty.");
      return new Mat();
    }

    Mat decodedMat = Imgcodecs.imdecode(new MatOfByte(byteArray), Imgcodecs.IMREAD_UNCHANGED);///< Decoding the byte array into a Mat object

    if (decodedMat.empty()) 
    {
      System.err.println("Deserialization failed: Decoded Mat is empty.");
    } 
    else 
    {
      System.out.println("Deserialized Mat: " + decodedMat.size() + ", Type: " + decodedMat.type());
    }
    return decodedMat;
  }

  /**
   * @brief Serializes a Mat object into a SerializedMat Object
   * 
   * @param Mat Object
   * 
   * @return SerializedMat Object
   */
  public SerializedMat serializeFromMat(Mat mat) 
  {
    SerializedMat smat = new SerializedMat();
    MatOfByte buffer = new MatOfByte();
    boolean success = true;

    if (mat.empty()) 
    {
      System.err.println("Cannot serialize an empty Mat.");
      return smat; // Return an empty SerializedMat to avoid further errors
    }
    success = Imgcodecs.imencode(".jpg", mat, buffer);

    if (!success) 
    {
      System.err.println("Failed to encode Mat during serialization.");
      return smat;
    }
    smat.setBytes(buffer.toArray());
    System.out.println("Serialized Mat: " + mat.size() + ", Type: " + mat.type());
    return smat;
  }

  
  /**
   * @brief Helper class to easily manage the byte array to send to the DataBase
   *  Identifies the type of integer in the Mat array and correctly stores it. 
   */
  public static class SerializedMat
  {
    byte[] bytes;
    short[] shorts;
    int[] ints;
    float[] floats;
    double[] doubles;

    int type;
    int rows;
    int cols;

    byte[] getBytes()
    {
      return bytes;
    }

    void setBytes(byte[] bytes)
    {
      this.bytes = bytes;
    }

    short[] getShorts()
    {
      return shorts;
    }

    void setShorts(short[] shorts)
    {
      this.shorts = shorts;
    }

    int[] getInts()
    {
      return ints;
    }

    void setInts(int[] ints)
    {
      this.ints = ints;
    }

    float[] getFloats()
    {
      return floats;
    }

    void setFloats(float[] floats)
    {
      this.floats = floats;
    }

    double[] getDoubles()
    {
      return doubles;
    }

    void setDoubles(double[] doubles)
    {
      this.doubles = doubles;
    }

    int getType()
    {
      return type;
    }

    void setType(int type)
    {
      this.type = type;
    }

    int getRows()
    {
      return rows;
    }

    void setRows(int rows)
    {
      this.rows = rows;
    }

    int getCols()
    {
      return cols;
    }

    void setCols(int cols)
    {
      this.cols = cols;
    }

    SerializedMat()
    {
    }
  }
}
