package br.edu.ufape.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class CharStream {

    private final BufferedReader reader;
    private int currentChar;

    private int line = 1;
    private int column = 1;

    public CharStream(Reader reader) throws IOException {
        this.reader = new BufferedReader(reader);
        advance();
    }

    public void advance() throws IOException {

        if (currentChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        currentChar = reader.read();
    }

    public int current() {
        return currentChar;
    }

    public boolean isEOF() {
        return currentChar == -1;
    }

    public int line() {
        return line;
    }

    public int column() {
        return column;
    }
}