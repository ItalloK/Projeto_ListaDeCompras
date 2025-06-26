package com.example.listadecompras.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.listadecompras.R;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextView goToRegister = view.findViewById(R.id.textGoToRegister);
        goToRegister.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_login_to_register));

        return view;
    }
}
