package projeto.tcc.cafeteiriagoumertdelivery.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import projeto.tcc.cafeteiriagoumertdelivery.CarrinhoActivity;
import projeto.tcc.cafeteiriagoumertdelivery.ConfigProdutoActivity;
import projeto.tcc.cafeteiriagoumertdelivery.R;
import projeto.tcc.cafeteiriagoumertdelivery.model.ProdutoModel;
import projeto.tcc.cafeteiriagoumertdelivery.util.CarrinhoUtil;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.MyViewHolder> {

    private DatabaseReference cupcakesRef = FirebaseDatabase.getInstance().getReference().child("cupcakes");

    List<ProdutoModel> list;
    Activity a;
    boolean gerente;
    boolean carrinho = false;

    public AdapterProdutos(List<ProdutoModel> list, Activity a, boolean gerente) {
        this.list = list;
        this.a = a;
        this.gerente = gerente;
    }

    public AdapterProdutos(List<ProdutoModel> list, Activity a, boolean gerente, boolean carrinho) {
        this.list = list;
        this.a = a;
        this.gerente = gerente;
        this.carrinho = carrinho;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false));
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProdutoModel produtoSeleciconado = list.get(position);

        holder.excluir_produto.setOnClickListener(v -> {
            cupcakesRef.child(produtoSeleciconado.getId()).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(a, "Removido com Sucesso!", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    this.notifyDataSetChanged();
                }
            });
        });

        holder.remover_carrinho.setOnClickListener(v -> {
            List<ProdutoModel> carrinhoList = CarrinhoUtil.returnCarrinho(a);

            for (int i = carrinhoList.size() - 1; i >= 0; i--) {
                ProdutoModel p = carrinhoList.get(i);
                if (p.getNome().equals(produtoSeleciconado.getNome())) {
                    carrinhoList.remove(i);
                    list.remove(i);
                }
            }

            CarrinhoUtil.saveCarrinho(carrinhoList, a);
            Toast.makeText(a, "Produto Removido", Toast.LENGTH_SHORT).show();
            a.finish();
            a.startActivity(new Intent(a, CarrinhoActivity.class));
        });

        if (!carrinho) {
            if (gerente) {
                holder.editarProduto.setVisibility(View.VISIBLE);
                holder.excluir_produto.setVisibility(View.VISIBLE);
            } else {
                holder.add_carrinho.setVisibility(View.VISIBLE);
                holder.add_carrinho.setOnClickListener(v -> {
                    List<ProdutoModel> carrinho = CarrinhoUtil.returnCarrinho(a);
                    carrinho.add(produtoSeleciconado);
                    CarrinhoUtil.saveCarrinho(carrinho, a);
                    Toast.makeText(a, "Produto Adicionado ao Carrinho!", Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            holder.remover_carrinho.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(produtoSeleciconado.getImagem()).into(holder.fotoProduto);

        holder.editarProduto.setOnClickListener(v -> {
            Intent i = new Intent(a, ConfigProdutoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("produto", new Gson().toJson(produtoSeleciconado));
            a.startActivity(i);
        });

        StringBuilder infoProduto = new StringBuilder();
        infoProduto.append("<b>").append(produtoSeleciconado.getNome()).append("</b>").append("<br>")
                .append(produtoSeleciconado.getDescricao());
        holder.textNome.setText(Html.fromHtml(infoProduto.toString()));

        holder.textPreco.setText("R$ " + produtoSeleciconado.getPreco());
        Picasso.get().load(produtoSeleciconado.getImagem()).into(holder.fotoProduto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button editarProduto, add_carrinho, remover_carrinho, excluir_produto;
        TextView textPreco, textNome;
        ImageView fotoProduto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            editarProduto = itemView.findViewById(R.id.editar_produto);
            textPreco = itemView.findViewById(R.id.textPreco);
            textNome = itemView.findViewById(R.id.textProduto);
            fotoProduto = itemView.findViewById(R.id.imageProduto);
            add_carrinho = itemView.findViewById(R.id.add_carrinho);
            remover_carrinho = itemView.findViewById(R.id.remover_carrinho);
            excluir_produto = itemView.findViewById(R.id.remover_produto);
        }
    }
}
