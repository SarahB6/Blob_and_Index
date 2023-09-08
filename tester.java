import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Index indexTester = new Index();
        indexTester.addFile("testerText");
        System.out.println("FIRST:  "+ indexTester.alrInIndex("testerText"));
        indexTester.addFile("testerTextTwo");
        indexTester.addFile("testerTextTwo");
        indexTester.removeFile("testerText");
    }
    
}
