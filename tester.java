import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Index indexTester = new Index();
        indexTester.addFile("testerText");
        System.out.println("FIRST:  "+ indexTester.alrInIndex("testerText"));
        System.out.println(indexTester.alrInIndex("testerTextTwo"));
        indexTester.addFile("testerTextTwo");
        System.out.println(indexTester.alrInIndex("testerTextTwo"));
        indexTester.addFile("testerTextTwo");
    }
    
}
