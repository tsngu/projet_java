public class Langue {

    private String code;
    private String name;

    public Langue(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Le code d'un pays : FR, VN, (...)
    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
