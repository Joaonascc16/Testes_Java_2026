package org.example;

public class CalculadoraFrete {

    public static double calcular(double pesoKg, boolean entregaExpressa) {
        if (pesoKg <= 0) {
            throw new IllegalArgumentException("O peso deve ser maior que zero.");
        }
        double valor = 8.0 + (2.0 * pesoKg);

        if (entregaExpressa){
            valor *= 1.5;
        }
        return valor;
    }
}
