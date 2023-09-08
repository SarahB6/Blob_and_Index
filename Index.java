import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Index 
{
    File f;
    public Index() throws IOException
    {
    }

    //checks if the file is already in the Index list
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
            currentString.setLength(0);
            
        }
        br.close();
        return false;
    }

    //adds the file to the index if it doesn't already exist
    public void addFile(String fileName) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));
        Blob currentBlob = new Blob(fileName);
        String SHA1_of_file = currentBlob.SHA1Name(fileName);
        if(!alrInIndex(fileName)) //SHOULD IT BE CHECKING IF THE HASH IS THE SAME????
        {
            System.out.println(fileName + "does not alr exist");
            bw.write(fileName + " : " + SHA1_of_file + "\n");
        }
        bw.close();
    }

    //removes file from the index list and objects folder
    public void removeFile(String fileName) throws IOException
    {
        if(alrInIndex(fileName))
        {
            BufferedReader br = new BufferedReader(new FileReader("index"));
            StringBuilder newIndex = new StringBuilder();
            Blob currentBlob = new Blob(fileName);
            String SHA1_of_file = currentBlob.SHA1Name(fileName);
            StringBuilder currentString = new StringBuilder();
            while(br.ready())
            {
                currentString.append(br.readLine());
                if(currentString.indexOf(fileName + " : " + SHA1_of_file) == -1)
                {
                    newIndex.append(currentString + "\n");
                }
                currentString.setLength(0);
                
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter("index", false));
            
            bw.write(newIndex.toString());
            
            bw.close();
            br.close();

            File toRemove = new File("./objects/" + SHA1_of_file);
            toRemove.delete();

        }
    }

    public void initialize() throws IOException
    {
        new File("objects").mkdirs();
        f = new File("index");
        f.createNewFile();
    }
}
