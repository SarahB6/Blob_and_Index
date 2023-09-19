
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTester 
{
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        //writing string to file
        PrintWriter out = new PrintWriter("./junit_example_file_data.txt");
        out.print("test file contents");
        out.close();
        //deleting index
        File myIndex = new File ("./index");
        myIndex.delete();
        //deleting objects
        File myObjects = new File ("./objects");
        File[] contents = myObjects.listFiles();
        if (contents != null) {
            for (File f : contents) {
                f.delete ();
            }
        }
        myObjects.delete();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        File junitExample = new File ("junit_example_file_data.txt");
        junitExample.delete();
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
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @Test
    @DisplayName("Test if addToTree method works correctly")
    void testAddToTree() throws Exception {
        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.addToTree("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        myTree.addToTree("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString ("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt\ntree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b"));
        assertTrue(checking.exists());
        Scanner scanner = new Scanner(checking);
        String fileContents = scanner.useDelimiter("\\A").next();
        scanner.close();
        assertTrue(fileContents.contains ("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b") && fileContents.contains ("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt"));
    }
    @Test
    @DisplayName("Test if removeBlob method works correctly")
    void testRemoveBlob() throws Exception {
        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.addToTree("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        myTree.removeBlob("file1.txt");
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString (""));
        assertTrue(checking.exists());
        BufferedReader br = new BufferedReader(new FileReader("./objects/" + getSHA1OfString ("")));
        boolean works = false;     
        if (br.readLine() == null) {
            works = true;
        }
        br.close ();
        assertTrue(works);
    }

    @Test
    @DisplayName("Test if removeTree method works correctly")
    void testRemoveTree() throws Exception {
        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.addToTree("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        myTree.removeTree("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString (""));
        assertTrue(checking.exists());
        BufferedReader br = new BufferedReader(new FileReader("./objects/" + getSHA1OfString ("")));
        boolean works = false;     
        if (br.readLine() == null) {
            works = true;
        }
        br.close();
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
        assertEquals(getSHA1OfString (""), myTree.SHA1NameTree ("./objects/" + getSHA1OfString ("")));
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
        assertEquals (getSHA1OfString(turnToBytes),getSHA1OfString (new String(bytes)));


        //byte[] bytes = javax.xml.bind.DatatypeConverter.parseHexBinary(myHex);
        //String result= new String(bytes, "UTF-8");
        /*
        byte[] bytes = Hex.decodeHex(myHex.toCharArray());
        String lastString = new String(bytes, "UTF-8");
        assertEquals (getSHA1OfString(turnToBytes),myHex);
        */
    }

    @Test
    void testSave() throws IOException, NoSuchAlgorithmException {
        Index i = new Index ();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.save();
        File checking = new File ("./objects/" + getSHA1OfString (""));
        assertTrue(checking.exists());
        assertEquals (checking.length(),0);
    }
}

