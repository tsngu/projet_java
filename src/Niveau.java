public enum Niveau {
    DEBUTANT("Débutant", 1),
    INTERMEDIAIRE("Intermediaire", 2),
    AVANCE("Avancé", 3);

    private String string;
    private int value;

    // Pour associer à chaque élément d'un enum un nom pour l'affichage et un code pour la sauvegarde des fichiers
    Niveau(String name, int value) {
        string = name;
        this.value = value;
    }

    // Pour obtenir un bel affichage des niveaux facilement
    @Override
    public String toString() {
        return string;
    }

    public int getValue() {
        return this.value;
    }
}