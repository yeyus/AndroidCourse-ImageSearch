package com.ea7jmf.androidcourse_imagesearch.fragments;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ea7jmf.androidcourse_imagesearch.R;

/**
 * Created by yeyus on 1/31/15.
 */
public class SettingsDialog extends DialogFragment {

    private Spinner spSize;
    private Spinner spType;
    private Spinner spColor;
    private EditText etSite;

    SettingsDialogListener mListener;

    public interface SettingsDialogListener {
        void onFinishSettingsDialog(Bundle settings);
    }

    public SettingsDialog() {}

    public static SettingsDialog newInstance(Bundle args) {
        SettingsDialog frag = new SettingsDialog();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Bundle ret = new Bundle();
        ret.putString("size", (String) spSize.getSelectedItem());
        ret.putString("type", (String) spType.getSelectedItem());
        ret.putString("color", (String) spColor.getSelectedItem());
        ret.putString("site", etSite.getText().toString());
        mListener.onFinishSettingsDialog(ret);
        super.onDismiss(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container);
        getDialog().setTitle("Settings");
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mListener = (SettingsDialogListener) getActivity();

        setupView(view);

        // Set the focus on the first editable widget
        spSize.requestFocus();

        return view;
    }

    private void setupView(View view) {
        spSize = (Spinner) view.findViewById(R.id.spSize);
        ArrayAdapter<String> aSize = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.size_array));
        spSize.setAdapter(aSize);
        spSize.setSelection(aSize.getPosition(getArguments().getString("size")));

        spColor = (Spinner) view.findViewById(R.id.spColor);
        ArrayAdapter<String> aColor = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.color_array));
        spColor.setAdapter(aColor);
        spColor.setSelection(aColor.getPosition(getArguments().getString("color")));

        spType = (Spinner) view.findViewById(R.id.spType);
        ArrayAdapter<String> aType = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.type_array));
        spType.setAdapter(aType);
        spType.setSelection(aType.getPosition(getArguments().getString("type")));

        etSite = (EditText) view.findViewById(R.id.etSite);
        etSite.setText(getArguments().getString("site"));
    }

}
