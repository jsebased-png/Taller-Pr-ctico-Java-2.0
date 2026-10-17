package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {

    private static final double PROBABILIDAD_CRITICO = 0.15;
    private static final int MULTIPLICADOR_CRITICO = 2;
    private static final int POCIONES_INICIALES = 2;
    private static final int UMBRAL_EXPERIENCIA_BASE = 100;
    private static final int BONO_VIDA_POR_NIVEL = 20;
    private static final int BONO_ATAQUE_POR_NIVEL = 3;

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;
    private int experiencia;
    private int pocionesGrandes;
    private int ultimoDano;
    private boolean ultimoGolpeCritico;

    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = normalizarNombre(nombre);
        this.vidaMaxima = Math.max(vidaMaxima, 1);
        this.puntosAtaque = Math.max(puntosAtaque, 1);
        this.nivel = 1;
        this.experiencia = 0;
        this.oro = 0.0;
        this.estaVivo = true;
        this.pocionesGrandes = POCIONES_INICIALES;
        this.ultimoDano = 0;
        this.ultimoGolpeCritico = false;
        this.setPuntosVida(this.vidaMaxima);
        this.setOro(oro);
    }

    public Personaje(String nombre) {
        this(nombre, 100, 10, 0.0);
    }

    public void atacar(Enemigo enemigo) {
        this.ultimoDano = 0;
        this.ultimoGolpeCritico = false;
        if (enemigo == null || !this.estaVivo || !enemigo.isEstaVivo()) {
            return;
        }
        boolean critico = Math.random() < PROBABILIDAD_CRITICO;
        int dano = critico ? this.puntosAtaque * MULTIPLICADOR_CRITICO : this.puntosAtaque;
        enemigo.recibirDano(dano);
        this.ultimoDano = dano;
        this.ultimoGolpeCritico = critico;
    }

    public void usarPocion(int cantidadRestaurada) {
        if (!this.estaVivo || this.pocionesGrandes <= 0 || cantidadRestaurada <= 0) {
            return;
        }
        this.pocionesGrandes--;
        this.setPuntosVida(this.puntosVida + cantidadRestaurada);
    }

    public void recibirDano(int cantidadDano) {
        if (!this.estaVivo || cantidadDano <= 0) {
            return;
        }
        this.setPuntosVida(this.puntosVida - cantidadDano);
    }

    public boolean ganarExperiencia(int puntos) {
        if (!this.estaVivo || puntos <= 0) {
            return false;
        }
        this.experiencia += puntos;
        boolean subioNivel = false;
        while (this.experiencia >= this.getExperienciaParaSiguienteNivel()) {
            this.experiencia -= this.getExperienciaParaSiguienteNivel();
            this.subirNivel();
            subioNivel = true;
        }
        return subioNivel;
    }

    public void agregarPocionGrande() {
        this.pocionesGrandes++;
    }

    private void subirNivel() {
        this.setNivel(this.nivel + 1);
        this.setVidaMaxima(this.vidaMaxima + BONO_VIDA_POR_NIVEL);
        this.setPuntosAtaque(this.puntosAtaque + BONO_ATAQUE_POR_NIVEL);
        this.setPuntosVida(this.vidaMaxima);
    }

    private static String normalizarNombre(String nombre) {
        return nombre == null || nombre.isBlank() ? "Sin nombre" : nombre.trim();
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

    public int getExperiencia() {
        return experiencia;
    }

    public int getExperienciaParaSiguienteNivel() {
        return UMBRAL_EXPERIENCIA_BASE * this.nivel;
    }

    public int getPocionesGrandes() {
        return pocionesGrandes;
    }

    public int getUltimoDano() {
        return ultimoDano;
    }

    public boolean isUltimoGolpeCritico() {
        return ultimoGolpeCritico;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            this.nombre = nombre.trim();
        }
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

    public void setVidaMaxima(int vidaMaxima) {
        if (vidaMaxima >= 1) {
            this.vidaMaxima = vidaMaxima;
            this.setPuntosVida(this.puntosVida);
        }
    }

    public void setPuntosAtaque(int puntosAtaque) {
        if (puntosAtaque >= 1) {
            this.puntosAtaque = puntosAtaque;
        }
    }

    protected void setNivel(int nivel) {
        if (nivel >= 1) {
            this.nivel = nivel;
        }
    }

    public final boolean setOro(double oro) {
        if (Double.isNaN(oro) || Double.isInfinite(oro) || oro < 0.0) {
            return false;
        }
        this.oro = oro;
        return true;
    }
}
