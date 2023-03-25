package mx.ipn.escom.compiladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;

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

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        //Aquí va el corazón del scanner.

        /*
        Analizar el texto de entrada para extraer todos los tokens
        y al final agregar el token de fin de archivo
         */
        tokens.add(new Token(TipoToken.EOF, "", null, linea));

        return tokens;
    }
}

private void scanToken() {
    char c = advance();
    switch (c) {
        case '(': agregarToken(TipoToken.PARENTESIS_IZQ); break;
        case ')': agregarToken(TipoToken.PARENTESIS_DER); break;
        case '{': agregarToken(TipoToken.LLAVE_IZQ); break;
        case '}': agregarToken(TipoToken.LLAVE_DER); break;
        case ',': agregarToken(TipoToken.COMA); break;
        case '.': agregarToken(TipoToken.PUNTO); break;
        case '-': agregarToken(TipoToken.MENOS); break;
        case '+': agregarToken(TipoToken.MAS); break;
        case ';': agregarToken(TipoToken.PUNTO_COMA); break;
        case '*': agregarToken(TipoToken.ASTERISCO); break;
        case '!': agregarToken(igual('=') ? TipoToken.DIFERENTE_QUE : TipoToken.EXCLAMACION); break;
        case '=': agregarToken(igual('=') ? TipoToken.IGUAL_QUE : TipoToken.IGUAL); break;
        case '<': agregarToken(igual('=') ? TipoToken.MENOR_IGUAL : TipoToken.MENOR); break;
        case '>': agregarToken(igual('=') ? TipoToken.MAYOR_IGUAL : TipoToken.MAYOR); break;
        case '/':
            if (igual('/')) {
                // Comentario de una sola línea, avanzamos hasta el final de la línea
                while (peek() != '\n' && !EOF()) advance();
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
            break;
        case ' ':
        case '\r':
        case '\t':
            // Ignoramos los espacios en blanco y los caracteres de control
            break;

        case '\n':
            // Incrementamos el número de línea
            linea++;
            break;

        default:
            if (isDigit(c)) {
                // Analizamos un número
                scanNumero();
            } else if (isAlpha(c)) {
                // Analizamos un identificador o palabra reservada
                scanIdentificador();
            } else {
                error(linea, "Caracter inesperado: " + c);
            }
            break;
    }
}

private void agregarToken(TipoToken tipo) {
    agregarToken(tipo, null);
}
