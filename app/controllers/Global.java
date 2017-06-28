import models.User;
import play.Application;
import play.GlobalSettings;

import java.util.List;


public class Global extends GlobalSettings {

    public void onStart(Application app){
        List<User> list=User.all();
        for (User user:list){
            if (user.isAdmin()){
                return;
            }
        }
        User user = new User("admin@root.com","12345");
        user.setAdmin(true);
        user.save();
    }
}