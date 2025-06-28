package com.example.listadecompras.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;
import com.example.listadecompras.api.*;
import com.example.listadecompras.models.AuthRequest;
import com.example.listadecompras.models.AuthResponse;
import com.example.listadecompras.util.Global;
import com.example.listadecompras.util.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText emailEdit, senhaEdit;
    private Button loginBtn;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        emailEdit = v.findViewById(R.id.editTextEmail);
        senhaEdit = v.findViewById(R.id.editTextSenha);
        loginBtn = v.findViewById(R.id.buttonLogin);

        tokenManager = new TokenManager(requireContext());

        v.findViewById(R.id.textGoToRegister).setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_login_to_register));

        loginBtn.setOnClickListener(view -> {
            String email = emailEdit.getText().toString().trim();
            String senha = senhaEdit.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api = RetrofitClient.getInstance(null).create(ApiService.class);
            AuthRequest req = new AuthRequest(email, senha, Global.APP_VERSION);

            api.login(req).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().status) {
                        tokenManager.saveTokens(response.body().token, response.body().refresh_token);
                        Navigation.findNavController(view).navigate(R.id.nav_minhaslistas);
                    } else {
                        Toast.makeText(getContext(), "Login incorreto ou inválido", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("LOG", t.getMessage());
                }
            });
        });

        return v;
    }
}
