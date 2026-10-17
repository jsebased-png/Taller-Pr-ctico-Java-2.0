package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Scanner;

public class Main {

    private static final double PRECIO_POCION_GRANDE = 30.0;
    private static final int CURACION_POCION_GRANDE = 50;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder historial = new StringBuilder();

        System.out.printf("=== ARENA DE HÉROES ===%n");
        System.out.printf("Nombre del héroe: ");
        String nombreIngresado = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        String nombreHeroe = nombreIngresado.isEmpty() ? "Aragorn" : nombreIngresado;

        Personaje heroe = new Personaje(nombreHeroe, 100, 15, 50.0);
        Enemigo enemigo = new Enemigo("Orco Guerrero", 80, 12, 2, 40.0);

        historial.append(String.format("%s (nivel %d) se enfrenta a %s (nivel %d).%n",
                heroe.getNombre(), heroe.getNivel(), enemigo.getNombre(), enemigo.getNivel()));

        boolean huyo = false;

        do {
            mostrarMenu(heroe, enemigo);
            if (!scanner.hasNextLine()) {
                huyo = true;
                continue;
            }

            String entrada = scanner.nextLine().trim();
            int opcion = entrada.matches("\\d{1,2}") ? Integer.parseInt(entrada) : -1;
            boolean turnoConsumido = false;

            switch (opcion) {
                case 1 -> {
                    ejecutarAtaque(heroe, enemigo, historial);
                    turnoConsumido = true;
                }
                case 2 -> turnoConsumido = ejecutarPocion(heroe, historial);
                case 3 -> mostrarEstado(heroe, enemigo);
                case 4 -> {
                    huyo = true;
                    historial.append(String.format("%s decide huir del combate.%n", heroe.getNombre()));
                }
                case 5 -> abrirTienda(scanner, heroe, historial);
                default -> {
                    if (opcion == -1) {
                        System.out.printf("Entrada no válida: ingrese un número del 1 al 5.%n");
                    } else {
                        System.out.printf("La opción %d no existe. Elija un número del 1 al 5.%n", opcion);
                    }
                }
            }

            if (turnoConsumido) {
                if (enemigo.isEstaVivo()) {
                    ejecutarTurnoEnemigo(heroe, enemigo, historial);
                }
                imprimirHistorial(historial);
            }
        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        resolverCombate(heroe, enemigo, historial);
        scanner.close();
    }

