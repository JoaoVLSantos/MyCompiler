package br.edu.ufape.lexer;

import java.io.IOException;
import java.io.Reader;

public class Lexer {

    private final CharStream stream;

    public Lexer(Reader reader) throws IOException {
        this.stream = new CharStream(reader);
    }

    private RuntimeException error(String msg) {
        return new RuntimeException(
                "Erro léxico na linha "
                        + stream.line()
                        + " coluna "
                        + stream.column()
                        + ": "
                        + msg
        );
    }

    public Token getNextToken() throws IOException {

        while (Character.isWhitespace(stream.current())) {
            stream.advance();
        }

        if (stream.current() == '/') {

            stream.advance();

            if (stream.current() == '/') {

                while (stream.current() != '\n' && !stream.isEOF()) {
                    stream.advance();
                }

                return getNextToken();
            }

            if (stream.current() == '*') {

                stream.advance();

                while (true) {

                    if (stream.isEOF()) {
                        throw error("comentário de bloco não fechado");
                    }

                    if (stream.current() == '*') {
                        stream.advance();

                        if (stream.current() == '/') {
                            stream.advance();
                            break;
                        }
                    } else {
                        stream.advance();
                    }
                }

                return getNextToken();
            }

            return new Token(
                    TokenType.TOKEN_DIV,
                    "/",
                    stream.line(),
                    stream.column()
            );
        }


        if (stream.isEOF()) {
            return new Token(TokenType.TOKEN_EOF, "", stream.line(), stream.column());
        }

        int line = stream.line();
        int column = stream.column();


        if (Character.isLetter(stream.current()) || stream.current() == '_') {

            StringBuilder lexeme = new StringBuilder();

            while (Character.isLetterOrDigit(stream.current()) || stream.current() == '_') {
                lexeme.append((char) stream.current());
                stream.advance();
            }

            String word = lexeme.toString();
            TokenType type = KeywordTable.get(word);

            return new Token(type, word, line, column);
        }

        if (Character.isDigit(stream.current())) {

            StringBuilder lexeme = new StringBuilder();
            boolean isFloat = false;

            while (Character.isDigit(stream.current())) {
                lexeme.append((char) stream.current());
                stream.advance();
            }

            if (stream.current() == '.') {

                isFloat = true;
                lexeme.append('.');
                stream.advance();

                if (!Character.isDigit(stream.current())) {
                    throw error("float inválido");
                }

                while (Character.isDigit(stream.current())) {
                    lexeme.append((char) stream.current());
                    stream.advance();
                }
            }

            return new Token(
                    isFloat ? TokenType.TOKEN_FLOAT_LITERAL : TokenType.TOKEN_INT_LITERAL,
                    lexeme.toString(),
                    line,
                    column
            );
        }

        if (stream.current()== '&') {

            stream.advance();

            if (stream.current() == '&') {
                stream.advance();
                return new Token(TokenType.TOKEN_AND, "&&", line, column);
            }

            else {
                throw error("operador & inválido");
            }
            
        }

        if (stream.current()== '|') {

            stream.advance();

            if (stream.current() == '|') {
                stream.advance();
                return new Token(TokenType.TOKEN_OR, "||", line, column);
            }

            else {
                throw error("operador | inválido");
            }

        }

        if (stream.current() == '\'') {

            stream.advance();

            char value;

            if (stream.current() == '\\') {

                stream.advance();

                switch (stream.current()) {

                    case 'n':
                        value = '\n';
                        break;

                    case 't':
                        value = '\t';
                        break;

                    case '\\':
                        value = '\\';
                        break;

                    default:
                        throw error("escape inválido");
                }

                stream.advance();

            } else {

                value = (char) stream.current();
                stream.advance();
            }

            if (stream.current() != '\'') {
                throw error("literal char inválido");
            }

            stream.advance();

            return new Token(
                    TokenType.TOKEN_CHAR_LITERAL,
                    String.valueOf(value),
                    line,
                    column
            );
        }
        

        switch (stream.current()) {

            case '+':
                stream.advance();
                return new Token(TokenType.TOKEN_PLUS, "+", line, column);

            case '-':
                stream.advance();
                return new Token(TokenType.TOKEN_MINUS, "-", line, column);

            case '*':
                stream.advance();
                return new Token(TokenType.TOKEN_MUL, "*", line, column);
            
            case '%':
                stream.advance();
                return new Token(TokenType.TOKEN_MOD, "%", line, column);

            case '(':
                stream.advance();
                return new Token(TokenType.TOKEN_LPAREN, "(", line, column);

            case ')':
                stream.advance();
                return new Token(TokenType.TOKEN_RPAREN, ")", line, column);

            case '{':
                stream.advance();
                return new Token(TokenType.TOKEN_LBRACE, "{", line, column);

            case '}':
                stream.advance();
                return new Token(TokenType.TOKEN_RBRACE, "}", line, column);

            case ';':
                stream.advance();
                return new Token(TokenType.TOKEN_SEMI, ";", line, column);

            case ',':
                stream.advance();
                return new Token(TokenType.TOKEN_COMMA, ",", line, column);

            case '=':
                stream.advance();
                if (stream.current() == '=') {
                    stream.advance();
                    return new Token(TokenType.TOKEN_EQ, "==", line, column);
                }
                return new Token(TokenType.TOKEN_ASSIGN, "=", line, column);

            case '!':
                stream.advance();
                if (stream.current() == '=') {
                    stream.advance();
                    return new Token(TokenType.TOKEN_NEQ, "!=", line, column);
                }
                throw error("operador ! inválido");

            case '>':
                stream.advance();
                if (stream.current() == '=') {
                    stream.advance();
                    return new Token(TokenType.TOKEN_GTE, ">=", line, column);
                }
                return new Token(TokenType.TOKEN_GT, ">", line, column);

            case '<':
                stream.advance();
                if (stream.current() == '=') {
                    stream.advance();
                    return new Token(TokenType.TOKEN_LTE, "<=", line, column);
                }
                return new Token(TokenType.TOKEN_LT, "<", line, column);

            default:
                char invalid = (char) stream.current();
                stream.advance();
                throw error("símbolo inválido: " + invalid);
        }
    }
}