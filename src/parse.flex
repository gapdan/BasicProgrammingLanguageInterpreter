import java.util.*;
import java.io.PrintStream;

%%

%class HelloLexer
%line
%int
%{
    ArrayList<Expr> buffer = new ArrayList<>();
    ArrayList<BigExpr> expressions = new ArrayList<>();
    HashMap<String, Integer> varsToValue = new HashMap();
    HashSet<String> vars = new HashSet<>();
    Integer poz = 0;
    boolean unassignedVar = false;

    public void add() {
        if (buffer.size() == 0) return;
        //add from buffer to expressions
        if (buffer.get(0) instanceof Instance) {
            for (int i = 1; i < buffer.size(); ++i) {
                vars.add(buffer.get(i).name);
            }
        } else if (buffer.get(0) instanceof Var) {
            //construct Arithmetic Expr
            AtribExpr atrib = new AtribExpr();
            atrib.var = (Var) buffer.get(0);
            //skip equal
            for (int i = 2; i < buffer.size(); ++i) {
                atrib.aritm.data.add(buffer.get(i));
            }
            atrib.varsToValue = varsToValue;
            atrib.vars = vars;
            expressions.add(atrib);
        } else if (buffer.get(0) instanceof OBracket) {
            //construct Logic Expr
            LogicExpr logicExpr = new LogicExpr();
            for (int i = 0; i < buffer.size(); ++i) {
                logicExpr.data.add(buffer.get(i));
            }
            logicExpr.varsToValue = varsToValue;
            logicExpr.vars = vars;
            expressions.add(logicExpr);
        } else {
            // error
        }
        buffer.clear();
    }

    boolean firstTime = true;
    public Tree getTree(PrintStream out) {
        Tree tree = null;
        if (firstTime) {
            tree = new MainNode();
            tree.out = out;
            tree.left = new SequenceNode();
            firstTime = false;
        } else {
            tree = new BlockNode();
            tree.left = new SequenceNode();
        }
        Tree curr = tree.left;
        while (poz < expressions.size()) {
            BigExpr expres = expressions.get(poz);
            if (expres instanceof If) {
                ++poz;
                //LogicExpr Block Else Block
                If aux = (If) expres;
                //fill If Fields
                aux.logicExpr = (LogicExpr) expressions.get(poz);
                //skip logicExpr and OBlock
                poz += 2;
                IfNode ifNode = new IfNode();
                ifNode.left = aux.logicExpr.eval();
                ifNode.right = getTree(out);
                //skip else and OBlock
                poz += 2;
                //create tree
                ifNode.nodeElse = getTree(out);
                curr.left = ifNode;
                curr.right = new SequenceNode();
                curr = curr.right;
            } else if (expres instanceof While) {
                ++poz;
                //LogicExpr Block
                While aux = (While) expres;
                aux.logicExpr = (LogicExpr) expressions.get(poz);
                //skip logicExpr and OBlock
                poz += 2;
                //fill While Fields
                WhileNode whileNode = new WhileNode();
                whileNode.left = aux.logicExpr.eval();
                whileNode.right = getTree(out);
                //create tree
                curr.left = whileNode;
                curr.right = new SequenceNode();
                curr = curr.right;
            } else if (expres instanceof AtribExpr) {
                 curr.left = expres.eval();
                 curr.right = new SequenceNode();
                 curr = curr.right;
                 unassignedVar = unassignedVar || expres.undeclaredVar;
                 ++poz;
            } else if (expres instanceof CBlock){
                ++poz;
                break;
            }
        }
        return tree;
    }
%}

integer = [1-9][0-9]* | 0
booleanVal = "true" | "false"
endline = \n | \r\n
whitespace = " "
endexpr = ";"
comma = ","
if = if
else = else
while = while
int = int
openBracket = "("
closeBracket = ")"
openBlock = "{"
closeBlock = "}"
not = "!"
plus = "+"
div = "/"
and = "&&"
equal = "="
greater = ">"
string  = ( a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z )+

%%

{integer} {
    buffer.add(new IntVal(yytext()));
}

{booleanVal} {
    buffer.add(new BoolVal(yytext()));
}

{endline} {}

{whitespace} {}

{endexpr} {
    add();
}

{greater} {
    buffer.add(new Greater());
}

{and} {
    buffer.add(new And());
}

{not} {
    buffer.add(new NotOp());
 }

{comma} {}

{plus} {
    buffer.add(new Plus());
}

{div} {
    buffer.add(new Div());
}

{while} {
    expressions.add(new While());
}

{if} {
    expressions.add(new If());
}

{else} {
    expressions.add(new Else());
}


{int} {
    buffer.add(new Instance());
}

{equal} {
    buffer.add(new Equal());
}

{openBracket} {
    buffer.add(new OBracket());
}

{closeBracket} {
    buffer.add(new CBracket());
}

{openBlock} {
    add();
    expressions.add(new OBlock());
}

{closeBlock} {
    expressions.add(new CBlock());
}

{string} {
    //get variable
    buffer.add(new Var(yytext()));
}

. {}