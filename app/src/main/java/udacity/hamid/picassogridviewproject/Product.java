package udacity.hamid.picassogridviewproject;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.name;

/**
 * Created by temp on 25/01/2017.
 */

public class
Product implements Parcelable {

    private String image;
    private String title;
    private String releaseDate;
    private String voteAvg;
    private String plotSynopsis;
    private int id;
    private String key;


    public Product(String image, String title, String releaseDate, String voteAvg, String plotSynopsis, int id) {
        this.image = image;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAvg = voteAvg;
        this.plotSynopsis = plotSynopsis;
        this.id = id;

    }

    public Product(String key) {
        this.key = key;
    }

    public Product() {
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(voteAvg);
        dest.writeString(plotSynopsis);
        dest.writeString(key);
        dest.writeInt(id);
    }

    protected Product(Parcel in) {
        image = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAvg = in.readString();
        plotSynopsis = in.readString();
        id = in.readInt();
        key = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
