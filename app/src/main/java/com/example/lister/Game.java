package com.example.lister;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private String gameTitle;
    private String gameGenre;
    private String gameReleaseDate;
    private String gameImage;

    public Game() {

    }

    public Game(String gameTitle, String gameGenre, String gameReleaseDate, String gameImage) {
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.gameReleaseDate = gameReleaseDate;
        this.gameImage = gameImage;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameGenre() {
        return gameGenre;
    }

    public void setGameGenre(String gameGenre) {
        this.gameGenre = gameGenre;
    }

    public String setGameReleaseDate() {
        return gameReleaseDate;
    }

    public void setGameReleaseDate(String gameReleaseDate) {
        this.gameReleaseDate = gameReleaseDate;
    }

    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
    }




    public Game(Parcel source) {
        gameTitle = source.readString();
        gameGenre = source.readString();
        gameReleaseDate = source.readString();
        gameImage = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameTitle);
        dest.writeString(gameGenre);
        dest.writeString(gameReleaseDate);
        dest.writeString(gameImage);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
