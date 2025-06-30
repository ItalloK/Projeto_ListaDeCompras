package com.example.listadecompras.ui.minhaslistas;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadecompras.api.*;
import com.example.listadecompras.databinding.FragmentMinhaslistasBinding;
import com.example.listadecompras.models.*;
import com.example.listadecompras.util.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MinhasListasFragment extends Fragment {

    private FragmentMinhaslistasBinding binding;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMinhaslistasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tokenManager = new TokenManager(requireContext());

        RecyclerView recyclerView = binding.recyclerViewListas;
        TextView txtQtdListas = binding.txtQtdListas;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String email = tokenManager.getUserEmail();
        if (email == null || email.isEmpty()) {
            txtQtdListas.setText("Usuário não identificado.");
            return root;
        }

        ApiService api = RetrofitClient.getInstance(getContext()).create(ApiService.class);

        api.getUserLists(email).enqueue(new Callback<ListResponse>() {
            @Override
            public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().status) {
                    List<ListModel> listas = response.body().data;
                    txtQtdListas.setText("Você tem " + listas.size() + " listas criadas");
                    recyclerView.setAdapter(new ListaAdapter(listas));
                } else {
                    txtQtdListas.setText("Erro ao carregar listas.");
                    try {
                        Log.e("LISTAS", "Erro: " + (response.errorBody() != null ? response.errorBody().string() : "desconhecido"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse> call, Throwable t) {
                txtQtdListas.setText("Erro de conexão.");
                Log.e("LISTAS", "Falha na requisição: " + t.getMessage());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
