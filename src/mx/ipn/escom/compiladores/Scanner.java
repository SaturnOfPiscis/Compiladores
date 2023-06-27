//Elaborado por: Ramirez Contreras Angel Humberto (5CV1) y Diaz Rosales Mauricio Yael (3CV15)
package mx.ipn.escom.compiladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;
    private int inicioToken = 0;
    private int current = 0;

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("y", TipoToken.Y);
        palabrasReservadas.put("clase", TipoToken.CLASE);
        palabrasReservadas.put("ademas", TipoToken.ADEMAS);
        palabrasReservadas.put("falso", TipoToken.FALSO);
        palabrasReservadas.put("para",TipoToken.PARA );
        palabrasReservadas.put("fun", TipoToken.FUN); //definir funciones
        palabrasReservadas.put("si", TipoToken.SI);
        palabrasReservadas.put("nulo", TipoToken.NULO);
        palabrasReservadas.put("o", TipoToken.O);
        palabrasReservadas.put("imprimir", TipoToken.IMPRIMIR);
        palabrasReservadas.put("retornar", TipoToken.RETORNAR);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("este", TipoToken.ESTE);
        palabrasReservadas.put("verdadero", TipoToken.VERDADERO);
        palabrasReservadas.put("var", TipoToken.VAR); //definir variables
        palabrasReservadas.put("mientras", TipoToken.MIENTRAS);
    }

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!EOF()) {
            inicioToken = current;
            scanToken();
        }

        tokens.add(new Token(TipoToken.EOF, "", null, linea));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> agregarToken(TipoToken.PARENTESIS_IZQ);
            case ')' -> agregarToken(TipoToken.PARENTESIS_DER);
            case '{' -> agregarToken(TipoToken.LLAVE_IZQ);
            case '}' -> agregarToken(TipoToken.LLAVE_DER);
            case ',' -> agregarToken(TipoToken.COMA);
            case '.' -> agregarToken(TipoToken.PUNTO);
            case '-' -> agregarToken(TipoToken.MENOS);
            case '+' -> agregarToken(TipoToken.MAS);
            case ';' -> agregarToken(TipoToken.PUNTO_COMA);
            case '*' -> agregarToken(TipoToken.ASTERISCO);
            case '!' -> agregarToken(igual('=') ? TipoToken.DIFERENTE_QUE : TipoToken.EXCLAMACION);
            case '=' -> agregarToken(igual('=') ? TipoToken.IGUAL_QUE : TipoToken.IGUAL);
            case '<' -> agregarToken(igual('=') ? TipoToken.MENOR_IGUAL : TipoToken.MENOR);
            case '>' -> agregarToken(igual('=') ? TipoToken.MAYOR_IGUAL : TipoToken.MAYOR);
            case '/' -> {
                if (igual('/')) {
                    // Comentario de una sola línea, avanzamos hasta el final de la línea
                    while (peek() != '\n' && !EOF()) {
                        advance();
                    }
                } else if (igual('*')) {
                    // Comentario de múltiples líneas, avanzamos hasta encontrar el cierre
                    boolean commentClosed = false;
                    while (!commentClosed && !EOF()) {
                        if (igual('*') && igual('/')) {
                            commentClosed = true;
                        } else {
                            advance();
                        }
                    }
                    if (!commentClosed) {
                        error(linea, "Comentario de múltiples líneas sin cierre");
                    }
                } else {
                    agregarToken(TipoToken.SLASH);
                }
            }
            case ' ', '\r', '\t' -> {
            }
            case '\n' -> // Incrementamos el número de línea
                linea++;
            default -> {
                if (isDigit(c)) {
                    // Analizamos un número
                    scanNumero();
                } else if (isAlpha(c)) {
                    // Analizamos un identificador o palabra reservada
                    scanIdentificador();
                } else {
                    error(linea, "Caracter inesperado: " + c);
                }
            }
        }
        // Ignoramos los espacios en blanco y los caracteres de control
            }

private void agregarToken(TipoToken tipo) {
    agregarToken(tipo, null);
}

private void agregarToken(TipoToken tipo, Object valor) {
    String lexema = source.substring(inicioToken, current);
    tokens.add(new Token(tipo, lexema, valor, linea));
}

private boolean EOF() {
    return current >= source.length();
}

private char advance() {
    current++;
    return source.charAt(current - 1);
}

private boolean igual(char expected) {
    if (EOF()) {
        return false;
    }
    if (source.charAt(current) != expected) {
        return false;
    }

    current++;
    return true;
}

private char peek() {
    if (EOF()) {
        return '\0';
    }
    return source.charAt(current);
}

private char peekNext() {
    if (current + 1 >= source.length()) {
        return '\0';
    }
    return source.charAt(current + 1);
}

private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
}

private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
}

private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
}

private void scanNumero() {
    while (isDigit(peek())) {
        advance();
    }

    if (peek() == '.' && isDigit(peekNext())) {
        advance();

        while (isDigit(peek())) {
            advance();
        }
    }
    String lexema = source.substring(inicioToken, current);
    double valor = Double.parseDouble(lexema);
    agregarToken(TipoToken.NUMERO, valor);
}

private void scanIdentificador() {
    while (isAlphaNumeric(peek())) advance();

    String lexema = source.substring(inicioToken, current);
    TipoToken tipo = palabrasReservadas.getOrDefault(lexema, TipoToken.IDENTIFICADOR);
    agregarToken(tipo);
}

private void error(int linea, String mensaje) {
    System.err.println("[Error en línea " + linea + "] " + mensaje);
}
}
