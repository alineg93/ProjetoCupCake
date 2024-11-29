package projeto.tcc.cafeteiriagoumertdelivery;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import projeto.tcc.cafeteiriagoumertdelivery.adapter.AdapterProdutos;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityCarrinhoBinding;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityMainBinding;
import projeto.tcc.cafeteiriagoumertdelivery.model.PedidoModel;
import projeto.tcc.cafeteiriagoumertdelivery.model.ProdutoModel;
import projeto.tcc.cafeteiriagoumertdelivery.util.CarrinhoUtil;

public class CarrinhoActivity extends AppCompatActivity {

    private DatabaseReference redPedidos = FirebaseDatabase.getInstance().getReference("pedidosCupCake");

    private ActivityCarrinhoBinding mainBinding;
    private AdapterProdutos adapterProdutos;
    private List<ProdutoModel> listaProdutos;

    @SuppressLint({"SimpleDateFormat", "NotifyDataSetChanged", "InlinedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityCarrinhoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Carrinho");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        mainBinding.produtoRv.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.produtoRv.setHasFixedSize(true);
        mainBinding.produtoRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listaProdutos = CarrinhoUtil.returnCarrinho(this);
        adapterProdutos = new AdapterProdutos(listaProdutos, this, false, true);
        mainBinding.produtoRv.setAdapter(adapterProdutos);


        mainBinding.total.setText(calcularValorTotal());

        mainBinding.finalizar.setOnClickListener(v -> {

            PedidoModel pedidoModel = new PedidoModel();
            pedidoModel.setData_hora(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
            pedidoModel.setProdutos(listaProdutos);
            pedidoModel.setTotal_compra(calcularValorTotal());
            pedidoModel.setId(UUID.randomUUID().toString());

            if (mainBinding.radioButton.isChecked()) {
                pedidoModel.setMetodo_pagamento("Cartão");
            } else if (mainBinding.radioButton2.isChecked()) {
                pedidoModel.setMetodo_pagamento("Pix");
            } else {
                pedidoModel.setMetodo_pagamento("Dinheiro");
            }

            redPedidos.child(pedidoModel.getId()).setValue(pedidoModel);
            CarrinhoUtil.saveCarrinho(new ArrayList<>(), this);
            listaProdutos.clear();
            mainBinding.total.setText(calcularValorTotal());
            adapterProdutos.notifyDataSetChanged();

            enviarNotificacao("Pedido Realizado", "O seu pedido já foi entregue para o gerente responsável.");
            Toast.makeText(this, "Pedido Realizado", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("DefaultLocale")
    private String calcularValorTotal() {

        float soma = 0;
        for (ProdutoModel produtoModel : listaProdutos) {
            soma += Float.parseFloat(produtoModel.getPreco().replace(",", "."));
        }

        return String.format("Total: R$ %.2f", soma).replace(".", ",");
    }


    @SuppressLint({"MissingPermission", "NotificationPermission", "NewApi"})
    public void enviarNotificacao(String titulo, String descricao) {
        String canalId = "meu_canal_id";

        // Configura o canal de notificação apenas para Android Oreo ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nome = "Canal de Notificação";
            String descricaoCanal = "Canal para notificações simples";
            int importancia = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel canal = new NotificationChannel(canalId, nome, importancia);
            canal.setDescription(descricaoCanal);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(canal);
        }

        // Constrói a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canalId)
                .setSmallIcon(R.drawable.baseline_coffee_24) // Ícone da notificação
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Fecha a notificação ao clicar

        // Envia a notificação
        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
        notificationManager1.notify(1, builder.build());
    }

}