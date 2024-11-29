package projeto.tcc.cafeteiriagoumertdelivery.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import projeto.tcc.cafeteiriagoumertdelivery.R;
import projeto.tcc.cafeteiriagoumertdelivery.model.PedidoModel;
import projeto.tcc.cafeteiriagoumertdelivery.model.ProdutoModel;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.MyViewHolder> {

    List<PedidoModel> lista;
    Context c;

    public AdapterPedidos(List<PedidoModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PedidoModel p = lista.get(position);

        StringBuilder builder = new StringBuilder();
        builder.append("<b>").append("Data: ").append("</b>").append(p.getData_hora()).append("<br>");
        builder.append("<b>").append("Total: ").append("</b>").append(p.getTotal_compra()).append("<br>");
        builder.append("<b>").append("Método: ").append("</b>").append(p.getMetodo_pagamento()).append("<br>");
        builder.append("<b>").append("Status: ").append("</b>").append(p.getStatus()).append("<br>");
        builder.append("<b>").append("Produtos: ").append("</b>").append("<br>");

        for (ProdutoModel produto : p.getProdutos()) {
            builder.append(produto.getNome()).append(" ").append(produto.getPreco()).append("<br>");
        }

        holder.textView_pedido.setText(
                Html.fromHtml(builder.toString())
        );

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView
            .ViewHolder {

        TextView textView_pedido;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_pedido = itemView.findViewById(R.id.textView_pedido);
        }
    }
}
