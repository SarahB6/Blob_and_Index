import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTester {

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
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        Index i = new Index();
        //TestHelper.runTestSuiteMethods("testInitialize");
        i.initialize ();

        // check if the file exists
        File myFile = new File("./index");
        Path path = Paths.get("./objects");

        //makes sure index file exists in corret location
        assertTrue(myFile.exists());

        //makes sure objects folder exists in correct location
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("Test addFile method works correctly")
    void testAddFile() throws IOException, NoSuchAlgorithmException {
        File myTesterText = new File ("myTesterText.txt");
        myTesterText.createNewFile();
        PrintWriter out = new PrintWriter("myTesterText.txt");
        out.print("this is some testertext!");
        out.close();
        Index i = new Index ();
        i.initialize();
        i.addFile("myTesterText.txt");
        File checking = new File ("index");
        Scanner scanner = new Scanner(checking);
        String fileContents = scanner.useDelimiter("\\A").next();
        scanner.close();

        //makes sure addFile properly adds an entry to the index file
        assertTrue(fileContents.contains ("myTesterText.txt : d97be938d56fa50d14940b3f1ce7cabfa830afb5"));
    }

    @Test
    @DisplayName("Test if alrInIndex method works correctly")
    void testAlrInIndex() throws IOException {
        File myTesterText = new File ("myTesterText.txt");
        myTesterText.createNewFile();
        PrintWriter out = new PrintWriter("myTesterText.txt");
        out.print("this is some testertext!");
        out.close();
        Index i = new Index ();
        i.initialize ();
        i.addFile ("myTesterText.txt");
        
        //checks whether "already in index" method positive for duplicates (it should be)
        assertTrue (i.alrInIndex ("myTesterText.txt"));
        i.removeFile ("myTesterText.txt");

        //checks whether "already in index" method negative when duplicate removed from inded (it should be)
        assertFalse (i.alrInIndex ("myTesterText.txt"));
    }

    @Test
    @DisplayName("Test if removeFile method works correctly")
    void testRemoveFile() throws NoSuchAlgorithmException, IOException {
        File myTesterText = new File ("myTesterText.txt");
        myTesterText.createNewFile();
        PrintWriter out = new PrintWriter("myTesterText.txt");
        out.print("this is some testertext!");
        out.close();
        Index i = new Index ();
        i.initialize();
        i.addFile("myTesterText.txt");
        i.removeFile ("myTesterText.txt");
        File checking = new File ("index");

        //makes sure file entry has been removed from index
        assertTrue(checking.length () == 0);
        File shouldNotBeDeleted = new File ("./objects/d97be938d56fa50d14940b3f1ce7cabfa830afb5");

        //checks to see if removed file's Blob has been deleted from the objects folder (it shouldn't be deleted)
        assertTrue (shouldNotBeDeleted.exists ());
    }

    @Test
    @DisplayName("Test if adding multiple files works correctly (extra credit)")
    void testAddingMultipleFilesEC () throws IOException, NoSuchAlgorithmException {
        //making 2 files
        File myTesterText1 = new File ("myTesterText1.txt");
        myTesterText1.createNewFile();
        PrintWriter out1 = new PrintWriter("myTesterText1.txt");
        out1.print("this is my first testertext!");
        out1.close();
        File myTesterText2 = new File ("myTesterText2.txt");
        myTesterText2.createNewFile();
        PrintWriter out2 = new PrintWriter("myTesterText2.txt");
        out2.print("this is my second testertext!");
        out2.close();

        Index i = new Index ();
        i.initialize();
        i.addFile("myTesterText1.txt");
        i.addFile ("myTesterText2.txt");

        //duplicate
        i.addFile ("myTesterText1.txt");

        //making sure no more than 2 lines
        int count = 0;
        Scanner sc = new Scanner(new File ("index"));
        while(sc.hasNextLine()) {
            sc.nextLine();
            count++;
        }
        sc.close ();

        File checking = new File ("index");
        Scanner scanner = new Scanner(checking);
        String fileContents = scanner.useDelimiter("\\A").next();
        scanner.close();

        //makes sure index has correct contents and length after addition of multiple files
        assertTrue(fileContents.contains ("myTesterText1.txt : 23f1a98a0bf8a74a5be0b7f81cec8b20357d0057") && count == 2);
    }

    @Test
    @DisplayName("Test if removing multiple files works correctly (extra credit)")
    void testRemovingMultipleFilesEC () throws IOException, NoSuchAlgorithmException {
        //making 2 files
        File myTesterText1 = new File ("myTesterText1.txt");
        myTesterText1.createNewFile();
        PrintWriter out1 = new PrintWriter("myTesterText1.txt");
        out1.print("this is my first testertext!");
        out1.close();
        File myTesterText2 = new File ("myTesterText2.txt");
        myTesterText2.createNewFile();
        PrintWriter out2 = new PrintWriter("myTesterText2.txt");
        out2.print("this is my second testertext!");
        out2.close();

        //adding files
        Index i = new Index ();
        i.initialize();
        i.addFile("myTesterText1.txt");
        i.addFile ("myTesterText2.txt");

        //removing files
        i.removeFile ("myTesterText1.txt");
        i.removeFile ("myTesterText2.txt");
        File checking = new File ("index");

        //makes sure index contains nothing after all entries have been removed
        assertTrue(checking.length () == 0);
        File shouldNotBeDeleted1 = new File ("./objects/23f1a98a0bf8a74a5be0b7f81cec8b20357d0057");
        File shouldNotBeDeleted2 = new File ("./objects/0c38c3c03a5cd84b59c1a100c9a09225a44f8f02");
        
        //makes sure two removed files have not had their corresponding Blobs deleted from the objects folder
        assertTrue (shouldNotBeDeleted1.exists () && shouldNotBeDeleted2.exists ());
    }
}
