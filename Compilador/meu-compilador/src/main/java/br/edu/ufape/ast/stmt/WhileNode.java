package br.edu.ufape.ast.stmt;

import br.edu.ufape.ast.expr.ExprNode;

public class WhileNode extends StmtNode {

    public final ExprNode condition;
    public final BlockNode block;

    public WhileNode(int line, int column,
                     ExprNode condition,
                     BlockNode block) {

        super(line, column);

        this.condition = condition;
        this.block = block;
    }
}