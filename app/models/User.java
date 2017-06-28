package models;

import org.apache.commons.codec.binary.Base64;
import play.data.validation.Constraints.Email;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Entity
public class User extends Model {

    @Id
    @Email
    private String email;
    private boolean isAdmin;

    private String passwordHash;
    private String salt;

    public User(String email, String password) {
        this.email = email;
        setPassword(password);
        this.isAdmin =false;
    }

    public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);
    private String getHash(String s) {
        s = SHA256(s);
        return s;
    }
    public static List<User> all() {
        return find.all();
    }
    public void setPassword(String password) {
        salt=genSalt();
        passwordHash=getHash(password+salt);
    }
    private boolean checkPassword(String password) {
        if (password.isEmpty() ) {
            return !password.isEmpty();
        }else{
            password=getHash(password+salt);
            if (password.equals(passwordHash)){
                return true;
            }else{
                return false;
            }
        }
    }
    public static String authenticate(String email, String password) {
        if (email.isEmpty() || password.isEmpty()){return "Поля не заполнены";}
        User us =User.find.byId(email);
        if (us == null){
            return "Пользователь с данным email не зарегистрирован";
        }else {
            if (us.checkPassword(password)){
                return null;
            }else {
                return "Не верный пароль";
            }
        }
    }
    public static boolean vailable(String email) {
        User us =User.find.byId(email);
        if (us != null){
            return false;
        }else {
            return true;
        }
    }

    public static String SHA256(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(str.getBytes());
            return javax.xml.bind.DatatypeConverter.printHexBinary(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String genSalt(){
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return Base64.encodeBase64(salt).toString();
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
