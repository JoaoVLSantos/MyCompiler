package br.edu.ufape;

import br.edu.ufape.lexer.*;
import br.edu.ufape.parser.Parser;
import br.edu.ufape.ast.*;
import br.edu.ufape.ast.stmt.*;
import br.edu.ufape.semantic.SemanticAnalyzer;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Uso: java Main <arquivo>");
            return;
        }

        String file = args[0];

        try {

            /* ==========================
               ANÁLISE LÉXICA
               ========================== */

            Lexer lexer = new Lexer(new FileReader(file));

            try (PrintWriter tokenWriter = new PrintWriter("tokens.txt")) {

                Token token;

                do {
                    token = lexer.getNextToken();
                    tokenWriter.println(token);
                }
                while (token.type() != TokenType.TOKEN_EOF);
            }

            System.out.println("✔ Análise léxica concluída (tokens.txt)");



            /* ==========================
               ANÁLISE SINTÁTICA
               ========================== */

            Lexer parserLexer = new Lexer(new FileReader(file));
            Parser parser = new Parser(parserLexer);

            ProgramNode program = parser.parse();

            try (PrintWriter astWriter = new PrintWriter("ast.txt")) {
                printProgram(program, astWriter);
            }

            System.out.println("✔ Análise sintática concluída (ast.txt)");



            /* ==========================
               ANÁLISE SEMÂNTICA
               ========================== */

            SemanticAnalyzer semantic = new SemanticAnalyzer();
            semantic.analyze(program);

            try (PrintWriter tableWriter = new PrintWriter("symbol_table.txt")) {
                tableWriter.println(semantic.getSymbolTableSnapshot());
            }

            System.out.println("✔ Análise semântica concluída (symbol_table.txt)");



        } catch (IOException e) {

            System.err.println("Erro de leitura de arquivo: " + e.getMessage());

        } catch (RuntimeException e) {

            System.err.println("✖ Erro: " + e.getMessage());
        }
    }


    /* ==========================
       IMPRESSÃO DA AST
       ========================== */

    private static void printProgram(ProgramNode program, PrintWriter out) {

        out.println("Program");

        out.println("  Declarações:");
        for (Node decl : program.declarations) {
            out.println("    " + decl);
        }

        out.println("  Rotinas:");
        for (Node r : program.routines) {
            out.println("    " + r);
        }

        out.println("  Bloco principal:");
        printBlock(program.mainBlock, "    ", out);
    }


    private static void printBlock(BlockNode block, String indent, PrintWriter out) {

        out.println(indent + "Block");

        for (StmtNode stmt : block.statements) {

            if (stmt instanceof AssignNode assign) {

                out.println(indent + "  Assign: " + assign.identifier);
                out.println(indent + "    Expr: " + assign.expression);

            }

            else if (stmt instanceof IfNode ifNode) {

                out.println(indent + "  If");
                out.println(indent + "    Condition: " + ifNode.condition);

                out.println(indent + "    Then:");
                printBlock(ifNode.thenBlock, indent + "      ", out);

                if (ifNode.elseBlock != null) {
                    out.println(indent + "    Else:");
                    printBlock(ifNode.elseBlock, indent + "      ", out);
                }
            }

            else if (stmt instanceof WhileNode whileNode) {

                out.println(indent + "  While");
                out.println(indent + "    Condition: " + whileNode.condition);

                printBlock(whileNode.block, indent + "      ", out);
            }

            else if (stmt instanceof PrintNode print) {

                out.println(indent + "  Print: " + print.expression);
            }

            else {

                out.println(indent + "  " + stmt);
            }
        }
    }
}

/*  cd .\meu-compilador\
    mvn compile
    mvn exec:java "-Dexec.args=teste.txt"
*/
