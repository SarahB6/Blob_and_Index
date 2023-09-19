import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTester {

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
    void testAddFile() {
        
    }

    @Test
    void testAlrInIndex() {
        
    }

    @Test
    void testRemoveFile() {
        
    }
}
