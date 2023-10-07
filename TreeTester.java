
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.PrintWriter;
import java.nio.file.Path;

public class TreeTester 
{
    static File examplefile1;
    static File examplefile2;
    static File examplefile3;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {//deleting index
        File myIndex = new File ("./index");
        myIndex.delete();
        //deleting objects
        File myObjects = new File ("./objects");
        myObjects.mkdirs();
        File[] contents = myObjects.listFiles();
        if (contents != null) {
            for (File f : contents) {
                f.delete ();
            }
        }

        
        myObjects.delete();
        File Direct = new File("./Directory");
        Direct.mkdirs();
        examplefile1= new File("./Directory/examplefile1.txt");
        examplefile1.createNewFile();
         BufferedWriter bw = new BufferedWriter(new FileWriter("./Directory/examplefile1.txt"));
         bw.write("thisisfile1");
         bw.close();

        examplefile2= new File("./Directory/examplefile2.txt");
        examplefile2.createNewFile();
         BufferedWriter bw2 = new BufferedWriter(new FileWriter("./Directory/examplefile2.txt"));
         bw2.write("thisisfile2");
         bw2.close();

        File examplefile3= new File("./Directory/examplefile3");
        examplefile3.createNewFile();
         BufferedWriter bw3 = new BufferedWriter(new FileWriter("./Directory/examplefile3"));
         bw3.write("thisisfile3");
         bw3.close();
        
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        File myIndex = new File ("./index");
        myIndex.delete();
        File myObjects = new File ("./objects");
        File[] contents = myObjects.listFiles();
        if (contents != null) {
            for (File f : contents) {
                f.delete ();
            }
        }
        myObjects.delete();
    }

    @Test
    @DisplayName("Test if addToTree method works correctly")
    void testAddToTree() throws Exception {
        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.addToTree("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b", "");
        myTree.addToTree("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt", "");
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString ("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt\ntree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b"));
        //checks to make sure Tree file has been saved to objects folder with correct SHA
        assertTrue(checking.exists());
        Scanner scanner = new Scanner(checking);
        String fileContents = scanner.useDelimiter("\\A").next();
        scanner.close();
        
