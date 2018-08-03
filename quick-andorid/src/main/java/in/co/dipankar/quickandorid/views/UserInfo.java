package in.co.dipankar.quickandorid.views;

public class UserInfo{
    SocialLoginView.Type type;
    String id, name, email, picture;

    public UserInfo(SocialLoginView.Type type, String id, String name, String email, String picture) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
}