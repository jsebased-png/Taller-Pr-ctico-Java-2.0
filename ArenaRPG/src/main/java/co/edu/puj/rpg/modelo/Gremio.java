package co.edu.puj.rpg.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Asociacion bidireccional: Gremio (1) <-> Personaje (0..*).
 * Ambos extremos se sincronizan sin recursion infinita.
 */
public class Gremio {

    private final String nombre;
    private final List<Personaje> miembros = new ArrayList<>();

    public Gremio(String nombre) {
        this.nombre = nombre;
    }

    public void agregarMiembro(Personaje p) {
        if (p == null || miembros.contains(p)) {
            return;
        }
        miembros.add(p);
        if (p.getGremio() != this) {
            p.unirseAGremio(this);
        }
    }

    public void removerMiembro(Personaje p) {
        if (p != null && miembros.remove(p) && p.getGremio() == this) {
            p.salirDelGremio();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    /** Solo lista nombres de los miembros: evita StackOverflowError con Personaje.toString(). */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gremio ").append(nombre).append(" [").append(miembros.size()).append(" miembros]: ");
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
