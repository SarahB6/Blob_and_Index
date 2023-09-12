import java.io.IOException;

public class TreeTester {
    public static void main (String [] args) throws IOException
    {
        Tree myTree = new Tree ();
        
        myTree.addToTree ("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        myTree.addToTree ("blob : 01d82591292494afd1602d175e165f94992f6f5f : file2.txt");
        myTree.addToTree ("blob : f1d82236ab908c86ed095023b1d2e6ddf78a6d83 : file3.txt");
        myTree.addToTree ("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        myTree.addToTree ("tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976");
        myTree.save();
        myTree.removeLine ("f1d82236ab908c86ed095023b1d2e6ddf78a6d83");
        myTree.save ();
        myTree.addToTree ("tree : e7d7eee8d3342fd15daf6ec36f4cb095b52fd976");
        myTree.save ();
    }
}
