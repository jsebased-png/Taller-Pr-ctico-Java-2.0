package co.edu.puj.rpg.modelo;

public class Enemigo extends Personaje {

    public Enemigo(String nombre, int vida, int ataqueBase, int posX, int posY) {
        super(nombre, vida, ataqueBase, posX, posY);
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        System.out.println(nombre + " arremete contra " + objetivo.getNombre() + ".");
        objetivo.defender(ataqueBase + 5);
    }

    @Override
    public char getSimbolo() {
        return 'E';
    }
}
