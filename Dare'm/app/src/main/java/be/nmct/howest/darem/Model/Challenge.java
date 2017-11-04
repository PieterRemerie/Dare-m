package be.nmct.howest.darem.Model;

import android.databinding.Bindable;
import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import be.nmct.howest.darem.BR;


/**
 * Created by michv on 29/10/2017.
 */

public class Challenge extends BaseObservable implements Parcelable {
    private String name;
    private String description;
    private String category;
    private String friends;

    public Challenge(){}
    public Challenge(String name, String description, String category, String friends) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.friends = friends;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
        notifyPropertyChanged(BR.friends);
    }

    protected Challenge(Parcel in) {
        name = in.readString();
        description = in.readString();
        category = in.readString();
        friends = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(friends);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Challenge> CREATOR = new Parcelable.Creator<Challenge>() {
        @Override
        public Challenge createFromParcel(Parcel in) {
            return new Challenge(in);
        }

        @Override
        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };
}