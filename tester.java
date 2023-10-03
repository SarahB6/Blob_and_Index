import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {
        Tree t = new Tree();
       //t.addToTree("blob : aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d: Test1.txt");
       //t.addToTree("blob : 3c8ec4874488f6090a157b014ce3397ca8e06d4f: Test2.txt");
        t.addDirectory("Directory");
        t.save();

        Index i = new Index();
        i.addDirectory("./Directory");
        
        
    }
    
}
