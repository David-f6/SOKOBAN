package sokoban;

import java.util.Scanner;

public class Sokoban {

    // ===================== Partie jeu : état de la partie =====================

    static char[][] grille;
    static int nb_mouvements;
    static int xjoueur;   // ligne du joueur
    static int yjoueur;   // colonne du joueur
    static int niveauActuel = 1;
    static int NB_NIVEAUX = 5;

    // Historique des coups pour l'annulation (une pile faite avec deux tableaux)
    static int TAILLE_HISTORIQUE = 10000;
    static int[] histoDx = new int[TAILLE_HISTORIQUE];
    static int[] histoDy = new int[TAILLE_HISTORIQUE];
    static boolean[] histoPoussee = new boolean[TAILLE_HISTORIQUE];
    static int sommetHisto = 0;

    // ===================== Niveaux =====================
    // # mur, ' ' libre, @ joueur, $ caisse, . objectif, * caisse sur objectif, + joueur sur objectif

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

    // ===================== Affichage =====================

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

    // ===================== Chargement =====================

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

    // Renvoie la case (i, j), ou un mur si elle est en dehors de la grille
    static char caseGrille(int i, int j) {
        if (i < 0 || i >= grille.length || j < 0 || j >= grille[i].length) {
            return '#';
        }
        return grille[i][j];
    }

    static boolean estCaisse(char c) {
        return c == '$' || c == '*';
    }

    // ===================== Déplacements =====================

    // La case derrière la caisse doit être libre (vide ou objectif)
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

    // Pousse la caisse située devant le joueur d'une case dans la direction (dx, dy)
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

