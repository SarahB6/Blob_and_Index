import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    //tests if a valid date is created
    //kinda depends on the testing date lol
    void testGetDate() throws IOException {
        Commit com = new Commit("prevSha", "Oren", "testing tree creation");
    }

    @Test
    //tests if the correct sha for the commit is being created
    void testGetSha1() throws IOException {
        //tests case with previous sha and next sha included
        Commit com = new Commit("prevSha", "nextSha","Oren", "testing tree creation");
        String sha = com.getSha1();
        assertEquals(sha, "bde1f5e857d552d2373df5b0fd49f41024cf9a17");

        //tests case with next sha ommited
        Commit com2 = new Commit("prevSha", "Oren", "testing tree creation");
        String sha2 = com2.getSha1();
        assertEquals(sha2, "1cff8927c29edcfc3a4b07a113dc1995c5fda5d");
    }

    @Test
    //simple test to see if hash method is working
    void testGetStringHash() {
        String sha1 = Commit.getStringHash("hello world");
        assertEquals("2aae6c35c94fcfb415dbe95f408b9ce91ee846ed", sha1);
    }

    @Test
    void testWriteToFile() throws IOException {
        //tests case with previous sha and next sha included
        Commit com = new Commit("prevSha", "nextSha","Oren", "testing tree creation");
        com.writeToFile();
        File comFile = new File("./objects/bde1f5e857d552d2373df5b0fd49f41024cf9a17");
        assertTrue(comFile.exists());

        Path comFilePath = Path.of("./objects/bde1f5e857d552d2373df5b0fd49f41024cf9a17");
        String comContents = Files.readString(comFilePath);
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709\nprevSha\nnextSha\nOren\n2023-09-21\ntesting tree creation", comContents);

        //tests case with next sha ommited
        Commit com2 = new Commit("prevSha","Oren", "testing tree creation");
        com2.writeToFile();
        File comFile2 = new File("./objects/1cff8927c29edcfc3a4b07a113dc1995c5fda5d");
        assertTrue(comFile2.exists());

        Path comFilePath2 = Path.of("./objects/1cff8927c29edcfc3a4b07a113dc1995c5fda5d");
        String comContents2 = Files.readString(comFilePath2);
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709\nprevSha\n\nOren\n2023-09-21\ntesting tree creation", comContents2);
        
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
