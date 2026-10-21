package co.edu.puj.rpg.modelo;

public class Guerrero extends Personaje {

    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int posX, int posY, int fuerzaEscudo) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.fuerzaEscudo = fuerzaEscudo;
    }

    public void atacar(Personaje objetivo) {
        System.out.println(nombre + " ataca a " + objetivo.getNombre() + " con su espada.");
        objetivo.defender(ataqueBase);
    }

    public void atacar(Personaje objetivo, boolean modoFuria) {
        if (modoFuria) {
            int dano = ataqueBase * 2;
            System.out.println(nombre + " entra en furia y golpea a " + objetivo.getNombre() + ".");
            objetivo.defender(dano);
        } else {
            atacar(objetivo);
        }
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        int dano = ataqueBase + fuerzaEscudo + 10;
        System.out.println(nombre + " lanza un Golpe de Escudo contra " + objetivo.getNombre() + ".");
        objetivo.defender(dano);
    }

    @Override
    public void defender(int danoRecibido) {
        int mitigado = Math.max(1, danoRecibido - fuerzaEscudo / 2);
        System.out.println("  El escudo de " + nombre + " absorbe " + (danoRecibido - mitigado) + " de dano.");
        recibirDano(mitigado);
    }

    @Override
    public char getSimbolo() {
        return 'G';
    }

    @Override
    public String describirEstado() {
        return super.describirEstado() + " | Escudo: " + fuerzaEscudo;
    }
}
