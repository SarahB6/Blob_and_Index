import java.io.IOException;

public class tester 
{
    public static void main(String[] args) throws IOException
    {   Index i = new Index();
        i.initialize();
         
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

        i.remove("toAdd7");
        i.addFile("toAdd10");
        Commit c4 = new Commit(c3.getSha1(), "Oren", "fourth commit");


    }
    
}
