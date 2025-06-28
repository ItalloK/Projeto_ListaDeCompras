package com.example.listadecompras.ui.login;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;

public class LoginFragment extends Fragment {

    private EditText emailEdit, senhaEdit;
    private Button loginBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        emailEdit = v.findViewById(R.id.editTextEmail);
        senhaEdit = v.findViewById(R.id.editTextSenha);
        loginBtn = v.findViewById(R.id.buttonLogin);

        v.findViewById(R.id.textGoToRegister).setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_login_to_register));

        loginBtn.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Login desativado nesta versão.", Toast.LENGTH_SHORT).show();

            // Navega para a tela principal sem autenticação
            Navigation.findNavController(view).navigate(R.id.nav_minhaslistas);
        });

        return v;
    }
}
