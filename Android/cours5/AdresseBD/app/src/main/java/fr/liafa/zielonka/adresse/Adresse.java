package fr.liafa.zielonka.adresse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zielonka on 12/10/15.
 */
public class Adresse implements Parcelable {
    private String rue;
    private String zip;
    private String ville;
    private String numero;

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Adresse> CREATOR =
            new Parcelable.Creator<Adresse>() {
                @Override
                public Adresse[] newArray(int size) {
                    return new Adresse[size];
                }

                @Override
                public Adresse createFromParcel(Parcel source) {
                    return new Adresse(source.readString(), source.readString(),source.readString(),source.readString());
                }
            };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rue);
        dest.writeString(zip);
        dest.writeString(ville);
        dest.writeString(numero);
    }

    public Adresse(String numero, String rue, String ville, String zip) {
        this.numero = numero;
        this.rue = rue;
        this.ville = ville;
        this.zip = zip;
    }

    public String getNumero() {
        return numero;
    }

    public String getRue() {
        return rue;
    }

    public String getVille() {
        return ville;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "numero='" + numero + '\'' +
                ", rue='" + rue + '\'' +
                ", zip='" + zip + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }
}
