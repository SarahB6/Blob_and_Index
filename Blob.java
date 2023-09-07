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
    private static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
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
      

    public Blob(String fileName) throws IOException
    {
        BufferedReader ogReader= new BufferedReader(new BufferedReader(new FileReader(fileName))); 
        StringBuilder sb = new StringBuilder();
        while(ogReader.ready())
        {
            sb.append((char)ogReader.read());
        }
        byte[] asBytes = sb.toString().getBytes();
        //String newFileName = toSHA1(asBytes);
        ogReader.close();

        String newFileName = Blob.encryptPassword(fileName);
        System.out.print("FILE NAME: " + newFileName);
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
