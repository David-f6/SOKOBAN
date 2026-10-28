# PROJET JAVA JEU SOKOBAN

Projet Java ESIEA, PASS Java 2026-2027 (Pr. Khadija Slimani) : un Sokoban jouable en console, avec un solveur automatique par recherche en profondeur (DFS) et en largeur (BFS).

Ce document sert de dossier d'explication. Il décrit **chaque partie du code**, **pourquoi chaque syntaxe a été choisie**, et **à quelle séance du cours** chaque notion se rattache. Les numéros de ligne renvoient à `src/sokoban/Sokoban.java`.

## Sommaire

1. [Compiler et lancer](#1-compiler-et-lancer)
2. [Utilisation](#2-utilisation)
3. [Correspondance avec le sujet](#3-correspondance-avec-le-sujet)
4. [Notions du cours utilisées](#4-notions-du-cours-utilisées)
5. [Choix de syntaxe](#5-choix-de-syntaxe)
6. [Explication du code, partie jeu](#6-explication-du-code-partie-jeu)
7. [Explication du code, partie solveur](#7-explication-du-code-partie-solveur)
8. [Explication du code, menus](#8-explication-du-code-menus)
9. [Analyse de complexité](#9-analyse-de-complexité)
10. [DFS et BFS : explications demandées par le sujet](#10-dfs-et-bfs--explications-demandées-par-le-sujet)
11. [Résultats de la comparaison](#11-résultats-de-la-comparaison)
12. [Questions probables à l'oral](#12-questions-probables-à-loral)
13. [Sources](#13-sources)

---

## 1. Compiler et lancer

Depuis la racine du dépôt :

```
javac -encoding UTF-8 -d out src/sokoban/Sokoban.java
java -Dstdout.encoding=UTF-8 -cp out sokoban.Sokoban
```

- `javac` compile le fichier source en bytecode (`out/sokoban/Sokoban.class`). `-encoding UTF-8` indique que le fichier contient des accents et le caractère `█`.
- `java` lance la méthode `main`. Le nom complet de la classe est `sokoban.Sokoban` car le fichier commence par `package sokoban;`.
- `-Dstdout.encoding=UTF-8` sert seulement à afficher correctement les accents et les murs `██` dans un terminal Windows.

Dans IntelliJ, il suffit d'ouvrir le projet et de lancer `main`.

## 2. Utilisation

Menu principal (le même que l'exemple du sujet, §15) :

```
================================
          SOKOBAN JAVA
================================
1. Nouvelle partie (niveau 1)
2. Choisir un niveau
3. Résoudre automatiquement
4. Comparer DFS / BFS
5. Afficher les règles
6. Quitter
Votre choix :
```

Commandes en jeu :

| Touche | Action |
|---|---|
| `z` | haut |
| `s` | bas |
| `q` | gauche |
| `d` | droite |
| `u` | annuler le dernier coup (bonus) |
| `r` | recommencer le niveau (bonus) |
| `x` | revenir au menu |

Les majuscules sont acceptées. Toute autre saisie affiche « Commande invalide. ».

Symboles de la grille (sujet §2) :

| Caractère | Signification | Affichage |
|---|---|---|
| `#` | mur | `██` |
| ` ` | case libre | espace |
| `@` | joueur | `@` |
| `$` | caisse | `$` |
| `.` | objectif | `.` |
| `*` | caisse sur un objectif | `*` |
| `+` | joueur sur un objectif | `+` |

## 3. Correspondance avec le sujet

### Niveau standard (sujet §12)

| Exigence | Méthode(s) | Lignes |
|---|---|---|
| Affichage d'une grille | `afficherGrille()`, `symbol()` | 79-95 |
| Déplacement du joueur | `deplacerJoueur()` | 149-170 |
| Déplacement des caisses | `peutPousser()`, `deplacerCaisse()` | 124-147 |
| Gestion des murs | `deplacementValide()`, `caseGrille()` | 113-138 |
| Détection de la victoire | `partieTerminee()` | 194-203 |
| Plusieurs niveaux | `Niveau_1` à `Niveau_5`, `getNiveau()`, `chargerNiveau()` | 19-111 |
| Validation des entrées | `switch` avec `default` dans `jouer()`, `choisirNiveau()`, `main()` | 240-272, 642-658 |
| Compteur de mouvements | `nb_mouvements` | 7, 162 |
| Méthodes correctement utilisées | toutes les méthodes du §11 existent avec ce nom exact | |

### Niveau avancé (sujet §13)

| Exigence | Méthode(s) | Lignes |
|---|---|---|
| Résolution par DFS | `resoudreDFS()`, `dfs()` | 475-502 |
| Résolution par BFS | `resoudreBFS()`, `enfiler()`, `defiler()` | 512-547 |
| États déjà visités | `ajouterEtat()`, `comparerEtats()` | 366-421 |
| Affichage de la solution | `afficherResultat()`, `rejouerSolution()` | 558-595 |
| Comparaison DFS/BFS | `comparer()` | 631-640 |
| Comptage des états explorés | `nbExplores` | 291 |
| Nombre de mouvements | longueur de la solution | 564 |

### Bonus (sujet §14)

| Bonus | Où |
|---|---|
| Niveaux supplémentaires | 5 niveaux au lieu de 3 |
| Redémarrage d'une partie | touche `r` (ligne 261) |
| Annulation du dernier mouvement | touche `u`, `annulerMouvement()` (ligne 172) |
| Détection de situations bloquées | `partieBloquee()` en jeu (ligne 205), `caseBloquante()` dans le solveur (ligne 441) |
| Optimisation du solveur | élagage des caisses coincées, ABR pour les états visités |
| Solution minimisant les mouvements | BFS |

### Contraintes techniques (sujet §10)

| Contrainte | Respect |
|---|---|
| Une seule classe | tout est dans `public class Sokoban` |
| Console, sans interface graphique | uniquement `System.out.println` et `Scanner` |
| Aucune bibliothèque externe | seul import : `java.util.Scanner`, fourni avec Java |
| Pas d'`ArrayList` ni de `HashMap` | pile, file et ensemble des visités écrits avec des tableaux |
| Compilable en ligne de commande | voir section 1 |

## 4. Notions du cours utilisées

| Séance | Notion du cours | Où dans le code |
|---|---|---|
| 1 | Types `int`, `boolean`, `char`, `String` | toutes les variables |
| 1 | Déclaration, affectation, initialisation | `static int nb_mouvements;`, `int cx = xjoueur + dx;` |
| 1 | Opérateurs arithmétiques `+ - * %` | calcul des cases : `xjoueur + 2 * dx`, `i * largeur + j` |
| 1 | Opérateurs de comparaison `== != < <= > >=` | tous les tests de cases |
| 1 | Opérateurs logiques `&& \|\| !` | `murVertical && murHorizontal`, `!deplacementValide(dx, dy)` |
| 1 | Incrémentation `++`, `--`, `+=`, `-=` | `nb_mouvements++`, `xjoueur += dx` |
| 1 | Opérateur ternaire `condition ? a : b` | `deplacerCaisse`, `deplacerJoueur`, `annulerMouvement` |
| 1 | `if / else if / else` | partout |
| 1 | `switch` sur `int`, `char` et `String` | `getNiveau` (`int`), `rejouerSolution` (`char`), `jouer` et menus (`String`) |
| 1 | Boucles `for` et `while` | parcours de la grille, boucle de jeu, BFS |
| 1 | Blocs et portée des variables | variables locales `cx`, `cy`, `tmp` |
| 2 | Méthodes `static`, paramètres, `return`, `void` | toutes les méthodes |
| 3 | Tableaux (indices de 0 à taille-1), parcours par boucle | `grille`, `histoDx`, `joueurEtat`, `file`… |
| 3 | Chaînes : accès au i-ème caractère, longueur, concaténation | `charAt(j)`, `length()`, `ligne + symbol(...)` |
| 4 | Complexité (notation O) | section 9 de ce document |
| 4 | Recherche séquentielle | `caisseEn()`, `partieTerminee()` |
| 4 | Tri | `trierCaisses()` (tri par insertion) |
| 5 | Récursivité : cas de base, cas récursif, pile d'appels | `dfs()` |
| 6 | Pile (LIFO) : empiler, dépiler, sommet | historique d'annulation, `cheminDFS` |
| 6 | File (FIFO) : enfiler, défiler, file vide | `enfiler()`, `defiler()`, `fileVide()` |
| 6 | Parcours en largeur avec ensemble des visités | `resoudreBFS()` |
| 7-8 | Arbre binaire de recherche (ABR) : insertion et recherche | `ajouterEtat()` avec `gauche[]` et `droite[]` |

### Éléments Java hors des slides, et pourquoi on les utilise

Les séances 3 à 8 sont écrites en pseudocode. Il a fallu les traduire en Java, ce qui demande quelques éléments que les slides ne montrent pas :

| Élément | Pourquoi |
|---|---|
| `Scanner` et `nextLine()` | Seul moyen de lire le clavier, exigé par le sujet (§3 « jouer au clavier »). |
| `charAt(i)` et `length()` | Traduction Java de `S[i]` et `Longueur(S)` du pseudocode de la séance 3. |
| `tableau.length` | Traduction Java de `taille(T)`. |
| Variables `static` en dehors des méthodes | Elles sont partagées par toutes les méthodes (la grille, le joueur, le compteur). Sans objet, c'est la seule façon de partager des données. |
| `new int[n]` | Création d'un tableau de taille `n`, traduction de « soit T un tableau de n entiers ». |
| `null` | Valeur « pas de chaîne », utilisée pour dire « pas de solution » (lignes 572, 577). |
| `package sokoban;` | Ajouté par IntelliJ : le fichier est rangé dans le dossier `src/sokoban`. |

## 5. Choix de syntaxe

| Choix | Raison |
|---|---|
| `while (enCours)` avec un `boolean`, pas de `while (true)` + `break` | La condition de sortie est lisible dans l'en-tête de la boucle. `break` n'est utilisé qu'à la fin des `case` d'un `switch`, comme dans le cours. |
| `switch` sur la `String` saisie | Vu en séance 1 (« depuis Java 7 »). Le `default` détecte toutes les commandes invalides en un seul endroit, sans `trim` ni `indexOf`. |
| `case "z": case "Z":` à la suite | Deux `case` sans `break` entre eux exécutent le même code : on accepte la minuscule et la majuscule. |
| Opérateur ternaire | Une case peut prendre deux valeurs selon qu'elle est sur un objectif ou non. `(c == '.') ? '+' : '@'` l'écrit en une ligne au lieu de quatre (séance 1). |
| Concaténation `ligne = ligne + ...` plutôt que `StringBuilder` | `StringBuilder` n'est pas dans le cours. La concaténation de la séance 3 suffit pour quelques dizaines de caractères. |
| Pas de `final` | Le mot-clé n'est pas vu en cours. Les « constantes » (`MAX_ETATS`, `NB_NIVEAUX`) sont écrites en majuscules par convention et ne sont jamais modifiées. |
| Pas de `for (char c : tableau)` | La boucle `for` avec indice est celle du cours, et on a souvent besoin de l'indice `i`. |
| Tableaux « parallèles » (`joueurEtat[e]`, `parent[e]`, `coup[e]`…) au lieu d'une classe `Etat` | Le sujet impose une seule classe. L'état numéro `e` est décrit par la case `e` de chaque tableau. |
| Une case du solveur = un seul entier `ligne * largeur + colonne` | Comparer deux entiers est plus simple et plus rapide que comparer des couples (ligne, colonne). |
| `xjoueur` = ligne, `yjoueur` = colonne | Convention gardée du premier code : on écrit toujours `grille[xjoueur][yjoueur]`, la ligne d'abord. |
| `dx`, `dy` pour une direction | `(-1, 0)` = haut, `(1, 0)` = bas, `(0, -1)` = gauche, `(0, 1)` = droite. Les quatre déplacements utilisent ainsi la même méthode. |

## 6. Explication du code, partie jeu

### 6.1 En-tête (lignes 1 à 5)

```java
package sokoban;
import java.util.Scanner;
public class Sokoban {
```

- `package sokoban;` : le fichier appartient au paquet `sokoban`, qui correspond au dossier `src/sokoban`.
- `import java.util.Scanner;` : rend la classe `Scanner` (lecture du clavier) utilisable. C'est le seul import.
- `public class Sokoban` : l'unique classe du programme. Son nom est le même que celui du fichier.

### 6.2 Variables de la partie (lignes 6 à 17)

```java
static char[][] grille;
static int nb_mouvements;
static int xjoueur;
static int yjoueur;
static int niveauActuel = 1;
static int NB_NIVEAUX = 5;
```

- `grille` : tableau à deux dimensions de caractères (séance 3). `grille[i][j]` est la case de la ligne `i`, colonne `j`.
- `nb_mouvements` : le compteur exigé par le sujet (§4).
- `xjoueur`, `yjoueur` : la ligne et la colonne du joueur. On les garde pour ne pas chercher le `@` dans toute la grille à chaque coup.
- `niveauActuel` : le niveau proposé par « Nouvelle partie ». Il passe au suivant après une victoire.
- `static` : la variable appartient à la classe et est partagée par toutes les méthodes (séance 2).

```java
static int TAILLE_HISTORIQUE = 10000;
static int[] histoDx = new int[TAILLE_HISTORIQUE];
static int[] histoDy = new int[TAILLE_HISTORIQUE];
static boolean[] histoPoussee = new boolean[TAILLE_HISTORIQUE];
static int sommetHisto = 0;
```

C'est une **pile** faite avec des tableaux (séance 6), qui sert à annuler les coups. Le coup numéro `k` est rangé dans `histoDx[k]`, `histoDy[k]` (la direction) et `histoPoussee[k]` (a-t-il poussé une caisse ?). `sommetHisto` est l'indice de la première place libre : empiler écrit à `sommetHisto` puis l'augmente, dépiler le diminue puis lit.

### 6.3 Les niveaux (lignes 19 à 77)

```java
static String[] Niveau_1 = {
        "########",
        ...
};
```

Chaque niveau est un tableau de `String` initialisé entre accolades (séance 3) : un élément par ligne du dessin. Le sujet (§5) autorise à stocker les niveaux dans le programme. Chaque niveau a un joueur, au moins une caisse, autant d'objectifs que de caisses, des murs et des cases libres, comme demandé.

Les lignes n'ont pas toutes la même longueur (niveaux 3 et 5) : c'est pour cela que la grille est un tableau « en escalier », où chaque ligne a sa propre taille.

```java
static String[] getNiveau(int numero) {
    switch (numero) {
        case 1: return Niveau_1;
        ...
        default: return Niveau_5;
    }
}
```

Un `switch` sur un `int` (séance 1) renvoie le tableau du niveau demandé. Le `return` quitte la méthode, donc aucun `break` n'est nécessaire. Le `default` est obligatoire pour que Java sache que la méthode renvoie toujours quelque chose.

### 6.4 Affichage : `symbol()` et `afficherGrille()` (lignes 79 à 95)

```java
static String symbol(char c) {
    if (c == '#') {
        return ("██");
    }
    return c + " ";
}
```

Une case de la console est plus haute que large. On affiche donc **deux caractères par case** pour que la grille ne soit pas écrasée : `██` pour un mur, le caractère suivi d'un espace pour le reste. `c + " "` est une concaténation `char + String`, qui donne une `String` (séance 3).

```java
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
```

- Deux boucles `for` imbriquées : `i` parcourt les lignes, `j` les colonnes de la ligne `i`.
- `grille[i].length` et non une largeur fixe, car chaque ligne a sa propre longueur.
- On construit la ligne complète puis on l'affiche en une fois, et on termine par le compteur.

### 6.5 Chargement : `chargerNiveau()` (lignes 97 à 111)

```java
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
```

- `new char[niveau.length][]` crée autant de lignes que le niveau, sans fixer encore leur taille.
- Chaque ligne `i` est créée avec la longueur de la chaîne correspondante, puis remplie caractère par caractère avec `charAt(j)` (équivalent de `S[j]` en séance 3).
- On repère au passage la position du joueur (`@`, ou `+` s'il commence sur un objectif).
- On remet le compteur et la pile d'annulation à zéro. C'est aussi ce qui permet de **recommencer** un niveau : il suffit de le recharger.
- On **copie** le niveau dans `grille` au lieu de modifier les `String` : le niveau d'origine reste intact pour pouvoir le recharger.

### 6.6 Outils : `caseGrille()` et `estCaisse()` (lignes 113 à 122)

```java
static char caseGrille(int i, int j) {
    if (i < 0 || i >= grille.length || j < 0 || j >= grille[i].length) {
        return '#';
    }
    return grille[i][j];
}
```

Renvoie la case `(i, j)`, ou un mur si elle est hors de la grille. Comme les lignes ont des longueurs différentes, une case peut exister sur une ligne et pas sur sa voisine. Sans cette méthode, on risquerait une `ArrayIndexOutOfBoundsException`. L'ordre des tests compte : grâce au `||` (évaluation paresseuse), `grille[i].length` n'est lu que si `i` est valide.

```java
static boolean estCaisse(char c) {
    return c == '$' || c == '*';
}
```

Une caisse peut être sur une case libre (`$`) ou sur un objectif (`*`). La méthode renvoie directement le résultat du test booléen.

### 6.7 Déplacements (lignes 124 à 170)

**`peutPousser(dx, dy)`**

```java
char derriere = caseGrille(xjoueur + 2 * dx, yjoueur + 2 * dy);
return derriere == ' ' || derriere == '.';
```

La caisse est à `+1` case du joueur, la case derrière elle à `+2`. On peut pousser seulement si cette case est libre ou un objectif, donc ni un mur ni une autre caisse (sujet §3).

**`deplacementValide(dx, dy)`**

```java
char cible = caseGrille(xjoueur + dx, yjoueur + dy);
if (cible == '#') {
    return false;
}
if (estCaisse(cible)) {
    return peutPousser(dx, dy);
}
return true;
```

Trois cas, dans l'ordre : mur, interdit ; caisse, autorisé si elle peut être poussée ; sinon (case libre ou objectif), autorisé. Chaque `return` quitte la méthode, donc pas besoin de `else`.

**`deplacerCaisse(dx, dy)`**

```java
int cx = xjoueur + dx;
int cy = yjoueur + dy;
int bx = xjoueur + 2 * dx;
int by = yjoueur + 2 * dy;
grille[bx][by] = (grille[bx][by] == '.') ? '*' : '$';
grille[cx][cy] = (grille[cx][cy] == '*') ? '.' : ' ';
```

- `(cx, cy)` est la case actuelle de la caisse, `(bx, by)` sa nouvelle case.
- Nouvelle case : si c'est un objectif, la caisse devient `*`, sinon `$`.
- Ancienne case : si la caisse était sur un objectif (`*`), on remet l'objectif `.`, sinon une case vide.
- Le ternaire (séance 1) évite quatre `if / else`.

**`deplacerJoueur(dx, dy)`**

```java
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
```

1. Si le coup est interdit, on prévient et on sort (`return` dans une méthode `void`, séance 2).
2. On retient s'il y a une caisse devant (`poussee`), puis on la pousse **avant** de déplacer le joueur, pour que sa case soit libre.
3. On efface le joueur de son ancienne case en remettant l'objectif s'il y en avait un.
4. On met à jour la position, puis on dessine le joueur (`+` sur un objectif, `@` sinon).
5. On compte le mouvement.

```java
if (sommetHisto < TAILLE_HISTORIQUE) {
    histoDx[sommetHisto] = dx;
    histoDy[sommetHisto] = dy;
    histoPoussee[sommetHisto] = poussee;
    sommetHisto++;
}
```

On **empile** le coup pour pouvoir l'annuler. Le test évite de dépasser la taille du tableau.

### 6.8 Annulation : `annulerMouvement()` (lignes 172 à 192)

```java
if (sommetHisto == 0) { ... return; }
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
```

- Pile vide : rien à annuler.
- On **dépile** le dernier coup (LIFO : le dernier joué est le premier annulé, séance 6).
- Si ce coup avait poussé une caisse, elle se trouve maintenant **devant** le joueur. On la retire de là et on la replace **sous** le joueur, qui va reculer.
- Sinon, on efface simplement le joueur.
- Le joueur recule d'une case (`-= dx`) et le compteur diminue.

### 6.9 Victoire et blocage (lignes 194 à 218)

**`partieTerminee()`**

```java
for (...) for (...)
    if (grille[i][j] == '$') return false;
return true;
```

La partie est gagnée quand **il ne reste aucune caisse hors objectif**. Il suffit de chercher un `$` : dès qu'on en trouve un, on renvoie `false` sans parcourir le reste. C'est une recherche séquentielle (séance 4). Cette méthode marche même s'il y a plus d'objectifs que de caisses.

**`partieBloquee()`** (bonus)

```java
boolean murVertical = caseGrille(i - 1, j) == '#' || caseGrille(i + 1, j) == '#';
boolean murHorizontal = caseGrille(i, j - 1) == '#' || caseGrille(i, j + 1) == '#';
if (murVertical && murHorizontal) return true;
```

Une caisse `$` (donc hors objectif) qui touche un mur en haut ou en bas **et** un mur à gauche ou à droite est dans un coin. On ne peut plus la pousser dans aucune direction : la partie est perdue. Le jeu affiche alors un avertissement.

### 6.10 Boucle de jeu : `jouer()` (lignes 220 à 275)

```java
String[] niveau = getNiveau(numero);
chargerNiveau(niveau);
boolean enCours = true;
while (enCours) {
    afficherGrille();
    if (partieTerminee()) {
        ... afficher « Victoire ! » et le nombre de mouvements ...
        enCours = false;
    } else {
        if (partieBloquee()) { ... avertissement ... }
        String saisie = sc.nextLine();
        switch (saisie) {
            case "z":
            case "Z":
                deplacerJoueur(-1, 0);
                break;
            ...
            default:
                System.out.println("Commande invalide.");
        }
    }
}
```

- Le `Scanner` est reçu en paramètre : on en crée un seul dans `main` et on le passe aux méthodes qui lisent le clavier.
- À chaque tour : on affiche, on teste la victoire, sinon on lit une commande. C'est le déroulé du sujet (§3).
- La boucle s'arrête quand `enCours` devient `false` : victoire ou touche `x`.
- `switch` sur une `String` (séance 1). Chaque `case` se termine par `break` pour ne pas exécuter le suivant. Le `default` attrape toutes les autres saisies : c'est la **validation des entrées**.
- `r` recharge le niveau, ce qui le remet à zéro.

## 7. Explication du code, partie solveur

### 7.1 Représentation d'un état

Le sujet (§6) demande que chaque état connaisse la position du joueur, la position des caisses, les mouvements possibles, les états déjà explorés et s'il est gagnant.

- Chaque case est numérotée par **un seul entier** : `case = ligne * largeur + colonne`. Monter d'une ligne revient à faire `- largeur`, aller à droite `+ 1`.
- Un état = la case du joueur + les cases des caisses, **triées par ordre croissant**. Le tri garantit qu'un même placement des caisses s'écrit toujours de la même façon, quel que soit l'ordre dans lequel on les a poussées.
- Les murs et les objectifs ne bougent jamais : ils sont rangés une seule fois dans `mur[]` et `objectif[]`, et ne font pas partie de l'état.

### 7.2 Variables du solveur (lignes 277 à 306)

| Variable | Rôle |
|---|---|
| `MAX_ETATS` | nombre maximal d'états mémorisés (un million) |
| `PROFONDEUR_MAX` | longueur maximale d'un chemin pour le DFS |
| `largeur`, `nbCases`, `nbCaisses` | dimensions du niveau |
| `mur[c]`, `objectif[c]` | la case `c` est-elle un mur, un objectif ? |
| `joueurEtat[e]` | case du joueur dans l'état `e` |
| `caissesEtat[e * nbCaisses + k]` | case de la caisse `k` dans l'état `e` (tableau à une dimension découpé en blocs de `nbCaisses`) |
| `parent[e]` | état d'où l'on vient (pour reconstruire la solution du BFS) |
| `coup[e]` | touche jouée pour arriver à l'état `e` |
| `nbEtats` | nombre d'états mémorisés |
| `nbExplores` | nombre d'états explorés, affiché dans la comparaison |
| `limiteAtteinte` | vrai si la mémoire de un million d'états est pleine |
| `gauche[e]`, `droite[e]` | fils gauche et droit de l'état `e` dans l'arbre binaire de recherche (`-1` = pas de fils) |
| `cheminDFS`, `longueurDFS` | pile des coups du DFS |
| `file`, `debutFile`, `finFile` | file du BFS |
| `touches`, `dLigne`, `dColonne` | les 4 directions : `touches[d]` est la lettre, `(dLigne[d], dColonne[d])` le décalage |

Ranger les directions dans des tableaux permet d'écrire `for (int d = 0; d < 4; d++)` au lieu de recopier quatre fois le même code.

### 7.3 `preparerSolveur()` (lignes 308 à 352)

1. Calcule la `largeur` = la ligne la plus longue du niveau.
2. Parcourt toutes les cases. Une case au-delà de la fin d'une ligne courte est considérée comme un mur (`(j < niveau[i].length()) ? niveau[i].charAt(j) : '#'`).
3. Remplit `mur[]` et `objectif[]`, compte les caisses et repère le joueur.
4. Crée les tableaux de mémorisation des états.
5. Trie les caisses de départ et enregistre l'état initial, qui porte le numéro 0 et n'a pas de parent (`-1`).

### 7.4 `trierCaisses()` : tri par insertion (lignes 354 à 364)

```java
for (int i = 1; i < t.length; i++) {
    int valeur = t[i];
    int j = i - 1;
    while (j >= 0 && t[j] > valeur) {
        t[j + 1] = t[j];
        j--;
    }
    t[j + 1] = valeur;
}
```

On prend chaque élément et on le fait glisser vers la gauche tant que l'élément précédent est plus grand. Ce tri est en O(k²) pour `k` caisses, mais `k` vaut au plus 7 ici, et le tableau n'a qu'une seule caisse déplacée à chaque fois : il est presque trié, cas où le tri par insertion est très rapide.

### 7.5 `comparerEtats()` (lignes 366 à 378)

Compare deux états dans un ordre fixé : d'abord la case du joueur, puis les caisses une par une. Elle renvoie `-1`, `0` ou `1`, comme on compare des mots dans un dictionnaire. C'est ce qui permet de ranger les états dans un arbre binaire de recherche.

### 7.6 `ajouterEtat()` : ensemble des états visités (lignes 380 à 421)

C'est la réponse à « le programme doit éviter de revisiter indéfiniment les mêmes états » (sujet §7).

```java
int e = nbEtats;
... on écrit le nouvel état dans la case e ...
if (e > 0) {
    int noeud = 0;
    boolean place = false;
    while (!place) {
        int cmp = comparerEtats(e, noeud);
        if (cmp == 0) {
            return -1;
        } else if (cmp < 0) {
            if (gauche[noeud] == -1) { gauche[noeud] = e; place = true; }
            else { noeud = gauche[noeud]; }
        } else {
            if (droite[noeud] == -1) { droite[noeud] = e; place = true; }
            else { noeud = droite[noeud]; }
        }
    }
}
nbEtats++;
return e;
```

Les états forment un **arbre binaire de recherche** (séances 7-8) : la racine est l'état 0, tout ce qui est plus petit qu'un nœud est à sa gauche, tout ce qui est plus grand est à sa droite. Les liens « fils gauche » et « fils droit » sont des numéros d'état rangés dans `gauche[]` et `droite[]` : pas besoin de classe `Noeud`.

- On descend depuis la racine en allant à gauche ou à droite selon la comparaison, comme une recherche dichotomique (séance 4).
- Si on trouve un état égal, il a **déjà été visité** : on renvoie `-1` et rien n'est ajouté (`nbEtats` n'augmente pas, donc la case `e` sera réécrite par le prochain candidat).
- Si on arrive sur une place vide, on y accroche le nouvel état.

**Pourquoi un ABR et pas une simple recherche séquentielle ?** Le BFS du niveau 4 mémorise plus de 600 000 états. Avec une recherche séquentielle, chaque ajout relirait tous les états déjà vus : de l'ordre de 600 000 × 600 000 / 2, soit environ 2 × 10¹¹ comparaisons, plusieurs minutes de calcul. Avec l'ABR, chaque ajout coûte environ log₂(600 000) ≈ 20 à 40 comparaisons : le niveau est résolu en environ une seconde.

### 7.7 `estVictoire()`, `caisseEn()`, `caseBloquante()` (lignes 423 à 448)

- `estVictoire(e)` : toutes les caisses de l'état `e` sont-elles sur un objectif ? Comme il y a autant de caisses que d'objectifs, c'est la victoire.
- `caisseEn(e, c)` : y a-t-il une caisse sur la case `c` dans l'état `e` ? Recherche séquentielle sur au plus 7 caisses.
- `caseBloquante(c)` : la case `c` est-elle un coin qui n'est pas un objectif ? Même règle que `partieBloquee()`, mais avec les numéros de case : la case au-dessus est `c - largeur`, en dessous `c + largeur`, à gauche `c - 1`, à droite `c + 1`.

### 7.8 `etatSuivant()` : les mouvements possibles (lignes 450 à 473)

```java
int decalage = dLigne[d] * largeur + dColonne[d];
int nouveauJoueur = joueurEtat[e] + decalage;
if (mur[nouveauJoueur]) return -1;
... copie des caisses dans tmp ...
if (caisseEn(e, nouveauJoueur)) {
    int derriere = nouveauJoueur + decalage;
    if (mur[derriere] || caisseEn(e, derriere) || caseBloquante(derriere)) return -1;
    ... la caisse passe de nouveauJoueur à derriere, puis on retrie ...
}
return ajouterEtat(nouveauJoueur, tmp, e, touches[d]);
```

Ce sont exactement les règles du jeu (§3), écrites sur des numéros de case : on ne traverse pas un mur, on pousse une caisse seulement si la case derrière est libre. En plus, on **élague** les poussées vers un coin, puisque ces états ne mènent jamais à la victoire. On renvoie le numéro du nouvel état, ou `-1` s'il est impossible ou déjà vu.

### 7.9 DFS récursif (lignes 475 à 510)

```java
static boolean dfs(int e, int profondeur) {
    nbExplores++;
    if (estVictoire(e)) {
        longueurDFS = profondeur;
        return true;
    }
    if (profondeur >= PROFONDEUR_MAX) {
        return false;
    }
    for (int d = 0; d < 4; d++) {
        int suivant = etatSuivant(e, d);
        if (suivant != -1) {
            cheminDFS[profondeur] = touches[d];
            if (dfs(suivant, profondeur + 1)) {
                return true;
            }
        }
    }
    return false;
}
```

- **Cas de base** : l'état est gagnant, on renvoie `true` et on retient la longueur du chemin.
- **Condition d'arrêt** : la profondeur atteint 300 coups, on abandonne cette branche. Sans cette limite, la pile d'appels de Java pourrait déborder (`StackOverflowError`, séance 5).
- **Cas récursif** : pour chaque direction menant à un état nouveau, on **empile** la lettre dans `cheminDFS[profondeur]` et on rappelle `dfs` sur cet état avec `profondeur + 1`. Si l'appel réussit, on remonte `true` jusqu'en haut.
- Si l'appel échoue, on passe à la direction suivante. La lettre sera écrasée par le prochain essai à la même profondeur : c'est le **dépilement**.
- La récursion se termine toujours : chaque appel porte sur un état jamais vu, et le nombre d'états est fini.

`resoudreDFS()` prépare le solveur et lance `dfs(0, 0)` depuis l'état initial. `solutionDFS()` lit la pile du bas vers le haut pour former la chaîne des coups.

### 7.10 BFS avec une file (lignes 512 à 556)

```java
static void enfiler(int e) { file[finFile] = e; finFile++; }
static int defiler() { int e = file[debutFile]; debutFile++; return e; }
static boolean fileVide() { return debutFile == finFile; }
```

Une **file** (FIFO, séance 6) faite avec un tableau et deux indices : on ajoute à la fin (`finFile`), on retire au début (`debutFile`). Le premier entré est le premier sorti. Le tableau n'a pas besoin d'être circulaire : chaque état n'est enfilé qu'une seule fois, donc `MAX_ETATS` places suffisent.

```java
enfiler(0);
while (!fileVide()) {
    int e = defiler();
    nbExplores++;
    if (estVictoire(e)) return e;
    for (int d = 0; d < 4; d++) {
        int suivant = etatSuivant(e, d);
        if (suivant != -1) enfiler(suivant);
    }
}
return -1;
```

C'est l'algorithme de parcours en largeur de la séance 6 : on enfile le départ, puis tant que la file n'est pas vide, on défile un état, on le teste et on enfile ses voisins jamais visités.

```java
static String solutionBFS(int e) {
    String s = "";
    while (parent[e] != -1) {
        s = coup[e] + s;
        e = parent[e];
    }
    return s;
}
```

On reconstruit la solution **à l'envers**, en remontant de parent en parent depuis l'état gagnant jusqu'à l'état initial (le seul dont le parent vaut `-1`). Chaque lettre est ajoutée **devant** la chaîne (`coup[e] + s`) pour obtenir les coups dans le bon ordre.

## 8. Explication du code, menus

| Méthode | Lignes | Rôle |
|---|---|---|
| `afficherResultat()` | 558 | Affiche « Solution trouvée », le nombre de mouvements, la solution et le nombre d'états explorés (format du sujet §9). |
| `lancerDFS()`, `lancerBFS()` | 570-578 | Lancent un solveur et renvoient la solution sous forme de `String`, ou `null` s'il n'y en a pas. |
| `rejouerSolution()` | 580 | Rejoue une solution sur la vraie grille, un coup à chaque appui sur Entrée. Réutilise `deplacerJoueur()`, ce qui prouve que la solution respecte les règles du jeu. `switch` sur un `char`. |
| `menuResolution()` | 597 | Choix du niveau, puis de la méthode (DFS ou BFS), puis propose de rejouer la solution. |
| `comparer()` | 631 | Lance DFS puis BFS sur le même niveau et affiche les deux résultats. |
| `choisirNiveau()` | 642 | Affiche la liste des niveaux et renvoie le numéro choisi, ou `-1` si la saisie est invalide. |
| `afficherRegles()` | 660 | Affiche les règles. |
| `main()` | 669 | Crée l'unique `Scanner`, affiche le menu principal en boucle et appelle la bonne méthode avec un `switch` sur la saisie. La boucle s'arrête quand `quitter` devient vrai. |

## 9. Analyse de complexité

Notations : `L` × `C` = taille de la grille, `k` = nombre de caisses, `n` = nombre d'états mémorisés.

| Opération | Complexité | Explication |
|---|---|---|
| `afficherGrille()`, `chargerNiveau()`, `partieTerminee()`, `partieBloquee()` | O(L × C) | une visite par case |
| `deplacerJoueur()`, `deplacementValide()`, `annulerMouvement()` | O(1) | quelques cases autour du joueur, grâce à `xjoueur`/`yjoueur` mémorisés |
| `trierCaisses()` | O(k²) au pire, O(k) si presque trié | tri par insertion |
| `caisseEn()`, `estVictoire()` | O(k) | recherche séquentielle |
| `ajouterEtat()` | O(k × log n) en moyenne, O(k × n) au pire | descente dans l'ABR, chaque comparaison coûte O(k) |
| Même chose avec une recherche séquentielle | O(k × n) à chaque fois | rejeté, trop lent (voir 7.6) |
| BFS complet | O(n × 4 × k × log n) en moyenne | chaque état est défilé une fois et produit au plus 4 voisins |
| Mémoire du solveur | O(n × k) | un bloc de `k` cases par état |

Le pire cas de l'ABR (O(n)) arrive si les états sont ajoutés dans l'ordre croissant, l'arbre devient alors une liste. En pratique, l'ordre de découverte des états est assez mélangé et l'arbre reste peu profond.

## 10. DFS et BFS : explications demandées par le sujet

### DFS (sujet §7)

- **Cas de base** : l'état courant est gagnant, toutes les caisses sont sur des objectifs.
- **Cas récursif** : on essaie chaque direction jouable vers un état jamais visité, et on relance la recherche sur cet état.
- **Condition d'arrêt** : victoire trouvée, ou profondeur maximale de 300 coups atteinte, ou plus aucun voisin nouveau. Les états déjà visités ne sont jamais réexplorés, ce qui empêche de tourner en rond.
- **Avantages** : code court et naturel en récursif ; ne garde en mémoire que le chemin en cours ; peut trouver une solution très vite si elle est « dans la première branche » (niveau 3 : 446 états au lieu de 778 pour le BFS).
- **Limites** : la solution trouvée est souvent beaucoup plus longue que nécessaire (229 coups au lieu de 23 au niveau 4) ; le résultat dépend de l'ordre des directions (`z`, `s`, `q`, `d`) ; la limite de profondeur et le marquage des visités peuvent faire manquer une solution ; une récursion trop profonde ferait déborder la pile d'appels.

### BFS (sujet §8)

On explore les états **par distance croissante** depuis le départ : d'abord tous les états à 1 coup, puis à 2 coups, etc. C'est la file qui garantit cet ordre, puisque les états à `d + 1` coups sont enfilés après tous ceux à `d` coups. Le premier état gagnant défilé est donc à la plus petite distance possible : **le BFS donne la solution la plus courte en nombre de mouvements**.

### Pourquoi les deux algorithmes donnent des résultats différents (sujet §9)

- Le DFS **fonce dans une branche** jusqu'au bout et s'arrête à la **première** solution rencontrée, sans savoir si elle est courte. Le nombre d'états qu'il explore dépend de la place de cette solution dans l'arbre : parfois très peu, parfois énormément.
- Le BFS **avance en largeur** : il doit avoir exploré tous les états plus proches avant d'atteindre la solution. Il trouve toujours la plus courte, mais peut explorer beaucoup plus d'états quand la solution est loin et que l'arbre est très large (niveau 4).
- Ils parcourent donc les mêmes états dans un **ordre différent**, ce qui change à la fois la solution trouvée et le nombre d'états visités avant de la trouver.

## 11. Résultats de la comparaison

Mesurés avec le menu « 4. Comparer DFS / BFS » :

| Niveau | Caisses | DFS : mouvements | DFS : états explorés | BFS : mouvements | BFS : états explorés |
|---|---|---|---|---|---|
| 1 | 1 | 1 | 2 | 1 | 2 |
| 2 | 2 | 67 | 3 630 | 13 | 993 |
| 3 | 2 | 69 | 446 | 41 | 778 |
| 4 | 5 | 229 | 77 503 | 23 | 615 821 |
| 5 | 7 | 142 | 771 571 | 34 | 46 247 |

On voit bien les deux comportements : le BFS donne toujours moins de mouvements ; le DFS explore parfois moins d'états (niveaux 3 et 4), parfois beaucoup plus (niveau 5).

## 12. Questions probables à l'oral

**Pourquoi `xjoueur` est-il la ligne et pas la colonne ?**
Parce qu'on accède à la grille par `grille[ligne][colonne]`. Garder la ligne en premier partout évite les inversions. C'est une convention, l'important est de la respecter partout.

**Pourquoi deux caractères par case à l'affichage ?**
Un caractère de console est environ deux fois plus haut que large. Deux caractères par case donnent des cases à peu près carrées.

**Comment le programme sait-il qu'on a gagné ?**
`partieTerminee()` cherche une caisse `$` qui n'est pas sur un objectif. S'il n'y en a aucune, toutes les caisses sont des `*` : c'est gagné.

**Pourquoi le joueur peut-il être `+` ?**
Quand il marche sur un objectif, il faut se souvenir que l'objectif est dessous pour le redessiner après son départ. `+` veut dire « joueur sur objectif », comme `*` veut dire « caisse sur objectif ».

**Comment fonctionne l'annulation ?**
Chaque coup est empilé (direction + caisse poussée ou non). Annuler dépile le dernier coup et fait l'inverse : le joueur recule, et si une caisse avait été poussée, elle revient sous lui.

**Pourquoi ne pas utiliser `ArrayList` ou `HashMap` ?**
Le sujet l'interdit (§10) : la pile, la file et l'ensemble des visités doivent être écrits par l'étudiant. Ils sont tous faits avec des tableaux et des indices.

**Comment évite-t-on de revisiter un état ?**
Tous les états rencontrés sont rangés dans un arbre binaire de recherche. Avant d'ajouter un état, on le cherche dans l'arbre : s'il y est déjà, on l'ignore.

**Pourquoi trier les caisses ?**
Deux caisses A et B sur les cases 10 et 20 forment le même état que B et A sur les cases 20 et 10. Le tri donne une écriture unique, sinon le même état serait exploré deux fois.

**Pourquoi le BFS trouve-t-il la solution la plus courte ?**
La file traite les états dans l'ordre de leur distance au départ. Le premier état gagnant sorti de la file est donc le plus proche.

**À quoi sert `PROFONDEUR_MAX` ?**
À arrêter le DFS dans une branche trop longue. Chaque appel récursif occupe de la place dans la pile d'appels de Java ; sans limite, elle pourrait déborder.

**Pourquoi un tableau `parent[]` ?**
Le BFS ne garde pas le chemin en cours comme le DFS. Pour retrouver la solution, chaque état retient d'où il vient et par quelle touche ; on remonte ensuite de l'état gagnant jusqu'au départ.

**Que se passe-t-il si un niveau n'a pas de solution ?**
Le BFS vide sa file sans trouver de victoire et le programme affiche « Solution trouvée : non ».

## 13. Sources

- **Sujet** : « Projet Java — Sokoban : jeu et résolution algorithmique », PASS Java 2026-2027, Pr. Khadija Slimani. Sections citées : §2 symboles, §3 commandes et règles, §4 victoire, §5 niveaux, §6 états, §7 DFS, §8 BFS, §9 comparaison, §10 contraintes, §11 méthodes attendues, §12-14 niveaux et bonus, §15 menu.
- **Cours PASS Java**, Pr. Khadija Slimani :
  - Séances 1-2 : types, opérateurs (dont le ternaire), conditions, `switch` sur `int`, `char` et `String`, boucles, méthodes `static`.
  - Séances 3-4 : tableaux, chaînes de caractères, complexité, recherche séquentielle et dichotomique, tri.
  - Séances 5-6 : récursivité (cas de base, cas récursif, pile d'appels), piles, files, parcours en largeur.
  - Séances 7-8 : listes chaînées, arbres, arbres binaires de recherche.
