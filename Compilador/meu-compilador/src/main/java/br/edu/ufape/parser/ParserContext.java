package br.edu.ufape.parser;

import br.edu.ufape.lexer.Lexer;
import br.edu.ufape.lexer.Token;
import br.edu.ufape.lexer.TokenType;

import java.io.IOException;

public class ParserContext {

    private final Lexer lexer;
    private Token lookahead;

    public ParserContext(Lexer lexer) throws IOException {
        this.lexer = lexer;
        this.lookahead = lexer.getNextToken();
    }

    public Token lookahead() {
        return lookahead;
    }

    public void match(TokenType expected) throws IOException {

        if (lookahead.type() == expected) {
            lookahead = lexer.getNextToken();
        } else {

            throw new RuntimeException(
                    "Erro sintático na linha "
                            + lookahead.line()
                            + " coluna "
                            + lookahead.column()
                            + ": esperado "
                            + expected
                            + " encontrado "
                            + lookahead.type()
            );
        }
    }
}
