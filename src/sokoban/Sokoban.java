package sokoban;

import java.util.Scanner;

public class Sokoban {
    static char[][] grille;
    static int nb_mouvements;
    static int xjoueur;
    static int yjoueur;
    static int niveauActuel = 1;
    static int NB_NIVEAUX = 5;

    static int TAILLE_HISTORIQUE = 10000;
    static int[] histoDx = new int[TAILLE_HISTORIQUE];
    static int[] histoDy = new int[TAILLE_HISTORIQUE];
    static boolean[] histoPoussee = new boolean[TAILLE_HISTORIQUE];
    static int sommetHisto = 0;

    static String[] Niveau_1 = {
            "########",
            "#      #",
            "# .    #",
            "# $    #",
            "# @    #",
            "########"
    };

    static String[] Niveau_2 = {
            "########",
            "#      #",
            "# .  $ #",
            "#  ##  #",
            "# $  . #",
            "#   @  #",
            "########"
    };

    static String[] Niveau_3 = {
            "  ####",
            "###  ####",
            "#     $ #",
            "# #  #$ #",
            "# . .#@ #",
            "#########"
    };

    static String[] Niveau_4 = {
            "#######",
            "#.   .#",
            "# $$$ #",
            "#  @  #",
            "# $ $ #",
            "#. . .#",
            "#######"
    };

    static String[] Niveau_5 = {
            "  #####",
            "###   #",
            "#.@$  #",
            "### $.#",
            "#.##$ #",
            "# # . ##",
            "#$ *$$.#",
            "#   .  #",
            "########"
    };

    static String[] getNiveau(int numero) {
        switch (numero) {
            case 1: return Niveau_1;
            case 2: return Niveau_2;
            case 3: return Niveau_3;
            case 4: return Niveau_4;
            default: return Niveau_5;
        }
    }

    static String symbol(char c) {
        if (c == '#') {
            return ("██");
        }
        return c + " ";
    }

    static void afficherGrille() {
        for (int i = 0; i < grille.length; i++) {
            String ligne = "";
            for (int j = 0; j < grille[i].length; j++) {
                ligne = ligne + symbol(grille[i][j]);
            }
            System.out.println(ligne);
        }
        System.out.println("Mouvements : " + nb_mouvements);
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
        sommetHisto = 0;
    }

    static char caseGrille(int i, int j) {
        if (i < 0 || i >= grille.length || j < 0 || j >= grille[i].length) {
            return '#';
        }
        return grille[i][j];
    }

    static boolean estCaisse(char c) {
        return c == '$' || c == '*';
    }

    static boolean peutPousser(int dx, int dy) {
        char derriere = caseGrille(xjoueur + 2 * dx, yjoueur + 2 * dy);
        return derriere == ' ' || derriere == '.';
    }

    static boolean deplacementValide(int dx, int dy) {
        char cible = caseGrille(xjoueur + dx, yjoueur + dy);
        if (cible == '#') {
            return false;
        }
        if (estCaisse(cible)) {
            return peutPousser(dx, dy);
        }
        return true;
    }

    static void deplacerCaisse(int dx, int dy) {
        int cx = xjoueur + dx;
        int cy = yjoueur + dy;
        int bx = xjoueur + 2 * dx;
        int by = yjoueur + 2 * dy;
        grille[bx][by] = (grille[bx][by] == '.') ? '*' : '$';
        grille[cx][cy] = (grille[cx][cy] == '*') ? '.' : ' ';
    }

    static void deplacerJoueur(int dx, int dy) {
        if (!deplacementValide(dx, dy)) {
            System.out.println("Déplacement impossible.");
            return;
        }
        boolean poussee = estCaisse(grille[xjoueur + dx][yjoueur + dy]);
        if (poussee) {
            deplacerCaisse(dx, dy);
        }
        grille[xjoueur][yjoueur] = (grille[xjoueur][yjoueur] == '+') ? '.' : ' ';
        xjoueur += dx;
        yjoueur += dy;
        grille[xjoueur][yjoueur] = (grille[xjoueur][yjoueur] == '.') ? '+' : '@';
        nb_mouvements++;

        if (sommetHisto < TAILLE_HISTORIQUE) {
            histoDx[sommetHisto] = dx;
            histoDy[sommetHisto] = dy;
            histoPoussee[sommetHisto] = poussee;
            sommetHisto++;
        }
    }

