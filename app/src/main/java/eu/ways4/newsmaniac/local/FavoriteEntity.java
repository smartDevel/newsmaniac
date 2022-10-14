package eu.ways4.newsmaniac.local;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;


/**
 * A model Class to represent a
 * table in room db
 * */

@Entity(tableName = "article_info")
public class FavoriteEntity implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "article_id")
    private int id;

    @ColumnInfo(name = "article_title")
    private String title;

    @ColumnInfo(name = "article_desc")
    private String description;

    @ColumnInfo(name = "image_url")
    private String urlToImage;

    @ColumnInfo(name = "url")
    private String url;


    @Ignore
    public FavoriteEntity(String title, String description, String urlToImage) {
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
    }


    public FavoriteEntity(int id, String title, String description, String urlToImage, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.url = url;
    }

    @Ignore
    public FavoriteEntity() {
    }

    protected FavoriteEntity(Parcel in) {
        title = in.readString();
        description = in.readString();
        urlToImage = in.readString();
        url = in.readString();

    }

    public static final Creator<FavoriteEntity> CREATOR = new Creator<FavoriteEntity>() {
        @Override
        public FavoriteEntity createFromParcel(Parcel in) {
            return new FavoriteEntity(in);
        }

        @Override
        public FavoriteEntity[] newArray(int size) {
            return new FavoriteEntity[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getUrlToImage() {
        return urlToImage;
    }

    public String getUrl(){
        return url;
    }

    @Override
    public String toString() {
        return "Article{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteEntity)) return false;
        FavoriteEntity article = (FavoriteEntity) o;
        return
                getTitle().equals(article.getTitle()) &&
                        getDescription().equals(article.getDescription()) &&
                        getUrlToImage().equals(article.getUrlToImage()) &&
                        getUrl().equals(article.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getUrlToImage(), getUrl());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(urlToImage);
        dest.writeString(url);
    }
}
