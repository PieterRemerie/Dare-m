package be.nmct.howest.darem.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by michv on 22/10/2017.
 */

public class Registratie extends BaseObservable {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    public Registratie(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(be.nmct.howest.darem.BR.name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(be.nmct.howest.darem.BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(be.nmct.howest.darem.BR.password);
    }

    @Bindable
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(be.nmct.howest.darem.BR.confirmPassword);
    }
}
