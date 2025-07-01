package com.example.listadecompras.ui.minhaslistas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadecompras.R;
import com.example.listadecompras.api.ApiService;
import com.example.listadecompras.api.RetrofitClient;
import com.example.listadecompras.databinding.FragmentMinhaslistasBinding;
import com.example.listadecompras.models.ApiResponse;
import com.example.listadecompras.models.ListModel;
import com.example.listadecompras.models.ListResponse;
import com.example.listadecompras.util.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MinhasListasFragment extends Fragment {

    private FragmentMinhaslistasBinding binding;
    private TokenManager tokenManager;
    private List<ListModel> listas;
    private ListaAdapter adapter;
    private ApiService api;

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

        api = RetrofitClient.getInstance(getContext()).create(ApiService.class);

        api.getUserLists(email).enqueue(new Callback<ListResponse>() {
            @Override
            public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().status) {
                    listas = response.body().data;
                    txtQtdListas.setText("Você tem " + listas.size() + " listas criadas");

                    adapter = new ListaAdapter(listas, (listModel, position) -> {
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setTitle("Confirmar exclusão")
                                .setMessage("Tem certeza que deseja excluir a lista \"" + listModel.title + "\"?")
                                .setPositiveButton("Excluir", (dialog, which) -> {
                                    api.deleteList(listModel.id).enqueue(new Callback<ApiResponse>() {
                                        @Override
                                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                            if (response.isSuccessful() && response.body() != null && response.body().status) {
                                                adapter.removeItem(position);
                                                binding.txtQtdListas.setText("Você tem " + listas.size() + " listas criadas");
                                                Toast.makeText(getContext(), "Lista deletada com sucesso", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Erro ao deletar lista", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                                            Toast.makeText(getContext(), "Falha na conexão", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    });

                    recyclerView.setAdapter(adapter);

                } else {
                    txtQtdListas.setText("Erro ao carregar listas.");
                }
            }

            @Override
            public void onFailure(Call<ListResponse> call, Throwable t) {
                txtQtdListas.setText("Erro de conexão.");
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
