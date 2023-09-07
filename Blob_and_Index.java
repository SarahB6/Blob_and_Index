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

    public void Blob()
    {

    }

    public void Index()
    {

    }
}
