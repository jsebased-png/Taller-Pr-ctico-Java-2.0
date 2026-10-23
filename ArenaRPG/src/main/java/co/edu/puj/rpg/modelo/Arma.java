package co.edu.puj.rpg.modelo;

/**
 * Asociacion unidireccional: Personaje -> Arma (0..1).
 * El Arma no conoce a su dueno.
 */
public class Arma {

    private final String nombre;
    private final int danioBase;

    public Arma(String nombre, int danioBase) {
        this.nombre = nombre;
        this.danioBase = danioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanioBase() {
        return danioBase;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(nombre).append(" (+").append(danioBase).append(" dano)")
                .toString();
    }
}
