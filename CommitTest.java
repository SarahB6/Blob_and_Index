import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTest {
    @BeforeAll
    static void setUpBeforeClass(){
        File objectDirectory = new File("objects");
        if(!objectDirectory.exists()){
            objectDirectory.mkdir();
        }
    }

    @AfterAll
    static void tearDownAfterClass(){
        File objectDirectory = new File("objects");
        if(objectDirectory.exists()){
            deleteDirectory(objectDirectory);
        }
    }
    @Test
    //tests if a tree is created and the output is correct 
    void testCreateTree() throws IOException {
        //runs the code
        Commit com = new Commit("prevSha", "Oren", "testing tree creation");
        String sha = com.createTree();

        //tests if output is correct and file is created
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", sha);
        File treeFile = new File("./objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
        assertTrue(treeFile.exists());
    }

    @Test
    void testGetSha1() {
        
    }

    @Test
    void testGetStringHash() {

    }

    @Test
    void testWriteToFile() {

    }

    //helper method to remove an entire directory and all files contained in it
    private static void deleteDirectory(File file)
    {
        // store all the paths of files and folders present
        // inside directory
        for (File subfile : file.listFiles()) {
 
            // if it is a subfolder,e.g Rohan and Ritik,
            //  recursively call function to empty subfolder
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
 
            // delete files and empty subfolders
            subfile.delete();
        }
    }
}
