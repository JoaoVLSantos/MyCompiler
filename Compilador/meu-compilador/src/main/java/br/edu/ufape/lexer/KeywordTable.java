package br.edu.ufape.lexer;

import java.util.Map;

public class KeywordTable {

    private static final Map<String, TokenType> keywords = Map.ofEntries(
            Map.entry("int", TokenType.TOKEN_INTEGER),
            Map.entry("float", TokenType.TOKEN_FLOAT),
            Map.entry("bool", TokenType.TOKEN_BOOLEAN),
            Map.entry("char", TokenType.TOKEN_CHAR),
            Map.entry("void", TokenType.TOKEN_VOID),

            Map.entry("proc", TokenType.TOKEN_PROC),
            Map.entry("func", TokenType.TOKEN_FUNC),
            Map.entry("return", TokenType.TOKEN_RETURN),
            Map.entry("if", TokenType.TOKEN_IF),
            Map.entry("else", TokenType.TOKEN_ELSE),
            Map.entry("while", TokenType.TOKEN_WHILE),
            Map.entry("break", TokenType.TOKEN_BREAK),
            Map.entry("continue", TokenType.TOKEN_CONTINUE),
            Map.entry("print", TokenType.TOKEN_PRINT),
            Map.entry("true", TokenType.TOKEN_TRUE),
            Map.entry("false", TokenType.TOKEN_FALSE)
    );

    public static TokenType get(String lexeme) {
        return keywords.getOrDefault(lexeme, TokenType.TOKEN_ID);
    }
}