package com.babadi.rxlivesearch.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babadi.rxlivesearch.R;

public class TownCursorAdapter extends CursorAdapter {

    private SearchView searchView;

    public TownCursorAdapter(@NonNull Context context, Cursor cursor, SearchView searchView) {
        super(context, cursor, false);
        this.searchView = searchView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.contact_row_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String townName = cursor.getString(cursor.getColumnIndex("townName"));
        final String townID = cursor.getString(cursor.getColumnIndex("townID"));
        TextView itemTextView = view.findViewById(R.id.row_item_txt);
        itemTextView.setText(townName);

        if (townID.equalsIgnoreCase("-1")) {
            itemTextView.setGravity(Gravity.CENTER);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (townID.equalsIgnoreCase("-1")) {
                    return;
                }
                searchView.setQuery(townName,false);
                searchView.clearFocus();
            }
        });
    }
}
