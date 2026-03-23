package br.edu.ufape;

import org.junit.jupiter.api.Test;

import br.edu.ufape.lexer.Lexer;
import br.edu.ufape.lexer.Token;
import br.edu.ufape.lexer.TokenType;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void testKeywordRecognition() throws Exception {

        Lexer lex = new Lexer(new StringReader("integer"));

        Token token = lex.getNextToken();

        assertEquals(TokenType.TOKEN_INTEGER, token.type());
        assertEquals("integer", token.lexeme());
    }

    @Test
    void testIdentifier() throws Exception {

        Lexer lex = new Lexer(new StringReader("variavel"));

        Token token = lex.getNextToken();

        assertEquals(TokenType.TOKEN_ID, token.type());
        assertEquals("variavel", token.lexeme());
    }

    @Test
    void testIntLiteral() throws Exception {

        Lexer lex = new Lexer(new StringReader("12345"));

        Token token = lex.getNextToken();

        assertEquals(TokenType.TOKEN_INT_LITERAL, token.type());
        assertEquals("12345", token.lexeme());
    }

    @Test
    void testFloatLiteral() throws Exception {

        Lexer lex = new Lexer(new StringReader("12.34"));

        Token token = lex.getNextToken();

        assertEquals(TokenType.TOKEN_FLOAT_LITERAL, token.type());
        assertEquals("12.34", token.lexeme());
    }

    @Test
    void testCompositeOperator() throws Exception {

        Lexer lex = new Lexer(new StringReader(">="));

        Token token = lex.getNextToken();

        assertEquals(TokenType.TOKEN_GTE, token.type());
        assertEquals(">=", token.lexeme());
    }

    @Test
    void testFullStatement() throws Exception {

        Lexer lex = new Lexer(new StringReader("integer x = 10;"));

        assertEquals(TokenType.TOKEN_INTEGER, lex.getNextToken().type());
        assertEquals(TokenType.TOKEN_ID, lex.getNextToken().type());
        assertEquals(TokenType.TOKEN_ASSIGN, lex.getNextToken().type());
        assertEquals(TokenType.TOKEN_INT_LITERAL, lex.getNextToken().type());
        assertEquals(TokenType.TOKEN_SEMI, lex.getNextToken().type());
        assertEquals(TokenType.TOKEN_EOF, lex.getNextToken().type());
    }
}