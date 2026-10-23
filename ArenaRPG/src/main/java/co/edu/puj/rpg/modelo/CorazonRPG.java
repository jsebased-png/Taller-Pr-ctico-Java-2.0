package co.edu.puj.rpg.modelo;

/**
 * Composicion: el CorazonRPG pertenece de forma exclusiva a un Personaje.
 * Solo el Personaje lo instancia (en su constructor) y no existe ningun setter
 * para reemplazarlo.
 */
public class CorazonRPG {

    private final int saludBase;
    private final int manaBase;
    private int saludActual;
    private int manaActual;

    public CorazonRPG(int saludBase, int manaBase) {
        this.saludBase = saludBase;
        this.manaBase = manaBase;
        this.saludActual = saludBase;
        this.manaActual = manaBase;
    }

    public int getSaludBase() {
        return saludBase;
    }

    public int getManaBase() {
        return manaBase;
    }

    public int getSaludActual() {
        return saludActual;
    }

    public int getManaActual() {
        return manaActual;
    }

    public void restarSalud(int cantidad) {
        saludActual = Math.max(0, saludActual - Math.max(0, cantidad));
    }

    /** Devuelve la salud realmente recuperada. */
    public int sumarSalud(int cantidad) {
        int antes = saludActual;
        saludActual = Math.min(saludBase, saludActual + Math.max(0, cantidad));
        return saludActual - antes;
    }

    public boolean gastarMana(int cantidad) {
        if (manaActual < cantidad) {
            return false;
        }
        manaActual -= cantidad;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Salud ").append(saludActual).append('/').append(saludBase)
                .append(" | Mana ").append(manaActual).append('/').append(manaBase)
                .toString();
    }
}
