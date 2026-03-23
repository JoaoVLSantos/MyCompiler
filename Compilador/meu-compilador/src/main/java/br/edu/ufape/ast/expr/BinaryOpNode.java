package br.edu.ufape.ast.expr;

public class BinaryOpNode extends ExprNode {

    public final String operator;
    public final ExprNode left;
    public final ExprNode right;

    public BinaryOpNode(int line, int column,
                        String operator,
                        ExprNode left,
                        ExprNode right) {

        super(line, column);

        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {

        String leftStr = (left != null) ? left.toString() : "null";
        String rightStr = (right != null) ? right.toString() : "null";

        return "(" + leftStr + " " + operator + " " + rightStr + ")";
    }
}