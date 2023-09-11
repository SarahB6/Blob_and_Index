import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Index i = new Index();
        i.initialize();
        i.addFile("Test1.txt");
        i.addFile("Test2.txt");
        i.removeFile("Test1.txt");
        i.addFile("Test3.txt");
        
        
    }
    
}
