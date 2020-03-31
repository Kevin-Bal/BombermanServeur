package Serveur;

public class Compteur extends Thread {
    private String nom;
    private int max;
    // Temps de pause en millisecondes
    private final static int PAUSE = 5000;


    public Compteur(String nom, int max) {
        this.nom = nom;
        this.max = max;
    }

    public Compteur(String nom) {
        this(nom, 10);
    }

    public void run() {
        for (int i = 1; i <= max; i++) {
            try {
                sleep(30);
            }
            catch(InterruptedException e) {
                System.err.println(nom + " a ete interrompu.");
            }
            System.out.println(nom + " : " + i);
        }
        System.out.println("*** " + nom + " a fini de compter jusqu'à " + max);
    }

    public static void main(String[] args) {
        Compteur[] compteurs = {
                new Compteur("Toto"),
                new Compteur("Bibi"),
                new Compteur("Robert"),
                new Compteur("Pierre")
        };
        for (int i = 0; i < compteurs.length; i++) {
            compteurs[i].start();
        }
    }

}