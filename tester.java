import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Index i = new Index();
        i.initialize();
        i.addFile("testerText");
        i.removeFile("testerText");
        i.addFile("testerTextTwo");
    }
    
}
