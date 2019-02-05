import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;

abstract class Tree {
    public Tree left;
    public Tree right;
    public PrintStream out;
    public Integer result = null;
    abstract public void print(Integer nrTabs);
    abstract public boolean interpret();
    public void printTabs(Integer nrTabs) {
        for (int i = 0; i < nrTabs; ++i) {
            out.print("\t");
        }
    }
}

class MainNode extends Tree{

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<MainNode>");
        left.out = out;
        left.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        return left.interpret();
    }
}

class IntNode extends Tree {
    public Integer data;

    public IntNode(Integer data) {
        this.data = data;
    }

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<IntNode> " + data);
    }

    @Override
    public boolean interpret() {
        result = data;
        return true;
    }
}

class BoolNode extends Tree {
    public String data;

    public BoolNode(String data) {
        this.data = data;
        if (data.equals("true")) result = 1;
            else result = 0;
    }

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<BoolNode> " + data);
    }

    @Override
    public boolean interpret() {
        return true;
    }
}

class VarNode extends Tree {
    public String data;
    public HashSet<String> vars;
    public HashMap<String, Integer> varToValue;

    public VarNode(String data, HashSet<String> vars, HashMap<String, Integer> varToValue) {
        this.data = data;
        this.vars = vars;
        this.varToValue = varToValue;
    }

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<VariableNode> " + data);
    }

    @Override
    public boolean interpret() {
        if (!vars.contains(data)) {
            out.println("UnassignedVar 6");
            return false;
        }
        result = varToValue.get(data);
        return true;
    }
}

class PlusNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<PlusNode> +" );
        left.out = out;
        left.print(nrTabs + 1);
        right.out = out;
        right.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        right.out = out;
        boolean ok = left.interpret();
        if (!ok) {
            return false;
        } else {
            ok = right.interpret();
            if (!ok ) {
                return false;
            }
            if (left.result == null || right.result == null) {
                out.println("UnassignedVar 6");
                return false;
            }
            result = left.result + right.result;
        }
        return true;
    }
}

class DivNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<DivNode> /");
        left.out = out;
        left.print(nrTabs + 1);
        right.out = out;
        right.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        right.out = out;
        boolean ok = left.interpret();
        if (!ok) {
            return false;
        } else {
            ok = right.interpret();
            if (!ok ) {
                return false;
            }
            if (left.result == null || right.result == null) {
                out.println("UnassignedVar 6");
                return false;
            }
            if (right.result == 0) {
                out.println("DivideByZero 4");
                return false;
            }
            result = left.result / right.result;
        }
        return true;
    }
}

class BracketNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<BracketNode> ()");
        left.out = out;
        left.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        boolean ok =  left.interpret();
        result = left.result;
        return ok;
    }
}

class AndNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<AndNode> &&");
        left.out = out;
        left.print(nrTabs + 1);
        right.out = out;
        right.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        right.out = out;
        boolean ok = left.interpret();
        if (!ok) {
            return false;
        } else {
            ok = right.interpret();
            if (!ok ) {
                return false;
            }
            if (left.result == null || right.result == null) {
                out.println("UnassignedVar 6");
                return false;
            }
            if (left.result == 0 || right.result == 0) result = 0;
                else result = 1;
        }
        return true;
    }
}

class GreaterNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<GreaterNode> >");
        left.out = out;
        left.print(nrTabs + 1);
        right.out = out;
        right.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        right.out = out;
        boolean ok = left.interpret();
        if (!ok) {
            return false;
        } else {
            ok = right.interpret();
            if (!ok ) {
                return false;
            }
            if (left.result == null || right.result == null) {
                out.println("UnassignedVar 6");
                return false;
            }
            if (left.result > right.result) result = 1;
                else result = 0;
        }
        return true;
    }
}

class NotNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<NotNode> !");
        left.out = out;
        left.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        boolean ok = left.interpret();
        if (ok) {
            if (left.result == null) {
                out.println("UnassignedVar 6");
                return false;
            }
            if (left.result == 0) result = 1;
                else result = 0;
        }
        return ok;
    }
}

class AssignmentNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<AssignmentNode> =");
        left.out = out;
        left.print(nrTabs + 1);
        right.out = out;
        right.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        right.out = out;
        boolean ok = left.interpret();
        if (ok) {
            ok = right.interpret();
            if (ok) {
               if (right.result == null) {
                   out.println("UnassignedVar 6");
                   return false;
               }
               VarNode aux = (VarNode) left;
               aux.varToValue.put(aux.data, right.result);
               aux.result = right.result;
            }
        }
        return ok;
    }
}

class BlockNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<BlockNode> {}");
        if (left != null) {
            left.out = out;
            left.print(nrTabs + 1);
        }
    }

    @Override
    public boolean interpret() {
        left.out = out;
        boolean ok = left.interpret();
        if (ok) {
            result = left.result;
        }
        return ok;
    }
}

class IfNode extends Tree {

    Tree nodeElse;

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<IfNode> if");
        left.out = out;
        left.print(nrTabs + 1);
        right.out = out;
        right.print(nrTabs + 1);
        nodeElse.out = out;
        nodeElse.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        right.out = out;
        nodeElse.out = out;
        boolean ok = left.interpret();
        if (ok) {
            if (left.result == 1) {
                ok = right.interpret();
            } else {
                ok = nodeElse.interpret();
            }
        }
        return ok;
    }
}

class WhileNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        printTabs(nrTabs);
        out.println("<WhileNode> while");
        left.out = out;
        left.print(nrTabs + 1);
        right.out = out;
        right.print(nrTabs + 1);
    }

    @Override
    public boolean interpret() {
        left.out = out;
        right.out = out;
        boolean ok = left.interpret();
        if (ok) {
            if (left.result == null) {
                out.println("UnassignedVar 6");
                return false;
            }
            while (left.result == 1) {
                ok = right.interpret();
                if (!ok) break;
                left.interpret();
            }
        }
        return ok;
    }
}

class SequenceNode extends Tree {

    @Override
    public void print(Integer nrTabs) {
        if (left != null) {
            if (left.left == null && left.right == null) {
                left = null;
            }
        }

        if (right != null) {
            if (right.left == null && right.right == null) {
                right = null;
            }
        }

        if (left == null && right == null) return;

        if (left != null && right != null) {
            printTabs(nrTabs);
            out.println("<SequenceNode>");
            ++nrTabs;
        }
        left.out = out;
        left.print(nrTabs);
        if (right != null) {
            right.out = out;
            right.print(nrTabs);
        }
    }

    @Override
    public boolean interpret() {
        boolean ok = true;
        if (left != null) {
            left.out = out;
            ok = left.interpret();
            if (ok && right != null) {
                right.out = out;
                ok = right.interpret();
            }
        }
        return ok;
    }
}

