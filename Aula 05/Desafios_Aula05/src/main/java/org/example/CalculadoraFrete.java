package org.example;

public class CalculadoraFrete {

    public static double calcular(double pesoKg, boolean entregaExpressa) {

        if (pesoKg <= 0) {
            throw new IllegalArgumentException("O peso deve ser maior que zero.");
        }

        double frete = 8 + (pesoKg * 2);

        if (entregaExpressa) {
            frete = frete * 1.5;
        }

        return frete;
    }
}