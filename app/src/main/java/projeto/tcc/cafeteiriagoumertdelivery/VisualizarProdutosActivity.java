package projeto.tcc.cafeteiriagoumertdelivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import projeto.tcc.cafeteiriagoumertdelivery.adapter.AdapterProdutos;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityMainBinding;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityVisualizarProdutosBinding;
import projeto.tcc.cafeteiriagoumertdelivery.model.ProdutoModel;

public class VisualizarProdutosActivity extends AppCompatActivity {

    private ActivityVisualizarProdutosBinding mainBinding;
    private DatabaseReference cupcakesRef = FirebaseDatabase.getInstance().getReference().child("cupcakes");
    private Bundle bundle;

    private boolean isGerente = false;
    private List<ProdutoModel> listaProdutos = new ArrayList<>();
    private AdapterProdutos adapterProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityVisualizarProdutosBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Produtos");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        try {
            bundle = getIntent().getExtras();
            isGerente = bundle.getBoolean("gerente", false);
            if (isGerente) {
                mainBinding.floatEditarUsuario.setVisibility(View.GONE);
                mainBinding.adicionarProduto.setVisibility(View.VISIBLE);
                mainBinding.floatPedido.setVisibility(View.VISIBLE);
                mainBinding.adicionarProduto.setOnClickListener(v -> startActivity(new Intent(this, ConfigProdutoActivity.class)));

                mainBinding.floatPedido.setOnClickListener(v -> startActivity(new Intent(this, PedidosActivity.class)));
            }
        } catch (Exception e) {
        }

        mainBinding.floatEditarUsuario.setOnClickListener(v -> startActivity(new Intent(this, AtualizarCadastroActivity.class)));

        mainBinding.recyclerProduto.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.recyclerProduto.setHasFixedSize(true);
        mainBinding.recyclerProduto.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapterProdutos = new AdapterProdutos(listaProdutos, this, isGerente);
        mainBinding.recyclerProduto.setAdapter(adapterProdutos);

        mainBinding.btnPesquisa.setOnClickListener(v ->
                preencherLista(mainBinding.edtPesquisa.getText().toString())
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        preencherLista("");
    }

    private void preencherLista(String query) {
        listaProdutos.clear();
        cupcakesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dado : snapshot.getChildren()) {
                        ProdutoModel produtoModel = dado.getValue(ProdutoModel.class);
                        if (query.isEmpty()) {
                            listaProdutos.add(produtoModel);
                        } else {
                            if (produtoModel.getNome().contains(query)) {
                                listaProdutos.add(produtoModel);
                            }
                        }
                    }
                    adapterProdutos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(VisualizarProdutosActivity.this).inflate(R.menu.menu_carrinho, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.carrinho) {
            startActivity(new Intent(this, CarrinhoActivity.class));
        } else if (item.getItemId() == R.id.sair) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}