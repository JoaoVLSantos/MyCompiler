package br.edu.ufape;

import org.junit.jupiter.api.Test;

import br.edu.ufape.ast.expr.*;
import br.edu.ufape.ast.stmt.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class astTest {

    @Test
    void testIntLiteralNode() {

        IntLiteralNode node = new IntLiteralNode(1, 1, "42");

        assertEquals(42, node.value);
        assertEquals(1, node.line);
        assertEquals(1, node.column);
    }

    @Test
    void testBinaryOpNode() {

        IntLiteralNode left = new IntLiteralNode(1,1,"5");
        IntLiteralNode right = new IntLiteralNode(1,3,"10");

        BinaryOpNode node = new BinaryOpNode(1,2,"+", left, right);

        assertEquals("+", node.operator);
        assertEquals(left, node.left);
        assertEquals(right, node.right);
    }

    @Test
    void testAssignNode() {

        IntLiteralNode expr = new IntLiteralNode(1,5,"10");

        AssignNode node = new AssignNode(1,1,"x",expr);

        assertEquals("x", node.identifier);
        assertEquals(expr, node.expression);
    }

    @Test
    void testVarDeclNode() {

        VarDeclNode node = new VarDeclNode(
                1,
                1,
                VarDeclNode.Type.INTEGER,
                List.of("a","b","c")
        );

        assertEquals(VarDeclNode.Type.INTEGER, node.type);
        assertEquals(3, node.identifiers.size());
    }

    @Test
    void testIfNode() {

        BooleanLiteralNode cond = new BooleanLiteralNode(1,1,true);

        BlockNode thenBlock = new BlockNode(
                1,
                5,
                List.of()
        );

        IfNode node = new IfNode(
                1,
                1,
                cond,
                thenBlock,
                null
        );

        assertEquals(cond, node.condition);
        assertEquals(thenBlock, node.thenBlock);
        assertNull(node.elseBlock);
    }

}
