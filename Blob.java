import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.file.*;

public class Blob 
{ 

    static final Path myPath = Paths.get("./objects/");

    public static String toSHA1(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
          result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
      }

    public Blob(String fileName) throws IOException
    {
        byte[] asBytes = fileName.getBytes();
        String newFileName = toSHA1(asBytes);

        
        //how do you save a file in the objects folder??
        BufferedReader br = new BufferedReader(new BufferedReader(new FileReader(fileName))); 
        BufferedWriter bw = new BufferedWriter(new FileWriter(newFileName));
        while(br.ready())
		{
            bw.write((char)br.read());
        }
        bw.close();
        br.close();
    }
}
