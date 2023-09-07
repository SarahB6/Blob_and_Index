import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob_and_Index 
{
    private static String toSHA1(byte[] convertme) //ask if it needs to be static
    {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        return new String(md.digest(convertme));
    }

    public void Blob(String fileName) throws IOException
    {
        byte[] asBytes = fileName.getBytes();
        String newFileName = toSHA1(asBytes);
        //String shouldBeOG = new String(asBytes);

        
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

    public void Index()
    {

    }
}
