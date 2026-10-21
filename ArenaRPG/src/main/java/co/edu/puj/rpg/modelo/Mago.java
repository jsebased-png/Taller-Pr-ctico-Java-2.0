package co.edu.puj.rpg.modelo;

public class Mago extends Personaje {

    private static final int COSTO_ATAQUE_ESPECIAL = 15;

    protected int puntosMana;

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.puntosMana = puntosMana;
    }

    public void atacar(Personaje objetivo) {
        System.out.println(nombre + " dispara un proyectil arcano a " + objetivo.getNombre() + ".");
        objetivo.defender(ataqueBase / 2);
    }

    public void atacar(Personaje objetivo, boolean modoFuria) {
        if (modoFuria) {
            System.out.println(nombre + " desata una rafaga de energia sobre " + objetivo.getNombre() + ".");
            objetivo.defender(ataqueBase);
        } else {
            atacar(objetivo);
        }
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (puntosMana < COSTO_ATAQUE_ESPECIAL) {
            System.out.println(nombre + " no tiene mana suficiente (" + puntosMana + "/" + COSTO_ATAQUE_ESPECIAL + ") y usa un ataque basico.");
            atacar(objetivo);
            return;
        }
        puntosMana -= COSTO_ATAQUE_ESPECIAL;
        int dano = ataqueBase * 2;
        System.out.println(nombre + " lanza una Bola de Fuego contra " + objetivo.getNombre() + ". Mana restante: " + puntosMana);
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
        return super.describirEstado() + " | Mana: " + puntosMana;
    }
}
