package com.babadi.rxlivesearch.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.babadi.rxlivesearch.R;
import com.babadi.rxlivesearch.remote.model.TownModel;

import java.util.List;

public class TownAdapter extends ArrayAdapter{

    Context context;
    List<TownModel> townList;
    TextView itemSearchTextview;

    public TownAdapter(@NonNull Context context, List<TownModel> townList) {
        super(context, 0,townList);
        this.context = context;
        this.townList = townList;
    }

    @Override
    public int getCount() {
        return townList.size();
    }

    @Nullable
    @Override
    public TownModel getItem(int position) {
        return townList.get(position);
    }

    public void notifyAdapter(List<TownModel> list) {
        this.townList = list;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                filterResults.values = townList;
                filterResults.count = townList.size();
                publishResults(constraint,filterResults);
                return filterResults;
            }

            @Override
            protected void publishResults(final CharSequence constraint, final FilterResults results) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (results != null && results.count > 0) {
                            townList = (List<TownModel>) results.values;
                            notifyDataSetChanged();
                        } else {
                            notifyDataSetInvalidated();
                        }
                    }
                });
            }
        };

        return myFilter;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.contact_row_item, parent, false);
        }

        itemSearchTextview = (TextView) convertView.findViewById(R.id.row_item_txt);
        try {
            TownModel town = townList.get(position);
            itemSearchTextview.setText(town.getTownNameFa());
        } catch (Exception e) {
            Log.e("IndexException", "index out of bound");
        }

        return convertView;
    }
}
