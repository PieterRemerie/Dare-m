package be.nmct.howest.darem.Model;

import android.databinding.BaseObservable;

/**
 * Created by katri on 13/11/2017.
 */

public class Profile extends BaseObservable{


    public String name;
    public String email;


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile(String name, String email){

        this.name = name;
        this.email = email;
    }

}
