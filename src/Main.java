import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        HelloLexer lexer = null;

        lexer = new HelloLexer(new FileReader("input"));

        try {
            lexer.yylex();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Tree AST = lexer.getTree(new PrintStream("arbore"));

        AST.print(0);

        PrintStream ps = new PrintStream("output");
        AST.out = ps;
        if (!lexer.unassignedVar) {
            boolean ok = AST.interpret();
            if (ok) {
                //print varriables
                for (String s : lexer.vars) {
                    String aux = s + "=" + lexer.varsToValue.get(s);
                    //System.out.println(aux);
                    ps.println(aux);
                }
            }
        } else {
            ps.println("UnassignedVar 6");
        }
    }
}
