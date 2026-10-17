package co.edu.puj.rpg.modelo.enemigo;
public class Enemigo {

    private static final int EXPERIENCIA_POR_NIVEL = 75;

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque, int nivel, double oro) {
        this.nombre = nombre == null || nombre.isBlank() ? "Enemigo desconocido" : nombre.trim();
        this.vidaMaxima = Math.max(vidaMaxima, 1);
        this.puntosAtaque = Math.max(puntosAtaque, 1);
        this.nivel = Math.max(nivel, 1);
        this.oro = Double.isNaN(oro) || Double.isInfinite(oro) || oro < 0.0 ? 0.0 : oro;
        this.estaVivo = true;
        this.setPuntosVida(this.vidaMaxima);
    }

    public void recibirDano(int cantidadDano) {
        if (!this.estaVivo || cantidadDano <= 0) {
            return;
        }
        this.setPuntosVida(this.puntosVida - cantidadDano);
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public int getExperienciaRecompensa() {
        return EXPERIENCIA_POR_NIVEL * this.nivel;
    }

    public final void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > this.vidaMaxima) {
            this.puntosVida = this.vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }
}
