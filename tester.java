import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Index indexTester = new Index();
        indexTester.initialize();
        Blob blobTester = new Blob("testerText");
    }
    
}
