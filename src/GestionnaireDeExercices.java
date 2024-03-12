import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionnaireDeExercices {
    private List<Exercice> exercices;

    public GestionnaireDeExercices() {
        this.exercices = new ArrayList<>();
    }

    public void addExercice(Exercice exercice) {
        this.exercices.add(exercice);
    }

    public Exercice findExercice(int index) {
        return this.exercices.get(index);
    }

    public void deleteExercice(int index) {
        this.exercices.remove(index);
    }

    public List<Exercice> getExercices() {
        return this.exercices;
    }

    //On ne sélectionne que les exercices correspondant à un niveau et une langue
    public List<Exercice> getExercicesSelonLangueNiveau(Langue langue, Niveau niveau) {
        List<Exercice> exercicesAdaptes = new ArrayList<>();
        for (Exercice exercice : exercices) {
            if (exercice.getNiveau() == niveau && exercice.getLangue() == langue) {
                exercicesAdaptes.add(exercice);
            }
        }
        return exercicesAdaptes;
    }

    private Scanner scanner = new Scanner(System.in);

    // Récupérer tout les exercices d'un fichier. Il y a besoin de la liste des professeurs, car chaque exercice
    // est lié à un professeur
    public void chargerExercices(String path, List<Professeur> professeurs) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(path));
        while (fileScanner.hasNextLine()) {
            String ligne = fileScanner.nextLine();
            if (ligne.trim() != "") {
                //Gestion méta données première ligne
                String[] parts = ligne.split(":");
                String langueCode = parts[0];
                Langue langue = Application.langues.get(langueCode);
                // Niveau : 1 Debutant, 2 Intermediaire, 3 Avance
                int niveauCode = Integer.parseInt(parts[1]);
                Niveau niveau = Application.codeToNiveau(niveauCode);
                int pointsParReponse = Integer.parseInt(parts[2]);
                int pointsNecessaires = Integer.parseInt(parts[3]);
                String professeurCode = parts[4];

                Exercice exercice = new Exercice(langue, niveau, pointsParReponse, pointsNecessaires);
                //Trouver le bon professeur pour ajouter l'exo à sa liste
                for (Professeur prof : professeurs) {
                    if (prof.getId().equals(professeurCode)) {
                        prof.addExercice(exercice);
                    }
                }

                //Gestion questions
                boolean continuer = true;
                while (fileScanner.hasNextLine() && continuer) {
                    String question = fileScanner.nextLine();
                    if (question.trim() == "") {
                        continuer = false;
                    } else {
                        exercice.addQuestion(question);
                    }
                }

                exercices.add(exercice);
            }
        }
    }

    // Prompteur pour créer un exercice
    public Exercice creationExercice() {
        Scanner stringScanner = new Scanner(System.in);
        System.out.println("Veuillez indiquer le code de la langue.");
        String langueCode = stringScanner.next();
        Langue langue = Application.langues.get(langueCode);
        System.out.println("Veuillez indiquer le chiffre correspondant au niveau. 1 : Debutant, 2 : Intermediaire, 3 : Avance");
        int niveauCode = scanner.nextInt();
        Niveau niveau = Application.codeToNiveau(niveauCode);
        System.out.println("Veuillez indiquer le nombre de points par bonne réponse.");
        int pointsParReponse = scanner.nextInt();
        System.out.println("Veuillez indiquer le nombre de points nécessaires.");
        int pointsNecessaires = scanner.nextInt();

        Exercice exercice = new Exercice(langue, niveau, pointsParReponse, pointsNecessaires);

        System.out.println("Ecrivez une question. Ecrivez \"exit\" lorsque vous avez terminé.");
        String question = "";
        stringScanner = new Scanner(System.in);
        question = stringScanner.nextLine();
        while (!question.equals("exit")) {
            exercice.addQuestion(question);
            question = stringScanner.nextLine();
        }
        return exercice;
    }

    public void updateExo(List<Professeur> professeurs) {
        String filename = "src/bdd/exercice.txt";
        try (FileWriter writer = new FileWriter(filename, false)) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("Echec lors de l'ajout de l'exercice, erreur : " + e.getMessage());
        }
        for (Exercice exercice : exercices) {
            majExercicesFichier(exercice, filename, professeurs);
        }
    }

    // Mise à jour du fichier exercices.txt
    private void majExercicesFichier(Exercice exercice, String filename, List<Professeur> professeurs) {
        String idProf = "";
        for(Professeur prof : professeurs){
            List<Exercice> exercicesProfesseur = prof.getListeExercices();
            if(exercicesProfesseur.contains(exercice)){
                idProf = prof.getId();
            }
        }
        String donneesExo = exercice.getLangue().getCode() + ":" + exercice.getNiveau().getValue() + ":" +
                exercice.getPointsParReponse() + ":" + exercice.getPointsNecessaires() + ":" +
                idProf + "\n";
        for(String question : exercice.getQuestions()){
            donneesExo += question + "\n";
        }
        System.out.println("");

        try (FileWriter writer = new FileWriter(filename, true)) {
            if (new File(filename).length() > 0) {
                // Si le fichier n'est pas vide, ajoute un séparateur de ligne
                writer.write(System.lineSeparator());
            }
            writer.write(donneesExo);
        } catch (IOException e) {
            System.out.println("Echec lors de l'ajout de l'exercice, erreur : " + e.getMessage());
        }
    }

    // Menu donnant toutes les options liées aux questions d'un exercice.
    public void menuModificationQuestion(Exercice exercice, Professeur professeur) {

        System.out.println("Quelle question voulez-vous modifier ? Entrez le numéro de la question");
        int choix = scanner.nextInt();
        if (choix > 0 && choix < exercice.getNbQuestions() + 1) {
            System.out.println("Ecrivez une question.");
            Scanner stringScanner = new Scanner(System.in);
            String question = stringScanner.nextLine();
            exercice.updateQuestion(question, choix-1);
        } else {
            System.out.println("Numéro non valide.");
        }
        menuModificationExercice(exercice, professeur);
    }

    // Menu donnant toutes les options liées aux exercices.
    public void menuModificationExercice(Exercice exercice, Professeur professeur) {
        exercice.generatePresentation();
        System.out.println("Que voulez-vous modifier ?");
        System.out.println("1. Code de la langue");
        System.out.println("2. Niveau");
        System.out.println("3. Nombre de points par bonne réponse");
        System.out.println("4. Nombre de points nécessaires");
        System.out.println("5. Questions");
        System.out.println("6. Finir la modification (Entrez 6 une deuxième fois pour confirmer le choix)");

        boolean continuer = true;
        while (continuer) {
            int choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    System.out.println("Veuillez indiquer le code de la langue.");
                    Scanner stringScanner = new Scanner(System.in);
                    String langueCode = stringScanner.next();
                    Langue langue = Application.langues.get(langueCode);
                    exercice.setLangue(langue);
                    menuModificationExercice(exercice, professeur);
                    break;
                case 2:
                    System.out.println("Veuillez indiquer le chiffre correspondant au niveau. 1 : Debutant, 2 : Intermediaire, 3 : Avance");
                    int niveauCode = scanner.nextInt();
                    Niveau niveau = Application.codeToNiveau(niveauCode);
                    exercice.setNiveau(niveau);
                    menuModificationExercice(exercice, professeur);
                    break;
                case 3:
                    System.out.println("Veuillez indiquer le nombre de points par bonne réponse.");
                    int pointsParReponse = scanner.nextInt();
                    exercice.setPointsParReponse(pointsParReponse);
                    menuModificationExercice(exercice, professeur);
                    break;
                case 4:
                    System.out.println("Veuillez indiquer le nombre de points nécessaires.");
                    int pointsNecessaires = scanner.nextInt();
                    exercice.setPointsNecessaires(pointsNecessaires);
                    menuModificationExercice(exercice, professeur);
                    break;
                case 5:
                    menuModificationQuestion(exercice, professeur);
                    break;
                case 6:
                    continuer = false;

                    break;
                default:
                    System.out.println("Choix non reconnu.");
            }
        }
    }
}
