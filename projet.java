import java.io.*;
import java.util.Objects;

public class projet {

    static BufferedReader flux = new BufferedReader(new InputStreamReader(System.in));

    //FONCTION FICHIER
    //permet d'afficher le direct.dat contenant l'historique des joueurs ayant termine une partie
    private static void afficherContenu (String fichierPhysic) throws IOException {
        String ligneLue;
        int i;
        //tmp pour tester l'existence
        File temp = new File(fichierPhysic);

        if (temp.length() == 0)  //ficher n'existe pas
        {
            System.out.println("vide");
        }
        else {
            RandomAccessFile fichier = new RandomAccessFile(fichierPhysic, "r"); //association fichier direct.dat en lecture

            fichier.seek(0);//place en debut de fichier

            i=1;
            ligneLue = fichier.readLine(); //flux.readLine()
            while(ligneLue != null) {
                System.out.println("ligne"+i+" : "+ligneLue);
                ligneLue = fichier.readLine();
                i=i+1;
            }
            fichier.close();
        }
    }

    private static void ajouterLignes(String fichierPhysic, String append) throws IOException {
        String ligneEcrite;

        PrintWriter fichier = new PrintWriter(new FileWriter(fichierPhysic, true)); //association ficher et direct.dat
        ligneEcrite = append;
        fichier.println(ligneEcrite); //System.out.println
        fichier.close();
    }

    //FONCTION POUR REJOUER UNE PARTIE SANS RELANCER LE JEU
    //Permet de reinitialiser le tableau avec la position des bombes
    private static void reconstitution_bombe(int[][] tableau_bombe) {
        for (int x = 0; x < tableau_bombe.length; x++) {
            for (int y = 0; y < tableau_bombe[x].length; y++) {
                tableau_bombe[x][y] = 0;
            }
        }
    }
    //Permet de reinitialiser le tableau de char
    private static void reconstitution_voisin(char[][] tableau_voisin) {
        for (int x = 0; x < tableau_voisin.length; x++) {
            for (int y = 0; y < tableau_voisin[x].length; y++) {
                tableau_voisin[x][y] = 'v';
            }
        }
    }
    //Verifie si une case du tableau existe (renvoie un booleen true si tel est le cas)
    private static boolean existe(int x, int y, char[][] tableau_voisin) {
        boolean b;
        //verifie les limites du tableau
        if (((x >= 0) && (x < tableau_voisin.length) && (y >= 0) && (y < tableau_voisin[x].length))) {
            b = true;
        }
        else {
            b = false;
        }
        return(b);
    }
    //Compte autour d'une case d'un tableau le nombre de bombes (8 possibilites)
    private static int nbBombesVoisines(int x, int y, char[][] tableau_voisin) {
        int nb;
        nb = 0;
        //case en haut a gauche
        if(existe(x-1,y-1, tableau_voisin)) {
            if (tableau_voisin[x-1][y-1] == 'b') {
                nb++;
            }
        }
        //case a gauche
        if(existe(x-1, y,tableau_voisin)) {
            if (tableau_voisin[x-1][y] == 'b') {
                nb++;
            }
        }
        //case en bas a gauche
        if (existe(x-1,y+1, tableau_voisin)) {
            if (tableau_voisin[x-1][y+1] == 'b') {
                nb++;
            }
        }
        //case en haut
        if (existe(x,y-1, tableau_voisin)) {
            if (tableau_voisin[x][y-1] == 'b') {
                nb++;
            }
        }
        //case en bas
        if(existe(x,y+1, tableau_voisin)) {
            if (tableau_voisin[x][y+1] == 'b') {
                nb++;
            }
        }
        //case en haut a droite
        if(existe(x+1,y+1, tableau_voisin)) {
            if (tableau_voisin[x+1][y+1] == 'b') {
                nb++;
            }
        }
        //case a droite
        if(existe(x+1, y,tableau_voisin)) {
            if (tableau_voisin[x+1][y] == 'b') {
                nb++;
            }
        }
        //case en bas a droite
        if (existe(x+1,y-1, tableau_voisin)) {
            if (tableau_voisin[x+1][y-1] == 'b') {
                nb++;
            }
        }
        return(nb);
    }

