package co.edu.puj.rpg.modelo;

public abstract class Personaje implements ICombatiente {

    protected String nombre;
    protected int vida;
    protected int vidaMaxima;
    protected int ataqueBase;
    protected int posicionX;
    protected int posicionY;

    public Personaje(String nombre, int vida, int ataqueBase, int posicionX, int posicionY) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataqueBase = ataqueBase;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    public abstract char getSimbolo();

    @Override
    public void defender(int danoRecibido) {
        recibirDano(danoRecibido);
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public void recibirDano(int cantidad) {
        int danoReal = Math.max(0, cantidad);
        vida = Math.max(0, vida - danoReal);
        System.out.println("  " + nombre + " recibe " + danoReal + " de dano. Vida: " + vida + "/" + vidaMaxima);
        if (!estaVivo()) {
            System.out.println("  " + nombre + " ha caido en combate.");
        }
    }

    public void curar(int cantidad) {
        int antes = vida;
        vida = Math.min(vidaMaxima, vida + Math.max(0, cantidad));
        System.out.println("  " + nombre + " recupera " + (vida - antes) + " de vida. Vida: " + vida + "/" + vidaMaxima);
    }

    public void moverA(int nuevaX, int nuevaY) {
        this.posicionX = nuevaX;
        this.posicionY = nuevaY;
    }

    public String describirEstado() {
        return nombre + " | Vida: " + vida + "/" + vidaMaxima + " | Pos: (" + posicionX + "," + posicionY + ")";
    }

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
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
}
