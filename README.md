# PROJET JAVA JEU SOKOBAN

Projet Java ESIEA, PASS Java 2026-2027 (Pr. Khadija Slimani) : un Sokoban jouable en console, réalisé au **niveau standard** (sujet §12) avec les quatre bonus du niveau standard (sujet §14).

Ce document sert de dossier d'explication. Il décrit **chaque partie du code**, **pourquoi chaque syntaxe a été choisie**, et **à quelle séance du cours** chaque notion se rattache. Les numéros de ligne renvoient à `src/sokoban/Sokoban.java`.

## Sommaire

1. [Compiler et lancer](#1-compiler-et-lancer)
2. [Utilisation](#2-utilisation)
3. [Correspondance avec le sujet](#3-correspondance-avec-le-sujet)
4. [Notions du cours utilisées](#4-notions-du-cours-utilisées)
5. [Choix de syntaxe](#5-choix-de-syntaxe)
6. [Explication du code, partie jeu](#6-explication-du-code-partie-jeu)
7. [Explication du code, menus](#7-explication-du-code-menus)
8. [Analyse de complexité](#8-analyse-de-complexité)
9. [Questions probables à l'oral](#9-questions-probables-à-loral)
10. [Sources](#10-sources)

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

Menu principal (inspiré de l'exemple du sujet §15, sans les entrées du solveur qui relèvent du niveau avancé) :

```
================================
          SOKOBAN JAVA
================================
1. Nouvelle partie (niveau 1)
2. Choisir un niveau
3. Afficher les règles
4. Quitter
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
| Validation des entrées | `switch` avec `default` dans `jouer()`, `choisirNiveau()`, `main()` | 240-272, 283-292, 319-338 |
| Compteur de mouvements | `nb_mouvements` | 7, 162 |
| Méthodes correctement utilisées | les méthodes du §11 qui concernent le jeu existent avec ce nom exact (`resoudreDFS` et `resoudreBFS` relèvent du niveau avancé) | |

### Bonus du niveau standard (sujet §14)

Les quatre bonus du niveau standard sont réalisés. Ils ne remplacent aucune fonctionnalité obligatoire : celles-ci sont toutes présentes (tableau ci-dessus).

| Bonus | Où |
|---|---|
| Niveaux supplémentaires | 5 niveaux au lieu de 3 |
| Redémarrage d'une partie | touche `r` (ligne 261) |
| Annulation du dernier mouvement | touche `u`, `annulerMouvement()` (ligne 172) |
| Détection de situations manifestement bloquées | `partieBloquee()` (ligne 205) : une caisse hors objectif coincée dans un coin |

### Contraintes techniques (sujet §10)

| Contrainte | Respect |
|---|---|
| Une seule classe | tout est dans `public class Sokoban` |
| Console, sans interface graphique | uniquement `System.out.println` et `Scanner` |
| Aucune bibliothèque externe | seul import : `java.util.Scanner`, fourni avec Java |
| Pas d'`ArrayList` ni de `HashMap` | la pile d'annulation est écrite avec des tableaux |
| Compilable en ligne de commande | voir section 1 |

## 4. Notions du cours utilisées

| Séance | Notion du cours | Où dans le code |
|---|---|---|
| 1 | Types `int`, `boolean`, `char`, `String` | toutes les variables |
| 1 | Déclaration, affectation, initialisation | `static int nb_mouvements;`, `int cx = xjoueur + dx;` |
| 1 | Opérateurs arithmétiques `+ - *` | calcul des cases : `xjoueur + 2 * dx` |
| 1 | Opérateurs de comparaison `== != < <= > >=` | tous les tests de cases |
| 1 | Opérateurs logiques `&& \|\| !` | `murVertical && murHorizontal`, `!deplacementValide(dx, dy)` |
| 1 | Incrémentation `++`, `--`, `+=`, `-=` | `nb_mouvements++`, `xjoueur += dx` |
| 1 | Opérateur ternaire `condition ? a : b` | `deplacerCaisse`, `deplacerJoueur`, `annulerMouvement` |
| 1 | `if / else` | partout |
| 1 | `switch` sur `int` et `String` | `getNiveau` (`int`), `jouer` et menus (`String`) |
| 1 | Boucles `for` et `while` | parcours de la grille, boucle de jeu, menu |
| 1 | Blocs et portée des variables | variables locales `cx`, `cy`, `cible` |
| 2 | Méthodes `static`, paramètres, `return`, `void` | toutes les méthodes |
| 3 | Tableaux (indices de 0 à taille-1), tableaux à deux dimensions, parcours par boucle | `grille`, `histoDx`, `histoDy`, `histoPoussee`, niveaux |
| 3 | Chaînes : accès au i-ème caractère, longueur, concaténation | `charAt(j)`, `length()`, `ligne + symbol(...)` |
| 4 | Complexité (notation O) | section 8 de ce document |
| 4 | Recherche séquentielle | `partieTerminee()`, `partieBloquee()` |
| 6 | Pile (LIFO) : empiler, dépiler, sommet | historique d'annulation (`histoDx`, `histoDy`, `histoPoussee`, `sommetHisto`) |

### Éléments Java hors des slides, et pourquoi on les utilise

Les séances 3 à 6 sont écrites en pseudocode. Il a fallu les traduire en Java, ce qui demande quelques éléments que les slides ne montrent pas :

| Élément | Pourquoi |
|---|---|
| `Scanner` et `nextLine()` | Seul moyen de lire le clavier, exigé par le sujet (§3 « jouer au clavier »). |
| `charAt(i)` et `length()` | Traduction Java de `S[i]` et `Longueur(S)` du pseudocode de la séance 3. |
| `tableau.length` | Traduction Java de `taille(T)`. |
| Variables `static` en dehors des méthodes | Elles sont partagées par toutes les méthodes (la grille, le joueur, le compteur). Sans objet, c'est la seule façon de partager des données. |
| `new int[n]` | Création d'un tableau de taille `n`, traduction de « soit T un tableau de n entiers ». |
| `package sokoban;` | Ajouté par IntelliJ : le fichier est rangé dans le dossier `src/sokoban`. |

## 5. Choix de syntaxe

| Choix | Raison |
|---|---|
| `while (enCours)` avec un `boolean`, pas de `while (true)` + `break` | La condition de sortie est lisible dans l'en-tête de la boucle. `break` n'est utilisé qu'à la fin des `case` d'un `switch`, comme dans le cours. |
| `switch` sur la `String` saisie | Vu en séance 1 (« depuis Java 7 »). Le `default` détecte toutes les commandes invalides en un seul endroit, sans `trim` ni `indexOf`. |
| `case "z": case "Z":` à la suite | Deux `case` sans `break` entre eux exécutent le même code : on accepte la minuscule et la majuscule. |
| Opérateur ternaire | Une case peut prendre deux valeurs selon qu'elle est sur un objectif ou non. `(c == '.') ? '+' : '@'` l'écrit en une ligne au lieu de quatre (séance 1). |
| Concaténation `ligne = ligne + ...` plutôt que `StringBuilder` | `StringBuilder` n'est pas dans le cours. La concaténation de la séance 3 suffit pour quelques dizaines de caractères. |
| Pas de `final` | Le mot-clé n'est pas vu en cours. Les « constantes » (`TAILLE_HISTORIQUE`, `NB_NIVEAUX`) sont écrites en majuscules par convention et ne sont jamais modifiées. |
| Pas de `for (char c : tableau)` | La boucle `for` avec indice est celle du cours, et on a souvent besoin de l'indice `i`. |
| Trois tableaux « parallèles » pour l'historique (`histoDx[k]`, `histoDy[k]`, `histoPoussee[k]`) au lieu d'une classe `Coup` | Le sujet impose une seule classe. Le coup numéro `k` est décrit par la case `k` de chaque tableau. |
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

## 7. Explication du code, menus

### `choisirNiveau()` (lignes 277 à 293)

```java
for (int n = 1; n <= NB_NIVEAUX; n++) {
    System.out.println(n + ". Niveau " + n);
}
String choix = sc.nextLine();
switch (choix) {
    case "1": return 1;
    ...
    default:
        System.out.println("Choix invalide.");
        return -1;
}
```

Affiche la liste des niveaux avec une boucle, puis renvoie le numéro choisi. Une saisie invalide renvoie `-1`, une valeur qui ne peut pas être un numéro de niveau : la méthode appelante sait ainsi qu'il ne faut rien lancer.

### `afficherRegles()` (lignes 295 à 302)

Une suite de `System.out.println` qui rappelle les règles et les touches.

### `main()` (lignes 304 à 341)

```java
Scanner sc = new Scanner(System.in);
boolean quitter = false;
int numero;
while (!quitter) {
    ... affichage du menu ...
    String choix = sc.nextLine();
    switch (choix) {
        case "1": jouer(sc, niveauActuel); break;
        case "2":
            numero = choisirNiveau(sc);
            if (numero != -1) { niveauActuel = numero; jouer(sc, numero); }
            break;
        case "3": afficherRegles(); break;
        case "4": quitter = true; break;
        default: System.out.println("Choix invalide.");
    }
}
```

- On crée **un seul** `Scanner` sur le clavier (`System.in`) et on le passe en paramètre aux méthodes qui lisent : plusieurs `Scanner` sur `System.in` se gêneraient.
- La boucle `while (!quitter)` réaffiche le menu après chaque partie. Elle s'arrête quand on choisit « Quitter ».
- `numero` est déclaré avant le `switch` : une variable déclarée dans un `case` serait visible dans tous les autres `case`, ce qui prête à confusion (portée des blocs, séance 1).
- « Nouvelle partie » lance `niveauActuel`, qui passe automatiquement au niveau suivant après une victoire (ligne 230).

## 8. Analyse de complexité

Notations : `L` × `C` = taille de la grille (nombre de lignes × nombre de colonnes).

| Opération | Complexité | Explication |
|---|---|---|
| `afficherGrille()`, `chargerNiveau()` | O(L × C) | une visite par case |
| `partieTerminee()` | O(L × C) au pire | recherche séquentielle d'un `$` ; s'arrête dès qu'elle en trouve un |
| `partieBloquee()` | O(L × C) | pour chaque caisse, on regarde ses 4 voisines : O(1) par case |
| `deplacementValide()`, `peutPousser()`, `deplacerCaisse()`, `deplacerJoueur()` | O(1) | on ne regarde que 2 cases devant le joueur, grâce à `xjoueur` et `yjoueur` mémorisés |
| `annulerMouvement()` | O(1) | dépiler le sommet de la pile ne dépend pas du nombre de coups joués |
| Un tour de jeu complet | O(L × C) | dominé par l'affichage et les tests de victoire et de blocage |

**Pourquoi mémoriser la position du joueur ?** Sans `xjoueur` et `yjoueur`, chaque déplacement devrait d'abord chercher le `@` dans toute la grille : O(L × C) au lieu de O(1).

**Pourquoi une pile pour l'annulation ?** Le coup à annuler est toujours le dernier joué : c'est exactement le principe LIFO. Empiler et dépiler se font en O(1).

## 9. Questions probables à l'oral

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

**Pourquoi ne pas utiliser `ArrayList` ?**
Le sujet l'interdit (§10). La pile d'annulation est faite avec trois tableaux et un indice de sommet, comme en séance 6.

**Que se passe-t-il si on tape « zz » ou « Z » ?**
« zz » tombe dans le `default` du `switch` : « Commande invalide. ». « Z » est accepté, grâce au `case "Z":` placé juste sous `case "z":`.

**Pourquoi `caseGrille()` renvoie-t-elle un mur hors de la grille ?**
Les lignes des niveaux 3 et 5 n'ont pas toutes la même longueur. Lire une case qui n'existe pas ferait planter le programme (`ArrayIndexOutOfBoundsException`). Considérer l'extérieur comme un mur est à la fois sûr et logique.

**La détection de blocage trouve-t-elle tous les blocages ?**
Non, seulement le cas le plus simple : une caisse hors objectif dans un coin. Le sujet demande de détecter « certaines situations simples » (§14). D'autres blocages existent, par exemple deux caisses côte à côte contre un mur.

**Que se passe-t-il quand on gagne ?**
`partieTerminee()` renvoie `true`, le programme affiche « Victoire ! » et le nombre de mouvements, met `enCours` à `false` pour sortir de la boucle, et propose le niveau suivant à la prochaine « Nouvelle partie ».

## 10. Sources

- **Sujet** : « Projet Java — Sokoban : jeu et résolution algorithmique », PASS Java 2026-2027, Pr. Khadija Slimani. Sections citées : §2 symboles, §3 commandes et règles, §4 victoire, §5 niveaux, §10 contraintes, §11 méthodes attendues, §12 niveau standard, §14 bonus, §15 menu.
- **Cours PASS Java**, Pr. Khadija Slimani :
  - Séances 1-2 : types, opérateurs (dont le ternaire), conditions, `switch` sur `int`, `char` et `String`, boucles, méthodes `static`.
  - Séances 3-4 : tableaux, chaînes de caractères, complexité, recherche séquentielle.
  - Séance 6 : piles (LIFO).
