import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {   Index i = new Index();
        i.initialize();
        /*  
        i.addFile("toAdd1");
       // i.addFile("toAdd2");
        Commit c1 = new Commit("Oren", "first commit");

        i.addFile("toAdd2");
       // i.addFile("toAdd4");
        //i.addDirectory("newFolder");
        Commit c2 = new Commit(c1.getSha1(), "Oren", "second commit");

      //  i.addDirectory("newFolder2");
      //  i.addDirectory("newFolder3");
        i.addFile("toAdd3");
      //  i.addFile("toAdd8");
        Commit c3 = new Commit(c2.getSha1(), "Oren", "third commit");
        */
        //i.remove("toAdd2");
        i.addFile("toAdd4");
        Commit c4 = new Commit("Oren", "fourth commit");

        BufferedWriter bw = new BufferedWriter(new FileWriter("toAdd4", true));
        bw.write("edit");
        bw.close();
        i.editExisting("toAdd4");
        Commit c5 = new Commit(c4.getSha1(), "Oren", "fifth commit");

    }
    
}
