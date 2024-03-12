import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.security.SecureRandom; // Pour générer un mdp
import java.util.Base64;

// Cette classe va permettre à l'utilisateur d'ajouter un professeur et de lui générer un ID et un MDP.
public class GestionnaireProfesseur extends GestionnaireUtilisateur {

    private static int compteurProf = 0; // Pour générer des IDs uniques
    private List<Professeur> professeurs = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    // Générateur d'ID uniques
    private String genererIDProf() {
        compteurProf++;
        return "P" + String.format("%03d", compteurProf);
    }

    // Générateur de mdp
    private String genererMDP() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[5];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    // Trouve le dernier ID en liste pour ne pas avoir de duplication
    private void dernierIDProf() {
        try (Scanner fileScanner = new Scanner(new File("src/bdd/professeurs.txt"))) {
            while (fileScanner.hasNextLine()) {
                String ligne = fileScanner.nextLine();
                String[] parts = ligne.split(", ");
                if (parts.length == 5) {
                    String id = parts[0].substring(1); // Ignore la lettre P

                    // Ajoutez une vérification supplémentaire pour s'assurer que l'ID est composé uniquement de chiffres
                    if (id.matches("^\\d+$")) {
                        int dernierIDint = Integer.parseInt(id);
                        if (dernierIDint > compteurProf) {
                            compteurProf = dernierIDint;
                        }
                    } else {
                        System.out.println("Format d'ID non valide dans le fichier professeurs.txt.");
                    }
                } else {
                    System.out.println("Format de ligne non valide dans le fichier professeurs.txt.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier professeurs.txt non trouvé.");
        }
    }

    // Charger le fichier et vérifier si les données sont ok
    private void chargerFichierProf(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(", ");
                if (parts.length == 5) {
                    String id = parts[0];
                    String motDePasse = parts[1];
                    String prenom = parts[2];
                    String nom = parts[3];
                    String langueEnseigneeCode = parts[4];
                    // Vérifications

                    if (id.startsWith("P") && motDePasse.length() >= 5 && !nom.isEmpty() && !prenom.isEmpty() && !langueEnseigneeCode.isEmpty()) {
                        // Les données sont valides, créez l'objet Professeurs
                        Professeur professeur = new Professeur(id, motDePasse, prenom, nom, Application.langues.get(langueEnseigneeCode), new ArrayList<>());
                        creerProfesseur(professeur, false);

                    } else {
                        // Les données ne sont pas valides, affichez un message d'erreur ou ignorez cette ligne
                        System.out.println("Données invalides détectées dans le fichier professeurs.txt.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier professeurs.txt non trouvé.");
        }
    }

    // Charger le dernier ID et le fichier
    public GestionnaireProfesseur() {
        dernierIDProf();
        chargerFichierProf("src/bdd/professeurs.txt");
    }

    // Créer l'utilisateur
    public void creerProfesseur(Professeur professeur, boolean majFichier) {
        professeurs.add(professeur);
        if (majFichier) {
            majProfesseurFichier(professeur, "src/bdd/professeurs.txt");
        }
    }

    // Créer le professeur dès l'input.
    public void creerProfesseurInput() {
        System.out.println("Entrez le prénom du professeur :");
        String prenom = scanner.nextLine();

        System.out.println("Entrez le nom du professeur : ");
        String nom = scanner.nextLine();

        System.out.println("Entrez la langue enseignée : ");
        String langueEnseigneeCode = scanner.nextLine();

        if (Application.langues.containsKey(langueEnseigneeCode)) {
            String id = genererIDProf();

            String motDePasse = genererMDP();

            List<Exercice> listeExercices = new ArrayList<>();

            Professeur professeur = new Professeur(id, motDePasse, prenom, nom, Application.langues.get(langueEnseigneeCode), listeExercices);
            creerProfesseur(professeur, true);
            System.out.println("\nProfesseur ajouté.\nVeillez à noter l'ID et le MDP dans un endroit sécurisé.\n" +
                    "ID : " + professeur.id + "\n" + "MDP : " + professeur.motDePasse + "\n");
        } else {
            System.out.println("Pays non reconnu. Création annulée.");
        }

    }

    // Afficher la liste des professeurs
    public void afficheProfesseurs() {
        System.out.println("Liste des professeurs inscrits : ");
        for (Professeur professeur : professeurs) {
            System.out.println(professeur);
        }
    }

    // Mise à jour de la liste
    private void majProfesseurFichier(Professeur professeur, String filename) {
        String donneesProf = professeur.getId() + ", " + professeur.getMotDePasse() + ", " +
                professeur.getPrenom() + ", " + professeur.getNom() + ", " +
                professeur.getLangueEnseignee().getCode();

        try (FileWriter writer = new FileWriter(filename, true)) {
            if (new File(filename).length() > 0) {
                // Si le fichier n'est pas vide, ajoute un séparateur de ligne
                writer.write(System.lineSeparator());
            }
            writer.write(donneesProf);
        } catch (IOException e) {
            System.out.println("Echec lors de l'ajout du professeur, erreur : " + e.getMessage());
        }
    }

    // Vérifie les identifiants
    public boolean login(String id, String mdp) {
        for (Professeur prof : this.professeurs) {
            if (prof.login(id, mdp)) {
                return true;
            }
        }
        return false;
    }

    public Professeur getProfesseur(String id) {
        for (Professeur prof : this.professeurs) {
            if (prof.id.equals(id)) {
                return prof;
            }
        }
        return null;
    }

    public List<Professeur> getProfesseurs() {
        return this.professeurs;
    }
}
