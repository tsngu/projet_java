import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Application {

    private GestionnaireProfesseur gestionnaireProfesseur;
    private GestionnaireEleve gestionnaireEleve;

    private GestionnaireDeExercices gestionnaireExercices = new GestionnaireDeExercices();
    static public Map<String, Langue> langues;

    private Scanner scanner = new Scanner(System.in);

    // Permet de passer d'un code entier (trouvable dans les fichiers textes) à un niveau défini dans l'enum Niveau
    static public Niveau codeToNiveau(int code) {
        Niveau niveau = Niveau.DEBUTANT;
        switch (code) {
            case 1:
                niveau = Niveau.DEBUTANT;
                break;
            case 2:
                niveau = Niveau.INTERMEDIAIRE;
                break;
            case 3:
                niveau = Niveau.AVANCE;
                break;
            default:
                niveau = Niveau.DEBUTANT;
                break;
        }
        return niveau;
    }

    // Toutes les options que peut faire un professeur connecté
    public void menuProfesseur(Professeur professeur) {
        System.out.println("Bonjour " + professeur.prenom + " " + professeur.nom);
        System.out.println("1. Voir ses élèves");
        System.out.println("2. Ajouter un exercice");
        System.out.println("3. Enlever un exercice");
        System.out.println("4. Modifier un exercice");
        System.out.println("5. Afficher vos exercices");
        System.out.println("6. Afficher la liste complète des exercices pour les modifier ou les supprimer");
        System.out.println("7. Se déconnecter");

        boolean continuer = true;
        while (continuer) {
            int choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    gestionnaireEleve.afficheEleves(professeur.getLangueEnseignee());
                    menuProfesseur(professeur);
                    break;
                case 2:
                    Exercice exerciceCree = gestionnaireExercices.creationExercice();
                    gestionnaireExercices.addExercice(exerciceCree);
                    professeur.addExercice(exerciceCree);
                    System.out.println("Exercice ajouté !\n");
                    gestionnaireExercices.updateExo(gestionnaireProfesseur.getProfesseurs());
                    menuProfesseur(professeur);
                    break;
                case 3:
                    System.out.println("Entrez le numéro de l'exercice à supprimer. Les numéros sont à prendre de la liste complète.\nEntrez 0 pour annuler");
                    int numberExerciceASupprimer = scanner.nextInt();
                    if (numberExerciceASupprimer > 0 && numberExerciceASupprimer < gestionnaireExercices.getExercices().size() + 1) {
                        gestionnaireExercices.deleteExercice(numberExerciceASupprimer - 1);
                        gestionnaireExercices.updateExo(gestionnaireProfesseur.getProfesseurs());
                    }
                    menuProfesseur(professeur);
                    break;
                case 4:
                    System.out.println("Entrez le numéro de l'exercice à modifier. Les numéros sont à prendre de la liste complète.\nEntrez 0 pour annuler");
                    int numberExerciceAModifier = scanner.nextInt();
                    if (numberExerciceAModifier > 0 && numberExerciceAModifier < gestionnaireExercices.getExercices().size() + 1) {
                        gestionnaireExercices.menuModificationExercice(gestionnaireExercices.findExercice(numberExerciceAModifier - 1), professeur);
                        gestionnaireExercices.updateExo(gestionnaireProfesseur.getProfesseurs());
                    } else {
                        menuProfesseur(professeur);
                    }
                    break;
                case 5:
                    int cpt1 = 1;
                    for (Exercice exercice : professeur.getListeExercices()) {
                        System.out.print(cpt1 + ". ");
                        exercice.generate();
                        System.out.println();
                        cpt1++;
                    }
                    menuProfesseur(professeur);
                    break;
                case 6:
                    int cpt2 = 1;
                    for (Exercice exercice : gestionnaireExercices.getExercices()){
                        System.out.print(cpt2 + ". ");
                        exercice.generate();
                        System.out.println();
                        cpt2++;
                    }
                    menuProfesseur(professeur);
                    break;
                case 7:
                    System.out.println("Déconnexion");
                    continuer = false;
                    break;
                default:
                    System.out.println("Commande non reconnue");
            }
        }
    }

    // Prompteur pour fournir toutes les réponses à un exercice
    public void resoudreExercice(Eleve eleve, Exercice exercice) {
        exercice.generate();
        System.out.println("Entrez les réponses dans l'ordre séparés d'un ','.");
        Scanner stringScanner = new Scanner(System.in);
        String reponses = stringScanner.nextLine();
        boolean reussite = exercice.resoudre(reponses);
        if (reussite) {
            System.out.println("Exercice résolu");
            eleve.addExerciceResolu(exercice);
        }
        System.out.println("Vous avez obtenu " + exercice.resoudreNbPoints(reponses) + " " + (exercice.getNbReponses() * exercice.getPointsParReponse()));
        gestionnaireEleve.updateEleves(gestionnaireExercices.getExercices());
    }

    // Toutes les options que peut faire un élève connecté
    public void menuEleve(Eleve eleve) {
        System.out.println("Bonjour " + eleve.prenom + " " + eleve.nom);
        System.out.println("1. Statut de son apprentissage");
        System.out.println("2. Voir les exercices disponibles");
        System.out.println("3. Faire un exercice");
        System.out.println("4. Se déconnecter");

        List<Exercice> exercicesDisponibles = gestionnaireExercices.getExercicesSelonLangueNiveau(eleve.getLanguePratiquee(), eleve.getNiveauLangue());
        boolean continuer = true;
        while (continuer) {
            int choix = scanner.nextInt();
            switch (choix) {
                case 1:
                    System.out.println(eleve.toString());
                    break;
                case 2:
                    int cpt = 1;
                    for (Exercice exercice : exercicesDisponibles) {
                        System.out.println(cpt + ". " + exercice.presentation());
                        cpt++;
                    }
                    break;
                case 3:
                    System.out.println("Entrez le numéro de l'exercice à résoudre. Les numéros sont affichés dans l'affichage des exercices. Entrez 0 pour annuler");
                    int choixExercice = scanner.nextInt();
                    if (choixExercice > 0 && choixExercice < exercicesDisponibles.size() + 1) {
                        resoudreExercice(eleve, exercicesDisponibles.get(choixExercice - 1));
                    } else {
                        System.out.println("Numéro d'exercice incorrect");
                    }
                    break;
                case 4:
                    System.out.println("Déconnexion");
                    continuer = false;
                    break;
                default:
                    System.out.println("Commande non reconnue");
            }
        }
        menuPrincipal();
    }

    // Les langues, exercices, professeurs et élèves sont chargés dans un ordre précis selon les dépendances lors du
    // lancement de l'application
    public Application() {
        this.langues = new HashMap<>();
        langues.put("FR", new Langue("FR", "Français"));
        langues.put("EN", new Langue("EN", "Anglais"));
        langues.put("VN", new Langue("VN", "Vietnamien"));
        this.gestionnaireProfesseur = new GestionnaireProfesseur();
        try {
            gestionnaireExercices.chargerExercices("src/bdd/exercice.txt", gestionnaireProfesseur.getProfesseurs());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.gestionnaireEleve = new GestionnaireEleve(gestionnaireExercices.getExercices());
    }

    // Toutes les options que peut faire un utilisateur non connecté
    public void menuPrincipal() {
        System.out.println("Bienvenue !");
        System.out.println("1. Se connecter");
        System.out.println("2. Accéder au gestionnaire d'utilisateurs");
        System.out.println("3. Quitter le programme");

        Scanner stringScanner = new Scanner(System.in);

        boolean continuer = true;
        while (continuer) {
            int choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    // faut faire un systeme de login ici
                    System.out.println("Veuillez indiquer votre login.");
                    String id = stringScanner.next();
                    System.out.println("Login : " + id);
                    System.out.println("Veuillez entrer votre mot de passe.");
                    String mdp = stringScanner.next();
                    System.out.println("MDP : " + mdp);
                    //Cas Professeur
                    if (id.startsWith("P")) {
                        boolean verificationProfesseur = gestionnaireProfesseur.login(id, mdp);
                        if (verificationProfesseur) {
                            menuProfesseur(gestionnaireProfesseur.getProfesseur(id));
                        } else {
                            System.out.println("Compte introuvable. Retour au menu.");
                        }
                    } // Cas Eleve
                    else if (id.startsWith("E")) {
                        boolean verificationEleve = gestionnaireEleve.login(id, mdp);
                        if (verificationEleve) {
                            menuEleve(gestionnaireEleve.getEleve(id));
                        } else {
                            System.out.println("Compte introuvable. Retour au menu.");
                        }
                    } // Mauvais login
                    else {
                        System.out.println("Compte introuvable. Retour au menu.");
                    }
                    break;
                case 2:
                    GestionnaireUtilisateur.gestionnaireUtilisateurs(gestionnaireExercices.getExercices());
                    break;
                case 3:
                    continuer = false;
                    System.out.println("Programme terminé.");
                    break;
                default:
                    System.out.println("Option non reconnue");
            }
            scanner.nextLine();
        }
    }


    public static void main(String[] args) {
        Application application = new Application();
        application.menuPrincipal();

    }
}
