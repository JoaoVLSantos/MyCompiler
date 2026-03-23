package br.edu.ufape.ast.stmt;

import java.util.List;

public class BlockNode extends StmtNode {

    public final List<StmtNode> statements;

    public BlockNode(int line, int column,
                     List<StmtNode> statements) {

        super(line, column);

        this.statements = statements;
    }
}