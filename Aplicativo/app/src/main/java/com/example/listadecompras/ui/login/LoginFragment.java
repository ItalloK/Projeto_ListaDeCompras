package com.example.listadecompras.ui.login;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;
import com.example.listadecompras.api.*;
import com.example.listadecompras.models.*;
import com.example.listadecompras.util.Global;
import com.example.listadecompras.util.TokenManager;

import retrofit2.*;

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
            String email = emailEdit.getText().toString();
            String senha = senhaEdit.getText().toString();

            ApiService api = RetrofitClient.getInstance("").create(ApiService.class);
            AuthRequest req = new AuthRequest(email, senha, Global.APP_VERSION);
            api.login(req).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().status) {
                        TokenManager tm = new TokenManager(requireContext());
                        tm.saveTokens(response.body().token, response.body().refreshToken);

                        Navigation.findNavController(view).navigate(R.id.nav_minhaslistas);
                    } else {
                        Toast.makeText(getContext(), "Erro ao logar.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Falha na conexão", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        });

        return v;
    }
}
