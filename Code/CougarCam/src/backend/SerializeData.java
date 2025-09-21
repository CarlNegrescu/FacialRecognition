/**
 * @brief A utility class for serializing and deserializing OpenCV {@link Mat} objects.
 * <p>
 * This class provides static methods to convert a {@code Mat} object, which represents
 * an image or facial encoding, into a byte array for storage in a database BLOB field,
 * and to convert it back into a {@code Mat} object upon retrieval.
 *
 * @author Carl Negrescu
 * @date 11/20/2024
 */
package backend;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


public class SerializeData 
{
  /**
   * Deserializes a byte array from a {@link SerializedMat} object back into an OpenCV {@link Mat} object.
   *
   * @param smat The {@link SerializedMat} container holding the byte array.
   * @return A {@link Mat} object representing the decoded image or feature vector.
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
   * Serializes an OpenCV {@link Mat} object into a {@link SerializedMat} object.
   * The Mat is encoded into a JPEG format byte array.
   *
   * @param mat The {@link Mat} object to be serialized.
   * @return A {@link SerializedMat} object containing the resulting byte array.
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
   * @brief A static nested class that acts as a data container for a serialized {@link Mat}.
   * <p>
   * This class is designed to hold the byte array representation of a Mat object,
   * along with its original metadata (rows, columns, type), though the current
   * implementation primarily relies on the byte array from {@code Imgcodecs.imencode}.
   */
  public static class SerializedMat
  {
    byte[]    bytes;
    short[]   shorts;
    int[]     ints;
    float[]   floats;
    double[]  doubles;
    int       type;
    int       rows;
    int       cols;

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
