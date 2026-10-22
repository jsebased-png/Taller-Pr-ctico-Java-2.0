package co.edu.puj.rpg.modelo;

import java.util.List;

public class Tablero {

    public static final char VACIA = '.';
    public static final char COFRE = 'C';

    private final char[][] matriz;
    private final int filas;
    private final int columnas;
    private final int cofreX;
    private final int cofreY;

    public Tablero(int filas, int columnas, int cofreX, int cofreY) {
        this.filas = filas;
        this.columnas = columnas;
        this.cofreX = cofreX;
        this.cofreY = cofreY;
        this.matriz = new char[filas][columnas];
    }

    public boolean dentroDeLimites(int x, int y) {
        return x >= 0 && x < filas && y >= 0 && y < columnas;
    }

    public char getCelda(int x, int y) {
        return matriz[x][y];
    }

    public int getCofreX() {
        return cofreX;
    }

    public int getCofreY() {
        return cofreY;
    }

    public void actualizar(List<? extends Personaje> heroes, List<? extends Personaje> enemigos) {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = VACIA;
            }
        }
        matriz[cofreX][cofreY] = COFRE;
        for (Personaje e : enemigos) {
            if (e.estaVivo()) {
                matriz[e.getPosicionX()][e.getPosicionY()] = e.getSimbolo();
            }
        }
        for (Personaje h : heroes) {
            if (h.estaVivo()) {
                matriz[h.getPosicionX()][h.getPosicionY()] = h.getSimbolo();
            }
        }
    }

    public void mostrar() {
        System.out.print("     ");
        for (int j = 0; j < columnas; j++) {
            System.out.print(" " + j + "  ");
        }
        System.out.println();
        for (int i = 0; i < filas; i++) {
            System.out.print("  " + i + "  ");
            for (int j = 0; j < columnas; j++) {
                System.out.print("[" + matriz[i][j] + "] ");
            }
            System.out.println();
        }
        System.out.println("  G Guerrero | M Mago | E Enemigo | C Cofre | . Vacia");
    }
}
