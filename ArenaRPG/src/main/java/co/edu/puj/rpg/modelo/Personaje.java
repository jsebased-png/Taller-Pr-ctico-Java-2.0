package co.edu.puj.rpg.modelo;

public abstract class Personaje implements ICombatiente {

    protected String nombre;
    protected int nivel;
    protected int ataqueBase;
    protected int posicionX;
    protected int posicionY;

    // Composicion 1..1: se crea en el constructor y no tiene setter.
    protected final CorazonRPG corazon;
    // Asociacion unidireccional 0..1.
    private Arma armaEquipada;
    // Asociacion bidireccional con Gremio (0..1 desde el Personaje).
    private Gremio gremio;

    public Personaje(String nombre, int vida, int ataqueBase, int posicionX, int posicionY) {
        this(nombre, vida, 0, ataqueBase, posicionX, posicionY);
    }

    public Personaje(String nombre, int vida, int mana, int ataqueBase, int posicionX, int posicionY) {
        this.nombre = nombre;
        this.nivel = 1;
        this.corazon = new CorazonRPG(vida, mana);
        this.ataqueBase = ataqueBase;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.armaEquipada = null;
        this.gremio = null;
    }

    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    public abstract char getSimbolo();

    @Override
    public void defender(int danoRecibido) {
        recibirDano(danoRecibido);
    }

    public boolean estaVivo() {
        return corazon.getSaludActual() > 0;
    }

    public void recibirDano(int cantidad) {
        int danoReal = Math.max(0, cantidad);
        corazon.restarSalud(danoReal);
        System.out.println("  " + nombre + " recibe " + danoReal + " de dano. Vida: " + getVida() + "/" + corazon.getSaludBase());
        if (!estaVivo()) {
            System.out.println("  " + nombre + " ha caido en combate.");
        }
    }

    public void curar(int cantidad) {
        int recuperada = corazon.sumarSalud(cantidad);
        System.out.println("  " + nombre + " recupera " + recuperada + " de vida. Vida: " + getVida() + "/" + corazon.getSaludBase());
    }

    public void moverA(int nuevaX, int nuevaY) {
        this.posicionX = nuevaX;
        this.posicionY = nuevaY;
    }

    public String describirEstado() {
        return nombre + " | Vida: " + getVida() + "/" + corazon.getSaludBase() + " | Pos: (" + posicionX + "," + posicionY + ")";
    }

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return corazon.getSaludActual();
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    // ---------- Asociacion unidireccional: Personaje -> Arma ----------

    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) {
            System.out.println(nombre + ": no se puede equipar un arma nula.");
            return;
        }
        this.armaEquipada = nuevaArma;
        System.out.println(nombre + " equipa " + nuevaArma + ".");
    }

    public void desequiparArma() {
        if (armaEquipada != null) {
            System.out.println(nombre + " guarda " + armaEquipada.getNombre() + " y queda desarmado.");
            armaEquipada = null;
        } else {
            System.out.println(nombre + " no tiene arma para retirar.");
        }
    }

    public int calcularAtaqueTotal() {
        int total = ataqueBase;
        if (armaEquipada != null) {
            total += armaEquipada.getDanioBase();
        }
        return total;
    }

    // ---------- Asociacion bidireccional: Personaje <-> Gremio ----------

    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null || nuevoGremio == this.gremio) {
            return;
        }
        if (this.gremio != null) {
            salirDelGremio();
        }
        this.gremio = nuevoGremio;
        nuevoGremio.agregarMiembro(this);
        System.out.println(nombre + " se une al gremio " + nuevoGremio.getNombre() + ".");
    }

    public void salirDelGremio() {
        if (gremio == null) {
            System.out.println(nombre + " no pertenece a ningun gremio.");
            return;
        }
        Gremio anterior = this.gremio;
        this.gremio = null;
        anterior.removerMiembro(this);
        System.out.println(nombre + " abandona el gremio " + anterior.getNombre() + ".");
    }

    public String getTipo() {
        return getClass().getSimpleName();
    }

    public int getNivel() {
        return nivel;
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public Gremio getGremio() {
        return gremio;
    }

    public CorazonRPG getCorazon() {
        return corazon;
    }

    /** Solo muestra el nombre del gremio (no su toString) para no provocar StackOverflowError. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTipo()).append(' ').append(nombre).append(" (nivel ").append(nivel).append(")\n");
        sb.append("  Corazon: ").append(corazon).append('\n');
        sb.append("  Arma:    ").append(armaEquipada != null ? armaEquipada.toString() : "Desarmado").append('\n');
        sb.append("  Gremio:  ").append(gremio != null ? gremio.getNombre() : "Sin gremio").append('\n');
        sb.append("  Ataque total: ").append(calcularAtaqueTotal());
        return sb.toString();
    }
}
