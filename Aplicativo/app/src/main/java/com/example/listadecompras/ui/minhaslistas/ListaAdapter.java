package com.example.listadecompras.ui.minhaslistas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadecompras.ui.deletelist.OnDeleteClickListener;
import com.example.listadecompras.R;
import com.example.listadecompras.models.ListModel;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder> {

    private List<ListModel> lista;
    private OnDeleteClickListener deleteClickListener;

    public ListaAdapter(List<ListModel> lista, OnDeleteClickListener deleteClickListener) {
        this.lista = lista;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);
        return new ListaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder holder, int position) {
        ListModel model = lista.get(position);
        holder.txtTitulo.setText(model.title);
        holder.txtQtdItens.setText("Itens: " + model.items.size());
        holder.txtCodigo.setText("Código: " + model.code);
        holder.txtDataCriacao.setText("Criado em: " + model.createdAt.split("T")[0]);
        holder.txtDataExpiracao.setText("Expira em: " + model.expiresAt.split("T")[0]);

        View.OnClickListener clickListener = v -> {
            Bundle bundle = new Bundle();
            bundle.putString("modo", (v == holder.btnVisualizar) ? "visualizar" : "editar");
            bundle.putSerializable("lista", model);

            Navigation.findNavController(v).navigate(R.id.listaDetalhesFragment, bundle);
        };

        holder.btnVisualizar.setOnClickListener(clickListener);
        holder.btnEditar.setOnClickListener(clickListener);

        // Listener de deletar
        holder.btnDeletar.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(model, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void removeItem(int position) {
        lista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lista.size());
    }

    static class ListaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtQtdItens, txtCodigo, txtDataCriacao, txtDataExpiracao;
        ImageButton btnVisualizar, btnEditar, btnDeletar;

        public ListaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtQtdItens = itemView.findViewById(R.id.txtQtdItens);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtDataCriacao = itemView.findViewById(R.id.txtDataCriacao);
            txtDataExpiracao = itemView.findViewById(R.id.txtDataExpiracao);
            btnVisualizar = itemView.findViewById(R.id.btnVisualizar);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }
}
