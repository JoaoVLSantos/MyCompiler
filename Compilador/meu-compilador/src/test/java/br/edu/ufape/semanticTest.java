package br.edu.ufape;

import org.junit.jupiter.api.Test;

import br.edu.ufape.ast.ProgramNode;
import br.edu.ufape.lexer.Lexer;
import br.edu.ufape.parser.Parser;
import br.edu.ufape.semantic.SemanticAnalyzer;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class semanticTest {

    private void analyze(String program) throws Exception {

        Lexer lexer = new Lexer(new StringReader(program));
        Parser parser = new Parser(lexer);

        ProgramNode ast = parser.parse();

        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        analyzer.analyze(ast);
    }

    @Test
    void testCorrectProgram() throws Exception {

        String program = """
                integer x;
                {
                    x = 10;
                    print(x);
                }
                """;

        assertDoesNotThrow(() -> analyze(program));
    }

    @Test
    void testUndeclaredVariable() {

        String program = """
                {
                    x = 10;
                }
                """;

        assertThrows(RuntimeException.class, () -> analyze(program));
    }

    @Test
    void testTypeMismatch() {

        String program = """
                integer x;
                {
                    x = true;
                }
                """;

        assertThrows(RuntimeException.class, () -> analyze(program));
    }

    @Test
    void testBreakOutsideLoop() {

        String program = """
                {
                    break;
                }
                """;

        assertThrows(RuntimeException.class, () -> analyze(program));
    }

    @Test
    void testIfConditionNotBoolean() {

        String program = """
                integer x;
                {
                    if (x == 1) {
                        x = 1;
                    }
                }
                """;

        assertDoesNotThrow(() -> analyze(program));
    }

    @Test
    void testFunctionCallCorrect() throws Exception {

        String program = """
                func integer soma(integer a, integer b) {
                    return a + b;
                }

                {
                    soma(1,2);
                }
                """;

        assertDoesNotThrow(() -> analyze(program));
    }

    @Test
    void testFunctionWrongArguments() {

        String program = """
                func integer soma(integer a, integer b) {
                    return a + b;
                }

                {
                    soma(1);
                }
                """;

        assertThrows(RuntimeException.class, () -> analyze(program));
    }

    @Test
    void testReturnTypeMismatch() {

        String program = """
                func integer soma(integer a, integer b) {
                    return true;
                }

                {
                }
                """;

        assertThrows(RuntimeException.class, () -> analyze(program));
    }

    @Test
    void testProcedureCall() throws Exception {

        String program = """
                proc hello(integer x) {
                    print(x);
                }

                {
                    hello(5);
                }
                """;

        assertDoesNotThrow(() -> analyze(program));
    }

    @Test
    void testReturnOutsideFunction() {

        String program = """
                {
                    return 5;
                }
                """;

        assertThrows(RuntimeException.class, () -> analyze(program));
    }
}