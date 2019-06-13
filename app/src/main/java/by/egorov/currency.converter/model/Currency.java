package by.egorov.currency.converter.model;

import android.support.annotation.NonNull;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import by.egorov.currency.converter.R;

@Root(name = "Valute")
public class Currency {

    @Attribute(name = "ID")
    private String mId;

    @Element(name = "NumCode")
    private String mNumCode;

    @Element(name = "CharCode")
    private String mCharCode;

    @Element(name = "Nominal")
    private Long mNominal;

    @Element(name = "Name")
    private String mName;

    @Element(name = "Value")
    private Double mValue;

    private String mImageCode;

    public Currency() {
    }

    public Currency(String id, @NonNull String numCode, @NonNull String charCode, Long nominal, String name, Double value) {
        mId = id;
        mNumCode = numCode;
        mCharCode = charCode;
        mNominal = nominal;
        mName = name;
        mValue = value;
        mImageCode = mNumCode;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getNumCode() {
        return mNumCode;
    }

    public void setNumCode(String numCode) {
        mNumCode = numCode;
    }

    public String getCharCode() {
        return mCharCode;
    }

    public void setCharCode(String charCode) {
        mCharCode = charCode;
    }

    public Long getNominal() {
        return mNominal;
    }

    public void setNominal(Long nominal) {
        mNominal = nominal;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Double getValue() {
        return mValue;
    }

    public void setValue(Double value) {
        mValue = value;
    }

    public void setImage(String image) {
        mImageCode = image;}
    public String getImage() {return mImageCode;}


    public String getFormatedName(){
//        String formatStr = "%1$s %2$s"; //, getCharCode()
//        return String.format(formatStr, getName() );
        return getName();
    }

    @Override
    public String toString() {
        return "Currency{" +
                "mId='" + mId + '\'' +
                ", mNumCode='" + mNumCode + '\'' +
                ", mCharCode='" + mCharCode + '\'' +
                ", mNominal=" + mNominal +
                ", mName='" + mName + '\'' +
                ", mValue=" + mValue +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (mId != null ? !mId.equals(currency.mId) : currency.mId != null) return false;
        if (!mNumCode.equals(currency.mNumCode)) return false;
        return mCharCode.equals(currency.mCharCode);
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + mNumCode.hashCode();
        result = 31 * result + mCharCode.hashCode();
        return result;
    }
}