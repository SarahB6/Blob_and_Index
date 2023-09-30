import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
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

public class BlobTester {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {

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
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {
        Blob myBlob = null;
        try {

            // Manually create the files and folders before the 'testAddFile'
            Index i = new Index ();
            i.initialize ();
            File myTesterText = new File ("myTesterText.txt");
            myTesterText.createNewFile();
            PrintWriter out = new PrintWriter("testerText");
            out.print("this is some testertext!");
            out.close();
            myBlob = new Blob ("testerText");

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        String SHA1 = myBlob.SHA1NameBlob ("./testerText");

        File file_junit1 = new File("./objects/" + SHA1);
        // Make sure Blob exists in the objects folder
        assertTrue("Blob file to add not found", file_junit1.exists());

        // Read new file contents
        Scanner scanner2 = new Scanner(new File("./objects/" + SHA1));
        String blobFileContents = scanner2.useDelimiter("\\A").next();
        scanner2.close();

        // Read original file contents
        Scanner scanner3 = new Scanner(new File("./testerText"));
        String originalFileContents = scanner3.useDelimiter("\\A").next();
        scanner3.close();
        
        //Makes sure Blob file contents match original file contents 
        assertEquals(blobFileContents,originalFileContents);        
    }

    @Test
    @DisplayName("Test if SHA1Name (class: Blob) method works correctly")
    void testSHA1NameBlob () throws Exception {

        // Run the person's code
        Index i = new Index();
        i.initialize ();
        Tree myTree = new Tree ();
        myTree.save ();

        //Ensures that the SHA1NameBlob method generates the same SHA1 String for myTree as getSHA1OfString does directly using file contents (empty) and String --> SHA1 converter
        assertEquals(getSHA1OfString (""), myTree.SHA1FilePath("./objects/" + getSHA1OfString ("")));
    }

    @Test
    @DisplayName("Test if ByteToHex (class: Blob) method works correctly")
    void testByteToHexBlob() throws Exception {
        String turnToBytes = "this will be converted to bytes!";
        Tree myTree = new Tree ();
        String myHex = myTree.byteToHexTree (turnToBytes.getBytes());
        String myHex2 = myHex.replaceAll("^(00)+", "");
        byte[] bytes = new byte[myHex2.length() / 2];
        for (int i = 0; i < myHex2.length(); i += 2)
        {
        bytes[i / 2] = (byte) ((Character.digit(myHex2.charAt(i), 16) << 4) + Character.digit(myHex2.charAt(i + 1), 16));
        }
        
        //Makes sure SHA1 helper method properly generates hex given input String through 2 different methods of calculating hex
        assertEquals (getSHA1OfString(turnToBytes),getSHA1OfString (new String(bytes)));
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
}