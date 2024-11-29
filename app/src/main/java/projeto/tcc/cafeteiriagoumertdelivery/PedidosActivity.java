package projeto.tcc.cafeteiriagoumertdelivery;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import projeto.tcc.cafeteiriagoumertdelivery.adapter.AdapterPedidos;
import projeto.tcc.cafeteiriagoumertdelivery.adapter.AdapterProdutos;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityAtualizarCadastroBinding;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityPedidosBinding;
import projeto.tcc.cafeteiriagoumertdelivery.model.PedidoModel;

public class PedidosActivity extends AppCompatActivity {

    private DatabaseReference redPedidos = FirebaseDatabase.getInstance().getReference("pedidosCupCake");

    private ActivityPedidosBinding mainBinding;
    private List<PedidoModel> listaPedido = new ArrayList<>();
    private AdapterPedidos dapterPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityPedidosBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pedidos");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        mainBinding.rvPedidos.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.rvPedidos.setHasFixedSize(true);
        mainBinding.rvPedidos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dapterPedidos = new AdapterPedidos(listaPedido, this);
        mainBinding.rvPedidos.setAdapter(dapterPedidos);

    }

    @Override
    protected void onStart() {
        super.onStart();
        listarPedidos();
    }

    private void listarPedidos(){
        redPedidos.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    listaPedido.clear();
                    for ( DataSnapshot dado : snapshot.getChildren()){
                        PedidoModel pedidoModel = dado.getValue(PedidoModel.class);
                        listaPedido.add(pedidoModel);
                    }
                    Collections.shuffle(listaPedido);
                    dapterPedidos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}