# PROJET JAVA JEU SOKOBAN

Projet Java ESIEA (PASS Java 2026-2027) : Sokoban en console, avec un solveur automatique (DFS et BFS).
Tout le programme tient dans une seule classe : `src/sokoban/Sokoban.java`. Aucune bibliothèque externe, pas d'`ArrayList` ni de `HashMap` : la pile, la file et l'ensemble des états visités sont écrits à la main avec des tableaux.

## Compiler et lancer

Depuis la racine du dépôt :

```
javac -encoding UTF-8 -d out src/sokoban/Sokoban.java
java -Dstdout.encoding=UTF-8 -cp out sokoban.Sokoban
```

(`-Dstdout.encoding=UTF-8` sert seulement à bien afficher les accents et les murs `██` dans un terminal Windows.)

## Menu

```
1. Nouvelle partie        (niveau en cours, passe au suivant après une victoire)
2. Choisir un niveau      (5 niveaux)
3. Résoudre automatiquement (DFS ou BFS, puis la solution peut être rejouée coup par coup)
4. Comparer DFS / BFS
5. Afficher les règles
6. Quitter
```

En jeu : `z` haut, `s` bas, `q` gauche, `d` droite, `u` annuler le dernier coup, `r` recommencer, `x` revenir au menu. Toute autre saisie affiche « Commande invalide. ».

Symboles : `#` mur (affiché `██`), `@` joueur, `$` caisse, `.` objectif, `*` caisse sur objectif, `+` joueur sur objectif.

## Fonctionnalités

| Exigence du sujet | Où dans le code |
|---|---|
| Affichage de la grille | `afficherGrille()`, `symbol()` |
| Déplacement du joueur, murs | `deplacerJoueur()`, `deplacementValide()` |
| Déplacement des caisses | `peutPousser()`, `deplacerCaisse()` |
| Victoire + compteur | `partieTerminee()`, `nb_mouvements` |
| Plusieurs niveaux | `Niveau_1` à `Niveau_5`, `getNiveau()`, `chargerNiveau()` |
| Validation des entrées | `switch` sur la saisie avec `default` |
| Boucle de jeu | `jouer()` |
| Bonus : recommencer, annuler, blocage | touche `r`, `annulerMouvement()`, `partieBloquee()` |
| DFS récursif | `resoudreDFS()`, `dfs()` |
| BFS avec une file | `resoudreBFS()`, `enfiler()`, `defiler()` |
| États déjà visités | `ajouterEtat()` (arbre binaire de recherche dans des tableaux) |
| Affichage de la solution, comparaison | `afficherResultat()`, `rejouerSolution()`, `comparer()` |

## Résultats de la comparaison

| Niveau | DFS : mouvements | DFS : états explorés | BFS : mouvements | BFS : états explorés |
|---|---|---|---|---|
| 1 | 1 | 2 | 1 | 2 |
| 2 | 67 | 3 630 | 13 | 993 |
| 3 | 69 | 446 | 41 | 778 |
| 4 | 229 | 77 503 | 23 | 615 821 |
| 5 | 142 | 771 571 | 34 | 46 247 |

## Explications du solveur

**Un état** = la case du joueur + les cases des caisses. Chaque case est un seul entier `ligne * largeur + colonne`. Les caisses sont triées (tri par insertion) pour que deux états identiques s'écrivent toujours pareil. Les états sont rangés dans des tableaux (`joueurEtat`, `caissesEtat`, `parent`, `coup`).

**États déjà visités** : un arbre binaire de recherche construit avec deux tableaux `gauche[]` et `droite[]`. Chercher ou ajouter un état coûte en moyenne O(log n) comparaisons au lieu de O(n) pour une recherche séquentielle, ce qui est indispensable pour les 600 000 états du niveau 4.

**Élagage (bonus)** : un coup qui pousse une caisse dans un coin qui n'est pas un objectif est ignoré, car cette caisse ne pourra plus jamais bouger.

### DFS (recherche en profondeur, récursive)

- **Cas de base** : l'état est gagnant (toutes les caisses sur des objectifs), on renvoie vrai.
- **Condition d'arrêt** : la profondeur atteint `PROFONDEUR_MAX` (300 coups), on renvoie faux. Les états déjà visités ne sont jamais rappelés, donc la recherche ne tourne pas en rond.
- **Cas récursif** : pour chacune des 4 directions, si le coup mène à un état nouveau, on empile la touche dans `cheminDFS` et on rappelle `dfs` sur cet état. Si l'appel échoue, on essaie la direction suivante (le coup est « dépilé » en étant écrasé).
- **Avantages** : simple à écrire, peu de mémoire pour le chemin (une pile de la profondeur de la recherche), trouve parfois une solution très vite (niveau 3 : 446 états).
- **Limites** : la solution est souvent très longue (229 coups au lieu de 23 au niveau 4), car le DFS garde la première solution trouvée dans la branche explorée. La limite de profondeur peut faire manquer une solution, et l'ordre des directions change fortement le résultat.

### BFS (recherche en largeur, avec une file)

On enfile l'état de départ, puis tant que la file n'est pas vide on défile un état, on teste la victoire et on enfile tous ses voisins jamais vus. Les états sont donc explorés par distance croissante depuis le départ : le premier état gagnant trouvé donne **la solution la plus courte** en nombre de mouvements. On la reconstruit en remontant le tableau `parent[]`.

### Pourquoi les résultats diffèrent

Le DFS fonce dans une seule branche jusqu'au bout : selon la place de la première solution dans l'arbre, il explore peu d'états (niveaux 3 et 4) ou beaucoup (niveau 5), et sa solution est longue. Le BFS explore tous les états à 1 coup, puis à 2 coups, etc. : il trouve toujours la solution la plus courte, mais doit parfois parcourir énormément d'états avant d'atteindre la profondeur de la solution (niveau 4 : 615 821 états).
