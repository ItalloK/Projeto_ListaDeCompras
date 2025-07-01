package com.example.listadecompras.ui.listadetalhes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;
import com.example.listadecompras.api.ApiService;
import com.example.listadecompras.api.RetrofitClient;
import com.example.listadecompras.models.ApiResponse;
import com.example.listadecompras.models.ItemModel;
import com.example.listadecompras.models.ListModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.text.TextWatcher;
import android.text.Editable;

import retrofit2.Call;

public class ListaDetalhesFragment extends Fragment {

    private TextView txtTitulo, txtCodigo, txtExpira, txtQuantidadeTotalItens, txtItensDistintos, txtValorTotal;
    private LinearLayout containerItens;
    private Button btnNovoItem, btnSalvar, btnSair;

    private ListModel listaAtual;
    private String modoAtual = "visualizar";

    public ListaDetalhesFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_detalhes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicialização dos componentes
        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtCodigo = view.findViewById(R.id.txtCodigo);
        txtExpira = view.findViewById(R.id.txtExpira);
        containerItens = view.findViewById(R.id.containerItens);
        btnNovoItem = view.findViewById(R.id.btnNovoItem);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        btnSair = view.findViewById(R.id.btnSair);
        txtQuantidadeTotalItens = view.findViewById(R.id.txtQuantidadeTotalItens);
        txtItensDistintos = view.findViewById(R.id.txtItensDistintos);
        txtValorTotal = view.findViewById(R.id.txtValorTotal);

        // Recupera argumentos
        Bundle args = getArguments();
        if (args != null) {
            listaAtual = (ListModel) args.getSerializable("lista");
            modoAtual = args.getString("modo", "visualizar");
        }

        // Atualiza campos com dados da lista
        if (listaAtual != null) {
            txtTitulo.setText(listaAtual.title);
            txtCodigo.setText("Código: " + listaAtual.code);

            String[] parts = listaAtual.expiresAt.split("T")[0].split("-");
            if (parts.length == 3) {
                txtExpira.setText("Expira em: " + parts[2] + "/" + parts[1] + "/" + parts[0]);
            } else {
                txtExpira.setText("Expira em: " + listaAtual.expiresAt.split("T")[0]);
            }

            containerItens.removeAllViews();
            for (ItemModel item : listaAtual.items) {
                adicionarItemNaTela(item, modoAtual);
            }
            calcularTotais();
        }

        // Define visibilidade e layout dos botões com base no modo
        btnSair.setVisibility(View.VISIBLE);

        if ("editar".equals(modoAtual)) {
            btnNovoItem.setVisibility(View.VISIBLE);
            btnSalvar.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams paramsSair = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            paramsSair.setMarginEnd(40);
            btnSair.setLayoutParams(paramsSair);

            LinearLayout.LayoutParams paramsSalvar = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            btnSalvar.setLayoutParams(paramsSalvar);

        } else {
            btnNovoItem.setVisibility(View.GONE);
            btnSalvar.setVisibility(View.GONE);

            LinearLayout.LayoutParams paramsSair = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnSair.setLayoutParams(paramsSair);
        }

        // Ações dos botões
        btnNovoItem.setOnClickListener(v -> {
            ItemModel novoItem = new ItemModel();
            novoItem.name = "";
            novoItem.quantity = 1;
            novoItem.value = 0.0;
            listaAtual.items.add(novoItem);
            adicionarItemNaTela(novoItem, "editar");
            calcularTotais();
        });

        btnSalvar.setOnClickListener(v -> salvarAlteracoes());

