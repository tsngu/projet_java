import java.util.List;

public class Professeur extends Utilisateur {

    private Langue langueEnseignee; // Langue enseignée par le professeur
    private List<Exercice> listeExercices; // Liste d'exercices crées par le professeur

    // Constructeur
    public Professeur(String id, String motDePasse, String prenom, String nom, Langue langueEnseignee, List<Exercice> listeExercices) {
        super(id, motDePasse, prenom, nom); // Prends les infos de la classe Utilisateur
        this.langueEnseignee = langueEnseignee;
        this.listeExercices = listeExercices;
    }

    // Getter
    public Langue getLangueEnseignee() {
        return langueEnseignee;
    }

    public List<Exercice> getListeExercices() {
        return listeExercices;
    }

    public void addExercice(Exercice exercice) {
        this.listeExercices.add(exercice);
    }

    @Override
    public String toString() {
        return "\nInformations sur chaque professeur :\n" +
                "ID : " + id + "\n" +
                "Prénom : " + prenom + "\n" +
                "Nom : " + nom + "\n" +
                "Langue enseignée : " + langueEnseignee.getName() + "\n";
    }
}
