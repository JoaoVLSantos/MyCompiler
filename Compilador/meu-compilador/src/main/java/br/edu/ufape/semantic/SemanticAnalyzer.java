package br.edu.ufape.semantic;

import br.edu.ufape.ast.*;
import br.edu.ufape.ast.routines.*;
import br.edu.ufape.ast.expr.*;
import br.edu.ufape.ast.stmt.*;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private final SymbolTable table = new SymbolTable();

    private String symbolTableSnapshot = "";

    private boolean foundReturn = false;

    private int loopDepth = 0;

    private Type currentReturnType = null;

    public void analyze(ProgramNode program) {

        table.enterScope();

        for (Node decl : program.declarations) {
            analyzeDeclaration(decl);
        }

        for (Node routine : program.routines) {
            registerRoutine(routine);
        }

        for (Node routine : program.routines) {
            analyzeRoutine(routine);
        }

        analyzeBlock(program.mainBlock);

        symbolTableSnapshot = table.toString();

        table.exitScope();
    }

    private void registerRoutine(Node node) {

        if (node instanceof FunctionNode fn) {

            List<Type> params = new ArrayList<>();

            for (ParameterNode p : fn.params) {
                params.add(convertType(p.type));
            }

            Type returnType = convertType(fn.returnType);

            table.declare(new FunctionSymbol(fn.name, returnType, params, "function"));
        }

        else if (node instanceof ProcedureNode proc) {

            List<Type> params = new ArrayList<>();

            for (ParameterNode p : proc.params) {
                params.add(convertType(p.type));
            }

            table.declare(new FunctionSymbol(proc.name, Type.VOID, params, "procedure"));
        }
    }

    private void analyzeRoutine(Node node) {

        if (node instanceof FunctionNode fn) {

            currentReturnType = convertType(fn.returnType);

            foundReturn = false;

            table.enterScope();

            for (ParameterNode p : fn.params) {

                table.declare(
                        new Symbol(
                                p.name,
                                convertType(p.type)
                        )
                );
            }

            analyzeBlock(fn.body);

            if (currentReturnType != Type.VOID && !foundReturn) {

                error(fn,
                        "função '" + fn.name + "' deve retornar valor");
            }

            table.exitScope();

            currentReturnType = null;
        }

        else if (node instanceof ProcedureNode proc) {

            currentReturnType = Type.VOID;

            table.enterScope();

            for (ParameterNode p : proc.params) {

                table.declare(
                        new Symbol(
                                p.name,
                                convertType(p.type)
                        )
                );
            }

            analyzeBlock(proc.body);

            table.exitScope();

            currentReturnType = null;
        }
    }

    private void analyzeDeclaration(Node node) {

        if (node instanceof VarDeclNode varDecl) {

            Type type = convertType(varDecl.type);

            for (String id : varDecl.identifiers) {

                table.declare(new Symbol(id, type));
            }
        }
    }

    private void analyzeBlock(BlockNode block) {

        table.enterScope();

        for (StmtNode stmt : block.statements) {

            if (stmt instanceof VarDeclNode varDecl) {

                Type type = convertType(varDecl.type);

                for (String id : varDecl.identifiers) {
                    table.declare(new Symbol(id, type));
                }

                continue;
            }

            analyzeStatement(stmt);
        }

        table.exitScope();
    }

    private void analyzeStatement(StmtNode stmt) {

        if (stmt instanceof AssignNode assign) {

            Symbol symbol = table.lookup(assign.identifier);

            Type exprType = analyzeExpression(assign.expression);

            if (!symbol.getType().isAssignableFrom(exprType)) {

                error(stmt,
                        "atribuição incompatível para '" +
                                assign.identifier +
                                "': esperado " + symbol.getType() +
                                ", encontrado " + exprType);
            }
        }

        else if (stmt instanceof IfNode ifNode) {

            Type condType = analyzeExpression(ifNode.condition);

            if (condType != Type.BOOLEAN) {
                error(stmt, "condição do if deve ser boolean");
            }

            analyzeBlock(ifNode.thenBlock);

            if (ifNode.elseBlock != null) {
                analyzeBlock(ifNode.elseBlock);
            }
        }

        else if (stmt instanceof WhileNode whileNode) {

            Type condType = analyzeExpression(whileNode.condition);

            if (condType != Type.BOOLEAN) {
                error(stmt, "condição do while deve ser boolean");
            }

            loopDepth++;

            analyzeBlock(whileNode.block);

            loopDepth--;
        }

        else if (stmt instanceof BreakNode) {

            if (loopDepth == 0) {
                error(stmt, "'break' fora de um loop");
            }
        }

        else if (stmt instanceof ContinueNode) {

            if (loopDepth == 0) {
                error(stmt, "'continue' fora de um loop");
            }
        }

        else if (stmt instanceof PrintNode print) {

            analyzeExpression(print.expression);
        }

        else if (stmt instanceof ReturnNode ret) {

            foundReturn = true;

            if (currentReturnType == null) {
                error(stmt, "return fora de função");
            }

            if (ret.expression != null) {

                Type exprType = analyzeExpression(ret.expression);

                if (!currentReturnType.isAssignableFrom(exprType)) {

                    error(stmt,
                            "tipo de retorno incompatível: esperado "
                                    + currentReturnType +
                                    " encontrado " + exprType);
                }
            }

            else if (currentReturnType != Type.VOID) {

                error(stmt,
                        "função deve retornar valor");
            }
        }

        else if (stmt instanceof CallStmtNode call) {

            analyzeCall(call.name, call.args, stmt);
        }
    }

    private Type analyzeExpression(ExprNode expr) {

        if (expr instanceof IntLiteralNode)
            return Type.INTEGER;

        if (expr instanceof FloatLiteralNode)
            return Type.FLOAT;

        if (expr instanceof CharLiteralNode)
            return Type.CHAR;

        if (expr instanceof BooleanLiteralNode)
            return Type.BOOLEAN;

        if (expr instanceof IdentifierNode id) {
            return table.lookup(id.name).getType();
        }

        if (expr instanceof BinaryOpNode bin) {

            Type left = analyzeExpression(bin.left);
            Type right = analyzeExpression(bin.right);

            String op = bin.operator;

            /* OPERADORES ARITMÉTICOS */

            if (isArithmetic(op)) {

                if (!left.isNumeric() || !right.isNumeric()) {
                    error(expr,
                            "operador '" + op + "' requer operandos numéricos");
                }

                if (left == Type.FLOAT || right == Type.FLOAT)
                    return Type.FLOAT;

                return Type.INTEGER;
            }

            /* OPERADORES RELACIONAIS */

            if (isRelational(op)) {

                if (left.isNumeric() && right.isNumeric()) {
                    return Type.BOOLEAN;
                }

                if (left == right && left.isComparable()) {
                    return Type.BOOLEAN;
                }

                error(expr,
                        "comparação entre tipos incompatíveis: "
                                + left + " e " + right);
            }

            /* OPERADORES LÓGICOS */

            if (op.equals("&&") || op.equals("||")) {

                if (!left.isBoolean() || !right.isBoolean()) {

                    error(expr,
                            "operador lógico '" + op +
                                    "' requer operandos booleanos");
                }

                return Type.BOOLEAN;
            }
        }

        if (expr instanceof CallExprNode call) {

            return analyzeCall(call.name, call.args, expr);
        }

        throw new RuntimeException("Expressão inválida");
    }

    private Type analyzeCall(String name, List<ExprNode> args, Node node) {

        Symbol symbol = table.lookup(name);

        if (!(symbol instanceof FunctionSymbol)) {
            error(node, name + " não é uma função/procedimento");
        }

        FunctionSymbol fn = (FunctionSymbol) symbol;

        if (fn.getParams().size() != args.size()) {

            error(node,
                    "número incorreto de argumentos em chamada de " + name);
        }

        for (int i = 0; i < args.size(); i++) {

            Type argType = analyzeExpression(args.get(i));
            Type paramType = fn.getParams().get(i);

            if (!paramType.isAssignableFrom(argType)) {

                error(node,
                        "tipo incompatível no argumento " + (i + 1));
            }
        }

        return fn.getType();
    }

    private boolean isArithmetic(String op) {

        return op.equals("+") ||
                op.equals("-") ||
                op.equals("*") ||
                op.equals("/") ||
                op.equals("%");
    }

    private boolean isRelational(String op) {

        return op.equals("==") ||
                op.equals("!=") ||
                op.equals(">") ||
                op.equals(">=") ||
                op.equals("<") ||
                op.equals("<=");
    }

    private Type convertType(VarDeclNode.Type type) {

        return switch (type) {

            case INTEGER -> Type.INTEGER;
            case FLOAT -> Type.FLOAT;
            case BOOLEAN -> Type.BOOLEAN;
            case CHAR -> Type.CHAR;
        };
    }

    private void error(Node node, String msg) {

        throw new RuntimeException(
                "Erro semântico na linha "
                        + node.line +
                        " coluna "
                        + node.column +
                        ": "
                        + msg
        );
    }

    public SymbolTable getSymbolTable() {
        return table;
    }

    public String getSymbolTableSnapshot() {
        return symbolTableSnapshot;
    }
}