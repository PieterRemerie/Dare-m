package be.nmct.howest.darem.Model;

import android.databinding.Bindable;
import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import be.nmct.howest.darem.BR;


/**
 * Created by michv on 29/10/2017.
 */

public class Challenge extends BaseObservable implements Parcelable {
    private String name;
    private String description;
    private String date;
    private String category;
    private String friends;
    private int categoryId;
    private String databaseId;
    private Boolean completed;


    public Challenge(){}
    public Challenge(String name, String description, String category, String friends, String date, int categoryId, String databaseId) {

        this.name = name;
        this.description = description;
        this.category = category;
        this.friends = friends;
        this.databaseId = databaseId;
        this.date = date;
        this.categoryId = categoryId;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected Challenge(Parcel in) {
        name = in.readString();
        description = in.readString();
        category = in.readString();
        friends = in.readString();
        databaseId = in.readString();
        date = in.readString();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;

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
        dest.writeString(databaseId);
        dest.writeString(date);
        dest.writeInt(categoryId);
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


    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}