package com.example.listadecompras.ui.register;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;
import com.example.listadecompras.api.*;
import com.example.listadecompras.models.*;
import com.example.listadecompras.util.Global;

import retrofit2.*;

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
            String nome = nomeEdit.getText().toString();
            String email = emailEdit.getText().toString();
            String senha = senhaEdit.getText().toString();

            ApiService api = RetrofitClient.getInstance("").create(ApiService.class);
            RegisterRequest req = new RegisterRequest(nome, email, senha, Global.APP_VERSION);
            api.register(req).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getContext(), "Registro realizado. Faça login!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_register_to_login);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Erro ao registrar", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return v;
    }
}
