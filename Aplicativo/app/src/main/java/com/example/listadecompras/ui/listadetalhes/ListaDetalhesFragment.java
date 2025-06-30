package com.example.listadecompras.ui.listadetalhes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.listadecompras.R;
import com.example.listadecompras.models.ItemModel;
import com.example.listadecompras.models.ListModel;

import java.util.ArrayList;
import java.util.List;

public class ListaDetalhesFragment extends Fragment {

    private TextView txtTitulo, txtCodigo, txtExpira;
    private LinearLayout containerItens;
    private Button btnNovoItem, btnSalvar, btnSair;

    private ListModel listaAtual;
    private String modoAtual;

    public ListaDetalhesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_detalhes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtCodigo = view.findViewById(R.id.txtCodigo);
        txtExpira = view.findViewById(R.id.txtExpira);
        containerItens = view.findViewById(R.id.containerItens);
        btnNovoItem = view.findViewById(R.id.btnNovoItem);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        btnSair = view.findViewById(R.id.btnSair);

        Bundle args = getArguments();
        if (args != null) {
            listaAtual = (ListModel) args.getSerializable("lista");
            modoAtual = args.getString("modo", "visualizar");

            if (listaAtual != null) {
                txtTitulo.setText(listaAtual.title);
                txtCodigo.setText("Código: " + listaAtual.code);
                txtExpira.setText("Expira em: " + listaAtual.expiresAt.split("T")[0]);

                btnNovoItem.setVisibility("editar".equals(modoAtual) ? View.VISIBLE : View.GONE);
                btnSalvar.setVisibility("editar".equals(modoAtual) ? View.VISIBLE : View.GONE);
                btnSair.setVisibility("editar".equals(modoAtual) ? View.VISIBLE : View.GONE);

                containerItens.removeAllViews();
                for (ItemModel item : listaAtual.items) {
                    adicionarItemNaTela(item, modoAtual);
                }
            }
        }

        btnNovoItem.setOnClickListener(v -> {
            // Cria novo item com valores padrão
            ItemModel novoItem = new ItemModel();
            novoItem.name = "";
            novoItem.quantity = 1;
            novoItem.value = 0.0;
            listaAtual.items.add(novoItem);

            adicionarItemNaTela(novoItem, "editar");
        });

        btnSalvar.setOnClickListener(v -> {
            salvarAlteracoes();
        });

        btnSair.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void adicionarItemNaTela(ItemModel item, String modo) {
        View itemView = LayoutInflater.from(getContext())
                .inflate(R.layout.item_lista_detalhe, containerItens, false);

        TextView txtNome = itemView.findViewById(R.id.txtNome);
        TextView txtQtd = itemView.findViewById(R.id.txtQtd);
        TextView txtValor = itemView.findViewById(R.id.txtValor);

        EditText edtNome = itemView.findViewById(R.id.edtNome);
        EditText edtQtd = itemView.findViewById(R.id.edtQtd);
        EditText edtValor = itemView.findViewById(R.id.edtValor);

        if ("editar".equals(modo)) {
            txtNome.setVisibility(View.GONE);
            txtQtd.setVisibility(View.GONE);
            txtValor.setVisibility(View.GONE);

            edtNome.setVisibility(View.VISIBLE);
            edtQtd.setVisibility(View.VISIBLE);
            edtValor.setVisibility(View.VISIBLE);

            edtNome.setText(item.name);
            edtQtd.setText(String.valueOf(item.quantity));
            edtValor.setText(String.format("%.2f", item.value));
        } else {
            txtNome.setVisibility(View.VISIBLE);
            txtQtd.setVisibility(View.VISIBLE);
            txtValor.setVisibility(View.VISIBLE);

            edtNome.setVisibility(View.GONE);
            edtQtd.setVisibility(View.GONE);
            edtValor.setVisibility(View.GONE);

            txtNome.setText(item.name);
            txtQtd.setText("Qtd: " + item.quantity);
            txtValor.setText(String.format("R$ %.2f", item.value));
        }

        containerItens.addView(itemView);
    }

    private void salvarAlteracoes() {
        List<ItemModel> itensAtualizados = new ArrayList<>();

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
                return;
            }

            int qtd;
            double valor;
            try {
                qtd = Integer.parseInt(qtdStr);
                if (qtd < 1) qtd = 1;
            } catch (NumberFormatException e) {
                qtd = 1;
            }

            try {
                valor = Double.parseDouble(valorStr.replace(",", "."));
                if (valor < 0) valor = 0.0;
            } catch (NumberFormatException e) {
                valor = 0.0;
            }

            ItemModel itemAtualizado = new ItemModel();
            itemAtualizado.name = nome;
            itemAtualizado.quantity = qtd;
            itemAtualizado.value = valor;

            itensAtualizados.add(itemAtualizado);
        }

        listaAtual.items = itensAtualizados;

        // aqui a logica do PUT pra atualizar a lista no BD

        if (getView() != null) {
            Navigation.findNavController(getView()).popBackStack();
        }
    }
}
