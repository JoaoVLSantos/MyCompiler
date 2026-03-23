package br.edu.ufape.ast.stmt;

import br.edu.ufape.ast.expr.ExprNode;

public class IfNode extends StmtNode {

    public final ExprNode condition;
    public final BlockNode thenBlock;
    public final BlockNode elseBlock;

    public IfNode(int line, int column,
                  ExprNode condition,
                  BlockNode thenBlock,
                  BlockNode elseBlock) {

        super(line, column);

        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }
}