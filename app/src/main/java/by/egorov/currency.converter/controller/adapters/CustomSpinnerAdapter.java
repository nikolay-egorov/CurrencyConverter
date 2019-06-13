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

import by.egorov.currency.converter.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    String[] spinnerTitles;
    String[] spinnerImages;
    Context mContext;

    public CustomSpinnerAdapter(@NonNull Context context, String[] titles, String[] images) {
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
        return spinnerTitles.length;
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


        int resId = this.mContext.getResources().getIdentifier("i" + spinnerImages[position], "drawable", "by.egorov.currency.converter");
        Drawable myDrawable = this.mContext.getResources().getDrawable(resId);
//        int resId= this.mContext.getResources().getIdentifier("android.resource://by.egorov.currency.converter." +"R.drawable.i"+spinnerImages[position],null,null);

        mViewHolder.mFlag.setImageDrawable(myDrawable);
        mViewHolder.mName.setText(spinnerTitles[position]);
        return convertView;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
    }


}