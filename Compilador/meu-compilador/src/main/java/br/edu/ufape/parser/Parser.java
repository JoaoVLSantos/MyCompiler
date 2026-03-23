package br.edu.ufape.parser;

import br.edu.ufape.ast.*;
import br.edu.ufape.ast.routines.*;
import br.edu.ufape.ast.stmt.BlockNode;
import br.edu.ufape.ast.stmt.VarDeclNode;
import br.edu.ufape.lexer.Lexer;
import br.edu.ufape.lexer.Token;
import br.edu.ufape.lexer.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final ParserContext ctx;
    private final DeclarationParser declParser;
    private final StatementParser stmtParser;

    public Parser(Lexer lexer) throws IOException {

        this.ctx = new ParserContext(lexer);

        this.declParser = new DeclarationParser(ctx);
        this.stmtParser = new StatementParser(ctx);
    }

    public ProgramNode parse() throws IOException {

        Token startToken = ctx.lookahead();

        List<Node> declarations = new ArrayList<>();
        List<Node> routines = new ArrayList<>();

        while (declParser.isTipo()) {
            declarations.add(declParser.parseDecl());
        }

        while (ctx.lookahead().type() == TokenType.TOKEN_PROC ||
               ctx.lookahead().type() == TokenType.TOKEN_FUNC) {

            routines.add(parseRoutine());
        }

        BlockNode mainBlock = stmtParser.parseBlock();

        if (ctx.lookahead().type() != TokenType.TOKEN_EOF) {

            Token t = ctx.lookahead();

            throw new RuntimeException(
                    "Erro sintático na linha "
                            + t.line()
                            + " coluna "
                            + t.column()
                            + ": código após fim do programa"
            );
        }

        return new ProgramNode(
                startToken.line(),
                startToken.column(),
                declarations,
                routines,
                mainBlock
        );
    }

    private Node parseRoutine() throws IOException {

        if (ctx.lookahead().type() == TokenType.TOKEN_PROC) {
            return parseProcedure();
        }

        return parseFunction();
    }

    private ProcedureNode parseProcedure() throws IOException {

        Token procToken = ctx.lookahead();
        ctx.match(TokenType.TOKEN_PROC);

        Token id = ctx.lookahead();
        ctx.match(TokenType.TOKEN_ID);

        ctx.match(TokenType.TOKEN_LPAREN);

        List<ParameterNode> params = parseParams();

        ctx.match(TokenType.TOKEN_RPAREN);

        BlockNode body = stmtParser.parseBlock();

        return new ProcedureNode(
                procToken.line(),
                procToken.column(),
                id.lexeme(),
                params,
                body
        );
    }

    private FunctionNode parseFunction() throws IOException {

        Token funcToken = ctx.lookahead();
        ctx.match(TokenType.TOKEN_FUNC);

        VarDeclNode.Type returnType = declParser.parseTipo();

        Token id = ctx.lookahead();
        ctx.match(TokenType.TOKEN_ID);

        ctx.match(TokenType.TOKEN_LPAREN);

        List<ParameterNode> params = parseParams();

        ctx.match(TokenType.TOKEN_RPAREN);

        BlockNode body = stmtParser.parseBlock();

        return new FunctionNode(
                funcToken.line(),
                funcToken.column(),
                id.lexeme(),
                returnType,
                params,
                body
        );
    }

    private List<ParameterNode> parseParams() throws IOException {

        List<ParameterNode> params = new ArrayList<>();

        if (declParser.isTipo()) {

            VarDeclNode.Type type = declParser.parseTipo();

            Token id = ctx.lookahead();
            ctx.match(TokenType.TOKEN_ID);

            params.add(
                    new ParameterNode(
                            id.line(),
                            id.column(),
                            type,
                            id.lexeme()
                    )
            );

            while (ctx.lookahead().type() == TokenType.TOKEN_COMMA) {

                ctx.match(TokenType.TOKEN_COMMA);

                type = declParser.parseTipo();

                id = ctx.lookahead();
                ctx.match(TokenType.TOKEN_ID);

                params.add(
                        new ParameterNode(
                                id.line(),
                                id.column(),
                                type,
                                id.lexeme()
                        )
                );
            }
        }

        return params;
    }
}