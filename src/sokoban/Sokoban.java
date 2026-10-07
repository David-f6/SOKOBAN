package sokoban;

import java.util.Scanner;

public class Sokoban {

    static char[][] grille;
    static int nb_mouvements;
    static int xjoueur;
    static int yjoueur;

    static final String[] Niveau_1 = {
            "########",
            "#      #",
            "# .    #",
            "# $    #",
            "# @    #",
            "########"
    };


    static String symbol(char c) {
        if (c == '#') {
            return ("██");
        }
        return String.valueOf(c) + " ";
    }

    static void chargerNiveau(String[] niveau) {
        grille = new char[niveau.length][];
        for (int i = 0; i < niveau.length; i++) {
            grille[i] = new char[niveau[i].length()];
            for (int j = 0; j < niveau[i].length(); j++) {
                grille[i][j] = niveau[i].charAt(j);
                if (grille[i][j] == '@' || grille[i][j] == '+') {
                    xjoueur = i;
                    yjoueur = j;
                }
            }
        }
        nb_mouvements = 0;
    }

    static void afficherGrille() {
        for (char[] chars : grille) {
            StringBuilder ligne = new StringBuilder();
            for (char aChar : chars) {
                ligne.append(symbol(aChar));
            }
            System.out.println(ligne);
        }
        System.out.println("Mouvements : " + nb_mouvements);
    }

    static void deplacerJoueur(int dx, int dy) {
        char cible = grille[xjoueur + dx][yjoueur + dy];
        if (cible == '#') {
            return;
        }
        grille[xjoueur][yjoueur] = ' ';
        xjoueur += dx;
        yjoueur += dy;
        grille[xjoueur][yjoueur] = '@';
        nb_mouvements++;
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("----Sokoban----");
        System.out.println();

        chargerNiveau(Niveau_1);

        Scanner sc = new Scanner(System.in);
        while (true) {
            afficherGrille();
            System.out.print("Commande (z/q/s/d, x pour quitter) : ");
            String saisie = sc.nextLine().trim();
            if (saisie.length() != 1 || "zqsdx".indexOf(saisie.charAt(0)) == -1) {
                System.out.println("Commande invalide.");
                continue;
            }
            char c = saisie.charAt(0);
            switch (c) {
                case 'z':
                    deplacerJoueur(-1, 0);
                    break;

                case 'q':
                    deplacerJoueur(0, -1);
                    break;

                case 's':
                    deplacerJoueur(1, 0);
                    break;

                case 'd':
                    deplacerJoueur(0, 1);
                    break;

                case 'x':
                    break;

                default:

            }


        }
    }
}



