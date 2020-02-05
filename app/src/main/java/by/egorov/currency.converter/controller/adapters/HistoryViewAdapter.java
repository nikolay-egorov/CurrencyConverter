package by.egorov.currency.converter.controller.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.CircularArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import by.egorov.currency.converter.R;
import by.egorov.currency.converter.model.HistoryItem;

public class HistoryViewAdapter extends ArrayAdapter<HistoryItem> {

    CircularArray<HistoryItem> listObjects;
    Context mContext;

    public HistoryViewAdapter(@NonNull Context context, @NonNull CircularArray objects) {
        super(context, R.layout.item_list_view_sample);
        this.listObjects = objects;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return listObjects.size();
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HistoryViewAdapter.ViewHolder mViewHolder = new HistoryViewAdapter.ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_list_view_sample, parent, false);
            mViewHolder.mDate = (TextView) convertView.findViewById(R.id.oper_info_date );
            mViewHolder.mSumFrom = (TextView) convertView.findViewById(R.id.oper_sum_from);
            View view =  convertView.findViewById(R.id.oper_info_from);
            mViewHolder.mFlagFrom = (ImageView) view.findViewById(R.id.ivFlag);
            mViewHolder.mCurrFrom = (TextView) view.findViewById(R.id.text1 );

            mViewHolder.mSumTo = (TextView) convertView.findViewById(R.id.oper_sum_to);
            view = convertView.findViewById(R.id.oper_info_to);
            mViewHolder.mFlagTo = (ImageView) view.findViewById(R.id.ivFlag);
            mViewHolder.mCurrTo = (TextView) view.findViewById(R.id.text1 );
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (HistoryViewAdapter.ViewHolder) convertView.getTag();
        }

        mViewHolder.mSumFrom.setText(listObjects.get(position).getValueFrom());
//        int res = this.mContext.getResources().getIdentifier("i"+listObjects[position].getCurrFrom().getNumCode(), "drawable", "by.egorov.currency.converter");
        int res = this.mContext.getResources().getIdentifier("i"+listObjects.get(position).getCurrFrom().getNumCode(), "drawable", "by.egorov.currency.converter");
        Drawable flagDrawable = this.mContext.getResources().getDrawable(res);

        mViewHolder.mFlagFrom.setImageDrawable(flagDrawable);
        mViewHolder.mCurrFrom.setText(listObjects.get(position).getCurrFrom().getName());

        mViewHolder.mSumTo.setText(listObjects.get(position).getValueTo());
        res = this.mContext.getResources().getIdentifier("i"+listObjects.get(position).getCurrTo().getNumCode(), "drawable", "by.egorov.currency.converter");
        flagDrawable = this.mContext.getResources().getDrawable(res);
        mViewHolder.mFlagTo.setImageDrawable(flagDrawable);
        mViewHolder.mCurrTo.setText(listObjects.get(position).getCurrTo().getName());

        mViewHolder.mDate.setText(String.format(this.mContext.getString(R.string.source_and_date_format),
                listObjects.get(position).getDate() ));


        return convertView;
    }



    private static class ViewHolder {
        TextView mSumFrom;
        ImageView mFlagFrom;
        TextView mCurrFrom;
        TextView mSumTo;
        ImageView mFlagTo;
        TextView mCurrTo;
        TextView mDate;
    }



}
