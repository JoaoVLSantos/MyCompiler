# Compilador

## Analisador Léxico, Sintático e Semântico (checagem de tipos)

Este projeto consiste na implementação de um compilador acadêmico desenvolvido como parte da avaliação da **2ª Verificação de Aprendizagem (2ª VA)** da disciplina de **Compiladores**.

O compilador foi implementado em **Java** utilizando **Maven** como sistema de gerenciamento de dependências e organização do projeto.

O objetivo do projeto é implementar as principais etapas iniciais do processo de compilação:

- **Análise Léxica**
- **Análise Sintática**
- **Análise Semântica (checagem de tipos)**

A linguagem utilizada no compilador é uma linguagem procedural simples, com suporte a variáveis, expressões, estruturas de controle, funções e procedimentos.

---

# Estrutura do Projeto

O projeto está organizado em pacotes, cada um representando uma etapa do compilador.

---

# 1. Lexer (Analisador Léxico)

O pacote **lexer** é responsável por realizar a **análise léxica do programa fonte**.

Nesta etapa, o código fonte é lido caractere por caractere e convertido em uma sequência de **tokens**, que representam unidades léxicas da linguagem.

Entre os elementos reconhecidos estão:

- Identificadores
- Palavras reservadas
- Operadores
- Literais (inteiros, float, char e boolean)
- Delimitadores

### Classes

- `Lexer.java` → realiza a leitura e geração dos tokens  
- `Token.java` → representa um token identificado  
- `TokenType.java` → enumeração dos tipos de tokens  
- `KeywordTable.java` → tabela de palavras reservadas  
- `CharStream.java` → controle de leitura do fluxo de caracteres  

---

# 2. Parser (Analisador Sintático)

O pacote **parser** implementa o **analisador sintático**, responsável por verificar se a sequência de tokens segue as regras da gramática da linguagem.

O parser foi implementado utilizando a técnica de **Recursive Descent Parsing (parser descendente recursivo)**.

Durante essa etapa, além de validar a sintaxe do programa, é construída uma **Árvore Sintática Abstrata (AST)** que representa a estrutura do programa.

### Classes

- `Parser.java` → analisador principal  
- `ParserContext.java` → controle de tokens e lookahead  
- `DeclarationParser.java` → análise de declarações  
- `ExpressionParser.java` → análise de expressões  
- `StatementParser.java` → análise de comandos  

---

# 3. AST (Abstract Syntax Tree)

O pacote **ast** define a estrutura da **Árvore Sintática Abstrata**.

A AST é uma representação hierárquica do programa que elimina detalhes puramente sintáticos e mantém apenas os elementos necessários para análise semântica.

Os nós da AST estão organizados em subpacotes:

### Exemplos de nós

- `BinaryOpNode`
- `AssignNode`
- `IfNode`
- `WhileNode`
- `FunctionNode`
- `ProcedureNode`
- `ProgramNode`

---

# 4. Semantic (Analisador Semântico)

O pacote **semantic** implementa a **análise semântica com checagem de tipos**.

Nesta etapa o compilador percorre a AST verificando propriedades semânticas do programa, como:

- Variáveis declaradas antes do uso
- Compatibilidade de tipos em expressões
- Tipos corretos em atribuições
- Chamadas de função válidas
- Número correto de argumentos
- Uso correto de `break` e `continue`
- Tipo de retorno de funções

### Classes

- `SemanticAnalyzer.java` → percorre a AST e realiza as verificações  
- `SymbolTable.java` → tabela de símbolos com controle de escopo  
- `Symbol.java` → representação de um símbolo  
- `FunctionSymbol.java` → representação de funções  
- `Type.java` → sistema de tipos da linguagem  

---

# Características da Linguagem

O compilador implementa uma linguagem com:

## Tipagem

- **Tipagem estática**
- **Tipagem forte**
- **Sem sobrecarga de funções ou operadores**

## Tipos suportados

- `integer`
- `float`
- `boolean`
- `char`
- `void`

## Estruturas suportadas

- Declaração de variáveis
- Atribuições
- Expressões aritméticas
- Expressões relacionais
- Estruturas condicionais (`if / else`)
- Laços (`while`)
- `break` e `continue`
- Funções
- Procedimentos
- `print`

---

# Tecnologias Utilizadas

- **Java 21**
- **Maven**
- **JUnit 5 (para testes)**

---

Este programa não representa um compilador completo, mas sim uma estrutura desenvolvida com fins acadêmicos, com o objetivo de aplicar na prática alguns dos principais conceitos estudados na disciplina de Compiladores.

