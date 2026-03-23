package br.edu.ufape.parser;

import br.edu.ufape.ast.expr.ExprNode;
import br.edu.ufape.ast.stmt.*;
import br.edu.ufape.lexer.Token;
import br.edu.ufape.lexer.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatementParser {

    private final ParserContext ctx;
    private final ExpressionParser exprParser;
    private final DeclarationParser declParser;

    public StatementParser(ParserContext ctx) {
        this.ctx = ctx;
        this.exprParser = new ExpressionParser(ctx);
        this.declParser = new DeclarationParser(ctx);
    }

    public BlockNode parseBlock() throws IOException {

        Token blockToken = ctx.lookahead();

        ctx.match(TokenType.TOKEN_LBRACE);

        List<StmtNode> stmts = new ArrayList<>();

        while (declParser.isTipo() || isCommandStart()) {

            if (declParser.isTipo()) {
                stmts.add(declParser.parseDecl());
            } else {
                stmts.add(parseCommand());
            }
        }

        ctx.match(TokenType.TOKEN_RBRACE);

        return new BlockNode(
                blockToken.line(),
                blockToken.column(),
                stmts
        );
    }

    private boolean isCommandStart() {

        TokenType t = ctx.lookahead().type();

        return t == TokenType.TOKEN_ID ||
               t == TokenType.TOKEN_IF ||
               t == TokenType.TOKEN_WHILE ||
               t == TokenType.TOKEN_RETURN ||
               t == TokenType.TOKEN_PRINT ||
               t == TokenType.TOKEN_BREAK ||
               t == TokenType.TOKEN_CONTINUE;
    }

    private StmtNode parseCommand() throws IOException {

        return switch (ctx.lookahead().type()) {

            case TOKEN_ID -> parseIdCommand();
            case TOKEN_IF -> parseIf();
            case TOKEN_WHILE -> parseWhile();
            case TOKEN_RETURN -> parseReturn();
            case TOKEN_PRINT -> parsePrint();
            case TOKEN_BREAK -> parseBreak();
            case TOKEN_CONTINUE -> parseContinue();

            default -> throw syntaxError("Comando inválido");
        };
    }

    private StmtNode parseIdCommand() throws IOException {

        Token idToken = ctx.lookahead();
        String id = idToken.lexeme();

        ctx.match(TokenType.TOKEN_ID);

        if (ctx.lookahead().type() == TokenType.TOKEN_ASSIGN) {

            ctx.match(TokenType.TOKEN_ASSIGN);

            ExprNode expr = exprParser.parseExpr();

            ctx.match(TokenType.TOKEN_SEMI);

            return new AssignNode(
                    idToken.line(),
                    idToken.column(),
                    id,
                    expr
            );
        }

        if (ctx.lookahead().type() == TokenType.TOKEN_LPAREN) {

            ctx.match(TokenType.TOKEN_LPAREN);

            List<ExprNode> args = new ArrayList<>();

            if (ctx.lookahead().type() != TokenType.TOKEN_RPAREN) {

                args.add(exprParser.parseExpr());

                while (ctx.lookahead().type() == TokenType.TOKEN_COMMA) {
                    ctx.match(TokenType.TOKEN_COMMA);
                    args.add(exprParser.parseExpr());
                }
            }

            ctx.match(TokenType.TOKEN_RPAREN);
            ctx.match(TokenType.TOKEN_SEMI);

            return new CallStmtNode(
                    idToken.line(),
                    idToken.column(),
                    id,
                    args
            );
        }

        throw syntaxError("Esperado '=' ou '(' após identificador");
    }

    private IfNode parseIf() throws IOException {

        Token ifToken = ctx.lookahead();

        ctx.match(TokenType.TOKEN_IF);
        ctx.match(TokenType.TOKEN_LPAREN);

        ExprNode cond = exprParser.parseBooleanExpr();

        ctx.match(TokenType.TOKEN_RPAREN);

        BlockNode thenBlock = parseBlock();

        BlockNode elseBlock = null;

        if (ctx.lookahead().type() == TokenType.TOKEN_ELSE) {

            ctx.match(TokenType.TOKEN_ELSE);

            elseBlock = parseBlock();
        }

        return new IfNode(
                ifToken.line(),
                ifToken.column(),
                cond,
                thenBlock,
                elseBlock
        );
    }

    private WhileNode parseWhile() throws IOException {

        Token whileToken = ctx.lookahead();

        ctx.match(TokenType.TOKEN_WHILE);
        ctx.match(TokenType.TOKEN_LPAREN);

        ExprNode cond = exprParser.parseBooleanExpr();

        ctx.match(TokenType.TOKEN_RPAREN);

        return new WhileNode(
                whileToken.line(),
                whileToken.column(),
                cond,
                parseBlock()
        );
    }

    private ReturnNode parseReturn() throws IOException {

        Token retToken = ctx.lookahead();

        ctx.match(TokenType.TOKEN_RETURN);

        ExprNode expr = null;

        if (ctx.lookahead().type() != TokenType.TOKEN_SEMI) {
            expr = exprParser.parseExpr();
        }

        ctx.match(TokenType.TOKEN_SEMI);

        return new ReturnNode(
                retToken.line(),
                retToken.column(),
                expr
        );
    }

    private PrintNode parsePrint() throws IOException {

        Token printToken = ctx.lookahead();

        ctx.match(TokenType.TOKEN_PRINT);
        ctx.match(TokenType.TOKEN_LPAREN);

        ExprNode expr = exprParser.parseExpr();

        ctx.match(TokenType.TOKEN_RPAREN);
        ctx.match(TokenType.TOKEN_SEMI);

        return new PrintNode(
                printToken.line(),
                printToken.column(),
                expr
        );
    }

    private BreakNode parseBreak() throws IOException {

        Token breakToken = ctx.lookahead();

        ctx.match(TokenType.TOKEN_BREAK);
        ctx.match(TokenType.TOKEN_SEMI);

        return new BreakNode(
                breakToken.line(),
                breakToken.column()
        );
    }

    private ContinueNode parseContinue() throws IOException {

        Token contToken = ctx.lookahead();

        ctx.match(TokenType.TOKEN_CONTINUE);
        ctx.match(TokenType.TOKEN_SEMI);

        return new ContinueNode(
                contToken.line(),
                contToken.column()
        );
    }

    private RuntimeException syntaxError(String msg) {

        Token t = ctx.lookahead();

        return new RuntimeException(
                "Erro sintático na linha "
                        + t.line()
                        + " coluna "
                        + t.column()
                        + ": "
                        + msg
        );
    }
}