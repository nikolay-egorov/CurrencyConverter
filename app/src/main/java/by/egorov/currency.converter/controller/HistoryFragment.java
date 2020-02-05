package by.egorov.currency.converter.controller;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.CircularArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import by.egorov.currency.converter.R;
import by.egorov.currency.converter.controller.adapters.HistoryViewAdapter;
import by.egorov.currency.converter.model.Cache;
import by.egorov.currency.converter.model.HistoryItem;
import by.egorov.currency.converter.util.SharedPreferencesHelper;


public class HistoryFragment extends Fragment {

    private static final String TAG = HistoryFragment.class.getSimpleName();

    private ListView mOperationHistory;
    private CircularArray operationHistoryArray;
    private HistoryViewAdapter historyViewAdapter;
    private Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        mOperationHistory = (ListView) v.findViewById(R.id.fc_operation_history);
        operationHistoryArray = loadOperationHistory();
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        historyViewAdapter = new HistoryViewAdapter(mContext, operationHistoryArray);
        mOperationHistory.setAdapter(historyViewAdapter);

    }



    private void findView(View v) {
        mOperationHistory = (ListView) v.findViewById(R.id.fc_operation_history);

    }


    private CircularArray<HistoryItem> loadOperationHistory(){
        Cache cache = SharedPreferencesHelper.getCache();
        if (cache == null)
            return new CircularArray<HistoryItem>(10);
        else
            return SharedPreferencesHelper.getCache().getCachedHistory();

    }

    protected void updateListViewData(){
        operationHistoryArray = loadOperationHistory();
        historyViewAdapter.notifyDataSetChanged();
    }


    protected void saveHistoryToCache(HistoryItem lastOperation){
        Cache cache = Cache.get();
        if(cache.getCachedHistory().size() >= 10){
            cache.getCachedHistory().removeFromEnd(1);
            operationHistoryArray.removeFromEnd(1);
        }
        cache.getCachedHistory().addFirst(lastOperation);
        SharedPreferencesHelper.saveCache(cache);
        operationHistoryArray.addFirst(lastOperation);
        historyViewAdapter.notifyDataSetChanged();
        cache.update();
    }


}
