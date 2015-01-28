package com.ea7jmf.androidcourse_imagesearch;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ea7jmf.androidcourse_imagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yeyus on 1/27/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private TextView txtTitle;
    private ImageView ivImage;

    public ImageResultsAdapter(Context context, List<ImageResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult ir = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.item_image_result, parent, false);
        }

        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

        txtTitle.setText(Html.fromHtml(ir.getTitle()));
        Picasso.with(getContext()).load(ir.getThumbUrl()).into(ivImage);

        return convertView;
    }
}
