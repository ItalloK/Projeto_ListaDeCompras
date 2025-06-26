package com.example.listadecompras.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.listadecompras.R;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        TextView goToLogin = view.findViewById(R.id.textGoToLogin);
        goToLogin.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_register_to_login));

        return view;
    }
}
