package by.egorov.currency.converter.controller.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.egorov.currency.converter.R;

public class CurrencySpinnerAdapter extends ArrayAdapter<String> {

    List<String> spinnerTitles;
    List<String> spinnerImages;
    Context mContext;

    public CurrencySpinnerAdapter(@NonNull Context context, List<String> titles, List<String> images) {
        super(context, R.layout.item_spinner_simple);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerTitles.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_spinner_simple, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.ivFlag);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        int resId = this.mContext.getResources().getIdentifier("i" + spinnerImages.get(position), "drawable", "by.egorov.currency.converter");
//        Drawable myDrawable = this.mContext.getResources().getDrawable(resId);

//        mViewHolder.mFlag.setImageDrawable(myDrawable);
        mViewHolder.mFlag.setImageResource(resId);
        mViewHolder.mName.setText(spinnerTitles.get(position));
        return convertView;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
    }


}