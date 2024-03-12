import java.util.List;
import java.util.Scanner;

// Permet de gérer les utilisateurs
public abstract class GestionnaireUtilisateur {

    private static Scanner scanner = new Scanner(System.in);

    // Permet d'accéder aux gestionnaires.
    public static void gestionnaireUtilisateurs(List<Exercice> exercices) {
        GestionnaireProfesseur gestionnaireProfesseur = new GestionnaireProfesseur();
        GestionnaireEleve gestionnaireEleve = new GestionnaireEleve(exercices);

        boolean continuer = true;

        while (continuer) {
            System.out.println("1. Créer un professeur");
            System.out.println("2. Afficher les professeurs");
            System.out.println("3. Créer un élève");
            System.out.println("4. Afficher les élèves");
            System.out.println("5. Quitter");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    gestionnaireProfesseur.creerProfesseurInput();
                    break;
                case 2:
                    gestionnaireProfesseur.afficheProfesseurs();
                    break;
                case 3:
                    gestionnaireEleve.creerEleveInput();
                    break;
                case 4:
                    gestionnaireEleve.afficheEleves();
                    break;
                case 5:
                    continuer = false;
                    System.out.println("Programme terminé.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    public abstract boolean login(String id, String mdp);
}
