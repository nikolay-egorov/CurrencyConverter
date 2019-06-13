package by.egorov.currency.converter.model;

import android.media.Image;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Root(name = "Foreign Currency Market", strict = false)
public class CurrencyMarket {

    @Attribute(name = "Date")
    private Date mDate;

    @ElementList(name = "Valute", inline = true)
    private List<Currency> mCurrencies;

//    @ElementList(name = "Image", inline = true)
//    private List<Image> mImages;

    public CurrencyMarket() {
        mCurrencies = new ArrayList<>();
    }

    public CurrencyMarket(Date date, List<Currency> currencies) {
        mDate = date;
        mCurrencies = currencies;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public List<Currency> getCurrencies() {
        return mCurrencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        mCurrencies = currencies;
    }
}