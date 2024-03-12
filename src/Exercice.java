import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exercice {
    private Langue langue;

    private Niveau niveau;
    private int pointsParReponse;
    private int pointsNecessaires;
    private List<String> questions;
    private List<String> reponses;

    public Exercice(Langue langue, Niveau niveau, int pointsParReponse, int pointsNecessaires) {
        this.langue = langue;
        this.niveau = niveau;
        this.pointsParReponse = pointsParReponse;
        this.pointsNecessaires = pointsNecessaires;
        this.questions = new ArrayList<>();
        this.reponses = new ArrayList<>();
    }

    // Affichage des informations liés à l'exercice
    public String presentation() {
        return langue.getCode() + " " + niveau.toString() + " " + pointsNecessaires + " nécessaires sur " + (pointsParReponse * this.reponses.size()) + " disponibles";
    }

    // Retourne les réponses associées à une questions
    private List<String> reponsesFromQuestion(String question) {
        List<String> reponses = new ArrayList<>();
        Pattern pattern = Pattern.compile("(#[^#]+#)");
        Matcher matcher = pattern.matcher(question);
        while (matcher.find()) {
            String reponse = matcher.group(0);
            reponse = reponse.replaceAll("#", "");
            reponses.add(reponse);
        }
        return reponses;
    }

    // Afficher l'exercice avec chaque question comme un élève doit le voir
    public void generate() {
        System.out.println("Liste de mots manquants : ");
        List<String> randomReponses = new ArrayList<>(reponses);
        Collections.shuffle(randomReponses);
        // Afficher les réponses possibles dans un ordre aléatoire
        for (String reponse : randomReponses) {
            System.out.print(reponse);
            if (!randomReponses.get(randomReponses.size() - 1).equals(reponse)) {
                System.out.print(", ");
            }
        }
        System.out.println("");
        System.out.println("");
        // Afficher chaque question en mettant le texte à trou
        int cpt = 1;
        for (String question : questions) {
            String newQuestion = question.replaceAll("#[^#]+#", "...");
            System.out.println(cpt + ". " + newQuestion);
            cpt++;
        }
    }

    // Afficher chaque question sans mettre le texte à trou
    public void generatePresentation() {

        int cpt = 1;
        for (String question : questions) {
            System.out.println("Q" + cpt + ". " + question);
            cpt++;
        }
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public void setPointsParReponse(int pointsParReponse) {
        this.pointsParReponse = pointsParReponse;
    }

    public void setPointsNecessaires(int pointsNecessaires) {
        this.pointsNecessaires = pointsNecessaires;
    }

    public void addQuestion(String question) {
        this.questions.add(question);
        List<String> reponsesDeQuestion = reponsesFromQuestion(question);
        reponses.addAll(reponsesDeQuestion);
    }

    public void updateQuestion(String question, int index) {
        this.questions.set(index, question);
        regenerateResponses();
    }

    // Regénère les réponses, si jamais une question a été modifié. L'ordre des réponses a de l'importance
    // pour la résolution
    private void regenerateResponses() {
        reponses.clear();
        for (String question : questions) {
            List<String> reponsesDeQuestion = reponsesFromQuestion(question);
            reponses.addAll(reponsesDeQuestion);
        }
    }

    public int getNbQuestions() {
        return this.questions.size();
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public Langue getLangue() {
        return this.langue;
    }

    public int getPointsNecessaires(){ return this.pointsNecessaires;}

    // Compte le nombre de points obtenus, et selon le barème, indique si l'exercice est complété
    public boolean resoudre(String reponsesSoumises) {
        int points = resoudreNbPoints(reponsesSoumises);
        if (points >= pointsNecessaires) {
            return true;
        } else {
            return false;
        }
    }

    public int getPointsParReponse() {
        return this.pointsParReponse;
    }

    // Compte le nombre de points obtenus selon les réponses
    public int resoudreNbPoints(String reponsesSoumises) {
        String[] reponsesParsed = reponsesSoumises.split(",");
        int points = 0;
        for (int i = 0; i < reponsesParsed.length; i++) {
            if (reponsesParsed[i].trim().equals(reponses.get(i))) {
                points += pointsParReponse;
            }
        }
        return points;
    }

    public int getNbReponses() {
        return this.reponses.size();
    }
    public List<String> getQuestions(){
        return this.questions;
    }

}
