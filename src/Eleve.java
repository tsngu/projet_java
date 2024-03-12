import java.util.List;
import java.util.Set;

public class Eleve extends Utilisateur {

    private Langue languePratiquee; // Langue que l'élève apprend
    private Apprentissage apprentissage; // Nombre d'exercices que l'élève a fait

    // Constructeur
    public Eleve(String id, String motDePasse, String prenom, String nom, Langue languePratiquee, Niveau niveauLangue) {
        super(id, motDePasse, prenom, nom); // Récupère les infos de la classe Utilisateur
        this.languePratiquee = languePratiquee;
        this.apprentissage = new Apprentissage(languePratiquee, niveauLangue);
    }

    // Getter
    public Langue getLanguePratiquee() {
        return languePratiquee;
    }

    public Niveau getNiveauLangue() {
        return apprentissage.getNiveauActuel();
    }

    @Override
    public String toString() {
        return "ID : " + id + "\n" +
                "Prénom : " + prenom + "\n" +
                "Nom : " + nom + "\n" +
                "Langue pratiquée : " + languePratiquee.getName() + "\n" +
                apprentissage.trackProgress();
    }

    public void addExerciceResolu(Exercice exercice) {
        apprentissage.addExerciceProgression(exercice);
    }

    public Set<Exercice> getExercices() {
        return this.apprentissage.getProgression();
    }
}