        // On empile le coup pour pouvoir l'annuler
        if (sommetHisto < TAILLE_HISTORIQUE) {
            histoDx[sommetHisto] = dx;
            histoDy[sommetHisto] = dy;
            histoPoussee[sommetHisto] = poussee;
            sommetHisto++;
        }
    }

    // Bonus : annule le dernier mouvement (on dépile le dernier coup)
    static void annulerMouvement() {
        if (sommetHisto == 0) {
            System.out.println("Aucun mouvement à annuler.");
            return;
        }
        sommetHisto--;
        int dx = histoDx[sommetHisto];
        int dy = histoDy[sommetHisto];
        if (histoPoussee[sommetHisto]) {
            // La caisse est devant le joueur : on la remet sous le joueur
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

    // ===================== Victoire et blocages =====================

    // Victoire : il ne reste plus aucune caisse '$' hors d'un objectif
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

    // Bonus : une caisse hors objectif coincée dans un coin ne pourra plus jamais bouger
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

    // ===================== Boucle de jeu =====================

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
                // On propose le niveau suivant à la prochaine « Nouvelle partie »
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

    // ===================== Solveur : représentation d'un état =====================
    // Une case est repérée par un seul entier : case = ligne * largeur + colonne.
    // Un état = la case du joueur + les cases des caisses, rangées dans l'ordre croissant
    // (ainsi deux états avec les mêmes caisses s'écrivent toujours de la même façon).

    static int MAX_ETATS = 1000000;
    static int PROFONDEUR_MAX = 300;   // limite du DFS pour éviter un débordement de pile

    static int largeur;
    static int nbCases;
    static int nbCaisses;
    static boolean[] mur;
    static boolean[] objectif;

    // Tous les états rencontrés sont rangés dans ces tableaux (l'état n° e)
    static int[] joueurEtat;
    static int[] caissesEtat;   // les caisses de l'état e sont dans caissesEtat[e * nbCaisses ...]
    static int[] parent;        // état d'où l'on vient (pour reconstruire la solution du BFS)
    static char[] coup;         // touche jouée pour arriver dans cet état
    static int nbEtats;
    static int nbExplores;
    static boolean limiteAtteinte;

    // Ensemble des états déjà visités : un arbre binaire de recherche (ABR) fait avec des tableaux
    static int[] gauche;
    static int[] droite;

    // Pile du chemin pour le DFS
    static char[] cheminDFS = new char[PROFONDEUR_MAX];

    // File des états à explorer pour le BFS (tableau + deux indices)
    static int[] file;
    static int debutFile;
    static int finFile;

    // Les 4 directions dans l'ordre z, s, q, d
    static char[] touches = {'z', 's', 'q', 'd'};
    static int[] dLigne = {-1, 1, 0, 0};
    static int[] dColonne = {0, 0, -1, 1};

    // Prépare le solveur à partir de la grille de départ d'un niveau
    static void preparerSolveur(String[] niveau) {
        largeur = 0;
        for (int i = 0; i < niveau.length; i++) {
            if (niveau[i].length() > largeur) {
                largeur = niveau[i].length();
            }
        }
        nbCases = niveau.length * largeur;
        mur = new boolean[nbCases];
        objectif = new boolean[nbCases];
        nbCaisses = 0;
        int joueurDepart = 0;
        int[] caissesDepart = new int[nbCases];
        for (int i = 0; i < niveau.length; i++) {
            for (int j = 0; j < largeur; j++) {
                int c = i * largeur + j;
                char car = (j < niveau[i].length()) ? niveau[i].charAt(j) : '#';
                mur[c] = (car == '#');
                objectif[c] = (car == '.' || car == '*' || car == '+');
                if (car == '$' || car == '*') {
                    caissesDepart[nbCaisses] = c;
                    nbCaisses++;
                }
                if (car == '@' || car == '+') {
                    joueurDepart = c;
                }
            }
        }
        joueurEtat = new int[MAX_ETATS];
        caissesEtat = new int[MAX_ETATS * nbCaisses];
        parent = new int[MAX_ETATS];
        coup = new char[MAX_ETATS];
        gauche = new int[MAX_ETATS];
        droite = new int[MAX_ETATS];
        nbEtats = 0;
        nbExplores = 0;
        limiteAtteinte = false;

        int[] tmp = new int[nbCaisses];
        for (int k = 0; k < nbCaisses; k++) {
            tmp[k] = caissesDepart[k];
        }
        trierCaisses(tmp);
        ajouterEtat(joueurDepart, tmp, -1, ' ');
    }

    // Tri par insertion des cases des caisses
    static void trierCaisses(int[] t) {
        for (int i = 1; i < t.length; i++) {
            int valeur = t[i];
            int j = i - 1;
            while (j >= 0 && t[j] > valeur) {
                t[j + 1] = t[j];
                j--;
            }
            t[j + 1] = valeur;
        }
    }

    // Compare l'état a et l'état b : -1 si a < b, 0 si égaux, 1 si a > b
    static int comparerEtats(int a, int b) {
        if (joueurEtat[a] != joueurEtat[b]) {
            return (joueurEtat[a] < joueurEtat[b]) ? -1 : 1;
        }
        for (int k = 0; k < nbCaisses; k++) {
            int ca = caissesEtat[a * nbCaisses + k];
            int cb = caissesEtat[b * nbCaisses + k];
            if (ca != cb) {
                return (ca < cb) ? -1 : 1;
            }
        }
        return 0;
    }

    // Ajoute un état s'il n'a jamais été vu.
    // Renvoie son numéro, ou -1 s'il était déjà visité (ou si la mémoire est pleine).
    static int ajouterEtat(int joueur, int[] caisses, int pere, char touche) {
        if (nbEtats >= MAX_ETATS) {
            limiteAtteinte = true;
            return -1;
        }
        int e = nbEtats;   // on écrit le candidat dans la première place libre
        joueurEtat[e] = joueur;
        for (int k = 0; k < nbCaisses; k++) {
            caissesEtat[e * nbCaisses + k] = caisses[k];
        }
        parent[e] = pere;
        coup[e] = touche;
        gauche[e] = -1;
        droite[e] = -1;

        if (e > 0) {
            // Recherche dans l'ABR (racine = état 0), comme une recherche dichotomique
            int noeud = 0;
            boolean place = false;
            while (!place) {
                int cmp = comparerEtats(e, noeud);
                if (cmp == 0) {
                    return -1;   // déjà visité
                } else if (cmp < 0) {
                    if (gauche[noeud] == -1) {
                        gauche[noeud] = e;
                        place = true;
                    } else {
                        noeud = gauche[noeud];
                    }
                } else {
                    if (droite[noeud] == -1) {
                        droite[noeud] = e;
                        place = true;
                    } else {
                        noeud = droite[noeud];
                    }
                }
            }
        }
        nbEtats++;
        return e;
    }

    static boolean estVictoire(int e) {
        for (int k = 0; k < nbCaisses; k++) {
            if (!objectif[caissesEtat[e * nbCaisses + k]]) {
                return false;
            }
        }
        return true;
    }

    static boolean caisseEn(int e, int c) {
        for (int k = 0; k < nbCaisses; k++) {
            if (caissesEtat[e * nbCaisses + k] == c) {
                return true;
            }
        }
        return false;
    }

    // Une caisse dans un coin hors objectif rend l'état perdu : inutile de l'explorer
    static boolean caseBloquante(int c) {
        if (objectif[c]) {
            return false;
        }
        boolean murVertical = mur[c - largeur] || mur[c + largeur];
        boolean murHorizontal = mur[c - 1] || mur[c + 1];
        return murVertical && murHorizontal;
    }

    // Joue la direction d depuis l'état e.
    // Renvoie le numéro du nouvel état, ou -1 si le coup est impossible ou mène à un état déjà vu.
    static int etatSuivant(int e, int d) {
        int decalage = dLigne[d] * largeur + dColonne[d];
        int nouveauJoueur = joueurEtat[e] + decalage;
        if (mur[nouveauJoueur]) {
            return -1;
        }
        int[] tmp = new int[nbCaisses];
        for (int k = 0; k < nbCaisses; k++) {
            tmp[k] = caissesEtat[e * nbCaisses + k];
        }
        if (caisseEn(e, nouveauJoueur)) {
            int derriere = nouveauJoueur + decalage;
            if (mur[derriere] || caisseEn(e, derriere) || caseBloquante(derriere)) {
                return -1;
            }
            for (int k = 0; k < nbCaisses; k++) {
                if (tmp[k] == nouveauJoueur) {
                    tmp[k] = derriere;
                }
            }
            trierCaisses(tmp);
        }
        return ajouterEtat(nouveauJoueur, tmp, e, touches[d]);
    }

    // ===================== DFS récursif =====================

    // Renvoie la longueur de la solution trouvée, ou -1 s'il n'y en a pas
    static int resoudreDFS(String[] niveau) {
        preparerSolveur(niveau);
        if (dfs(0, 0)) {
            return longueurDFS;
        }
        return -1;
    }

    static int longueurDFS;

    static boolean dfs(int e, int profondeur) {
        nbExplores++;
        // Cas de base 1 : toutes les caisses sont sur des objectifs
        if (estVictoire(e)) {
            longueurDFS = profondeur;
            return true;
        }
        // Cas de base 2 (condition d'arrêt) : chemin trop long
        if (profondeur >= PROFONDEUR_MAX) {
            return false;
        }
        // Cas récursif : on essaie chaque direction vers un état jamais vu
        for (int d = 0; d < 4; d++) {
            int suivant = etatSuivant(e, d);
            if (suivant != -1) {
                cheminDFS[profondeur] = touches[d];   // on empile le coup
                if (dfs(suivant, profondeur + 1)) {
                    return true;
                }
                // échec : le coup sera écrasé (dépilé) au prochain essai
            }
        }
        return false;
    }

    static String solutionDFS(int longueur) {
        String s = "";
        for (int i = 0; i < longueur; i++) {
            s = s + cheminDFS[i];
        }
        return s;
    }

    // ===================== BFS avec une file =====================

    static void enfiler(int e) {
        file[finFile] = e;
        finFile++;
    }

    static int defiler() {
        int e = file[debutFile];
        debutFile++;
        return e;
    }

    static boolean fileVide() {
        return debutFile == finFile;
    }

    // Renvoie le numéro de l'état gagnant, ou -1 s'il n'y a pas de solution
    static int resoudreBFS(String[] niveau) {
        preparerSolveur(niveau);
        file = new int[MAX_ETATS];
        debutFile = 0;
        finFile = 0;
        enfiler(0);
        while (!fileVide()) {
            int e = defiler();
            nbExplores++;
            if (estVictoire(e)) {
                return e;
            }
            for (int d = 0; d < 4; d++) {
                int suivant = etatSuivant(e, d);
                if (suivant != -1) {
                    enfiler(suivant);
                }
            }
        }
        return -1;
    }

    // On remonte les parents depuis l'état gagnant jusqu'à l'état de départ
    static String solutionBFS(int e) {
        String s = "";
        while (parent[e] != -1) {
            s = coup[e] + s;
            e = parent[e];
        }
        return s;
    }

    // ===================== Menus du solveur =====================

    static void afficherResultat(String nom, String solution) {
        System.out.println(nom);
        if (solution == null) {
            System.out.println("Solution trouvée : non" + (limiteAtteinte ? " (limite de mémoire atteinte)" : ""));
        } else {
            System.out.println("Solution trouvée : oui");
            System.out.println("Mouvements : " + solution.length());
            System.out.println("Solution : " + solution);
        }
        System.out.println("États explorés : " + nbExplores);
    }

    static String lancerDFS(int numero) {
        int longueur = resoudreDFS(getNiveau(numero));
        return (longueur == -1) ? null : solutionDFS(longueur);
    }

    static String lancerBFS(int numero) {
        int gagnant = resoudreBFS(getNiveau(numero));
        return (gagnant == -1) ? null : solutionBFS(gagnant);
    }

    // Rejoue une solution coup par coup sur la grille du jeu
    static void rejouerSolution(Scanner sc, int numero, String solution) {
        chargerNiveau(getNiveau(numero));
        afficherGrille();
        for (int i = 0; i < solution.length(); i++) {
            System.out.println("Entrée pour le coup suivant (" + solution.charAt(i) + ")");
            sc.nextLine();
            switch (solution.charAt(i)) {
                case 'z': deplacerJoueur(-1, 0); break;
                case 's': deplacerJoueur(1, 0); break;
                case 'q': deplacerJoueur(0, -1); break;
                default: deplacerJoueur(0, 1);
            }
            afficherGrille();
        }
        System.out.println("Victoire !");
    }

    static void menuResolution(Scanner sc) {
        int numero = choisirNiveau(sc);
        if (numero == -1) {
            return;
        }
        System.out.println("Méthode : 1. DFS (profondeur)   2. BFS (largeur, solution la plus courte)");
        String choix = sc.nextLine();
        String solution;
        switch (choix) {
            case "1":
                solution = lancerDFS(numero);
                afficherResultat("DFS", solution);
                break;
            case "2":
                solution = lancerBFS(numero);
                afficherResultat("BFS", solution);
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }
        if (solution != null) {
            System.out.println("Voir la solution coup par coup ? (o/n)");
            String rep = sc.nextLine();
            switch (rep) {
                case "o":
                case "O":
                    rejouerSolution(sc, numero, solution);
                    break;
                default:
            }
        }
    }

    static void comparer(Scanner sc) {
        int numero = choisirNiveau(sc);
        if (numero == -1) {
            return;
        }
        System.out.println("========== COMPARAISON (niveau " + numero + ") ==========");
        afficherResultat("DFS", lancerDFS(numero));
        System.out.println();
        afficherResultat("BFS", lancerBFS(numero));
    }

    // ===================== Menu principal =====================

    // Renvoie le numéro du niveau choisi, ou -1 si la saisie est invalide
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
            System.out.println("3. Résoudre automatiquement");
            System.out.println("4. Comparer DFS / BFS");
            System.out.println("5. Afficher les règles");
            System.out.println("6. Quitter");
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
                    menuResolution(sc);
                    break;
                case "4":
                    comparer(sc);
                    break;
                case "5":
                    afficherRegles();
                    break;
                case "6":
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
        System.out.println("Au revoir !");
    }
}
