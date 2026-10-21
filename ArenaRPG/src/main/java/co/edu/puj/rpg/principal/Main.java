package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Enemigo;
import co.edu.puj.rpg.modelo.Guerrero;
import co.edu.puj.rpg.modelo.Inventario;
import co.edu.puj.rpg.modelo.Mago;
import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.Pocion;
import co.edu.puj.rpg.modelo.Tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static boolean cofreAlcanzado = false;

    public static void main(String[] args) {
        Tablero tablero = new Tablero(5, 5, 2, 2);

        List<Personaje> heroes = new ArrayList<>();
        heroes.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        heroes.add(new Mago("Gandalf", 80, 25, 1, 1, 50));
        Personaje heroePrincipal = heroes.get(0);

        List<Personaje> enemigos = new ArrayList<>();
        enemigos.add(new Enemigo("Orco", 60, 12, 0, 4));
        enemigos.add(new Enemigo("Trasgo", 45, 10, 4, 0));
        enemigos.add(new Enemigo("Troll", 90, 14, 4, 4));

        Inventario<Pocion> inventario = new Inventario<>();
        inventario.agregar(new Pocion("Pocion menor", 30));
        inventario.agregar(new Pocion("Pocion menor", 30));
        inventario.agregar(new Pocion("Pocion mayor", 60));

        int turno = 0;

        try {
            do {
                turno++;
                System.out.println();
                System.out.println("==================== TURNO " + turno + " ====================");
                tablero.actualizar(heroes, enemigos);
                mostrarEstado(tablero, heroes, enemigos);

                for (Personaje heroe : heroes) {
                    if (!heroe.estaVivo() || enemigos.isEmpty() || cofreAlcanzado) {
                        continue;
                    }
                    turnoDelHeroe(heroe, heroePrincipal, heroes, enemigos, inventario, tablero);
                    enemigos.removeIf(e -> !e.estaVivo());
                    tablero.actualizar(heroes, enemigos);
                }

                if (!enemigos.isEmpty() && !cofreAlcanzado) {
                    System.out.println();
                    System.out.println("--- Turno enemigo ---");
                    for (Personaje enemigo : enemigos) {
                        if (enemigo.estaVivo() && !heroes.isEmpty()) {
                            turnoDelEnemigo(enemigo, heroes, enemigos, tablero);
                            heroes.removeIf(h -> !h.estaVivo());
                            tablero.actualizar(heroes, enemigos);
                        }
                    }
                }

                heroes.removeIf(h -> !h.estaVivo());
                enemigos.removeIf(e -> !e.estaVivo());
            } while (!heroes.isEmpty() && !enemigos.isEmpty() && !cofreAlcanzado);
        } catch (NoSuchElementException ex) {
            System.out.println();
            System.out.println("Entrada finalizada. Se cierra la partida.");
            return;
        }

        tablero.actualizar(heroes, enemigos);
        System.out.println();
        tablero.mostrar();
        System.out.println();
        if (heroes.isEmpty()) {
            System.out.println("DERROTA: todos los heroes han caido.");
        } else if (cofreAlcanzado) {
            System.out.println("VICTORIA: " + heroePrincipal.getNombre() + " alcanzo el cofre del tesoro.");
        } else {
            System.out.println("VICTORIA: todos los enemigos han sido eliminados.");
        }
    }

    private static void mostrarEstado(Tablero tablero, List<Personaje> heroes, List<Personaje> enemigos) {
        tablero.mostrar();
        System.out.println();
        System.out.println("Heroes:");
        for (Personaje h : heroes) {
            System.out.println("  " + h.describirEstado());
        }
        System.out.println("Enemigos:");
        for (Personaje e : enemigos) {
            System.out.println("  " + e.describirEstado());
        }
    }

    private static void turnoDelHeroe(Personaje heroe, Personaje heroePrincipal, List<Personaje> heroes,
                                      List<Personaje> enemigos, Inventario<Pocion> inventario, Tablero tablero) {
        boolean turnoUsado = false;
        do {
            System.out.println();
            System.out.println("Turno de " + heroe.getNombre());
            System.out.println("  1. Atacar");
            System.out.println("  2. Usar inventario");
            System.out.println("  3. Mover en tablero");
            int opcion = leerEntero("Elige una opcion: ", 1, 3);

            switch (opcion) {
                case 1:
                    Personaje objetivo = elegirEnemigo(enemigos);
                    heroe.realizarAtaqueEspecial(objetivo);
                    turnoUsado = true;
                    break;
                case 2:
                    turnoUsado = usarInventario(heroe, inventario);
                    break;
                case 3:
                    turnoUsado = moverHeroe(heroe, heroePrincipal, tablero, heroes, enemigos);
                    break;
                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        } while (!turnoUsado);
    }

    private static Personaje elegirEnemigo(List<Personaje> enemigos) {
        System.out.println("Enemigos disponibles:");
        for (int i = 0; i < enemigos.size(); i++) {
            Personaje e = enemigos.get(i);
            System.out.println("  " + (i + 1) + ". " + e.describirEstado());
        }
        int eleccion = leerEntero("Elige el objetivo: ", 1, enemigos.size());
        return enemigos.get(eleccion - 1);
    }

    private static boolean usarInventario(Personaje heroe, Inventario<Pocion> inventario) {
        if (inventario.estaVacio()) {
            System.out.println("El inventario esta vacio.");
            return false;
        }
        System.out.println("Inventario:");
        for (int i = 0; i < inventario.tamano(); i++) {
            System.out.println("  " + (i + 1) + ". " + inventario.obtener(i));
        }
        System.out.println("  0. Cancelar");
        int eleccion = leerEntero("Elige una pocion: ", 0, inventario.tamano());
        if (eleccion == 0) {
            return false;
        }
        Pocion pocion = inventario.quitar(eleccion - 1);
        System.out.println(heroe.getNombre() + " usa " + pocion.getNombre() + ".");
        heroe.curar(pocion.getCuracion());
        return true;
    }

    private static boolean moverHeroe(Personaje heroe, Personaje heroePrincipal, Tablero tablero,
                                      List<Personaje> heroes, List<Personaje> enemigos) {
        System.out.println("Mover: W arriba, S abajo, A izquierda, D derecha, X cancelar");
        String entrada = leerLinea("Direccion: ").trim().toUpperCase();
        if (entrada.isEmpty()) {
            System.out.println("Direccion no valida.");
            return false;
        }

        int nuevaX = heroe.getPosicionX();
        int nuevaY = heroe.getPosicionY();

        switch (entrada.charAt(0)) {
            case 'W':
                nuevaX--;
                break;
            case 'S':
                nuevaX++;
                break;
            case 'A':
                nuevaY--;
                break;
            case 'D':
                nuevaY++;
                break;
            case 'X':
                return false;
            default:
                System.out.println("Direccion no valida.");
                return false;
        }

        if (!tablero.dentroDeLimites(nuevaX, nuevaY)) {
            System.out.println("Movimiento fuera del tablero.");
            return false;
        }

        char destino = tablero.getCelda(nuevaX, nuevaY);
        if (destino == Tablero.COFRE && heroe != heroePrincipal) {
            System.out.println("Solo el heroe principal puede llegar al cofre.");
            return false;
        }
        if (destino != Tablero.VACIA && destino != Tablero.COFRE) {
            System.out.println("La celda esta ocupada.");
            return false;
        }

        heroe.moverA(nuevaX, nuevaY);
        System.out.println(heroe.getNombre() + " se mueve a (" + nuevaX + "," + nuevaY + ").");
        if (heroe == heroePrincipal && nuevaX == tablero.getCofreX() && nuevaY == tablero.getCofreY()) {
            cofreAlcanzado = true;
        }
        tablero.actualizar(heroes, enemigos);
        return true;
    }

    private static void turnoDelEnemigo(Personaje enemigo, List<Personaje> heroes, List<Personaje> enemigos,
                                        Tablero tablero) {
        Personaje objetivo = heroeMasCercano(enemigo, heroes);
        int distancia = distancia(enemigo, objetivo);

        if (distancia <= 2) {
            enemigo.realizarAtaqueEspecial(objetivo);
            return;
        }

        int difX = Integer.compare(objetivo.getPosicionX(), enemigo.getPosicionX());
        int difY = Integer.compare(objetivo.getPosicionY(), enemigo.getPosicionY());
        boolean prioridadX = Math.abs(objetivo.getPosicionX() - enemigo.getPosicionX())
                >= Math.abs(objetivo.getPosicionY() - enemigo.getPosicionY());

        int[][] intentos = prioridadX
                ? new int[][]{{difX, 0}, {0, difY}}
                : new int[][]{{0, difY}, {difX, 0}};

        for (int[] paso : intentos) {
            if (paso[0] == 0 && paso[1] == 0) {
                continue;
            }
            int nuevaX = enemigo.getPosicionX() + paso[0];
            int nuevaY = enemigo.getPosicionY() + paso[1];
            if (tablero.dentroDeLimites(nuevaX, nuevaY) && tablero.getCelda(nuevaX, nuevaY) == Tablero.VACIA) {
                enemigo.moverA(nuevaX, nuevaY);
                System.out.println(enemigo.getNombre() + " avanza a (" + nuevaX + "," + nuevaY + ").");
                return;
            }
        }
        System.out.println(enemigo.getNombre() + " no encuentra camino y espera.");
    }

    private static Personaje heroeMasCercano(Personaje enemigo, List<Personaje> heroes) {
        Personaje masCercano = heroes.get(0);
        for (Personaje h : heroes) {
            if (distancia(enemigo, h) < distancia(enemigo, masCercano)) {
                masCercano = h;
            }
        }
        return masCercano;
    }

    private static int distancia(Personaje a, Personaje b) {
        return Math.abs(a.getPosicionX() - b.getPosicionX()) + Math.abs(a.getPosicionY() - b.getPosicionY());
    }

    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            String linea = leerLinea(mensaje).trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException ex) {
            }
            System.out.println("Ingresa un numero entre " + min + " y " + max + ".");
        }
    }
}
