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

         File toAdd3 = new File("toAdd3");
        PrintWriter out3 = new PrintWriter("toAdd3");
        out3.print("3");
        out3.close();

        File toAdd4 = new File("toAdd4");
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
        Commit c = new Commit("Oren", "first commit");
        
        i.addFile("toAdd3");
        i.addFile("toAdd4");
        i.addDirectory("newFolder");

        Commit c2 = new Commit(c.getSha1(), "Oren", "second commit");

        File blob1 = new File("objects/356a192b7913b04c54574d18c28d46e6395428ab");
        File blob2 = new File("objects/da4b9237bacccdf19c0760cab7aec4a8359010b0");
        File blob3 = new File("objects/77de68daecd823babbb58edb1c8e14d7106e83bb");
        File blob4 = new File("objects/1b6453892473a467d07372d45eb05abc2031647a");
        File blob5 = new File("objects/ac3478d69a3c81fa62e60f5c3696165a4e5e6ac4");
        assertTrue(blob1.exists() && blob2.exists() && blob3.exists() && blob4.exists() && blob5.exists());

        File directoryTree = new File("objects/680541cb5386e6a543f4c8378f6ef71dca778347");
        String directoryTreeInfo = TextInFile(directoryTree);
        File treeForFirstCommit = new File("objects/ffb9a45711a60ea105d8fc3ab5cb8796faf73148");
        String treeForFirstCommitInfo = TextInFile(treeForFirstCommit);
        File treeForSecondCommit =  new File("objects/dbc1b8aca159d8c9840255ada59b0d68c480f672");
        String treeForSecondCommitInfo = TextInFile(treeForSecondCommit);
        assertTrue(directoryTree.exists() && treeForFirstCommit.exists() && treeForSecondCommit.exists());
        assertTrue(directoryTreeInfo.contains("blob : ac3478d69a3c81fa62e60f5c3696165a4e5e6ac4 : toAdd5"));
        assertTrue(treeForFirstCommitInfo.contains("blob : 356a192b7913b04c54574d18c28d46e6395428ab : toAdd1\n" + //
                "blob : da4b9237bacccdf19c0760cab7aec4a8359010b0 : toAdd2"));
        assertTrue(treeForSecondCommitInfo.contains("tree : ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n" + //
                "tree : 680541cb5386e6a543f4c8378f6ef71dca778347 : newFolder\n" + //
                "blob : 77de68daecd823babbb58edb1c8e14d7106e83bb : toAdd3\n" + //
                "blob : 1b6453892473a467d07372d45eb05abc2031647a : toAdd4"));

        File firstCommit = new File("objects/782941d47d2ddfed342024dd1c68e6b0766556b7");
        String firstCommitInfo = TextInFile(firstCommit);
        File secondCommit = new File("objects/794d704f1e02f08a63f532762a1d423c904585f");
        String secondCommitInfo = TextInFile(secondCommit);
        assertTrue(firstCommit.exists() && secondCommit.exists());
        assertTrue(firstCommitInfo.contains("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n794d704f1e02f08a63f532762a1d423c904585f\nOren\n" + c2.getDate() +"\nfirst commit"));
        assertTrue(secondCommitInfo.contains("dbc1b8aca159d8c9840255ada59b0d68c480f672\n782941d47d2ddfed342024dd1c68e6b0766556b7\n\nOren\n" + c2.getDate() + "\nsecond commit"));
    
    }

    @Test
    void testFourCommits() throws IOException
    {
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

         File toAdd3 = new File("toAdd3");
        PrintWriter out3 = new PrintWriter("toAdd3");
        out3.print("3");
        out3.close();

        File toAdd4 = new File("toAdd4");
        PrintWriter out4= new PrintWriter("toAdd4");
        out4.print("4");
        out4.close();

        File newFolder = new File("newFolder");
        newFolder.mkdir();
            File toAdd5 = new File("newFolder/toAdd5");
            PrintWriter out5 = new PrintWriter("newFolder/toAdd5");
            out5.print("5");
            out5.close();

        File newFolder2 = new File("newFolder2");
        newFolder2.mkdir();
            File toAdd6 = new File("newFolder2/toAdd6");
            PrintWriter out6 = new PrintWriter("newFolder2/toAdd6");
            out6.print("6");
            out6.close();

        File newFolder3 = new File("newFolder3");
        newFolder3.mkdir();

        File toAdd7 = new File("toAdd7");
        PrintWriter out7 = new PrintWriter("toAdd7");
        out7.print("7");
        out7.close();

        File toAdd8 = new File("toAdd8");
        PrintWriter out8 = new PrintWriter("toAdd8");
        out8.print("8");
        out8.close();

        File toAdd9 = new File("toAdd9");
        PrintWriter out9 = new PrintWriter("toAdd9");
        out9.print("9");
        out9.close();

        File toAdd10 = new File("toAdd10");
        PrintWriter out10 = new PrintWriter("toAdd10");
        out10.print("10");
        out10.close();

        i.addFile("toAdd1");
        i.addFile("toAdd2");
        Commit c1 = new Commit("Oren", "first commit");

        i.addFile("toAdd3");
        i.addFile("toAdd4");
        i.addDirectory("newFolder");
        Commit c2 = new Commit(c1.getSha1(), "Oren", "second commit");

        i.addDirectory("newFolder2");
        i.addDirectory("newFolder3");
        i.addFile("toAdd7");
        i.addFile("toAdd8");
        Commit c3 = new Commit(c2.getSha1(), "Oren", "third commit");

        i.addFile("toAdd9");
        i.addFile("toAdd10");
        Commit c4 = new Commit(c3.getSha1(), "Oren", "fourth commit");
        

        File blob1 = new File("objects/356a192b7913b04c54574d18c28d46e6395428ab");
        File blob2 = new File("objects/da4b9237bacccdf19c0760cab7aec4a8359010b0");
        File blob3 = new File("objects/77de68daecd823babbb58edb1c8e14d7106e83bb");
        File blob4 = new File("objects/1b6453892473a467d07372d45eb05abc2031647a");
        File blob5 = new File("objects/ac3478d69a3c81fa62e60f5c3696165a4e5e6ac4");
        File blob6 = new File("objects/c1dfd96eea8cc2b62785275bca38ac261256e278");
        File blob7 = new File("objects/902ba3cda1883801594b6e1b452790cc53948fda");
        File blob8 = new File("objects/fe5dbbcea5ce7e2988b8c69bcfdfde8904aabc1f");
        File blob9 = new File("objects/0ade7c2cf97f75d009975f4d720d1fa6c19f4897");
        File blob10 = new File("objects/b1d5781111d84f7b3fe45a0852e59758cd7a87e5");
        assertTrue(blob1.exists() && blob2.exists() && blob3.exists() && blob4.exists() && blob5.exists());
        assertTrue(blob6.exists() && blob7.exists() && blob8.exists() && blob9.exists() && blob10.exists());

        File newFolderTree = new File("objects/680541cb5386e6a543f4c8378f6ef71dca778347");
        String newFolderTreeInfo = TextInFile(newFolderTree);
        File newFolderTree2 = new File("objects/2aac0f39f4e62df95f1f4554f8e07cee855fc2d4");
        String newFolderTree2Info = TextInFile(newFolderTree2);
        File newFolderTree3 = new File("objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
        String newFolderTree3Info = TextInFile(newFolderTree3);
        assertTrue(newFolderTree.exists() && newFolderTree3.exists() && newFolderTree3.exists());
        assertTrue(newFolderTreeInfo.contains("blob : ac3478d69a3c81fa62e60f5c3696165a4e5e6ac4 : toAdd5"));
        assertTrue(newFolderTree2Info.contains("blob : c1dfd96eea8cc2b62785275bca38ac261256e278 : toAdd6"));
        assertTrue(newFolderTree3Info.length() == 0);

        File treeForFirstCommit = new File("objects/ffb9a45711a60ea105d8fc3ab5cb8796faf73148");
        String treeForFirstCommitInfo = TextInFile(treeForFirstCommit);
        File treeForSecondCommit =  new File("objects/dbc1b8aca159d8c9840255ada59b0d68c480f672");
        String treeForSecondCommitInfo = TextInFile(treeForSecondCommit);
        File treeForThirdCommit = new File("objects/0cfef0b84c8792df110b720a95d86cb30d425460");
        String treeForThirdCommitInfo = TextInFile(treeForThirdCommit);
        File treeForFourthCommit = new File("objects/4f43dc033b3a34514864d2a53cbbfb93ea32ca3d");
        String treeForFourthCommitInfo = TextInFile(treeForFourthCommit);
        assertTrue(treeForFirstCommitInfo.contains("blob : 356a192b7913b04c54574d18c28d46e6395428ab : toAdd1\n" + //
                "blob : da4b9237bacccdf19c0760cab7aec4a8359010b0 : toAdd2"));
        assertTrue(treeForSecondCommitInfo.contains("tree : ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n" + //
                "tree : 680541cb5386e6a543f4c8378f6ef71dca778347 : newFolder\n" + //
                "blob : 77de68daecd823babbb58edb1c8e14d7106e83bb : toAdd3\n" + //
                "blob : 1b6453892473a467d07372d45eb05abc2031647a : toAdd4"));
        assertTrue(treeForThirdCommitInfo.contains("tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : newFolder3\ntree : 2aac0f39f4e62df95f1f4554f8e07cee855fc2d4 : newFolder2\ntree : dbc1b8aca159d8c9840255ada59b0d68c480f672\nblob : fe5dbbcea5ce7e2988b8c69bcfdfde8904aabc1f : toAdd8\nblob : 902ba3cda1883801594b6e1b452790cc53948fda : toAdd7"));
        assertTrue(treeForFourthCommitInfo.contains("blob : b1d5781111d84f7b3fe45a0852e59758cd7a87e5 : toAdd10\ntree : 0cfef0b84c8792df110b720a95d86cb30d425460\nblob : 0ade7c2cf97f75d009975f4d720d1fa6c19f4897 : toAdd9"));

        File firstCommit = new File("objects/782941d47d2ddfed342024dd1c68e6b0766556b7");
        String firstCommitInfo = TextInFile(firstCommit);
        File secondCommit = new File("objects/794d704f1e02f08a63f532762a1d423c904585f");
        String secondCommitInfo = TextInFile(secondCommit);
        File thirdCommit = new File("objects/24eb8a329c23098ed176228e24f5a8edbf592c14");
        String thirdCommitInfo = TextInFile(thirdCommit);
        File fourthCommit = new File("objects/c0724fd40208be21ca786391c65e25b359273654");
        String fourthCommitInfo = TextInFile(fourthCommit);
        assertTrue(firstCommit.exists() && secondCommit.exists() && thirdCommit.exists() && fourthCommit.exists());
        assertTrue(firstCommitInfo.contains("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n794d704f1e02f08a63f532762a1d423c904585f\nOren\n" + c2.getDate() +"\nfirst commit"));
        assertTrue(secondCommitInfo.contains("dbc1b8aca159d8c9840255ada59b0d68c480f672\n782941d47d2ddfed342024dd1c68e6b0766556b7\n24eb8a329c23098ed176228e24f5a8edbf592c14\nOren\n" + c2.getDate() + "\nsecond commit"));
        assertTrue(thirdCommitInfo.contains("0cfef0b84c8792df110b720a95d86cb30d425460\n794d704f1e02f08a63f532762a1d423c904585f\nc0724fd40208be21ca786391c65e25b359273654\nOren\n" + c3.getDate() + "\n"));
        assertTrue(fourthCommitInfo.contains("4f43dc033b3a34514864d2a53cbbfb93ea32ca3d\n24eb8a329c23098ed176228e24f5a8edbf592c14\n\nOren\n" + c4.getDate() + "\nfourth commit"));
    }
    
    private String TextInFile(File f) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        while (br.ready())
        {
            sb.append(br.readLine() + "\n");
        }
        if(sb.length() > 0)
        {
            sb.setLength(sb.length()-1);
        }
        return sb.toString();
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