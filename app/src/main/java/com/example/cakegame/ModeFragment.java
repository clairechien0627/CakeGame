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

public class ModeFragment extends BottomSheetDialogFragment {

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
        return inflater.inflate(R.layout.fragement_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button easyButton = view.findViewById(R.id.easyButton);
        Button normalButton = view.findViewById(R.id.normalButton);
        Button hardButton = view.findViewById(R.id.hardButton);
        Button devilButton = view.findViewById(R.id.devilButton);

        easyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Easy");
            }
            dismiss();
        });

        normalButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Normal");
            }
            dismiss();
        });

        hardButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Hard");
            }
            dismiss();
        });

        devilButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectDialog("Devil");
            }
            dismiss();
        });
    }

    public void setModeListener(OnDialogButtonFragmentListener listener) {
        this.listener = listener;
    }
}
