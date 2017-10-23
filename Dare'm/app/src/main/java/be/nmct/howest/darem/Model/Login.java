package be.nmct.howest.darem.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


/**
 * Created by michv on 22/10/2017.
 */

public class Login extends BaseObservable {
    private String email;
    private String password;

    @Bindable
    public String getPassword() {
        return password;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(be.nmct.howest.darem.BR.email);
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(be.nmct.howest.darem.BR.password);
    }


    public Login(String email, String password){
        this.email = email;
        this.password = password;
    }
}
