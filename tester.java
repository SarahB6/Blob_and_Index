import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Index i = new Index();
        i.initialize();
        i.addFile("testerText");
        i.addFile("testerText");
        i.addFile("example.txt");
        i.removeFile("testerText");
        i.removeFile("example.txt");
        
    }
    
}
