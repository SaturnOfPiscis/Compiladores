//Elaborado por: Ramirez Contreras Angel Humberto (5CV1) y Diaz Rosales Mauricio Yael (3CV15)
package mx.ipn.escom.compiladores;

import java.text.ParseException;
import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void parse() throws ParseException, Exception {
        declaration();
    }

    private void declaration() throws ParseException, Exception {
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
        } else if (!match("}")) {
            statement();
            declaration();
        }
    }

    private void classInher() throws ParseException {
        if (match("<")) {
            match("id");
        }
    }

    private void function() throws ParseException, Exception {
        match("id");
        match("(");
        parametersOpc();
        match(")");
        block();
    }

    private void functions() throws ParseException, Exception {
        if (!match("}")) {
            function();
            functions();
        }
    }

    private void parametersOpc() throws ParseException, Exception {
        if (!match(")")) {
            parameters();
        }
    }

    private void parameters() throws ParseException, Exception {
        match("id");
        parameters2();
    }

    private void parameters2() throws ParseException, Exception {
        if (match(",")) {
            match("id");
            parameters2();
        }
    }

    private void varInit() throws ParseException, Exception {
        if (match("=")) {
            expression();
        }
    }

    private void statement() throws ParseException, Exception {
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

    private void elseStatement() throws ParseException, Exception {
        if (match("else")) {
            statement();
        }
    }

    private void exprStmt() throws ParseException, Exception {
        expression();
    }

    private void forStmt1() throws ParseException, Exception {
        if (match("var")) {
            match("id");
            varInit();
            match(";");
        } else if (!match(";")) {
            exprStmt();
            match(";");
        }
    }

    private void forStmt2() throws ParseException, Exception {
        if (!match(";")) {
            expression();
            match(";");
        }
    }

    private void forStmt3() throws ParseException, Exception {
        if (!match(")")) {
            expression();
        }
    }

    private void block() throws ParseException, Exception {
        blockDecl();
        match("}");
    }

    private void blockDecl() throws ParseException, Exception {
        if (!match("}")) {
            declaration();
            blockDecl();
        }
    }

    private void returnExpOpc() throws ParseException, Exception {
        if (!match(";")) {
            expression();
        }
    }

    private void expression() throws ParseException, Exception {
        assignment();
    }

    private void assignment() throws ParseException, Exception {
        logicOr();
        assignmentOpc();
    }

    private void assignmentOpc() throws ParseException, Exception {
        if (match("=")) {
            expression();
        }
    }

    private void logicOr() throws ParseException, Exception {
        logicAnd();
        logicOr2();
    }

    private void logicOr2() throws ParseException, Exception {
        if (match("or")) {
            logicAnd();
            logicOr2();
        }
    }

    private void logicAnd() throws ParseException, Exception {
        equality();
        logicAnd2();
    }

    private void logicAnd2() throws ParseException, Exception {
        if (match("and")) {
            equality();
            logicAnd2();
        }
    }

    private void equality() throws ParseException, Exception {
        comparison();
        equality2();
    }

    private void equality2() throws ParseException, Exception {
        if (match("!=") || match("==")) {
            comparison();
            equality2();
        }
    }

    private void comparison() throws ParseException, Exception {
        term();
        comparison2();
    }

    private void comparison2() throws ParseException, Exception {
        if (match(">") || match(">=") || match("<") || match("<=")) {
            term();
            comparison2();
        }
    }

    private void term() throws ParseException, Exception {
        factor();
        term2();
    }

    private void term2() throws ParseException, Exception {
        if (match("-") || match("+")) {
            factor();
            term2();
        }
    }

    private void factor() throws ParseException, Exception {
        unary();
        factor2();
    }

    private void factor2() throws ParseException, Exception {
        if (match("/") || match("*")) {
            unary();
            factor2();
        }
    }

    private void unary() throws ParseException, Exception {
        if (match("!") || match("-")) {
            unary();
        } else {
            call();
        }
    }

    private void call() throws ParseException, Exception {
        primary();
        call2();
    }

    private void call2() throws ParseException, Exception {
        if (match("(")) {
            argumentsOpc();
            match(")");
            call2();
        } else if (match(".") && match("id")) {
            call2();
        }
    }

    private void argumentsOpc() throws ParseException, Exception {
        if (!match(")")) {
            arguments();
        }
    }

    private void arguments() throws ParseException, Exception {
        expression();
        arguments2();
    }

    private void arguments2() throws ParseException, Exception {
        if (match(",")) {
            expression();
            arguments2();
        }
    }

    private void primary() throws ParseException, Exception {
        if (match("true") || match("false") || match("null") || match("this") || match("number") ||
                match("string") || match("id")) {
            // Terminal symbol
        } else if (match("(")) {
            expression();
            match(")");
        } else if (match("super") && match(".") && match("id")) {
            // Terminal symbol
        } else {
            throw new ParseException("Invalid syntax. Expected primary expression.", currentTokenIndex);
        }
    }

    private boolean match(String token) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(token)) {
            currentTokenIndex++;
            return true;
        }
        return false;
    }
}