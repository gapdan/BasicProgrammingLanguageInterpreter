import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

abstract class BigExpr extends Expr{
    public abstract Tree eval();
}

class If extends BigExpr {
    public LogicExpr logicExpr = new LogicExpr();

    @Override
    public Tree eval() {
        return null;
    }
}

class While extends BigExpr {
    public LogicExpr logicExpr = new LogicExpr();

    @Override
    public Tree eval() {
        return null;
    }
}

class OBlock extends BigExpr {

    @Override
    public Tree eval() {
        return null;
    }
}

class CBlock extends BigExpr {

    @Override
    public Tree eval() {
        return null;
    }
}

class Else extends BigExpr {

    @Override
    public Tree eval() {
        return null;
    }
}

class ArithmExpr extends BigExpr {
    public ArrayList<Expr> data = new ArrayList<>();
    public HashMap<String, Integer> varsToValue;
    public HashSet<String> vars;
    Integer poz = 0;

    @Override
    public Tree eval() {
        return evalPlus();
    }

    public Tree evalDiv() {
        Tree x, y;
        x = evalItem();
        while ((poz < data.size()) &&  (data.get(poz) instanceof Div)) {
            y = new DivNode();
            y.left = x;
            ++poz;
            y.right = evalItem();
            x = y;
        }
        return x;
    }

    public Tree evalPlus() {
        Tree x, y;
        x = evalDiv();
        while ((poz < data.size()) && (data.get(poz) instanceof Plus)) {
            y = new PlusNode();
            y.left = x;
            ++poz;
            y.right = evalDiv();
            x = y;
        }
        return x;
    }

    public Tree evalItem() {
        Tree x = null;
        if (poz > data.size()) return x;
        if (data.get(poz) instanceof OBracket) {
                ++poz;
                x = new BracketNode();
                x.left = evalPlus();
                ++poz;
        } else {
            if (data.get(poz) instanceof IntVal) {
                    String s = ((IntVal) data.get(poz)).name;
                    x = new IntNode(Integer.parseInt(s));
                    ++poz;
            } else {
                    String s =((Var)data.get(poz)).name;
                    if (!vars.contains(s)) {
                            this.undeclaredVar = true;
                    }
                    x = new VarNode(s, vars, varsToValue);
                    ++poz;
            }
        }
        return x;
    }
}

class LogicExpr extends BigExpr {
    public ArrayList<Expr> data = new ArrayList<>();
    public HashMap<String, Integer> varsToValue;
    public HashSet<String> vars;
    Integer poz = 0;

    @Override
    public Tree eval() {
        Tree tree = new BracketNode();
        poz = 1;
        data.remove(data.size() - 1);
        tree.left = evalAnd();
        return tree;
    }

    public Tree evalDiv() {
        Tree x, y;
        x = evalItem();
        while ((poz < data.size()) &&  (data.get(poz) instanceof Div)) {
            y = new DivNode();
            y.left = x;
            ++poz;
            y.right = evalItem();
            x = y;
        }
        return x;
    }

    public Tree evalPlus() {
        Tree x, y;
        x = evalDiv();
        while ((poz < data.size()) && (data.get(poz) instanceof Plus)) {
            y = new PlusNode();
            y.left = x;
            ++poz;
            y.right = evalDiv();
            x = y;
        }
        return x;
    }

    public Tree evalAnd() {
        Tree x, y;
        x = evalGreater();
        while ((poz < data.size()) &&  (data.get(poz) instanceof And)) {
            y = new AndNode();
            y.left = x;
            ++poz;
            y.right = evalGreater();
            x = y;
        }
        return x;
    }

    public Tree evalGreater() {
        Tree x, y;
        x = evalPlus();
        while ((poz < data.size()) && (data.get(poz) instanceof Greater)) {
            y = new GreaterNode();
            y.left = x;
            ++poz;
            y.right = evalPlus();
            x = y;
        }
        return x;
    }

    public Tree evalItem() {
        Tree x = null;
        if (poz > data.size()) return x;
        if (data.get(poz) instanceof OBracket) {
                ++poz;
                x = new BracketNode();
                x.left = evalAnd();
                ++poz;
        } else if (data.get(poz) instanceof NotOp) {
                ++poz;
                x = new NotNode();
                x.left = evalItem();
        } else {
            if (data.get(poz) instanceof IntVal) {
                    String s = ((IntVal) data.get(poz)).name;
                    x = new IntNode(Integer.parseInt(s));
                    ++poz;
            } else if (data.get(poz) instanceof  Var) {
                    String s = ((Var) data.get(poz)).name;
                    x = new VarNode(s, vars, varsToValue);
                    ++poz;
            } else if (data.get(poz) instanceof BoolVal) {
                    String s = ((BoolVal) data.get(poz)).name;
                    x = new BoolNode(s);
                    ++poz;
            }
        }
        return x;
    }
}

class AtribExpr extends BigExpr {
    public Var var;
    public ArithmExpr aritm = new ArithmExpr();
    public HashMap<String, Integer> varsToValue;
    public HashSet<String> vars;

    @Override
    public Tree eval() {
        Tree tree = new AssignmentNode();
        tree.left = new VarNode(var.name, vars, varsToValue);
        aritm.vars = vars;
        aritm.varsToValue = varsToValue;
        tree.right = aritm.eval();
        if (!vars.contains(var.name)) {
            this.undeclaredVar = true;
        } else {
            this.undeclaredVar = aritm.undeclaredVar;
        }

        return tree;
    }
}


