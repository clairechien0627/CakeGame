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

public class OrderFragment extends BottomSheetDialogFragment {

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
            throw new RuntimeException(context + " must implement OnDialogButtonFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button scoreButton = view.findViewById(R.id.scoreButton);
        Button cakeButton = view.findViewById(R.id.cakeButton);

        scoreButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Score");
            }
            dismiss();
        });

        cakeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Cake");
            }
            dismiss();
        });
    }

    public void setOrderListener(OnDialogButtonFragmentListener listener) {
        this.listener = listener;
    }
}
