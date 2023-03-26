package mx.ipn.escom.compiladores;

public enum TipoToken {
    // Palabras clave:
    Y, CLASE, ADEMAS, FALSO, PARA, FUN, SI, NULO, O, IMPRIMIR, RETORNAR, SUPER, ESTE, VERDADERO, VAR, MIENTRAS, NUMERO,
    // Crear un tipoToken: identificador, una cadena y numero
    MENOS, MAS, ASTERISCO, EXCLAMACION, IDENTIFICADOR,
    //Operadores
    IGUAL, IGUAL_QUE, MENOR_IGUAL, MENOR, MAYOR_IGUAL, MAYOR, DIFERENTE_QUE,
    // Signos de puntuación
    PARENTESIS_IZQ, PARENTESIS_DER, LLAVE_IZQ, LLAVE_DER, COMA, PUNTO, PUNTO_COMA, SLASH, 
    // Final de cadena
    EOF
}
