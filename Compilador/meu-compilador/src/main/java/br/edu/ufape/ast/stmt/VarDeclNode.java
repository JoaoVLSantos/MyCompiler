package br.edu.ufape.ast.stmt;

import java.util.List;

public class VarDeclNode extends StmtNode {

    public enum Type {
        INTEGER,
        FLOAT,
        BOOLEAN,
        CHAR
    }

    public final Type type;
    public final List<String> identifiers;

    public VarDeclNode(int line, int column,
                       Type type,
                       List<String> identifiers) {

        super(line, column);

        this.type = type;
        this.identifiers = identifiers;
    }

    @Override
    public String toString() {
        return "VarDeclNode{" +
                "type=" + type +
                ", identifiers=" + identifiers +
                '}';
    }
}