package br.edu.ufape.parser;

import br.edu.ufape.ast.stmt.VarDeclNode;
import br.edu.ufape.lexer.Token;
import br.edu.ufape.lexer.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeclarationParser {

    private final ParserContext ctx;

    public DeclarationParser(ParserContext ctx) {
        this.ctx = ctx;
    }

    public boolean isTipo() {

        TokenType t = ctx.lookahead().type();

        return t == TokenType.TOKEN_INTEGER ||
               t == TokenType.TOKEN_FLOAT ||
               t == TokenType.TOKEN_BOOLEAN ||
               t == TokenType.TOKEN_CHAR;
    }

    public VarDeclNode parseDecl() throws IOException {

        Token typeToken = ctx.lookahead();

        VarDeclNode.Type type = parseTipo();

        List<String> ids = parseListaId();

        ctx.match(TokenType.TOKEN_SEMI);

        return new VarDeclNode(
                typeToken.line(),
                typeToken.column(),
                type,
                ids
        );
    }

    VarDeclNode.Type parseTipo() throws IOException {

        switch (ctx.lookahead().type()) {

            case TOKEN_INTEGER:
                ctx.match(TokenType.TOKEN_INTEGER);
                return VarDeclNode.Type.INTEGER;

            case TOKEN_FLOAT:
                ctx.match(TokenType.TOKEN_FLOAT);
                return VarDeclNode.Type.FLOAT;

            case TOKEN_BOOLEAN:
                ctx.match(TokenType.TOKEN_BOOLEAN);
                return VarDeclNode.Type.BOOLEAN;

            case TOKEN_CHAR:
                ctx.match(TokenType.TOKEN_CHAR);
                return VarDeclNode.Type.CHAR;

            default:
                throw syntaxError("Tipo inválido");
        }
    }

    private List<String> parseListaId() throws IOException {

        List<String> ids = new ArrayList<>();

        Token idToken = ctx.lookahead();

        if (idToken.type() != TokenType.TOKEN_ID) {
            throw syntaxError("Identificador esperado");
        }

        ids.add(idToken.lexeme());
        ctx.match(TokenType.TOKEN_ID);

        while (ctx.lookahead().type() == TokenType.TOKEN_COMMA) {

            ctx.match(TokenType.TOKEN_COMMA);

            Token nextId = ctx.lookahead();

            if (nextId.type() != TokenType.TOKEN_ID) {
                throw syntaxError("Identificador esperado após ','");
            }

            ids.add(nextId.lexeme());
            ctx.match(TokenType.TOKEN_ID);
        }

        return ids;
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