        //checks to make sure file contents were correctly saved to the tree file (tree/blob log)
        assertTrue(fileContents.contains ("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b") && fileContents.contains ("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt"));
    }
    @Test
    @DisplayName("Test if removeBlob method works correctly")
    void testRemoveBlob() throws Exception {
        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.addToTree("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt", "");
        myTree.removeBlob("file1.txt");
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString (""));
        //checks to make sure empty Tree file has been saved correctly w/ correct SHA after blob entry removal
        assertTrue(checking.exists());
        BufferedReader br = new BufferedReader(new FileReader("./objects/" + getSHA1OfString ("")));
        boolean works = false;     
        if (br.readLine() == null) {
            works = true;
        }
        br.close ();
        //checks to make sure saved Tree file is empty after blob entry removal
        assertTrue(works);
    }

    @Test
    @DisplayName("Test if removeTree method works correctly")
    void testRemoveTree() throws Exception {
        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.addToTree("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b", "");
        myTree.removeTree("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString (""));
        //checks to make sure Blob file has been saved w/ correct SHA (SHA of empty file) after tree entry removal
        assertTrue(checking.exists());
        BufferedReader br = new BufferedReader(new FileReader("./objects/" + getSHA1OfString ("")));
        boolean works = false;     
        if (br.readLine() == null) {
            works = true;
        }
        br.close();
        //checks to make sure saved Blob file is empty after blob entry removal
        assertTrue(works);
    }

    private String getSHA1OfString (String input) throws FileNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        //hashes file with SHA1 hash code into String called SHA1
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(input.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest())
        {
            formatter.format("%02x", b);
        }
        String SHA1 = formatter.toString();
        formatter.close();
        return SHA1;
    }

    @Test
    @DisplayName("Test if SHA1Name (class: Tree) method works correctly")
    void testSHA1NameTree () throws Exception {

        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.save ();
        //checks if method to get SHA1 encoding as String from a file gives same result as method that gives encoding as String from String. The latter was verified to work via online encoder comparison (getSHA1OfString (String )
        assertEquals(getSHA1OfString (""), myTree.SHA1FilePath("./objects/" + getSHA1OfString ("")));
    }


    @Test
    @DisplayName("Test if ByteToHex (class: Tree) method works correctly")
    void testByteToHexTree() throws Exception {
        String turnToBytes = "this will be converted to bytes!";
        Tree myTree = new Tree ();
        String myHex = myTree.byteToHexTree (turnToBytes.getBytes());
        String myHex2 = myHex.replaceAll("^(00)+", "");
        byte[] bytes = new byte[myHex2.length() / 2];
        for (int i = 0; i < myHex2.length(); i += 2)
        {
                bytes[i / 2] = (byte) ((Character.digit(myHex2.charAt(i), 16) << 4) + Character.digit(myHex2.charAt(i + 1), 16));
        }
        //checks if method to get SHA1 encoding as Hex from a file gives same result as alternative code that gives encoding as Hex from String. The alternative code was verified to work via online encoder comparison (getSHA1OfString (String ))
        assertEquals (getSHA1OfString(turnToBytes),getSHA1OfString (new String(bytes)));
    }

    @Test
    @DisplayName("Test if save method works correctly")
    void testSave() throws IOException, NoSuchAlgorithmException {
        Index i = new Index ();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString (""));
        //checks that Tree file has been created
        assertTrue(checking.exists());
        //checks that Tree file contains correct contents (Tree/Blob entries)
        assertEquals (checking.length(),0);
    }

    @Test
    @DisplayName("Test if add directories works with empty files")
    void testIfAddDirectoryCreatesDirectory() throws InvalidPathException, IOException 
    {
        Tree myTree = new Tree();
        myTree.addDirectory("./Directory");
        File treeDirectory = new File("./Directory");
        assertTrue(treeDirectory.exists());
    }

    @Test
    @DisplayName("Test if add directories works with only files")
    void testIfAddDirectoryWorksWithFiles() throws InvalidPathException, IOException 
    {

        File basicDirectory = new File ("basicDirectory");
        basicDirectory.mkdir();
        makeTextFile("./basicDirectory/textFile1", "hello");
        makeTextFile("./basicDirectory/textFile2", "hello2");
        makeTextFile("./basicDirectory/textFile3", "hello3");
       
        //testing addDirectory
        Tree myTree = new Tree ();
        Index i = new Index();
        i.initialize();

        myTree.addDirectory ("basicDirectory");
        File file1 = new File ("./objects/aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d");
        File file2 = new File ("./objects/0f1defd5135596709273b3a1a07e466ea2bf4fff");
        File file3 = new File ("./objects/53d001f65e513a8c9560a0a40b1b823ece93204c");

        //blobs exist
        assertTrue (file1.exists () && file2.exists () && file3.exists ());
       
        //blobs have the right stuff
        assertTrue (fileHas ("./objects/aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", "hello") 
        && fileHas("./objects/0f1defd5135596709273b3a1a07e466ea2bf4fff", "hello2") 
        && fileHas("./objects/53d001f65e513a8c9560a0a40b1b823ece93204c", "hello3"));
        
        // inside tree exists
        File file = new File("./objects/9e620df593ff4550f30e10b11113c3747fda9b94"); 
        assertTrue (file.exists ());
        //inside tree has the right content
        assertTrue (fileHas ("./objects/9e620df593ff4550f30e10b11113c3747fda9b94", "blob : aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d : textFile1") 
        && fileHas("./objects/9e620df593ff4550f30e10b11113c3747fda9b94", "blob : 0f1defd5135596709273b3a1a07e466ea2bf4fff : textFile2")
         && fileHas("./objects/9e620df593ff4550f30e10b11113c3747fda9b94", "blob : 53d001f65e513a8c9560a0a40b1b823ece93204c : textFile3"));

    }

    @Test
    @DisplayName("Test advanced directory")
    void testIfAddDirectoryWorksWithFilesAndDirectories() throws InvalidPathException, IOException 
    {

        File AdvancedDirectory = new File ("AdvancedDirectory");
        AdvancedDirectory.mkdir();
        File InsideFolder = new File("./AdvancedDirectory/InsideFolder");
        InsideFolder.mkdirs();
        
        makeTextFile("./AdvancedDirectory/InsideFolder/example3.txt", "hello3");
        makeTextFile("./AdvancedDirectory/example1.txt", "hello");
        makeTextFile("./AdvancedDirectory/example2.txt", "hello2");

        
       
        //testing addDirectory
        Tree myTree = new Tree ();
        Index i = new Index();
        i.initialize();

        myTree.addDirectory ("AdvancedDirectory");
        File file1 = new File ("./objects/aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d");
        File file2 = new File ("./objects/0f1defd5135596709273b3a1a07e466ea2bf4fff");
        File file3 = new File ("./objects/53d001f65e513a8c9560a0a40b1b823ece93204c");

        //blobs exist
        assertTrue (file1.exists () && file2.exists () && file3.exists ());
       
        //blobs have the right stuff
        assertTrue (fileHas ("./objects/aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", "hello") 
        && fileHas("./objects/0f1defd5135596709273b3a1a07e466ea2bf4fff", "hello2") 
        && fileHas("./objects/53d001f65e513a8c9560a0a40b1b823ece93204c", "hello3"));
        
        // inside tree exists
        File file = new File("./objects/24671a88ec8152c7a8ecf069d09521cd1d1a3bf4"); 
        assertTrue (file.exists ());
        //check if insideTreeHasBlobInfo
        assertTrue(fileHas("./objects/24671a88ec8152c7a8ecf069d09521cd1d1a3bf4","blob : 53d001f65e513a8c9560a0a40b1b823ece93204c : example3.txt"));

    }

    private void makeTextFile (String path, String contents) throws IOException
    {
        File theFile = new File (path);
        theFile.createNewFile ();
        FileWriter writer = new FileWriter(path,false);
        PrintWriter out = new PrintWriter(writer);
        out.print (contents);
        writer.close ();
        out.close ();
    }


    private boolean fileHas (String filePathString, String contents) throws IOException
    {
       Scanner scan = new Scanner(new File (filePathString));
        scan.useDelimiter("\\A");  
        String content = scan.next();     
        scan.close ();
        return content.contains (contents);
    }


    public String getSha1(String input) throws IOException
    {
        String dataAsString = input;

        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(dataAsString.getBytes("UTF-8"));
            sha1 = byteToHexTree(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    //helper for above method
    public String byteToHexTree(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