    /* fonction recursive de parcours de case permettant d'ouvrir toutes les cases n'ayant pas de bombes voisines a partir de l'ouverture
    d'une case vide.
     */
    private static void ouvre(int x, int y, char[][] tableau_voisin, int[][][] tableau_position_casePixel,int[][][] tableau_position_casePixel_String,int[][] tableau_bombe, int[][] lvl, String[] classement, int difficulte, double[] temps_classement) {
        int nbBV;
        boolean etat = true;
        nbBV = nbBombesVoisines(x,y,tableau_voisin);

        if (nbBV == 0 && tableau_voisin[x][y] != 'b') {
            tableau_voisin[x][y] = 'o';
            paint(x, y, tableau_position_casePixel, 170, 170, 170);
            //appel droite
            if (existe(x + 1, y, tableau_voisin)) {
                if (tableau_voisin[x + 1][y] == 'v') {
                    ouvre(x + 1, y, tableau_voisin, tableau_position_casePixel, tableau_position_casePixel_String, tableau_bombe, lvl, classement, difficulte,temps_classement);
                }
            }
            //appel bas
            if (existe(x, y + 1, tableau_voisin)) {
                if (tableau_voisin[x][y + 1] == 'v') {
                    ouvre(x, y + 1, tableau_voisin, tableau_position_casePixel, tableau_position_casePixel_String, tableau_bombe, lvl, classement, difficulte,temps_classement);
                }
            }
            //appel haut
            if (existe(x, y - 1, tableau_voisin)) {
                if (tableau_voisin[x][y - 1] == 'v') {
                    ouvre(x, y - 1, tableau_voisin, tableau_position_casePixel, tableau_position_casePixel_String, tableau_bombe, lvl, classement, difficulte,temps_classement);
                }
            }
            //appel gauche
            if (existe(x - 1, y, tableau_voisin)) {
                if (tableau_voisin[x - 1][y] == 'v') {
                    ouvre(x - 1, y, tableau_voisin, tableau_position_casePixel, tableau_position_casePixel_String, tableau_bombe, lvl, classement, difficulte,temps_classement);
                }
            }

            else {
                tableau_voisin[x][y] = (char) (nbBV + 48);
            }
        }
        //affiche un string sur la case du nombre de bombes voisines
        if (tableau_voisin[x][y] != 'b')
            switch (nbBV) {
                case 1:
                    //paint(x, y, tableau_position_casePixel, 0, 0, 255);
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 0, 0, 255, "1");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
                case 2:
                    //paint(x, y, tableau_position_casePixel, 0, 255, 0);
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 0, 100, 100, "2");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
                case 3:
                    //paint(x, y, tableau_position_casePixel, 210, 0, 0);
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 210, 0, 0, "3");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
                case 4:
                    //paint(x, y, tableau_position_casePixel, 220, 0, 0);
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 230, 0, 0, "4");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
                case 5:
                    //paint(x, y, tableau_position_casePixel, 230, 0, 0);
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 255, 0, 0, "5");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
                case 6:
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 255, 0, 0, "6");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
                case 7:
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 255, 0, 0, "7");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
                case 8:
                    paint(x, y, tableau_position_casePixel, 170, 170, 170);
                    drawString(x, y, tableau_position_casePixel_String, 255, 0, 0, "8");
                    tableau_voisin[x][y] = 'o';
                    EcranGraphique.flush();
                    break;
            }
        //si la case est une bombe
        if (tableau_voisin[x][y] == 'b') {
            //etat = false;
            paint(x, y, tableau_position_casePixel, 0, 0, 0);
            afficher_image("bombe.png",tableau_position_casePixel[x][y][0],tableau_position_casePixel[x][y][1]);
            //fin de la partie -> reinitialisation des tableaux
            reconstitution_bombe(tableau_bombe);
            reconstitution_voisin(tableau_voisin);
            //ecran de fin de partie
            EcranGraphique.setColor(0,0,0);
            EcranGraphique.drawString(10,400, EcranGraphique.COLABA8x13, "Vous avez perdu ! Appuyez sur la fleche gauche pour revenir au menu");
            EcranGraphique.flush();

            //permet au joueur de revenir au menu
            int specialInput = getSpecialKey();
            switch(specialInput) {

                case 37 :
                    try {
                        EcranGraphique.clear();
                        menu(tableau_bombe, lvl, tableau_position_casePixel, classement, tableau_voisin, tableau_position_casePixel_String, difficulte, temps_classement);
                        break;

                    } catch(IOException ie) {
                        ie.printStackTrace();
                    }

            }

            // EcranGraphique.clear();
            //EcranGraphique.setColor(255,255,255);
            // EcranGraphique.drawString(400,400, EcranGraphique.COLABA8x13, "Vous avez perdu");

        }
    }

    //affiche le leaderboard
    private static void leaderboard (String[] classement, double[] temps_classement) {
        int ytext;

        for (int x = 9; x > -1; x--) {
            EcranGraphique.setColor(255,255,255);
            if (classement[x] != "") {
                EcranGraphique.drawString(180, (-20*(10-x)) + (20 * 10)  , EcranGraphique.COLABA8x13, " " + classement[x] + " a gagne en " + temps_classement[x]+ " secondes");
                EcranGraphique.flush();
            }
            //si la liste classement est vide (n'a que des "") n'affiche pas de leaderboard mais previens l'utilisateur que personne n'a joue encore
            if (Objects.equals(classement[9], "")) {
                EcranGraphique.drawString(120,50,EcranGraphique.COLABA8x13, "Aucun joueur n'a termine de partie");
                EcranGraphique.flush();
            }
        }
    }

    //affichage d'un png sur l'ecran aux coordonnees voulues a partir d'un fichier
    private static void afficher_image (String nom_img, int x, int y) {

        int [][] img = EcranGraphique.loadPNGFile(nom_img);;

        if ( img != null ) {

            EcranGraphique.drawImage(x - 1, y - 1, img);
            EcranGraphique.flush();
        }
    }

    //affichage d'un png sur l'ecran aux coordonnees voulues a partir d'un url
    private static void afficher_image_url (String url_img) {

        int [][] img = EcranGraphique.downloadPNGFile(url_img);

        if ( img != null ) {

            EcranGraphique.drawImage(0,0,img);
            EcranGraphique.flush(); }

    }

    //Fonction principale qui affiche le menu et toutes les fonctionalites de maniere arborescente.
    private static void menu(int [][] tableau_bombe, int[][] lvl, int[][][] tableau_position_casePixel, String[] classement, char[][] tableau_voisin, int[][][] tableau_position_casePixel_String, int difficulte, double[]temps_classement) throws IOException {

        boolean etat = true;
        int specialInput;
        int positionFleche = 230;
        String joueur = "";
        //affichage du menu
	    
	//ecran de saisie du nom du joueur
	EcranGraphique.clear();
	EcranGraphique.setColor(255,255,255);
	//affichage des commandes
	EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"Commandes :");
	EcranGraphique.drawString(200,155,EcranGraphique.COLABA8x13 ,"fleche droite -> valider choix");
	EcranGraphique.drawString(200,170,EcranGraphique.COLABA8x13 ,"fleche haut -> choix precedant");
	EcranGraphique.drawString(200,185,EcranGraphique.COLABA8x13 ,"fleche bas -> choix suivant");	    
	    
        EcranGraphique.setColor(255,255,255);
        EcranGraphique.drawString(285,  100 , EcranGraphique.SYMBOL8x13, "DEMINEUR");

        EcranGraphique.setColor(211,211,211);
        EcranGraphique.fillRect(213,213, 213, 71);

        EcranGraphique.setColor(0,0,0);
        EcranGraphique.drawString(290,  255 , EcranGraphique.SYMBOL8x13, "JOUER");

        EcranGraphique.setColor(211,211,211);
        EcranGraphique.fillRect(213,310, 213, 71);

        EcranGraphique.setColor(0,0,0);
        EcranGraphique.drawString(285,  352 , EcranGraphique.SYMBOL8x13, "SETTINGS");

        EcranGraphique.setColor(211,211,211);
        EcranGraphique.fillRect(213,407, 213, 71);

        EcranGraphique.setColor(0,0,0);
        EcranGraphique.drawString(275,  449 , EcranGraphique.SYMBOL8x13, "LEADERBOARD");

        EcranGraphique.setColor(211,211,211);
        EcranGraphique.fillRect(213,407 + 97, 213, 71);

        EcranGraphique.setColor(0,0,0);
        EcranGraphique.drawString(280,  449 + 97, EcranGraphique.SYMBOL8x13, "QUITTER");

        afficher_image("fleche.png",170,230);

        EcranGraphique.flush();

        while(etat) {

            specialInput = getSpecialKey();

            switch(specialInput) {
                //le joueur presse la fleche du haut
                case(38) :
                    EcranGraphique.clear();

                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(285,  100 , EcranGraphique.SYMBOL8x13, "DEMINEUR");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,213, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(290,  255 , EcranGraphique.SYMBOL8x13, "JOUER");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,310, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(285,  352 , EcranGraphique.SYMBOL8x13, "SETTINGS");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,407, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(275,  449 , EcranGraphique.SYMBOL8x13, "LEADERBOARD");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,407 + 97, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(280,  449 + 97, EcranGraphique.SYMBOL8x13, "QUITTER");

                    EcranGraphique.flush();

                    positionFleche -= 97;

                    //suivant la position de la fleche elle peut revenir en haut apres la derniere case (quitter)
                    if (positionFleche == 133) {
                        positionFleche = 521;
                        afficher_image("fleche.png",170, positionFleche);
                    }

                    else {
                        afficher_image("fleche.png",170, positionFleche);
                    }
                    break;


                //l'utilisateur presse la fleche du bas
                case(40) :
                    EcranGraphique.clear();

                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(285,  100 , EcranGraphique.SYMBOL8x13, "DEMINEUR");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,213, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(290,  255 , EcranGraphique.SYMBOL8x13, "JOUER");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,310, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(285,  352 , EcranGraphique.SYMBOL8x13, "SETTINGS");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,407, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(275,  449 , EcranGraphique.SYMBOL8x13, "LEADERBOARD");

                    EcranGraphique.setColor(211,211,211);
                    EcranGraphique.fillRect(213,407 + 97, 213, 71);

                    EcranGraphique.setColor(0,0,0);
                    EcranGraphique.drawString(280,  449 + 97, EcranGraphique.SYMBOL8x13, "QUITTER");

                    EcranGraphique.flush();

                    positionFleche += 97;

                    //suivant la position de la fleche elle peut revenir en bas apres la derniere case du haut (jouer)
                    if (positionFleche == 521 + 97) {
                        positionFleche = 230;
                        afficher_image("fleche.png",170, positionFleche);
                    }

                    else {
                        afficher_image("fleche.png",170, positionFleche);
                    }
                    break;

                case 39:
                    //l'utilisateur presse la fleche droite au coordonnes de la case JOUER
                    //JOUER
                    if (positionFleche == 230) {
                        switch (difficulte) {
                            //le joueur a plusieurs choix de difficulte -> utilisation de troix fonctions pour trois difficultes
                            case 1 :
                                randomisation_facile(tableau_bombe, tableau_voisin);
                                break;
                            case 2 :
                                randomisation_normal(tableau_bombe, tableau_voisin);
                                break;
                            case 3 :
                                randomisation_difficile(tableau_bombe, tableau_voisin);
                                break;
                        }

                        //ecran de saisie du nom du joueur
                        EcranGraphique.clear();
                        EcranGraphique.setColor(255,255,255);
                        //affichage des commandes
                        EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                        EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                        EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                        EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom (en minuscule)");
                        EcranGraphique.flush();

                        //le joueur peut saisir son nom
                        joueur = saisie();
                        //joueur = flux.readLine();

                        EcranGraphique.clear();

                        render(lvl, tableau_position_casePixel, tableau_position_casePixel_String);
                        //debut de l'acquisition du temps de la partie
                        double tempsDepart = System.currentTimeMillis();

                        while(etat) {

                            jeu(position_souris(),tableau_voisin, tableau_position_casePixel, etat, tableau_position_casePixel_String, tableau_bombe, lvl, classement, difficulte, temps_classement);
                            //tant que le nombre de voisin est non nul la partie continue
                            int nombre_v = 0;
                            for (int x = 0; x < tableau_voisin.length; x++) {
                                for (int y = 0; y < tableau_voisin[x].length; y++) {
                                    if (tableau_voisin[x][y] == 'v') {
                                        nombre_v++;
                                    }
                                }
                            }
                            if (nombre_v == 0) {
                                etat = false; // la partie s'arrete
                            }
                        }
                        //fin de la partie reinitialisation des tableaux
                        reconstitution_bombe(tableau_bombe);
                        reconstitution_voisin(tableau_voisin);
                        EcranGraphique.clear();
                        EcranGraphique.setColor(255,255,255);
                        //fin de l'acquisition du temps de jeu
                        double tempsFin = System.currentTimeMillis();
                        //le joueur a gagne, enregistrement de son nom dans une liste classement et de son temps de jeu dans une liste temps_classement
                        ajouter_liste(temps_classement,(duree(tempsDepart,tempsFin)));
                        ajouter_liste_string(classement, joueur);
                        //les listes classement et temps_classement sont triees dans l'ordre de rapidite des joueurs. Du plus rapide au moisn rapide, avec le plus lent en position 10.
                        tri_selection(temps_classement,classement);

                        String fichierPhysic;
                        String append;
                        fichierPhysic = "direct.dat";
                        append = joueur + " a gagne en " + duree(tempsDepart,tempsFin) + " secondes";
                        //enregistrement dans le fichier direct.dat (creation si il n'existe pas)
                        ajouterLignes(fichierPhysic,append);

                        EcranGraphique.drawString(10,400, EcranGraphique.COLABA8x13, "Vous avez gagne en "+ duree(tempsDepart,tempsFin) + " secondes ! Appuyez sur la fleche gauche");
                        EcranGraphique.flush();

                        //revenir au menu apres la fin de la partie
                        specialInput = getSpecialKey();
                        switch(specialInput) {

                            case 37 :
                                EcranGraphique.clear();
                                menu(tableau_bombe, lvl, tableau_position_casePixel, classement, tableau_voisin, tableau_position_casePixel_String, difficulte,temps_classement);
                                break;
                        }
                        menu(tableau_bombe, lvl, tableau_position_casePixel, classement, tableau_voisin, tableau_position_casePixel_String, difficulte,temps_classement);
                    }
                    //SETTINGS
                    if (positionFleche == 327) {

                        etat = false;
                        boolean etatSettings = true;
                        positionFleche = 230;

                        EcranGraphique.clear();
                        EcranGraphique.setColor(255,255,255);
                        EcranGraphique.drawString(285,  100 , EcranGraphique.SYMBOL8x13, "SETTINGS");

                        EcranGraphique.setColor(211,211,211);
                        EcranGraphique.fillRect(213,213, 213, 71);

                        EcranGraphique.setColor(0,0,0);
                        EcranGraphique.drawString(290,  255 , EcranGraphique.SYMBOL8x13, "FACILE");

                        EcranGraphique.setColor(211,211,211);
                        EcranGraphique.fillRect(213,310, 213, 71);

                        EcranGraphique.setColor(0,0,0);
                        EcranGraphique.drawString(285,  352 , EcranGraphique.SYMBOL8x13, "NORMAL");

                        EcranGraphique.setColor(211,211,211);
                        EcranGraphique.fillRect(213,407, 213, 71);

                        EcranGraphique.setColor(0,0,0);
                        EcranGraphique.drawString(275,  449 , EcranGraphique.SYMBOL8x13, "DIFFICILE");

                        EcranGraphique.setColor(211,211,211);
                        EcranGraphique.fillRect(213,407 + 97, 213, 71);

                        EcranGraphique.setColor(0,0,0);
                        EcranGraphique.drawString(280,  449 + 97, EcranGraphique.SYMBOL8x13, "MENU");

                        afficher_image("fleche.png",170,230);

                        EcranGraphique.flush();



                        while(etatSettings) {

                            specialInput = getSpecialKey();
                            switch (specialInput) {

                                case (37):
                                    etatSettings = false;
                                    EcranGraphique.clear();
                                    menu(tableau_bombe, lvl, tableau_position_casePixel, classement, tableau_voisin, tableau_position_casePixel_String, difficulte,temps_classement);
                                    break;

                                case (38)://fleche haut

                                    EcranGraphique.clear();

                                    EcranGraphique.clear();
                                    EcranGraphique.setColor(255, 255, 255);
                                    EcranGraphique.drawString(285, 100, EcranGraphique.SYMBOL8x13, "SETTINGS");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 213, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(290, 255, EcranGraphique.SYMBOL8x13, "FACILE");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 310, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(285, 352, EcranGraphique.SYMBOL8x13, "NORMAL");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 407, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(275, 449, EcranGraphique.SYMBOL8x13, "DIFFICILE");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 407 + 97, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(280, 449 + 97, EcranGraphique.SYMBOL8x13, "MENU");

                                    EcranGraphique.flush();

                                    positionFleche -= 97;

                                    if (positionFleche == 133) {
                                        positionFleche = 521; //   -> 424
                                        afficher_image("fleche.png", 170, positionFleche);
                                    } else {
                                        afficher_image("fleche.png", 170, positionFleche);
                                    }
                                    break;


                                case (40): //Fleche bas
                                    EcranGraphique.clear();

                                    EcranGraphique.clear();
                                    EcranGraphique.setColor(255, 255, 255);
                                    EcranGraphique.drawString(285, 100, EcranGraphique.SYMBOL8x13, "SETTINGS");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 213, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(290, 255, EcranGraphique.SYMBOL8x13, "FACILE");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 310, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(285, 352, EcranGraphique.SYMBOL8x13, "NORMAL");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 407, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(275, 449, EcranGraphique.SYMBOL8x13, "DIFFICILE");

                                    EcranGraphique.setColor(211, 211, 211);
                                    EcranGraphique.fillRect(213, 407 + 97, 213, 71);

                                    EcranGraphique.setColor(0, 0, 0);
                                    EcranGraphique.drawString(280, 449 + 97, EcranGraphique.SYMBOL8x13, "MENU");

                                    EcranGraphique.flush();

                                    positionFleche += 97;

                                    if (positionFleche == 521 + 97) {
                                        positionFleche = 230;
                                        afficher_image("fleche.png", 170, positionFleche);
                                    } else {
                                        afficher_image("fleche.png", 170, positionFleche);
                                    }
                                    break;
                                // difficulte = 1 ---> randomisation tableau facile, 2 ---> randomisation tableau normale, 3 ---> randomisation tableau difficile
                                case (39):

                                    //FACILE
                                    if (positionFleche == 230) {
                                        EcranGraphique.clear();
                                        EcranGraphique.setColor(255, 255, 255);
                                        EcranGraphique.drawString(20, 300, EcranGraphique.COLABA8x13, " difficulte facile, moins de bombes ! Appuyez sur fleche gauche");
                                        EcranGraphique.flush();
                                        difficulte = 1;

                                    }

                                    //NORMAL
                                    if (positionFleche == 327) {
                                        EcranGraphique.clear();
                                        EcranGraphique.setColor(255, 255, 255);
                                        EcranGraphique.drawString(20, 300, EcranGraphique.COLABA8x13,  "difficulte normal ! Appuyez sur fleche gauche");
                                        EcranGraphique.flush();
                                        difficulte = 2;
                                    }

                                    //DIFFICILE
                                    if (positionFleche == 424) {
                                        EcranGraphique.clear();
                                        EcranGraphique.setColor(255, 255, 255);
                                        EcranGraphique.drawString(20, 300, EcranGraphique.COLABA8x13, "Difficile, il y aura plus de bombes ! Appuyez sur fleche gauche");
                                        EcranGraphique.flush();
                                        difficulte = 3;
                                    }

                                    //MENU
                                    if (positionFleche == 521) {
                                        etatSettings = false;
                                        EcranGraphique.clear();
                                        EcranGraphique.flush();
                                        menu(tableau_bombe, lvl, tableau_position_casePixel, classement, tableau_voisin, tableau_position_casePixel_String, difficulte,temps_classement);
                                    }
                                    break;

                            }
                        }
                    }
                    //LEADERBOARD
                    if (positionFleche == 424) {
                        etat = false;
                        EcranGraphique.clear();
                        leaderboard(classement, temps_classement);

                        specialInput = getSpecialKey();
                        switch(specialInput) {

                            case 37 : //fleche gauche
                                EcranGraphique.clear();
                                menu(tableau_bombe, lvl, tableau_position_casePixel, classement, tableau_voisin, tableau_position_casePixel_String, difficulte,temps_classement);
                                break;
                        }

                    }
                    //QUITTER
                    if (positionFleche == 521) {
                        etat = false;
                        EcranGraphique.clear();
                        EcranGraphique.setColor(255,255,255);
                        EcranGraphique.drawString(250,300, EcranGraphique.COLABA8x13,"Merci d'avoir joue");
                        EcranGraphique.flush();
                        EcranGraphique.wait(1000);
                        EcranGraphique.exit();

                    }
                    break;
            }
        }

        /* 37 gauche, 39 droite,  38 haut, 40 bas */

    }
    //permet d'afficher un tableau de int (utile pendant les demarches de recherche)
    private static void affiche_tableau (int[][] tableau) {
        for (int x = 0; x < tableau.length; x++) {
            for (int y = 0; y < tableau[x].length; y++) {
                int value = tableau[x][y];

                if (y < tableau.length - 1) {
                    System.out.print(value + ", ");
                }

                else {
                    System.out.println(value);
                }

            }
        }
    }
    //permet d'afficher un tableau de char (utile pendant les demarches de recherche)
    private static void affiche_tableau_char (char[][] tableau) {
        for (int x = 0; x < tableau.length; x++) {
            for (int y = 0; y < tableau[x].length; y++) {

                char value = tableau[y][x];

                if (y < tableau.length - 1) {
                    System.out.print(value + ", ");
                }

                else {
                    System.out.println(value);
                }
            }
        }
    }
    //permet d'afficher un tableau de String (utile pendant les demarches de recherche)
    private static void affiche_tableau_string (String[][] tableau) {
        for (int x = 0; x < tableau.length; x++) {
            for (int y = 0; y < tableau[x].length; y++) {

                String value = tableau[y][x];

                if (y < tableau.length - 1) {
                    System.out.print(value + ", ");
                }

                else {
                    System.out.println(value);
                }

            }
        }
    }

    //bombe -> 1 , case vide -> 0
    //ajoute des bombes de facon aleatoire, ici probabilite de 2/45 d'avoir une bombe

    private static int[][] randomisation_facile (int[][] tableau_bombe, char[][] tableau_voisin) {
        int value;
        for (int x = 0; x < tableau_bombe.length; x++) {
            for (int y = 0; y < tableau_bombe[x].length; y++) {
                value = (int) (Math.random() * 45);

                if (value > 2) {
                    value = 0; //vide
                    tableau_bombe[x][y] = value;
                }

                else {
                    value = 1;//bombe
                    tableau_bombe[x][y] = value;
                    tableau_voisin[x][y] = 'b';
                }
            }
        }
        return tableau_bombe;
    }
    //probabilite de 2/35
    private static int[][] randomisation_normal(int[][] tableau_bombe, char[][] tableau_voisin) {
        int value;
        for (int x = 0; x < tableau_bombe.length; x++) {
            for (int y = 0; y < tableau_bombe[x].length; y++) {
                value = (int) (Math.random() * 35);

                if (value > 2) {
                    value = 0;
                    tableau_bombe[x][y] = value;
                }

                else {
                    value = 1;
                    tableau_bombe[x][y] = value;
                    tableau_voisin[x][y] = 'b';
                }
            }
        }
        return tableau_bombe;
    }
    //probabilite de 2/25
    private static int[][] randomisation_difficile (int[][] tableau_bombe, char[][] tableau_voisin) {
        int value;
        for (int x = 0; x < tableau_bombe.length; x++) {
            for (int y = 0; y < tableau_bombe[x].length; y++) {
                value = (int) (Math.random() * 25 );

                if (value > 2) {
                    value = 0;
                    tableau_bombe[x][y] = value;
                }

                else {
                    value = 1;
                    tableau_bombe[x][y] = value;
                    tableau_voisin[x][y] = 'b';
                }
            }
        }
        return tableau_bombe;
    }
    //fonction qui affiche la fresque de carre gris
    private static void render(int[][] lvl, int[][][] tableau_position_casePixel, int[][][] tableau_position_casePixel_String) {

        EcranGraphique.setColor(100, 100, 100);
        EcranGraphique.fillRect(0 , 0, 640, 640);

        for (int y = 0; y < lvl.length; y++) {
            for ( int x = 0; x < lvl[y].length; x++) {

                //int id = lvl[y][x];
                int Xpixel;
                int Ypixel;
                int XpixelString;
                int YpixelString;

				/* switch (id) {

					case 0 :
						Xpixel = x*32+2;
						Ypixel = y*32+2;
						EcranGraphique.setColor(211 ,211 , 211);
						EcranGraphique.fillRect(Xpixel ,Ypixel, 28, 28);
						tableau_position_casePixel[x][y][0] = Xpixel;
						tableau_position_casePixel[x][y][1] = Ypixel;
						EcranGraphique.flush();
						break;

					case 1 :
						EcranGraphique.setColor(255 ,0 , 0);
						Xpixel = x*32+2;
						Ypixel = y*32+2;
						EcranGraphique.fillRect(Xpixel , Ypixel, 28, 28);
						tableau_position_casePixel[x][y][0] = Xpixel;
						tableau_position_casePixel[x][y][1] = Ypixel;
						EcranGraphique.flush();
						break;
				}

				 */

                Xpixel = x*32+2;
                Ypixel = y*32+2;

                XpixelString = x*32+10;
                YpixelString = y*32+20;

                EcranGraphique.setColor(211 ,211 , 211);
                EcranGraphique.fillRect(Xpixel ,Ypixel, 28, 28);
                //pour chaque indice des carres en affecte les x,y coordonnees en pixel pour plus tard afficher des images ou peindres a la bonne position
                tableau_position_casePixel[x][y][0] = Xpixel;
                tableau_position_casePixel[x][y][1] = Ypixel;
                tableau_position_casePixel_String[x][y][0] = XpixelString;
                tableau_position_casePixel_String[x][y][1]	= YpixelString;
                //EcranGraphique.flush();
            }
        }
        EcranGraphique.flush();
    }


    //PEINDRE
    private static void peindre_caseV2(int[] position_souris, char[][] tableau_voisin, int[][][] tableau_position_casePixel,int[][][] tableau_position_casePixel_String, int[][] tableau_bombe, int[][] lvl, String[] classement, int difficulte, double[] temps_classement) {

        //position_souris[] est renvoyee par position_souris c'est une liste qui indique le x,y a l'instant donne du curseur quand on presse une touche designee
        int x = position_souris[0];
        int y = position_souris[1];
        //ouvre va permettre le demarrage de l'algorithme recurcif qui se propage : cf seed
        ouvre(x ,y ,tableau_voisin, tableau_position_casePixel, tableau_position_casePixel_String, tableau_bombe, lvl, classement, difficulte, temps_classement);
        //la fonction peint differemment suivant ce qu'il y a dans la cases : bombe -> affiche une image, vide -> peint en gris
        switch (tableau_voisin[y][x]) {

            case 0,'o' :

                EcranGraphique.setColor(170, 170, 170);
                EcranGraphique.fillRect(tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1],28, 28);
                EcranGraphique.flush();
                break;

            case 1 :

                EcranGraphique.setColor(255, 0, 0);
                afficher_image("bombe.png", tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1]);
                /* EcranGraphique.fillRect(tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1],28, 28); */

                EcranGraphique.flush();
                break;
        }

    }
    //premiere version de peindre_case
    private static void peindre_case(int[] position_souris, int[][] position_bombe, int[][][] tableau_position_casePixel) {

        int x = position_souris[0];
        int y = position_souris[1];

        switch (position_bombe[y][x]) {

            case 0 :

                EcranGraphique.setColor(170, 170, 170);
                EcranGraphique.fillRect(tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1],28, 28);
                EcranGraphique.flush();
                break;

            case 1 :

                EcranGraphique.setColor(255, 0, 0);
                afficher_image("bombe.png", tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1]);
                /* EcranGraphique.fillRect(tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1],28, 28); */

                EcranGraphique.flush();
                break;
        }

    }

    //FONCTION JEU
    private static void jeu(int[] position_souris, char[][] tableau_voisin, int[][][] tableau_position_casePixel, boolean etat, int[][][] tableau_position_casePixel_String, int[][] tableau_bombe, int [][] lvl, String[] classement, int difficulte, double[] temps_classement) {

        int x = position_souris[0];
        int y = position_souris[1];

        if (position_souris[2] == 0) { // 0 -> ouvre
            ouvre(x ,y ,tableau_voisin, tableau_position_casePixel, tableau_position_casePixel_String, tableau_bombe, lvl, classement, difficulte, temps_classement);
        }

        if (position_souris[2] == 1) { // 1 -> affiche un drapeau
            afficher_image("drapeau.png", tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1]);
        }
    }

    //permet d'afficher un rectangle au (x,y) donnes
    private static void paint(int x, int y, int[][][] tableau_position_casePixel, int r, int g, int b) {

        EcranGraphique.setColor(r, g, b);
        EcranGraphique.fillRect(tableau_position_casePixel[x][y][0], tableau_position_casePixel[x][y][1],28, 28);
        EcranGraphique.flush();
    }

    //permet d'afficher un string au (x,y) donne
    private static void drawString(int x, int y, int[][][] tableau_position_casePixel_String, int r, int g, int b, String string) {
        EcranGraphique.setColor(r, g, b);
        EcranGraphique.drawString(tableau_position_casePixel_String[x][y][0],  tableau_position_casePixel_String[x][y][1] , EcranGraphique.SYMBOL8x13, string);
    }



    //POSITION SOURIS
    private static int[] position_souris() {

        boolean etat = true;
        int[] liste = {0, 0, 0};
        int position_souris_x;
        int position_souris_y;

        //liste[2] == 0 -> ouvrir
        //liste[2] == 1 -> draoeau

        while(etat) {

            switch (getKey()) {

                //on a essaye de mettre getMouse() mais EcranGraphique.getMouseState ne marche pas sans print...
                case 97, 65: //presse a A

                    position_souris_x = EcranGraphique.getMouseX();
                    position_souris_y = EcranGraphique.getMouseY();
                    liste[0] = (position_souris_x / 32);
                    liste[1] = (position_souris_y / 32) ;
                    etat = false;
                    break;


                case 122, 90: //presse z Z

                    position_souris_x = EcranGraphique.getMouseX();
                    position_souris_y = EcranGraphique.getMouseY();
                    liste[0] = (position_souris_x / 32);
                    liste[1] = (position_souris_y / 32) ;
                    liste[2] = 1;
                    etat = false;
                    break;


            }
        }

        return(liste);

    }
    //fonction utile pendant les recherches
    //AFFICHE POSITION SOURIS
    private static void affiche_position_souris() {

        boolean etat = true;
        int[] liste = {0, 0};
        int position_souris_x;
        int position_souris_y;

        while(etat) {

            switch (getKey()) {

                case 97, 65: //press a A

                    position_souris_x = EcranGraphique.getMouseX();
                    position_souris_y = EcranGraphique.getMouseY();
                    liste[0] = (position_souris_x / 32);
                    liste[1] = (position_souris_y / 32);
                    etat = false;
                    break;
            }
        }

        System.out.println(liste[0] + "  / " + liste[1]);

    }

    private static int getMouse() {

        int value = -1;
        boolean etat = true;
        int keyMouse = -1;

        while(etat) {
            System.out.println("");
            keyMouse = EcranGraphique.getMouseState();
            //2 --> click relache
            if (keyMouse == 2) {
                etat = false;
            }
        }

        if (EcranGraphique.getMouseButton() == 1) { // -> click gauche

            value = 97; //pour ouvrir
        }

        if (EcranGraphique.getMouseButton() == 3) { // -> 3 click droit

            value = 122; // pour ajouter un drapeau
        }

        return(value);
    }
    //fonction utile pendant les recherches
    private static void affiche_getKey() {

        boolean etat = true;
        int key = 0;
        while(etat) {

            key = EcranGraphique.getKey();
            if(key!=0)
            {
                etat = false;
            }
        }

        System.out.println(key);
    }
    //fonction utile pendant les recherches
    private static void affiche_getSpecialKey() {

        boolean etat = true;
        int key = 0;
        while(etat) {

            key = EcranGraphique.getSpecialKey();
            if(key!=0)
            {
                etat = false;
            }
        }

        System.out.println(key);
    }
    //permet l'acquisition d'un int correspondant a une touche du clavier
    private static int getKey() {

        boolean etat = true;
        int key = 0;
        while(etat) {

            key = EcranGraphique.getKey();
            if(key!=0)
            {
                etat = false;
            }
        }

        return(key);
    }
    //permet l'acquisition d'un int correspondant a une touche speciale du clavier
    private static int getSpecialKey() {

        boolean etat = true;
        int key = 0;
        while(etat) {

            key = EcranGraphique.getSpecialKey();
            if(key!=0)
            {
                etat = false;
            }
        }

        return(key);
    }

    private static void  randomisation_tableau_bombe(int[][] tableau_bombe) {
        int value;
        for (int x = 0; x < tableau_bombe.length; x++) {
            for (int y = 0; y < tableau_bombe[x].length; y++) {

                value = (int) Math.random() * 5;
                tableau_bombe[x][y] = value;

                if (y == tableau_bombe[x].length - 1) {
                    System.out.println(value + "");

                }

                else {
                    System.out.print(value + "");
                }
            }
        }
    }
    //Ajoute a une liste de string "" un element
    private static void ajouter_liste_string(String[] classement, String joueur) {
        for (int k = 0; k < classement.length; k++) {
            if (classement[k] == "") {
                classement[k] = joueur;
                break;
            }
        }
    }
    //permet de visualiser une liste de string contenant des "" et des string plus longs
    private static void afficher_liste_string(String[] liste) {
        for (int k = 0; k < liste.length; k++) {
            if (liste[k] != "") {
                System.out.print(" " + liste[k]);
            }
        }
    }
    //Ajoute a une liste de double un element different de 0
    private static void ajouter_liste(double[] classement, double temps) {
        for (int k = 0; k < classement.length; k++) {
            if (classement[k] == 0) {
                classement[k] = temps;
                break;
            }
        }
    }
    //permet de visualiser une liste de double contenant des 0 et d'autres nombres
    private static void afficher_liste(double[] liste) {
        for (int k = 0; k < liste.length; k++) {
            if (liste[k] != 0) {
                System.out.print(" " + liste[k]);
            }
        }
    }
    //permet de trier une liste de double de maniere decroissante (sert a leaderboard pour classer les joueurs)
    private static void tri_selection(double[] temps_classement, String[] classement) {
        for (int i = 0; i < temps_classement.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < temps_classement.length; j++) {
                if (temps_classement[j] < temps_classement[index]) {
                    index = j;
                }
            }
            String minString = classement[index];
            double min = temps_classement[index];
            classement[index] = classement[i];
            temps_classement[index] = temps_classement[i];
            classement[i] = minString;
            temps_classement[i] = min;
        }
    }
    //permet calculer le temps entre deux System.currentTimeMillis() en seconde
    private static double duree(double tempsDepart, double tempsFin) {
        double duree =  (tempsFin - tempsDepart) / 1000;
        return(duree);
    }

    //permet d'afficher un texte taper au clavier

    private static String saisie() {
        boolean etat = true;
        String lettre = "";
        int compteur = 0;
        String [] lettres = {"","","","","","","","","","","","","","","","","","","","","","","",""};
        while (etat) {
            switch (getKey()) {
                case 97 :
                    compteur += 1;
                    lettre += "A";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 98 :
                    compteur += 1;
                    lettre += "B";
                    EcranGraphique.setColor(255,255,255);

                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 99 :
                    compteur += 1;
                    lettre += "C";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 100 :
                    compteur += 1;
                    lettre += "D";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 101 :
                    compteur += 1;
                    lettre += "E";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 102 :
                    compteur += 1;
                    lettre += "F";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 103 :
                    compteur += 1;
                    lettre += "G";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 104 :
                    compteur += 1;
                    lettre += "H";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 105 :
                    compteur += 1;
                    lettre += "I";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 106 :
                    compteur += 1;
                    lettre += "J";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 107 :
                    compteur += 1;
                    lettre += "K";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 108 :
                    compteur += 1;
                    lettre += "L";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 109 :
                    compteur += 1;
                    lettre += "M";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 110 :
                    compteur += 1;
                    lettre += "N";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 111 :
                    compteur += 1;
                    lettre += "O";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 112 :
                    compteur += 1;
                    lettre += "P";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 113 :
                    compteur += 1;
                    lettre += "Q";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 114 :
                    compteur += 1;
                    lettre += "R";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 115 :
                    compteur += 1;
                    lettre += "S";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 116 :
                    compteur += 1;
                    lettre += "T";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 117 :
                    compteur += 1;
                    lettre += "U";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 118 :
                    compteur += 1;
                    lettre += "V";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 119 :
                    compteur += 1;
                    lettre += "W";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 120 :
                    compteur += 1;
                    lettre += "X";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 121 :
                    compteur += 1;
                    lettre += "Y";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 122 :
                    compteur += 1;
                    lettre += "Z";
                    EcranGraphique.setColor(255,255,255);
                    EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                    EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                    EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                    EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                    EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                    EcranGraphique.flush();
                    break;
                case 10 :
                    etat = false;
                    break;
                case 8 :
                    if (compteur == 0){
                        lettre = lettres[compteur];
                        EcranGraphique.clear();
                        EcranGraphique.setColor(255,255,255);
                        EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                        EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                        EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                        EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                        EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                        EcranGraphique.flush();
                    }
                    else {
                        compteur = compteur - 1;
                        lettre = lettres[compteur];
                        EcranGraphique.clear();
                        EcranGraphique.setColor(255,255,255);
                        EcranGraphique.drawString(200,250, EcranGraphique.COLABA8x13, lettre);
                        EcranGraphique.drawString(200,100,EcranGraphique.COLABA8x13 ,"Commandes :");
                        EcranGraphique.drawString(200,140,EcranGraphique.COLABA8x13 ,"A -> ouvrir case");
                        EcranGraphique.drawString(200,180,EcranGraphique.COLABA8x13 ,"Z -> deposer un drapeau");
                        EcranGraphique.drawString(200,400,EcranGraphique.COLABA8x13 ,"Veuillez saisir un nom");
                        EcranGraphique.flush();
                    }
                    break;
            }
            lettres[compteur] = lettre;

        }
        EcranGraphique.clear();
        EcranGraphique.flush();
        return(lettre);
    }

    //main
    public static void main (String[] arguments) throws IOException {

        boolean etat = true;
        int position_souris_x;
        int position_souris_y;
        int gamestate = -1;

        int difficulte = 2;

        String[] classement = { "", "", "", "", "", "", "", "", "" ,""};

        double[] temps_classement = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

        int[][] lvl = {

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },

        };

        int[][] tableau_bombe= {

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },

        };

        char[][] tableau_voisin = {

                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},
                {'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v', 'v',},

        };

        int[][][] tableau_position_casePixel = {

                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },

        };

        int[][][] tableau_position_casePixel_String = {

                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },
                {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, },

        };

        //initialisation de la fenetre graphique
        EcranGraphique.init(500,500, 700,740,640,640,"DEMINEUR");
        EcranGraphique.setWindowResizable(false);
        //lancement la fonction menu
        menu(tableau_bombe, lvl, tableau_position_casePixel, classement, tableau_voisin, tableau_position_casePixel_String, difficulte, temps_classement);


    }

    
    //ajout 16/11/23
    //fermeture du programme bouton quitter
    //Activation/desactivation de la possibilite de changer la taille de la fenetre par l'utilisateur.
}
