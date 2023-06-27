//Elaborado por: Ramirez Contreras Angel Humberto (5CV1) y Diaz Rosales Mauricio Yael (3CV15)
package mx.ipn.escom.compiladores;

import java.util.*;

class Parser {
    private List<String> tokens;
    private int currentTokenIndex;

    public boolean parse(List<String> tokenList) {
        tokens = tokenList;
        currentTokenIndex = 0;
        try {
            declaration();
            return match("Ɛ");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void declaration() throws ParseException {
        if (match("class")) {
            match("id");
            classInher();
            match("{");
            functions();
            match("}");
            declaration();
        } else if (match("fun")) {
            function();
            declaration();
        } else if (match("var")) {
            match("id");
            varInit();
            match(";");
            declaration();
        } else if (!match("Ɛ")) {
            statement();
            declaration();
        }
    }

    private void classInher() throws ParseException {
        if (match("<")) {
            match("id");
        }
    }

    private void function() throws ParseException {
        match("id");
        match("(");
        parametersOpc();
        match(")");
        block();
    }

    private void functions() throws ParseException {
        if (!match("Ɛ")) {
            function();
            functions();
        }
    }

    private void parametersOpc() throws ParseException {
        if (!match(")")) {
            parameters();
        }
    }

    private void parameters() throws ParseException {
        match("id");
        parameters2();
    }

    private void parameters2() throws ParseException {
        if (match(",")) {
            match("id");
            parameters2();
        }
    }

    private void varInit() throws ParseException {
        if (match("=")) {
            expression();
        }
    }

    private void statement() throws ParseException {
        if (match("for")) {
            match("(");
            forStmt1();
            forStmt2();
            forStmt3();
            match(")");
            statement();
        } else if (match("if")) {
            match("(");
            expression();
            match(")");
            statement();
            elseStatement();
            statement();
        } else if (match("print")) {
            expression();
            match(";");
        } else if (match("return")) {
            returnExpOpc();
            match(";");
        } else if (match("while")) {
            match("(");
            expression();
            match(")");
            statement();
        } else if (match("{")) {
            block();
        } else {
            exprStmt();
            match(";");
        }
    }

    private void elseStatement() throws ParseException {
        if (match("else")) {
            statement();
        }
    }

    private void exprStmt() throws ParseException {
        expression();
    }

    private void forStmt1() throws ParseException {
        if (match("var")) {
            match("id");
            varInit();
            match(";");
        } else if (!match(";")) {
            exprStmt();
            match(";");
        }
    }

    private void forStmt2() throws ParseException {
        if (!match(";")) {
            expression();
            match(";");
        }
    }

    private void forStmt3() throws ParseException {
        if (!match(")")) {
            expression();
        }
    }

    private void block() throws ParseException {
        blockDecl();
        match("}");
    }

    private void blockDecl() throws ParseException {
        if (!match("Ɛ")) {
            declaration();
            blockDecl();
        }
    }

    private void returnExpOpc() throws ParseException {
        if (!match(";")) {
            expression();
        }
    }

    private void expression() throws ParseException {
        assignment();
    }

    private void assignment() throws ParseException {
        logicOr();
        assignmentOpc();
    }

    private void assignmentOpc() throws ParseException {
        if (match("=")) {
            expression();
        }
    }

    private void logicOr() throws ParseException {
        logicAnd();
        logicOr2();
    }

    private void logicOr2() throws ParseException {
        if (match("or")) {
            logicAnd();
            logicOr2();
        }
    }

    private void logicAnd() throws ParseException {
        equality();
        logicAnd2();
    }

    private void logicAnd2() throws ParseException {
        if (match("and")) {
            equality();
            logicAnd2();
        }
    }

    private void equality() throws ParseException {
        comparison();
        equality2();
    }

    private void equality2() throws ParseException {
        if (match("!=") || match("==")) {
            comparison();
            equality2();
        }
    }

    private void comparison() throws ParseException {
        term();
        comparison2();
    }

    private void comparison2() throws ParseException {
        if (match(">") || match(">=") || match("<") || match("<=")) {
            term();
            comparison2();
        }
    }

    private void term() throws ParseException {
        factor();
        term2();
    }

    private void term2() throws ParseException {
        if (match("-") || match("+")) {
            factor();
            term2();
        }
    }

    private void factor() throws ParseException {
        unary();
        factor2();
    }

    private void factor2() throws ParseException {
        if (match("/") || match("*")) {
            unary();
            factor2();
        }
    }

    private void unary() throws ParseException {
        if (match("!") || match("-")) {
            unary();
        } else {
            call();
        }
    }

    private void call() throws ParseException {
        primary();
        call2();
    }

    private void call2() throws ParseException {
        if (match("(")) {
            argumentsOpc();
            match(")");
            call2();
        } else if (match(".") && match("id")) {
            call2();
        }
    }

    private void argumentsOpc() throws ParseException {
        if (!match(")")) {
            arguments();
        }
    }

    private void arguments() throws ParseException {
        expression();
        arguments2();
    }

    private void arguments2() throws ParseException {
        if (match(",")) {
            expression();
            arguments2();
        }
    }

    private void primary() throws ParseException {
        if (match("true") || match("false") || match("null") || match("this") || match("number") ||
                match("string") || match("id")) {
            // Terminal symbol
        } else if (match("(")) {
            expression();
            match(")");
        } else if (match("super") && match(".") && match("id")) {
            // Terminal symbol
        } else {
            throw new ParseException("Invalid syntax. Expected primary expression.");
        }
    }

    private boolean match(String token) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(token)) {
            currentTokenIndex++;
            return true;
        }
        return false;
    }

    private class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}

public class main {
    public static void main(String[] args) {
        List<String> tokens = Arrays.asList("class", "MyClass", "{", "fun", "myFunction", "(", "var", "x", "=", "5", ";", ")", "{", "print", "x", ";", "}", "}");
        Parser parser = new Parser();
        boolean isValid = parser.parse(tokens);
        System.out.println("Is valid? " + isValid);
    }
}
