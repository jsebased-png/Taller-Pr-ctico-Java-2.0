package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Arma;
import co.edu.puj.rpg.modelo.Escuadron;
import co.edu.puj.rpg.modelo.Gremio;
import co.edu.puj.rpg.modelo.Guerrero;
import co.edu.puj.rpg.modelo.Mago;
import co.edu.puj.rpg.modelo.Personaje;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Menu interactivo de la Arena de Heroes RPG (Relaciones entre Clases).
 * Ejecutar esta clase para el taller; Main sigue siendo la batalla del taller anterior.
 */
public class AplicacionArenaRPG {

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Personaje> personajes = new ArrayList<>();
    private static final List<Gremio> gremios = new ArrayList<>();
    private static final List<Escuadron> escuadrones = new ArrayList<>();

    public static void main(String[] args) {
        int opcion = 0;
        try {
            do {
                System.out.println("\n=== MENU ARENA DE HEROES RPG ===");
                System.out.println("1. Crear Personaje");
                System.out.println("2. Crear Gremio");
                System.out.println("3. Reclutar Personaje a Escuadron");
                System.out.println("4. Equipar Arma a Personaje");
                System.out.println("5. Mostrar Estado Detallado (Personaje y Corazon RPG)");
                System.out.println("6. Salir");
                opcion = leerEntero("Seleccione una opcion: ", 1, 6);

                switch (opcion) {
                    case 1:
                        crearPersonaje();
                        break;
                    case 2:
                        crearGremio();
                        break;
                    case 3:
                        reclutarEnEscuadron();
                        break;
                    case 4:
                        equiparArma();
                        break;
                    case 5:
                        mostrarEstado();
                        break;
                    case 6:
                        System.out.println("Cerrando la Arena de Heroes...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } while (opcion != 6);
        } catch (NoSuchElementException ex) {
            System.out.println("\nEntrada finalizada. Cerrando la Arena de Heroes...");
        }
    }

    // ---------- 1. Crear personaje ----------
    private static void crearPersonaje() {
        String nombre = leerTexto("Nombre del personaje: ");
        System.out.println("  1. Guerrero (vida 120, ataque 15, escudo 10)");
        System.out.println("  2. Mago     (vida 80, ataque 25, mana 50)");
        int tipo = leerEntero("Tipo: ", 1, 2);
        Personaje p = (tipo == 1)
                ? new Guerrero(nombre, 120, 15, 0, 0, 10)
                : new Mago(nombre, 80, 25, 0, 0, 50);
        personajes.add(p);
        System.out.println("Personaje creado: " + p.getTipo() + " " + nombre + " (con su CorazonRPG).");

        if (!gremios.isEmpty() && confirmar("Unirlo a un gremio? (s/n): ")) {
            Gremio g = elegirGremio();
            p.unirseAGremio(g);
        }
    }

    // ---------- 2. Crear gremio ----------
    private static void crearGremio() {
        Gremio g = new Gremio(leerTexto("Nombre del gremio: "));
        gremios.add(g);
        System.out.println("Gremio creado: " + g.getNombre());

        while (!personajes.isEmpty() && confirmar("Agregar un personaje existente al gremio? (s/n): ")) {
            Personaje p = elegirPersonaje();
            if (p.getGremio() == g) {
                System.out.println(p.getNombre() + " ya esta en este gremio.");
            } else {
                // Si estaba en otro gremio, unirseAGremio lo retira primero (sincronizacion cruzada).
                p.unirseAGremio(g);
            }
        }
    }

    // ---------- 3. Reclutar en escuadron (agregacion) ----------
    private static void reclutarEnEscuadron() {
        if (personajes.isEmpty()) {
            System.out.println("Primero crea un personaje (opcion 1).");
            return;
        }
        Escuadron escuadron;
        if (escuadrones.isEmpty()) {
            escuadron = crearEscuadron();
        } else {
            System.out.println("Escuadrones:");
            for (int i = 0; i < escuadrones.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + escuadrones.get(i));
            }
            System.out.println("  " + (escuadrones.size() + 1) + ". [Crear nuevo escuadron]");
            int e = leerEntero("Elige: ", 1, escuadrones.size() + 1);
            escuadron = (e == escuadrones.size() + 1) ? crearEscuadron() : escuadrones.get(e - 1);
        }
        Personaje p = elegirPersonaje();
        if (escuadron.reclutarPersonaje(p)) {
            System.out.println(p.getNombre() + " fue reclutado en el escuadron " + escuadron.getNombre() + ".");
        } else {
            System.out.println(p.getNombre() + " ya pertenece a ese escuadron.");
        }
    }

    private static Escuadron crearEscuadron() {
        Escuadron e = new Escuadron(leerTexto("Nombre del nuevo escuadron: "));
        escuadrones.add(e);
        return e;
    }

    // ---------- 4. Equipar arma (asociacion unidireccional) ----------
    private static void equiparArma() {
        if (personajes.isEmpty()) {
            System.out.println("Primero crea un personaje (opcion 1).");
            return;
        }
        Personaje p = elegirPersonaje();
        System.out.println("  1. Equipar nueva arma");
        System.out.println("  2. Desequipar arma actual");
        int accion = leerEntero("Accion: ", 1, 2);
        if (accion == 2) {
            p.desequiparArma();
            return;
        }
        String nombreArma = leerTexto("Nombre del arma: ");
        int danio = leerEntero("Dano base (1-100): ", 1, 100);
        p.equiparArma(new Arma(nombreArma, danio));
        System.out.println("Ataque total de " + p.getNombre() + ": " + p.calcularAtaqueTotal());
    }

    // ---------- 5. Estado detallado ----------
    private static void mostrarEstado() {
        System.out.println("\n--- PERSONAJES ---");
        if (personajes.isEmpty()) {
            System.out.println("(no hay personajes)");
        } else {
            System.out.printf("%-12s %-9s %-5s %-11s %-9s %-18s %-12s %-6s%n",
                    "Nombre", "Tipo", "Nivel", "Salud", "Mana", "Arma", "Gremio", "Ataque");
            for (Personaje p : personajes) {
                System.out.printf("%-12s %-9s %-5d %-11s %-9s %-18s %-12s %-6d%n",
                        p.getNombre(), p.getTipo(), p.getNivel(),
                        p.getCorazon().getSaludActual() + "/" + p.getCorazon().getSaludBase(),
                        p.getCorazon().getManaActual() + "/" + p.getCorazon().getManaBase(),
                        p.getArmaEquipada() != null ? p.getArmaEquipada().getNombre() : "Desarmado",
                        p.getGremio() != null ? p.getGremio().getNombre() : "Sin gremio",
                        p.calcularAtaqueTotal());
            }
            System.out.println("\nDetalle:");
            for (Personaje p : personajes) {
                System.out.println(p);
            }
        }
        System.out.println("\n--- GREMIOS ---");
        if (gremios.isEmpty()) {
            System.out.println("(no hay gremios)");
        }
        for (Gremio g : gremios) {
            System.out.println(g);
        }
        System.out.println("\n--- ESCUADRONES ---");
        if (escuadrones.isEmpty()) {
            System.out.println("(no hay escuadrones)");
        }
        for (Escuadron e : escuadrones) {
            System.out.println(e);
        }
    }

    // ---------- utilidades ----------
    private static Personaje elegirPersonaje() {
        System.out.println("Personajes:");
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            System.out.printf("  %d. %s (%s)%n", i + 1, p.getNombre(), p.getTipo());
        }
        return personajes.get(leerEntero("Elige personaje: ", 1, personajes.size()) - 1);
    }

    private static Gremio elegirGremio() {
        System.out.println("Gremios:");
        for (int i = 0; i < gremios.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, gremios.get(i).getNombre());
        }
        return gremios.get(leerEntero("Elige gremio: ", 1, gremios.size()) - 1);
    }

    private static boolean confirmar(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = scanner.nextLine().trim();
            if (!t.isEmpty()) {
                return t;
            }
            System.out.println("El texto no puede estar vacio.");
        }
    }

    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= min && v <= max) {
                    return v;
                }
            } catch (NumberFormatException ex) {
                // se vuelve a pedir
            }
            System.out.println("Ingresa un numero entre " + min + " y " + max + ".");
        }
    }
}
