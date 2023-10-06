import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {   Index i = new Index();
        i.initialize();
        Commit c1 = new Commit("Oren", "testing tree creation");
        i.setFirstAdded(true);
        i.addFile("Test1.txt");
        Commit c2 = new Commit("555ce05a67b2e4320ca320f3a91afe3118c9038b", "Oren", "second commit");

    }
    
}
