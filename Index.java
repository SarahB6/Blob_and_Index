import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class Index 
{
    private Map <char[], char[]> map = new HashMap<char[], char[]>();
    Boolean fileExists;
    File f;
    public Index() throws IOException//should NOT put an entry twice (this is kinda hard)
    {
        initialize();
    }

    public void addFile(String fileName) throws IOException
    {
        //check if it alr exists
        if(!map.containsKey(fileName.toCharArray())) //SHOULD IT BE CHECKING IF THE HASH IS THE SAME????
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));
            Blob currentBlob = new Blob(fileName);
            String SHA1_of_file = currentBlob.SHA1Name(fileName);
            map.put(fileName.toCharArray(), SHA1_of_file.toCharArray());
            bw.write(fileName + " : " + SHA1_of_file + "\n");
            bw.close();
        }
    }

    public void removeFile(String fileName) throws IOException
    {

    }

    public void initialize() throws IOException
    {
        fileExists = true;
        new File("objects").mkdirs();

        f = new File("index");
        f.createNewFile();
    }
}
