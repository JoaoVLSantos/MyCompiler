package br.edu.ufape.ast.stmt;

public class BreakNode extends StmtNode {

    public BreakNode(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "BreakNode";
    }
}