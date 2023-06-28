package mx.ipn.escom.compiladores;

public class Token {
    final TipoToken tipo;
    final String lexema;
    final Object literal;

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
    }

    @Override
    public String toString(){
        return tipo + " " + lexema + " " + literal;
    }

    boolean palabrasReservadas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    // Métodos auxiliares
    public boolean esOperando(){
        switch (this.tipo){
            case IDENTIFICADOR:
            case NUMERO:
            case CADENA:
            case VERDADERO:
            case FALSO:
                return true;
            default:
                return false;
        }
    }

    public boolean esOperador(){
        switch (this.tipo){
            case SUMA:
            case RESTA:
            case MULTIPLICACION:
            case DIVISION:
            case IGUAL:
            case IGUAL_QUE:
            case MAYOR:
            case MAYOR_IGUAL:
            case MENOR:
            case MENOR_IGUAL:
            case DIFERENTE_QUE:
                return true;
            default:
                return false;
        }
    }

    public boolean esPalabraReservada(){
        switch (this.tipo){
            case VAR:
            case IMPRIMIR:
            case Y:
            case CLASE:
            case FUN:
            case NULO:
            case O:
            case RETORNAR:
            case SUPER:
            case ESTE:
                return true;
            default:
                return false;
        }
    }

    public boolean esEstructuraDeControl(){
        switch (this.tipo){
            case SI:
            case ADEMAS:
            case MIENTRAS:
            case PARA:
                return true;
            default:
                return false;
        }
    }

    public boolean precedenciaMayorIgual(Token t){
        return this.obtenerPrecedencia() >= t.obtenerPrecedencia();
    }

    private int obtenerPrecedencia(){
        switch (this.tipo){
            case MULTIPLICACION:
            case DIVISION:
                return 7;
            case SUMA:
            case RESTA:
                return 6;
            case MENOR:
            case MENOR_IGUAL:
            case MAYOR:
            case MAYOR_IGUAL:
                return 5;
            case IGUAL_QUE:
            case DIFERENTE_QUE:
                return 4;
            case Y:
                return 3;
            case O:
                return 2;
            case IGUAL:
                return 1;
        }
        return 0;
    }

    public int aridad(){
        switch (this.tipo){
            case MULTIPLICACION:
            case DIVISION:
                return 2;
            case SUMA:
            case RESTA:
                return 2;
            case MENOR:
            case MENOR_IGUAL:
            case MAYOR:
            case MAYOR_IGUAL:
                return 2;
            case IGUAL_QUE:
            case DIFERENTE_QUE:
                return 2;
            case Y:
                return 2;
            case O:
                return 2;
            case IGUAL:
                return 2;
        }
        return 0;
    }
}
