package com.example.listadecompras.ui.register;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;

public class RegisterFragment extends Fragment {

    private EditText nomeEdit, emailEdit, senhaEdit;
    private Button registerBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        nomeEdit = v.findViewById(R.id.editTextNome);
        emailEdit = v.findViewById(R.id.editTextEmail);
        senhaEdit = v.findViewById(R.id.editTextSenha);
        registerBtn = v.findViewById(R.id.buttonRegister);

        v.findViewById(R.id.textGoToLogin).setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_register_to_login));

        registerBtn.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Registro desativado nesta versão.", Toast.LENGTH_SHORT).show();
        });

        return v;
    }
}
