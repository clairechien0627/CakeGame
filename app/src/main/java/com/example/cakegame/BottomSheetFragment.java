package com.example.cakegame;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public interface OnDialogButtonFragmentListener {
        void onSelectDialog(String select);
    }

    public OnDialogButtonFragmentListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogButtonFragmentListener) {
            listener = (OnDialogButtonFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDialogButtonFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button shareButton = view.findViewById(R.id.shareButton);
        Button linkButton = view.findViewById(R.id.linkButton);

        shareButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Share");
            }
            dismiss();
        });

        linkButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Link");
            }
            dismiss();
        });
    }

    public void setListener(OnDialogButtonFragmentListener listener) {
        this.listener = listener;
    }
}
