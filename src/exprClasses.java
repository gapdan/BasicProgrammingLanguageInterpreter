abstract class Expr {
    public String name;
    public boolean undeclaredVar = false;
}

class BoolVal extends Expr {
    public BoolVal(String name) {
        this.name = name;
    }
}

class Greater extends Expr {

}

class And extends Expr {

}

class NotOp extends Expr {

}

class Div extends Expr {

}

class Plus extends Expr {

}

class Equal extends Expr {

}

class IntVal extends Expr {
    public IntVal(String name) {
        this.name = name;
    }
}

class Instance extends Expr {

}

class Var extends Expr {
    public Var(String name) {
        this.name = name;
    }
}

class OBracket extends Expr {

}

class CBracket extends Expr {

}