    static void annulerMouvement() {
        if (sommetHisto == 0) {
            System.out.println("Aucun mouvement à annuler.");
            return;
        }
        sommetHisto--;
        int dx = histoDx[sommetHisto];
        int dy = histoDy[sommetHisto];
        if (histoPoussee[sommetHisto]) {
            int cx = xjoueur + dx;
            int cy = yjoueur + dy;
            grille[cx][cy] = (grille[cx][cy] == '*') ? '.' : ' ';
            grille[xjoueur][yjoueur] = (grille[xjoueur][yjoueur] == '+') ? '*' : '$';
        } else {
            grille[xjoueur][yjoueur] = (grille[xjoueur][yjoueur] == '+') ? '.' : ' ';
        }
        xjoueur -= dx;
        yjoueur -= dy;
        grille[xjoueur][yjoueur] = (grille[xjoueur][yjoueur] == '.') ? '+' : '@';
        nb_mouvements--;
    }

    static boolean partieTerminee() {
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == '$') {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean partieBloquee() {
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == '$') {
                    boolean murVertical = caseGrille(i - 1, j) == '#' || caseGrille(i + 1, j) == '#';
                    boolean murHorizontal = caseGrille(i, j - 1) == '#' || caseGrille(i, j + 1) == '#';
                    if (murVertical && murHorizontal) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static void jouer(Scanner sc, int numero) {
        String[] niveau = getNiveau(numero);
        chargerNiveau(niveau);
        System.out.println("===== Niveau " + numero + " =====");
        boolean enCours = true;
        while (enCours) {
            afficherGrille();
            if (partieTerminee()) {
                System.out.println("Victoire !");
                System.out.println("Nombre de mouvements : " + nb_mouvements);
                if (numero < NB_NIVEAUX) {
                    niveauActuel = numero + 1;
                }
                enCours = false;
            } else {
                if (partieBloquee()) {
                    System.out.println("Attention : une caisse est bloquée dans un coin. Tapez u pour annuler ou r pour recommencer.");
                }
                System.out.println("Commande (z/q/s/d, u annuler, r recommencer, x quitter) :");
                String saisie = sc.nextLine();
                switch (saisie) {
                    case "z":
                    case "Z":
                        deplacerJoueur(-1, 0);
                        break;
                    case "q":
                    case "Q":
                        deplacerJoueur(0, -1);
                        break;
                    case "s":
                    case "S":
                        deplacerJoueur(1, 0);
                        break;
                    case "d":
                    case "D":
                        deplacerJoueur(0, 1);
                        break;
                    case "u":
                    case "U":
                        annulerMouvement();
                        break;
                    case "r":
                    case "R":
                        chargerNiveau(niveau);
                        System.out.println("Niveau recommencé.");
                        break;
                    case "x":
                    case "X":
                        enCours = false;
                        break;
                    default:
                        System.out.println("Commande invalide.");
                }
            }
        }
    }

    static int choisirNiveau(Scanner sc) {
        for (int n = 1; n <= NB_NIVEAUX; n++) {
            System.out.println(n + ". Niveau " + n);
        }
        System.out.println("Votre choix :");
        String choix = sc.nextLine();
        switch (choix) {
            case "1": return 1;
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            default:
                System.out.println("Choix invalide.");
                return -1;
        }
    }

    static void afficherRegles() {
        System.out.println("===== RÈGLES =====");
        System.out.println("Poussez toutes les caisses ($) sur les objectifs (.).");
        System.out.println("Le joueur (@) se déplace avec z (haut), s (bas), q (gauche), d (droite).");
        System.out.println("On peut pousser une caisse si la case derrière elle est libre, jamais la tirer.");
        System.out.println("Une caisse sur un objectif s'affiche *, le joueur sur un objectif +.");
        System.out.println("u annule le dernier coup, r recommence le niveau, x revient au menu.");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean quitter = false;
        int numero;
        while (!quitter) {
            System.out.println();
            System.out.println("================================");
            System.out.println("          SOKOBAN JAVA");
            System.out.println("================================");
            System.out.println("1. Nouvelle partie (niveau " + niveauActuel + ")");
            System.out.println("2. Choisir un niveau");
            System.out.println("3. Afficher les règles");
            System.out.println("4. Quitter");
            System.out.println("Votre choix :");
            String choix = sc.nextLine();
            switch (choix) {
                case "1":
                    jouer(sc, niveauActuel);
                    break;
                case "2":
                    numero = choisirNiveau(sc);
                    if (numero != -1) {
                        niveauActuel = numero;
                        jouer(sc, numero);
                    }
                    break;
                case "3":
                    afficherRegles();
                    break;
                case "4":
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
        System.out.println("Au revoir !");
    }
}
