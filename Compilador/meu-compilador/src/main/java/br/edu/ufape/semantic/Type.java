package br.edu.ufape.semantic;

public enum Type {

    INTEGER,
    FLOAT,
    BOOLEAN,
    CHAR,
    VOID;

    public boolean isNumeric() {
        return this == INTEGER || this == FLOAT;
    }

    public boolean isBoolean() {
        return this == BOOLEAN;
    }

    public boolean isChar() {
        return this == CHAR;
    }

    public boolean isVoid() {
        return this == VOID;
    }

    public boolean isComparable() {
        return this == INTEGER ||
               this == FLOAT ||
               this == CHAR;
    }

    public boolean isAssignableFrom(Type other) {

    if (this == other)
        return true;

    if (this == FLOAT && other == INTEGER)
        return true;

    return false;
    }
}