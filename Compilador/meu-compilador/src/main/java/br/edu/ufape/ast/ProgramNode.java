package br.edu.ufape.ast;

import java.util.List;

import br.edu.ufape.ast.stmt.BlockNode;

public class ProgramNode extends Node {

    public final List<Node> declarations;
    public final List<Node> routines;
    public final BlockNode mainBlock;

    public ProgramNode(
            int line,
            int column,
            List<Node> declarations,
            List<Node> routines,
            BlockNode mainBlock
    ) {

        super(line, column);

        this.declarations = declarations;
        this.routines = routines;
        this.mainBlock = mainBlock;
    }
}