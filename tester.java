import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Index indexTester = new Index();
        indexTester.addFile("testerText");
        indexTester.addFile("testerTextTwo");
    }
    
}