    private static void mostrarMenu(Personaje heroe, Enemigo enemigo) {
        System.out.printf("%n--- TU TURNO ---%n");
        System.out.printf("%s: %d/%d PV | %s: %d/%d PV%n",
                heroe.getNombre(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
        System.out.printf("1. Atacar%n");
        System.out.printf("2. Usar Poción%n");
        System.out.printf("3. Ver Estado%n");
        System.out.printf("4. Huir%n");
        System.out.printf("5. Tienda%n");
        System.out.printf("Seleccione una opción: ");
    }

    private static void mostrarEstado(Personaje heroe, Enemigo enemigo) {
        System.out.printf("%n=== ESTADO DEL COMBATE ===%n");
        System.out.printf("Héroe: %s | Nivel: %d | Vida: %d/%d | Ataque: %d%n",
                heroe.getNombre(), heroe.getNivel(), heroe.getPuntosVida(),
                heroe.getVidaMaxima(), heroe.getPuntosAtaque());
        System.out.printf("Oro: %.2f | Pociones grandes: %d | Experiencia: %d/%d | Vivo: %b%n",
                heroe.getOro(), heroe.getPocionesGrandes(), heroe.getExperiencia(),
                heroe.getExperienciaParaSiguienteNivel(), heroe.isEstaVivo());
        System.out.printf("Enemigo: %s | Nivel: %d | Vida: %d/%d | Ataque: %d | Vivo: %b%n",
                enemigo.getNombre(), enemigo.getNivel(), enemigo.getPuntosVida(),
                enemigo.getVidaMaxima(), enemigo.getPuntosAtaque(), enemigo.isEstaVivo());
    }

    private static void ejecutarAtaque(Personaje heroe, Enemigo enemigo, StringBuilder historial) {
        heroe.atacar(enemigo);
        if (heroe.isUltimoGolpeCritico()) {
            historial.append(String.format("¡GOLPE CRÍTICO! %s inflige %d de daño a %s.%n",
                    heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
        } else {
            historial.append(String.format("%s inflige %d de daño a %s.%n",
                    heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
        }
        if (!enemigo.isEstaVivo()) {
            historial.append(String.format("%s ha sido derrotado.%n", enemigo.getNombre()));
        }
    }

    private static boolean ejecutarPocion(Personaje heroe, StringBuilder historial) {
        if (heroe.getPocionesGrandes() <= 0) {
            System.out.printf("No tienes Pociones Grandes de Salud. Visita la tienda (opción 5).%n");
            return false;
        }
        if (heroe.getPuntosVida() == heroe.getVidaMaxima()) {
            System.out.printf("Tu salud ya está al máximo; no es necesario usar una poción.%n");
            return false;
        }
        int vidaAntes = heroe.getPuntosVida();
        heroe.usarPocion(CURACION_POCION_GRANDE);
        int recuperado = heroe.getPuntosVida() - vidaAntes;
        historial.append(String.format("%s usa una Poción Grande y recupera %d puntos de vida.%n",
                heroe.getNombre(), recuperado));
        return true;
    }

    private static void ejecutarTurnoEnemigo(Personaje heroe, Enemigo enemigo, StringBuilder historial) {
        int vidaAntes = heroe.getPuntosVida();
        heroe.recibirDano(enemigo.getPuntosAtaque());
        int danoRecibido = vidaAntes - heroe.getPuntosVida();
        historial.append(String.format("%s contraataca e inflige %d de daño a %s.%n",
                enemigo.getNombre(), danoRecibido, heroe.getNombre()));
        if (!heroe.isEstaVivo()) {
            historial.append(String.format("%s ha caído en combate.%n", heroe.getNombre()));
        }
    }

    private static void abrirTienda(Scanner scanner, Personaje heroe, StringBuilder historial) {
        boolean enTienda = true;
        while (enTienda && scanner.hasNextLine()) {
            System.out.printf("%n--- TIENDA ---%n");
            System.out.printf("Oro disponible: %.2f%n", heroe.getOro());
            System.out.printf("1. Poción Grande de Salud (+%d PV) - %.2f de oro%n",
                    CURACION_POCION_GRANDE, PRECIO_POCION_GRANDE);
            System.out.printf("2. Volver al combate%n");
            System.out.printf("Seleccione una opción: ");

            String entrada = scanner.nextLine().trim();
            if (entrada.equals("1")) {
                comprarPocionGrande(heroe, historial);
            } else if (entrada.equals("2")) {
                enTienda = false;
            } else {
                System.out.printf("Opción inválida en la tienda. Elija 1 o 2.%n");
            }
        }
    }

    private static void comprarPocionGrande(Personaje heroe, StringBuilder historial) {
        double saldoResultante = heroe.getOro() - PRECIO_POCION_GRANDE;
        boolean transaccionAprobada = heroe.setOro(saldoResultante);
        if (transaccionAprobada) {
            heroe.agregarPocionGrande();
            System.out.printf("Compra exitosa. Saldo actual: %.2f | Pociones grandes: %d%n",
                    heroe.getOro(), heroe.getPocionesGrandes());
            historial.append(String.format("%s compra una Poción Grande por %.2f de oro.%n",
                    heroe.getNombre(), PRECIO_POCION_GRANDE));
        } else {
            System.out.printf("Saldo insuficiente: la poción cuesta %.2f y tienes %.2f.%n",
                    PRECIO_POCION_GRANDE, heroe.getOro());
        }
    }

    private static void imprimirHistorial(StringBuilder historial) {
        System.out.printf("%n=== HISTORIAL DE BATALLA ===%n%s", historial);
    }

    private static void resolverCombate(Personaje heroe, Enemigo enemigo, StringBuilder historial) {
        if (!heroe.isEstaVivo()) {
            historial.append(String.format("DERROTA: %s ha sido vencido por %s.%n",
                    heroe.getNombre(), enemigo.getNombre()));
        } else if (!enemigo.isEstaVivo()) {
            double botin = enemigo.getOro();
            heroe.setOro(heroe.getOro() + botin);
            int experienciaGanada = enemigo.getExperienciaRecompensa();
            boolean subioNivel = heroe.ganarExperiencia(experienciaGanada);
            historial.append(String.format("VICTORIA: %s obtiene %.2f de oro y %d puntos de experiencia.%n",
                    heroe.getNombre(), botin, experienciaGanada));
            if (subioNivel) {
                historial.append(String.format("¡%s sube al nivel %d! Vida máxima: %d | Ataque: %d%n",
                        heroe.getNombre(), heroe.getNivel(), heroe.getVidaMaxima(), heroe.getPuntosAtaque()));
            }
        } else {
            historial.append(String.format("%s abandona la arena. El combate queda sin vencedor.%n",
                    heroe.getNombre()));
        }
        System.out.printf("%n=== HISTORIAL FINAL ===%n%s", historial);
        mostrarEstado(heroe, enemigo);
    }
}
