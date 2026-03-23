package br.edu.ufape;

import org.junit.jupiter.api.Test;

import br.edu.ufape.ast.ProgramNode;
import br.edu.ufape.ast.stmt.BlockNode;
import br.edu.ufape.lexer.Lexer;
import br.edu.ufape.parser.Parser;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class parserTest {

    @Test
    void testSimpleProgram() throws Exception {

        String program = """
                integer x;
                {
                    x = 10;
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        assertNotNull(ast);
        assertEquals(1, ast.declarations.size());
        assertNotNull(ast.mainBlock);
    }

    @Test
    void testAssignmentCommand() throws Exception {

        String program = """
                integer x;
                {
                    x = 5;
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        BlockNode block = ast.mainBlock;

        assertEquals(1, block.statements.size());
    }

    @Test
    void testIfStatement() throws Exception {

        String program = """
                integer x;
                {
                    if (x == 0) {
                        x = 1;
                    }
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        assertNotNull(ast.mainBlock);
        assertEquals(1, ast.mainBlock.statements.size());
    }

    @Test
    void testWhileStatement() throws Exception {

        String program = """
                integer x;
                {
                    while (x < 10) {
                        x = x + 1;
                    }
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        assertNotNull(ast.mainBlock);
        assertEquals(1, ast.mainBlock.statements.size());
    }

    @Test
    void testPrintCommand() throws Exception {

        String program = """
                integer x;
                {
                    print(x);
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        assertNotNull(ast.mainBlock);
        assertEquals(1, ast.mainBlock.statements.size());
    }

    @Test
    void testProcedureDeclaration() throws Exception {

        String program = """
                proc hello() {
                    print(1);
                }

                {
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        assertNotNull(ast);
        assertEquals(1, ast.routines.size());
    }

    @Test
    void testFunctionDeclaration() throws Exception {

        String program = """
                func integer soma(integer a, integer b) {
                    return a + b;
                }

                {
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        assertNotNull(ast);
        assertEquals(1, ast.routines.size());
    }

    @Test
    void testFunctionCall() throws Exception {

        String program = """
                func integer soma(integer a, integer b) {
                    return a + b;
                }

                {
                    soma(1,2);
                }
                """;

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        assertNotNull(ast.mainBlock);
        assertEquals(1, ast.mainBlock.statements.size());
    }
}