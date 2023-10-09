import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

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
        
        assertTrue(commitFileContent.toString().contains("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n\nOren\n" + com.getDate() + "\ntesting one with files"));



        
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

        String SHA1OfFirstCommit = SHA1StringInput("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n\nOren\n" + c2.getDate() + "\nfirst commit");
        File firstCommit = new File("objects/" + SHA1OfFirstCommit);
        String firstCommitInfo = TextInFile(firstCommit);

        String SHA1OfSecondCommit = SHA1StringInput("dbc1b8aca159d8c9840255ada59b0d68c480f672\n" + SHA1OfFirstCommit + "\n\nOren\n" + c2.getDate() +"\nsecond commit");
        File secondCommit = new File("objects/" + SHA1OfSecondCommit);
        String secondCommitInfo = TextInFile(secondCommit);
        assertTrue(firstCommit.exists() && secondCommit.exists());
        assertTrue(firstCommitInfo.contains("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n" + SHA1OfSecondCommit + "\nOren\n" + c2.getDate() +"\nfirst commit"));
        assertTrue(secondCommitInfo.contains("dbc1b8aca159d8c9840255ada59b0d68c480f672\n" + SHA1OfFirstCommit + "\n\nOren\n" + c2.getDate() + "\nsecond commit"));
    
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

        String SHA1OfFirstCommit = SHA1StringInput("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n\nOren\n" + c2.getDate() +"\nfirst commit");
        File firstCommit = new File("objects/" + SHA1OfFirstCommit);
        String firstCommitInfo = TextInFile(firstCommit);
        
        String SHA1OfSecondCommit = SHA1StringInput("dbc1b8aca159d8c9840255ada59b0d68c480f672\n" + SHA1OfFirstCommit + "\n\nOren\n" + c2.getDate() +"\nsecond commit");
        File secondCommit = new File("objects/" + SHA1OfSecondCommit);
        String secondCommitInfo = TextInFile(secondCommit);

        String SHA1OfThirdCommit = SHA1StringInput("0cfef0b84c8792df110b720a95d86cb30d425460\n" + SHA1OfSecondCommit + "\n\nOren\n" + c2.getDate() +"\nthird commit");
        File thirdCommit = new File("objects/" + SHA1OfThirdCommit);
        String thirdCommitInfo = TextInFile(thirdCommit);
        
        String SHA1OfFourthCommit = SHA1StringInput("4f43dc033b3a34514864d2a53cbbfb93ea32ca3d\n" + SHA1OfThirdCommit + "\n\nOren\n" + c2.getDate() +"\nfourth commit");
        File fourthCommit = new File("objects/" + SHA1OfFourthCommit);
        String fourthCommitInfo = TextInFile(fourthCommit);
        assertTrue(firstCommit.exists() && secondCommit.exists() && thirdCommit.exists() && fourthCommit.exists());
        assertTrue(firstCommitInfo.contains("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n" + SHA1OfSecondCommit+ "\nOren\n" + c1.getDate() +"\nfirst commit"));
        assertTrue(secondCommitInfo.contains("dbc1b8aca159d8c9840255ada59b0d68c480f672\n" + SHA1OfFirstCommit + "\n" + SHA1OfThirdCommit + "\nOren\n" + c2.getDate() + "\nsecond commit"));
        assertTrue(thirdCommitInfo.contains("0cfef0b84c8792df110b720a95d86cb30d425460\n" + SHA1OfSecondCommit + "\n" + SHA1OfFourthCommit + "\nOren\n" + c3.getDate() + "\nthird commit"));
        assertTrue(fourthCommitInfo.contains("4f43dc033b3a34514864d2a53cbbfb93ea32ca3d\n" + SHA1OfThirdCommit + "\n\nOren\n" + c4.getDate() + "\nfourth commit"));
    }
    
    @Test
    void testFiveCommitsWithDeleteAndEdit() throws IOException
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
        BufferedWriter bw1 = new BufferedWriter(new FileWriter("toAdd2", true));
        bw1.write("edit");
        bw1.close();
        i.editExisting("toAdd1");
        i.addFile("toAdd7");
        i.addFile("toAdd8");
        i.remove("toAdd1");
        Commit c3 = new Commit(c2.getSha1(), "Oren", "third commit");
        
        i.remove("toAdd2");
        i.addFile("toAdd9");
        Commit c4 = new Commit(c3.getSha1(), "Oren", "fourth commit");

        BufferedWriter bw2 = new BufferedWriter(new FileWriter("toAdd4", true));
        bw2.write("edit");
        bw2.close();
        i.editExisting("toAdd4");
        i.addFile("toAdd10");
        Commit c5 = new Commit(c4.getSha1(), "Oren", "fifth commit");

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
        
        File treeForThirdCommit = new File("objects/546b6cfe59567bddbd105ece5949d4c2f99f07f0");
        String treeForThirdCommitInfo = TextInFile(treeForThirdCommit);
        
        File treeForFourthCommit = new File("objects/19b518e02791c5cb25b29db89a7ae99b8f3dcf7c");
        String treeForFourthCommitInfo = TextInFile(treeForFourthCommit);
        
        File treeForFifthCommit = new File("./objects/842194b3b22be30ef40379efa12071d165b861a1");
        String treeForFifthCommitInfo = TextInFile(treeForFifthCommit);
        
        assertTrue(treeForFirstCommitInfo.contains("blob : 356a192b7913b04c54574d18c28d46e6395428ab : toAdd1\n" + //
                "blob : da4b9237bacccdf19c0760cab7aec4a8359010b0 : toAdd2"));
        assertTrue(treeForSecondCommitInfo.contains("tree : ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n" + //
                "tree : 680541cb5386e6a543f4c8378f6ef71dca778347 : newFolder\n" + //
                "blob : 77de68daecd823babbb58edb1c8e14d7106e83bb : toAdd3\n" + //
                "blob : 1b6453892473a467d07372d45eb05abc2031647a : toAdd4"));
        assertTrue(treeForThirdCommitInfo.contains("tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : newFolder3\ntree : 680541cb5386e6a543f4c8378f6ef71dca778347 : newFolder\ntree : 2aac0f39f4e62df95f1f4554f8e07cee855fc2d4 : newFolder2\nblob : fe5dbbcea5ce7e2988b8c69bcfdfde8904aabc1f : toAdd8\nblob : 77de68daecd823babbb58edb1c8e14d7106e83bb : toAdd3\nblob : 1b6453892473a467d07372d45eb05abc2031647a : toAdd4\nblob : 356a192b7913b04c54574d18c28d46e6395428ab : toAdd1\nblob : 902ba3cda1883801594b6e1b452790cc53948fda : toAdd7\nblob : da4b9237bacccdf19c0760cab7aec4a8359010b0 : toAdd2"));
        assertTrue(treeForFourthCommitInfo.contains("tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : newFolder3\ntree : 680541cb5386e6a543f4c8378f6ef71dca778347 : newFolder\ntree : 2aac0f39f4e62df95f1f4554f8e07cee855fc2d4 : newFolder2\nblob : fe5dbbcea5ce7e2988b8c69bcfdfde8904aabc1f : toAdd8\nblob : 77de68daecd823babbb58edb1c8e14d7106e83bb : toAdd3\nblob : 1b6453892473a467d07372d45eb05abc2031647a : toAdd4\nblob : 0ade7c2cf97f75d009975f4d720d1fa6c19f4897 : toAdd9\nblob : 356a192b7913b04c54574d18c28d46e6395428ab : toAdd1\nblob : 902ba3cda1883801594b6e1b452790cc53948fda : toAdd7"));
        assertTrue(treeForFifthCommitInfo.contains("blob : 55f07fe4ca003e46c52fd01480d8d06eac822f98 : toAdd4\n" + //
                "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : newFolder3\n" + //
                "tree : 680541cb5386e6a543f4c8378f6ef71dca778347 : newFolder\n" + //
                "tree : 2aac0f39f4e62df95f1f4554f8e07cee855fc2d4 : newFolder2\n" + //
                "blob : b1d5781111d84f7b3fe45a0852e59758cd7a87e5 : toAdd10\n" + //
                "blob : fe5dbbcea5ce7e2988b8c69bcfdfde8904aabc1f : toAdd8\n" + //
                "blob : 77de68daecd823babbb58edb1c8e14d7106e83bb : toAdd3\n" + //
                "blob : 0ade7c2cf97f75d009975f4d720d1fa6c19f4897 : toAdd9\n" + //
                "blob : 356a192b7913b04c54574d18c28d46e6395428ab : toAdd1\n" + //
                "blob : 902ba3cda1883801594b6e1b452790cc53948fda : toAdd7"));


        String SHA1OfFirstCommit = SHA1StringInput("ffb9a45711a60ea105d8fc3ab5cb8796faf73148\n\n\nOren\n" + c2.getDate() +"\nfirst commit");
        File firstCommit = new File("objects/" + SHA1OfFirstCommit);
        String firstCommitInfo = TextInFile(firstCommit);
        
        String SHA1OfSecondCommit = SHA1StringInput("dbc1b8aca159d8c9840255ada59b0d68c480f672\n" + SHA1OfFirstCommit + "\n\nOren\n" + c2.getDate() +"\nsecond commit");
        File secondCommit = new File("objects/" + SHA1OfSecondCommit);
        String secondCommitInfo = TextInFile(secondCommit);

        String SHA1OfThirdCommit = SHA1StringInput(treeForThirdCommit.getName() + "\n" + SHA1OfSecondCommit + "\n\nOren\n" + c2.getDate() +"\nthird commit");
        File thirdCommit = new File("objects/" + SHA1OfThirdCommit);
        String thirdCommitInfo = TextInFile(thirdCommit);
        
        String SHA1OfFourthCommit = SHA1StringInput(treeForFourthCommit.getName() + "\n" + SHA1OfThirdCommit + "\n\nOren\n" + c2.getDate() +"\nfourth commit");
        File fourthCommit = new File("objects/" + SHA1OfFourthCommit);
        String fourthCommitInfo = TextInFile(fourthCommit);

        String SHA1OfFifthCommit = SHA1StringInput(treeForFifthCommit.getName() + "\n" + SHA1OfFourthCommit + "\n\nOren\n" + c2.getDate() +"\nfifth commit");
        File fifthCommit = new File("objects/" + SHA1OfFifthCommit);
        String fifthCommitInfo = TextInFile(fifthCommit);
        assertTrue(firstCommit.exists() && secondCommit.exists() && thirdCommit.exists() && fourthCommit.exists());
        assertTrue(firstCommitInfo.contains(treeForFirstCommit.getName()+ "\n\n" + SHA1OfSecondCommit+ "\nOren\n" + c1.getDate() +"\nfirst commit"));
        assertTrue(secondCommitInfo.contains(treeForSecondCommit.getName()+ "\n" + SHA1OfFirstCommit + "\n" + SHA1OfThirdCommit + "\nOren\n" + c2.getDate() + "\nsecond commit"));
        assertTrue(thirdCommitInfo.contains(treeForThirdCommit.getName() + "\n" + SHA1OfSecondCommit + "\n" + SHA1OfFourthCommit + "\nOren\n" + c3.getDate() + "\nthird commit"));
        assertTrue(fourthCommitInfo.contains(treeForFourthCommit.getName() + "\n" + SHA1OfThirdCommit + "\n" + SHA1OfFifthCommit + "\nOren\n" + c4.getDate() + "\nfourth commit"));
        assertTrue(fifthCommitInfo.contains(treeForFifthCommit.getName() + "\n" + SHA1OfFourthCommit + "\n\nOren\n" + c4.getDate() + "\nfifth commit"));
    
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
    void testWithEditandDelete() throws IOException
    {

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

        public String SHA1StringInput(String input) throws IOException
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