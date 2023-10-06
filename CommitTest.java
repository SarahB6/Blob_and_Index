import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    void testFirstCommit() throws IOException {
        //runs the code
        Index i = new Index();
        i.initialize();
        File f = new File("./index");
        Commit com = new Commit("Oren", "testing tree creation");
        //String sha = com.createTree();
        String sha = com.getSha1();
        //tests if output is correct and file is created
       // assertEquals("5a6f1bc395350d82693ae2a4fac28498f8226e8c", sha);
        File commitFile = new File("./objects/" + sha);
        assertTrue(commitFile.exists());
    }

        @Test
    //tests if a tree is created and the output is correct 
    void testFirstCommitWithFiles() throws IOException {
        //runs the code
        Index i = new Index();
        i.initialize();
        File f = new File("./index");
        File toAdd1 = new File("toAdd1");
        PrintWriter out = new PrintWriter("toAdd1");
        out.print("1");
        out.close();

        File toAdd2 = new File("toAdd2");
        PrintWriter out2 = new PrintWriter("toAdd2");
        out2.print("2");
        out2.close();

        i.addFile(toAdd1.getPath());
        i.addFile(toAdd2.getPath());

        Commit com = new Commit("Oren", "testing one with files");
        String sha = com.getSha1();
        File commitFile = new File("./objects/" + sha);
        StringBuilder commitFileContent = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(commitFile));
        while(br.ready())
        {
            commitFileContent.append(br.readLine() + "\n");
        }
        br.close();

        File blob1 = new File("objects/356a192b7913b04c54574d18c28d46e6395428ab");
        File blob2 = new File("objects/da4b9237bacccdf19c0760cab7aec4a8359010b0");
        assertTrue(blob1.exists() && blob2.exists());

        File treeFile =  new File("objects/ffb9a45711a60ea105d8fc3ab5cb8796faf73148");
        assertTrue(treeFile.exists());
         StringBuilder treeFileContentsb = new StringBuilder();
        BufferedReader br2 = new BufferedReader(new FileReader(treeFile));
        while(br2.ready())
        {
            treeFileContentsb.append(br2.readLine() + "\n");
        }
        br2.close();
        String treeFileContent = treeFileContentsb.toString();
        assertTrue(treeFileContent.contains("blob : 356a192b7913b04c54574d18c28d46e6395428ab : toAdd1\n" + //
                "blob : da4b9237bacccdf19c0760cab7aec4a8359010b0 : toAdd2"));
        
        assertTrue(commitFileContent.toString().contains("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n\nOren\n2023-10-06\ntesting one with files"));



        
    }

    @Test
    //tests if a valid date is created
    //kinda depends on the testing date lol
    void testTwoCommits() throws IOException {
        Index i = new Index();
        i.initialize(); 
        
         File toAdd1 = new File("toAdd1");
        PrintWriter out = new PrintWriter("toAdd1");
        out.print("1");
        out.close();

        File toAdd2 = new File("toAdd2");
        PrintWriter out2 = new PrintWriter("toAdd2");
        out2.print("2");
        out2.close();

         File toAdd3 = new File("toAdd1");
        PrintWriter out3 = new PrintWriter("toAdd3");
        out.print("3");
        out3.close();

        File toAdd4 = new File("toAdd2");
        PrintWriter out4= new PrintWriter("toAdd4");
        out4.print("4");
        out4.close();

        File newFolder = new File("newFolder");
        newFolder.mkdir();
            File toAdd5 = new File("newFolder/toAdd5");
            PrintWriter out5 = new PrintWriter("newFolder/toAdd5");
            out5.print("5");
            out5.close();

        //Add first 2 to first commit
        i.addFile("toAdd1");
        i.addFile("toAdd2");
        Commit c = new Commit("Oren", "testing tree creation");
        
        i.addFile("toAdd3");
        i.addFile("toAdd4");
        i.addDirectory("newFolder");

        Commit c2 = new Commit(c.getSha1(), "Oren", "test second commit with folder");





    }

    @Test
    //simple test to see if hash method is working
    void testGetStringHash() {
        String sha1 = Commit.getStringHash("hello world");
        assertEquals("2aae6c35c94fcfb415dbe95f408b9ce91ee846ed", sha1);
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
}