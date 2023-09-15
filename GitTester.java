


import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GitTester 
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
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        Index i = new Index();
        //TestHelper.runTestSuiteMethods("testInitialize");
        i.initialize ();

        // check if the file exists
        File myFile = new File("./index");
        Path path = Paths.get("./objects");

        assertTrue(myFile.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {

        try {

            // Manually create the files and folders before the 'testAddFile'
            Index i = new Index ();
            i.initialize ();
            File myTesterText = new File ("myTesterText.txt");
            File myTesterText2 = new File ("myTesterText2.txt");
            File myTesterText3 = new File ("myTesterText3.txt");
            

            // TestHelper.runTestSuiteMethods("testCreateBlob", file1.getName());

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        Blob myBlob = new Blob ("testerText");

        //sarahs blob is kind of messed up (& couldn't use myBlob in try-catch) so just copy-pasting my own sha1 method here:
        /*
        Scanner scanner = new Scanner(new File("/Users/chrisheadley/Desktop/Comp Sci/Blob_and_Index/testerText"));
        String myString = scanner.useDelimiter("\\A").next();
        scanner.close();
        //hashes file with SHA1 hash code into String called SHA1
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(myString.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest())
        {
            formatter.format("%02x", b);
        }
        String SHA1 = formatter.toString();
        formatter.close();
        */
        String SHA1 = myBlob.SHA1Name ("./testerText");

        // Check blob exists in the objects folder
        File file_junit1 = new File("./objects/" + SHA1);
        assertTrue("Blob file to add not found", file_junit1.exists());

        // Read new file contents

        Scanner scanner2 = new Scanner(new File("./objects/" + SHA1));
        String indexFileContents = scanner2.useDelimiter("\\A").next();
        scanner2.close();

        // Read original file contents
        Scanner scanner3 = new Scanner(new File("/Users/chrisheadley/Desktop/Comp Sci/Blob_and_Index/testerText"));
        String originalFileContents = scanner3.useDelimiter("\\A").next();
        scanner3.close();
        assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
                originalFileContents);
                
    }
}
