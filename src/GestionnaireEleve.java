import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.security.SecureRandom; // Pour générer un mdp
import java.util.Base64;

// Cette classe va permettre à l'utilisateur d'ajouter un eleve et de lui générer un ID et un MDP.
public class GestionnaireEleve extends GestionnaireUtilisateur {

    private static int compteurEleve = 0; // Pour générer des IDs uniques
    private List<Eleve> eleves = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    // Générateur d'ID uniques
    private String genererIDEleve() {
        compteurEleve++;
        return "E" + String.format("%03d", compteurEleve);
    }

    // Générateur de mdp
    private String genererMDP() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[5];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    // Trouve le dernier ID en liste pour ne pas avoir de duplication
    private void dernierIDEleve() {
        try (Scanner fileScanner = new Scanner(new File("src/bdd/eleves.txt"))) {
            while (fileScanner.hasNextLine()) {
                String ligne = fileScanner.nextLine();
                String[] parts = ligne.split(", ");
                if (parts.length >= 5) {
                    String id = parts[0].substring(1); // Ignore la lettre P

                    // Ajoutez une vérification supplémentaire pour s'assurer que l'ID est composé uniquement de chiffres
                    if (id.matches("^\\d+$")) {
                        int dernierIDint = Integer.parseInt(id);
                        if (dernierIDint > compteurEleve) {
                            compteurEleve = dernierIDint;
                        }
                    } else {
                        System.out.println("Format d'ID non valide dans le fichier elevex.txt.");
                    }
                } else {
                    System.out.println("Format de ligne non valide dans le fichier eleves.txt.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier eleves.txt non trouvé.");
        }
    }

    // Charger le fichier et vérifier si les données sont ok
    private void chargerFichierEleve(String filename, List<Exercice> exercices) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(", ");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String motDePasse = parts[1];
                    String prenom = parts[2];
                    String nom = parts[3];
                    String languePratiqueeCode = parts[4];
                    int niveauLangueCode = Integer.parseInt(parts[5]);
                    // Vérifications

                    if (id.startsWith("E") && motDePasse.length() >= 5 && !nom.isEmpty() && !prenom.isEmpty() && !languePratiqueeCode.isEmpty()) {
                        // Les données sont valides, créez l'objet Eleves
                        Eleve eleve = new Eleve(id, motDePasse, prenom, nom, Application.langues.get(languePratiqueeCode), Application.codeToNiveau(niveauLangueCode));
                        if (parts.length > 6) {
                            for (int i = 6; i < parts.length; i++) {
                                eleve.addExerciceResolu(exercices.get(Integer.parseInt(parts[i])));
                            }
                        }
                        creerEleve(eleve, false, exercices);

                    } else {
                        // Les données ne sont pas valides, affichez un message d'erreur ou ignorez cette ligne
                        System.out.println("Données invalides détectées dans le fichier eleves.txt.");
                    }


                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier eleves.txt non trouvé.");
        }
    }

    // Charger le dernier ID et le fichier
    public GestionnaireEleve(List<Exercice> exercices) {
        dernierIDEleve();
        chargerFichierEleve("src/bdd/eleves.txt", exercices);
    }

    // Créer l'utilisateur
    public void creerEleve(Eleve eleve, boolean majFichier, List<Exercice> exercices) {
        eleves.add(eleve);
        if (majFichier) {
            majEleveFichier(eleve, "src/bdd/eleves.txt", exercices);
        }
    }

    // Créer le eleve dès l'input.
    public void creerEleveInput() {
        System.out.println("Entrez le prénom de l'eleve :");
        String prenom = scanner.nextLine();

        System.out.println("Entrez le nom de l'eleve : ");
        String nom = scanner.nextLine();

        System.out.println("Entrez la langue pratiquée : ");
        String languePratiqueeCode = scanner.nextLine();
        if (Application.langues.containsKey(languePratiqueeCode)) {
            Langue languePratiquee = Application.langues.get(languePratiqueeCode);
            String id = genererIDEleve();

            String motDePasse = genererMDP();

            Eleve eleve = new Eleve(id, motDePasse, nom, prenom, languePratiquee, Niveau.DEBUTANT);
            creerEleve(eleve, true, List.of());
            System.out.println("\nEleve ajouté.\nVeillez à noter l'ID et le MDP dans un endroit sécurisé.\n" +
                    "ID : " + eleve.id + "\n" + "MDP : " + eleve.motDePasse + "\n");
        } else {
            System.out.println("Pays non reconnu. Création annulée.");
        }

    }

    // Afficher la liste des eleves
    public void afficheEleves(Langue langue) {
        System.out.println("Liste des eleves inscrits : ");
        for (Eleve eleve : eleves) {
            if (eleve.getLanguePratiquee() == langue) {
                System.out.println(eleve);
            }
        }
    }

    public void afficheEleves() {
        System.out.println("Liste des eleves inscrits : ");
        for (Eleve eleve : eleves) {
            System.out.println(eleve);
        }
    }

    public void updateEleves(List<Exercice> exercices) {
        String filename = "src/bdd/eleves.txt";
        try (FileWriter writer = new FileWriter(filename, false)) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("Echec lors de l'ajout de l'éleve, erreur : " + e.getMessage());
        }
        for (Eleve eleve : eleves) {
            majEleveFichier(eleve, filename, exercices);
        }
    }

    // Mise à jour de la liste
    private void majEleveFichier(Eleve eleve, String filename, List<Exercice> exercices) {
        String donneesEleve = eleve.getId() + ", " + eleve.getMotDePasse() + ", " +
                eleve.getPrenom() + ", " + eleve.getNom() + ", " +
                eleve.getLanguePratiquee().getCode() + ", " + eleve.getNiveauLangue().getValue();
        for (Exercice exerciceProgression : eleve.getExercices()) {
            int indexExercice = 0;
            int cpt = 0;
            for (Exercice exercice : exercices) {
                if (exercice.equals(exerciceProgression)) {
                    indexExercice = cpt;
                }
                cpt++;
            }
            donneesEleve += ", " + indexExercice;
        }

        try (FileWriter writer = new FileWriter(filename, true)) {
            if (new File(filename).length() > 0) {
                // Si le fichier n'est pas vide, ajoute un séparateur de ligne
                writer.write(System.lineSeparator());
            }
            writer.write(donneesEleve);
        } catch (IOException e) {
            System.out.println("Echec lors de l'ajout de l'éleve, erreur : " + e.getMessage());
        }
    }

    @Override
    public boolean login(String id, String mdp) {
        for (Eleve eleve : this.eleves) {
            if (eleve.login(id, mdp)) {
                return true;
            }
        }
        return false;
    }

    public Eleve getEleve(String id) {
        for (Eleve eleve : this.eleves) {
            if (eleve.id.equals(id)) {
                return eleve;
            }
        }
        return null;
    }
}