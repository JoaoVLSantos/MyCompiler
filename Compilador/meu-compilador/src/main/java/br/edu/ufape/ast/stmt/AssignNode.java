package br.edu.ufape.ast.stmt;

import br.edu.ufape.ast.expr.ExprNode;

public class AssignNode extends StmtNode {

    public final String identifier;
    public final ExprNode expression;

    public AssignNode(int line, int column,
                      String identifier,
                      ExprNode expression) {

        super(line, column);

        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString() {
    return identifier + " = " + expression;
    }
}
