package com.example.listadecompras.ui.register;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;
import com.example.listadecompras.api.*;
import com.example.listadecompras.models.ApiResponse;
import com.example.listadecompras.models.RegisterRequest;
import com.example.listadecompras.util.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            String nome = nomeEdit.getText().toString().trim();
            String email = emailEdit.getText().toString().trim();
            String senha = senhaEdit.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api = RetrofitClient.getInstance(null).create(ApiService.class);
            RegisterRequest req = new RegisterRequest(nome, email, senha, Global.APP_VERSION);

            api.register(req).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().status) {
                        Toast.makeText(getContext(), "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_register_to_login);
                    } else {
                        Toast.makeText(getContext(), "Erro ao registrar: " + (response.body() != null ? response.body().message : "Erro desconhecido"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return v;
    }
}
