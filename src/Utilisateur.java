import java.util.Scanner;

public class Utilisateur {

    // Données communes aux deux classes Professeurs et Eleves
    protected String id;
    protected String motDePasse;
    protected String nom;
    protected String prenom;

    // Constructeur
    public Utilisateur(String id, String motDePasse, String prenom, String nom) {
        this.id = id;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    // Pour se connecter
    public boolean login(String idEntre, String mdpEntre) {
        // Vérifier si l'ID et le MDP entrés correspondent
        if (idEntre.equals(id) && mdpEntre.equals(motDePasse)) {
            return true; // Connexion réussie
        } else {
            return false; // Connexion ratée
        }
    }
}
