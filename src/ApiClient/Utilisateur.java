package ApiClient;

public class Utilisateur {
    private Long id;
    private String passWord;
    private String userName;
    private String email;

    public Utilisateur() {
        super();
    }
    public Utilisateur(Utilisateur utilisateur) {
        super();
        this.id = utilisateur.getId();
        this.passWord = utilisateur.getPassWord();
        this.userName = utilisateur.getUserName();
        this.email = utilisateur.getEmail();
    }
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userNAme) {
        this.userName = userNAme;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
