import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {   Index i = new Index();
        i.initialize();

        Tree t = new Tree();
       //t.addToTree("blob : aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d: Test1.txt");
       //t.addToTree("blob : 3c8ec4874488f6090a157b014ce3397ca8e06d4f: Test2.txt");
       // t.addDirectory("AdvancedDirectory");
       // t.save();

       i.addFile("myTesterText.txt");
       Commit c = new Commit("Sarah", "first commit");
       i.setFirstAdded(true);
       i.addFile("Test1.txt");
      // i.addFile("Test2.txt");
       Commit c2 = new Commit("110c71f43dfae881f26e2f7b91e63de036c4ccf8", "Sarah", "second commit");
        
    }
    
}
