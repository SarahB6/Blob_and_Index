import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.nio.file.*;

public class Blob 
{ 
    //codes the input and returns a sha
    public String SHA1Name(String input) throws IOException
    {
        BufferedReader ogReader= new BufferedReader(new BufferedReader(new FileReader(input))); 
        StringBuilder sb = new StringBuilder();
        while(ogReader.ready())
        {
            sb.append((char)ogReader.read());
        }
        ogReader.close();

        String dataAsString = sb.toString();

        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(dataAsString.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    //helper for above method
    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
      
    static final Path myPath = Paths.get("./objects/");

    //creates the Blob and saves it to the objects folder
    public Blob(String fileName) throws IOException
    {

        String newFileName = SHA1Name(fileName);
        BufferedReader br = new BufferedReader(new BufferedReader(new FileReader(fileName))); 
        BufferedWriter bw = new BufferedWriter(new FileWriter(myPath + "/" + newFileName));
        while(br.ready())
		{
            bw.write((char)br.read());
        }
        bw.close();
        br.close();
    }
}
