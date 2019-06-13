package by.egorov.currency.converter.model;

import java.util.Date;
import java.util.Objects;

public class HistoryItem {

    private String mValueFrom;

    private String mValueTo;

    private Currency mCurrFrom;

    private Currency mCurrTo;

    private Date mDate;

    public HistoryItem() {
    }

    public HistoryItem(String mValueFrom, String mValueTo, Currency mCurrFrom, Currency mCurrTo, Date date) {
        this.mValueFrom = mValueFrom;
        this.mValueTo = mValueTo;
        this.mCurrFrom = mCurrFrom;
        this.mCurrTo = mCurrTo;
        this.mDate = date;
    }


    public String getValueFrom() {
        return mValueFrom;
    }

    public void setValueFrom(String mValueFrom) {
        this.mValueFrom = mValueFrom;
    }

    public String getValueTo() {
        return mValueTo;
    }

    public void setValueTo(String mValueTo) {
        this.mValueTo = mValueTo;
    }

    public Currency getCurrFrom() {
        return mCurrFrom;
    }

    public void setCurrFrom(Currency mCurrFrom) {
        this.mCurrFrom = mCurrFrom;
    }

    public Currency getCurrTo() {
        return mCurrTo;
    }

    public void setCurrTo(Currency mCurrTo) {
        this.mCurrTo = mCurrTo;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryItem that = (HistoryItem) o;
        return mValueFrom.equals(that.mValueFrom) &&
                mValueTo.equals(that.mValueTo) &&
                mCurrFrom.equals(that.mCurrFrom) &&
                mCurrTo.equals(that.mCurrTo) &&
                mDate.equals(that.mDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mValueFrom, mValueTo, mCurrFrom, mCurrTo, mDate);
    }
}
