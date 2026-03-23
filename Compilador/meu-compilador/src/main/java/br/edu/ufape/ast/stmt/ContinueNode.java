package br.edu.ufape.ast.stmt;

public class ContinueNode extends StmtNode {

    public ContinueNode(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "ContinueNode";
    }
}