        btnSair.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }


    private void adicionarItemNaTela(ItemModel item, String modo) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_detalhe, containerItens, false);

        TextView txtNome = itemView.findViewById(R.id.txtNome);
        TextView txtQtd = itemView.findViewById(R.id.txtQtd);
        TextView txtValor = itemView.findViewById(R.id.txtValor);

        EditText edtNome = itemView.findViewById(R.id.edtNome);
        EditText edtQtd = itemView.findViewById(R.id.edtQtd);
        EditText edtValor = itemView.findViewById(R.id.edtValor);

        edtQtd.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) { calcularTotais(); }
        });

        edtValor.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) { calcularTotais(); }
        });

        if ("editar".equals(modo)) {
            txtNome.setVisibility(View.GONE);
            txtQtd.setVisibility(View.GONE);
            txtValor.setVisibility(View.GONE);

            edtNome.setVisibility(View.VISIBLE);
            edtQtd.setVisibility(View.VISIBLE);
            edtValor.setVisibility(View.VISIBLE);

            edtNome.setText(item.name);
            edtQtd.setText(String.valueOf(item.quantity));
            edtValor.setText(new DecimalFormat("0.00").format(item.value));
        } else {
            txtNome.setVisibility(View.VISIBLE);
            txtQtd.setVisibility(View.VISIBLE);
            txtValor.setVisibility(View.VISIBLE);

            edtNome.setVisibility(View.GONE);
            edtQtd.setVisibility(View.GONE);
            edtValor.setVisibility(View.GONE);

            txtNome.setText(item.name);
            txtQtd.setText(String.valueOf(item.quantity));
            txtValor.setText(String.format("R$ %.2f", item.value));
        }

        containerItens.addView(itemView);
    }

    private void calcularTotais() {
        int totalQuantidade = 0;
        double valorTotal = 0.0;
        Set<String> itensDistintos = new HashSet<>();

        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (int i = 0; i < containerItens.getChildCount(); i++) {
            View itemView = containerItens.getChildAt(i);

            String nome = "", qtdStr = "", valorStr = "";
            int qtd = 0;
            double valor = 0.0;

            EditText edtNome = itemView.findViewById(R.id.edtNome);
            if (edtNome.getVisibility() == View.VISIBLE) {
                nome = edtNome.getText().toString().trim();
                qtdStr = ((EditText) itemView.findViewById(R.id.edtQtd)).getText().toString().trim();
                valorStr = ((EditText) itemView.findViewById(R.id.edtValor)).getText().toString().trim();
            } else {
                nome = ((TextView) itemView.findViewById(R.id.txtNome)).getText().toString().trim();
                qtdStr = ((TextView) itemView.findViewById(R.id.txtQtd)).getText().toString().trim();
                valorStr = ((TextView) itemView.findViewById(R.id.txtValor)).getText().toString().replace("R$", "").trim().replace(",", ".");
            }

            try { qtd = Integer.parseInt(qtdStr); } catch (NumberFormatException ignored) {}
            try { valor = Double.parseDouble(valorStr.replace(",", ".")); } catch (NumberFormatException ignored) {}

            totalQuantidade += qtd;
            valorTotal += qtd * valor;

            if (!nome.isEmpty()) {
                itensDistintos.add(nome.toLowerCase());
            }
        }

        txtQuantidadeTotalItens.setText(String.valueOf(totalQuantidade));
        txtItensDistintos.setText(String.valueOf(itensDistintos.size()));
        txtValorTotal.setText("R$ " + df.format(valorTotal));
    }

    private void salvarAlteracoes() {
        List<ItemModel> itensAtualizados = new ArrayList<>();
        boolean hasError = false;

        for (int i = 0; i < containerItens.getChildCount(); i++) {
            View itemView = containerItens.getChildAt(i);
            EditText edtNome = itemView.findViewById(R.id.edtNome);
            EditText edtQtd = itemView.findViewById(R.id.edtQtd);
            EditText edtValor = itemView.findViewById(R.id.edtValor);

            String nome = edtNome.getText().toString().trim();
            String qtdStr = edtQtd.getText().toString().trim();
            String valorStr = edtValor.getText().toString().trim();

            if (nome.isEmpty()) {
                edtNome.setError("Nome obrigatório");
                edtNome.requestFocus();
                hasError = true;
                break;
            }

            int qtd = 1;
            double valor = 0.0;
            try { qtd = Integer.parseInt(qtdStr); } catch (NumberFormatException ignored) {}
            try { valor = Double.parseDouble(valorStr.replace(",", ".")); } catch (NumberFormatException ignored) {}

            ItemModel itemAtualizado = new ItemModel();
            itemAtualizado.name = nome;
            itemAtualizado.quantity = Math.max(qtd, 1);
            itemAtualizado.value = Math.max(valor, 0.0);

            itensAtualizados.add(itemAtualizado);
        }

        if (hasError) {
            Toast.makeText(getContext(), "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Atualiza os itens da lista localmente
        listaAtual.items = itensAtualizados;
        calcularTotais();

        // Aqui vem a chamada PUT para atualizar no backend
        ApiService apiService = RetrofitClient.getInstance(getContext()).create(ApiService.class);

        apiService.updateList(listaAtual.id, listaAtual).enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Lista salva com sucesso!", Toast.LENGTH_SHORT).show();
                    if (getView() != null) {
                        Navigation.findNavController(getView()).popBackStack();
                    }
                } else {
                    Toast.makeText(getContext(), "Falha ao salvar lista: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Erro na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
