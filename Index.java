import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Index 
{
    private  Map <String, String> map = new HashMap<String, String>();
    Boolean fileExists;
    File f;
    public Index() throws IOException//should NOT put an entry twice (this is kinda hard)
    {
        initialize();
    }

    public Boolean alrInIndex(String fileName) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("index"));
        Blob currentBlob = new Blob(fileName);
        String SHA1_of_file = currentBlob.SHA1Name(fileName);
        StringBuilder currentString = new StringBuilder();
        while(br.ready())
        {
            currentString.append(br.readLine());
            if(currentString.indexOf(fileName + " : " + SHA1_of_file) > -1)
            {
                return true;
            }
            
        }
        br.close();
        return false;
    }

    public void addFile(String fileName) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));
        Blob currentBlob = new Blob(fileName);
        String SHA1_of_file = currentBlob.SHA1Name(fileName);
        if(!alrInIndex(fileName)) //SHOULD IT BE CHECKING IF THE HASH IS THE SAME????
        {
            System.out.println(fileName + "does not alr exist");
            map.put(fileName, SHA1_of_file);
            bw.write(fileName + " : " + SHA1_of_file + "\n");
        }
        bw.close();
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
