package co.edu.puj.rpg.modelo;

public class Mago extends Personaje {

    private static final int COSTO_ATAQUE_ESPECIAL = 15;

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, puntosMana, ataqueBase, posX, posY);
    }

    public void atacar(Personaje objetivo) {
        System.out.println(nombre + " dispara un proyectil arcano a " + objetivo.getNombre() + ".");
        objetivo.defender(calcularAtaqueTotal() / 2);
    }

    public void atacar(Personaje objetivo, boolean modoFuria) {
        if (modoFuria) {
            System.out.println(nombre + " desata una rafaga de energia sobre " + objetivo.getNombre() + ".");
            objetivo.defender(calcularAtaqueTotal());
        } else {
            atacar(objetivo);
        }
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (!corazon.gastarMana(COSTO_ATAQUE_ESPECIAL)) {
            System.out.println(nombre + " no tiene mana suficiente (" + corazon.getManaActual() + "/" + COSTO_ATAQUE_ESPECIAL + ") y usa un ataque basico.");
            atacar(objetivo);
            return;
        }
        int dano = calcularAtaqueTotal() * 2;
        System.out.println(nombre + " lanza una Bola de Fuego contra " + objetivo.getNombre() + ". Mana restante: " + corazon.getManaActual());
        objetivo.defender(dano);
    }

    @Override
    public void defender(int danoRecibido) {
        int mitigado = danoRecibido - danoRecibido / 5;
        System.out.println("  La barrera magica de " + nombre + " reduce el dano.");
        recibirDano(mitigado);
    }

    @Override
    public char getSimbolo() {
        return 'M';
    }

    @Override
    public String describirEstado() {
        return super.describirEstado() + " | Mana: " + corazon.getManaActual();
    }
}
