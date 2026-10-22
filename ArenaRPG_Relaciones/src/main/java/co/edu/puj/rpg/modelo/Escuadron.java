package co.edu.puj.rpg.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregacion: el Escuadron agrupa Personajes creados externamente.
 * Si el escuadron desaparece, los personajes siguen existiendo.
 */
public class Escuadron {

    private final String nombre;
    private final List<Personaje> miembros = new ArrayList<>();

    public Escuadron(String nombre) {
        this.nombre = nombre;
    }

    public boolean reclutarPersonaje(Personaje p) {
        if (p == null || miembros.contains(p)) {
            return false;
        }
        miembros.add(p);
        return true;
    }

    public boolean expulsarPersonaje(Personaje p) {
        return miembros.remove(p);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Escuadron ").append(nombre).append(" [").append(miembros.size()).append(" miembros]: ");
        if (miembros.isEmpty()) {
            sb.append("(vacio)");
        }
        for (int i = 0; i < miembros.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(miembros.get(i).getNombre());
        }
        return sb.toString();
    }
}
