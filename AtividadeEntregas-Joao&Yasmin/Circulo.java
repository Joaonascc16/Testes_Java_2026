package org.example;

public class Circulo {

    private double raio;

    public Circulo (double raio) {
        if(raio <= 0) {
            throw new IllegalArgumentException("O raio do circulo deve ser maior que zero!");
        }
        this.raio = raio;
    }
    public double calcularArea() {
        return Math.PI * raio * raio;
    }
    public boolean circuloGrande() {
        return calcularArea() > 100;
    }

    public double getRaio() {
        return raio;
    }
}
