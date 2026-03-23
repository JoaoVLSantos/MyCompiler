package br.edu.ufape.parser;

import br.edu.ufape.ast.expr.*;
import br.edu.ufape.lexer.Token;
import br.edu.ufape.lexer.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {

    private final ParserContext ctx;

    public ExpressionParser(ParserContext ctx) {
        this.ctx = ctx;
    }

    public ExprNode parseExpr() throws IOException {

        ExprNode left = parseTerm();

        while (ctx.lookahead().type() == TokenType.TOKEN_PLUS ||
               ctx.lookahead().type() == TokenType.TOKEN_MINUS) {

            Token opToken = ctx.lookahead();
            String op = opToken.lexeme();

            ctx.match(opToken.type());

            ExprNode right = parseTerm();

            left = new BinaryOpNode(
                    opToken.line(),
                    opToken.column(),
                    op,
                    left,
                    right
            );
        }

        return left;
    }

    private ExprNode parseTerm() throws IOException {

        ExprNode left = parseFactor();

        while (ctx.lookahead().type() == TokenType.TOKEN_MUL ||
            ctx.lookahead().type() == TokenType.TOKEN_DIV ||
            ctx.lookahead().type() == TokenType.TOKEN_MOD) {

            Token opToken = ctx.lookahead();
            String op = opToken.lexeme();

            ctx.match(opToken.type());

            ExprNode right = parseFactor();

            left = new BinaryOpNode(
                    opToken.line(),
                    opToken.column(),
                    op,
                    left,
                    right
            );
        }

        return left;
    }

    private ExprNode parseFactor() throws IOException {

        Token token = ctx.lookahead();

        return switch (token.type()) {

            case TOKEN_INT_LITERAL -> {
                ctx.match(TokenType.TOKEN_INT_LITERAL);
                yield new IntLiteralNode(token.line(), token.column(), token.lexeme());
            }

            case TOKEN_FLOAT_LITERAL -> {
                ctx.match(TokenType.TOKEN_FLOAT_LITERAL);
                yield new FloatLiteralNode(token.line(), token.column(), token.lexeme());
            }

            case TOKEN_CHAR_LITERAL -> {
                ctx.match(TokenType.TOKEN_CHAR_LITERAL);
                yield new CharLiteralNode(token.line(), token.column(), token.lexeme());
            }

            case TOKEN_TRUE -> {
                ctx.match(TokenType.TOKEN_TRUE);
                yield new BooleanLiteralNode(token.line(), token.column(), true);
            }

            case TOKEN_FALSE -> {
                ctx.match(TokenType.TOKEN_FALSE);
                yield new BooleanLiteralNode(token.line(), token.column(), false);
            }

            case TOKEN_ID -> {

                String name = token.lexeme();

                ctx.match(TokenType.TOKEN_ID);

                if (ctx.lookahead().type() == TokenType.TOKEN_LPAREN) {

                    ctx.match(TokenType.TOKEN_LPAREN);

                    List<ExprNode> args = new ArrayList<>();

                    if (ctx.lookahead().type() != TokenType.TOKEN_RPAREN) {

                        args.add(parseExpr());

                        while (ctx.lookahead().type() == TokenType.TOKEN_COMMA) {
                            ctx.match(TokenType.TOKEN_COMMA);
                            args.add(parseExpr());
                        }
                    }

                    ctx.match(TokenType.TOKEN_RPAREN);

                    yield new CallExprNode(
                            token.line(),
                            token.column(),
                            name,
                            args
                    );
                }

                yield new IdentifierNode(token.line(), token.column(), name);
            }

            case TOKEN_LPAREN -> {
                ctx.match(TokenType.TOKEN_LPAREN);
                ExprNode expr = parseBooleanExpr();
                ctx.match(TokenType.TOKEN_RPAREN);
                yield expr;
            }

            default -> throw syntaxError("Fator inválido");
        };
    }

    public ExprNode parseBooleanExpr() throws IOException {
        return parseOrExpr();
    }

    private ExprNode parseOrExpr() throws IOException {

        ExprNode left = parseAndExpr();

        while (ctx.lookahead().type() == TokenType.TOKEN_OR) {

            Token opToken = ctx.lookahead();
            ctx.match(TokenType.TOKEN_OR);

            ExprNode right = parseAndExpr();

            left = new BinaryOpNode(
                    opToken.line(),
                    opToken.column(),
                    opToken.lexeme(),
                    left,
                    right
            );
        }

        return left;
    }

    private ExprNode parseAndExpr() throws IOException {

        ExprNode left = parseRelationalExpr();

        while (ctx.lookahead().type() == TokenType.TOKEN_AND) {

            Token opToken = ctx.lookahead();
            ctx.match(TokenType.TOKEN_AND);

            ExprNode right = parseRelationalExpr();

            left = new BinaryOpNode(
                    opToken.line(),
                    opToken.column(),
                    opToken.lexeme(),
                    left,
                    right
            );
        }

        return left;
    }

    private ExprNode parseRelationalExpr() throws IOException {

        ExprNode left = parseExpr();

        if (isRelOp(ctx.lookahead().type())) {

            Token opToken = ctx.lookahead();

            ctx.match(opToken.type());

            ExprNode right = parseExpr();

            return new BinaryOpNode(
                    opToken.line(),
                    opToken.column(),
                    opToken.lexeme(),
                    left,
                    right
            );
        }

        return left;
    }

    private boolean isRelOp(TokenType type) {

        return type == TokenType.TOKEN_EQ ||
               type == TokenType.TOKEN_NEQ ||
               type == TokenType.TOKEN_GT ||
               type == TokenType.TOKEN_GTE ||
               type == TokenType.TOKEN_LT ||
               type == TokenType.TOKEN_LTE;
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