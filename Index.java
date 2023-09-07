import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Index 
{
    Boolean fileExists;
    public Index() throws IOException //should NOT put an entry twice
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("index"));
        bw.write("string name" + " : " + "SHA1 name" + "\n"); //MAKE THIS CORRECT
        bw.close();
    }
}
