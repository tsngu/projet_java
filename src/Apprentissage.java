import java.util.HashSet;
import java.util.Set;

public class Apprentissage {
    private Set<Exercice> progression;
    private Niveau niveauActuel;
    private Langue langue;

    boolean apprentissageFini;

    public Apprentissage(Langue langue, Niveau niveauActuel) {
        this.progression = new HashSet<>();
        this.langue = langue;
        this.niveauActuel = niveauActuel;
        this.apprentissageFini = false;
    }

    public void addExerciceProgression(Exercice exercice) {
        this.progression.add(exercice);
        verifierAvancement();
    }

    // Compte le nombre d'exercices résolus du niveau actuel, pour voir si on doit évoluer ou non.
    private void verifierAvancement() {
        int compteurExercicesNiveauResolus = 0;
        for (Exercice exercice : progression) {
            if (exercice.getNiveau() == niveauActuel) {
                compteurExercicesNiveauResolus++;
            }
        }
        if (compteurExercicesNiveauResolus > 3) {
            switch (niveauActuel) {
                case DEBUTANT:
                    niveauActuel = Niveau.INTERMEDIAIRE;
                    break;
                case INTERMEDIAIRE:
                    niveauActuel = Niveau.AVANCE;
                    break;
                case AVANCE:
                    apprentissageFini = true;
                    break;
            }
        }
    }

    public String trackProgress() {
        return "Nombre d'exercices résolus : " + progression.size() + "\n" +
                "Niveau : " + niveauActuel.toString() + "\n";
    }

    public Niveau getNiveauActuel() {
        return this.niveauActuel;
    }

    public Set<Exercice> getProgression() {
        return this.progression;
    }
}
