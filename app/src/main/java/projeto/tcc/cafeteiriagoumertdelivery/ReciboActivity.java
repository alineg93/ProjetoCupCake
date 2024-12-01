package projeto.tcc.cafeteiriagoumertdelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityMainBinding;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityReciboBinding;

public class ReciboActivity extends AppCompatActivity {

    private ActivityReciboBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityReciboBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Recibo");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Bundle b =  getIntent().getExtras();
        assert b != null;
        mainBinding.cliente.setText(b.getString("Cliente", ""));
        mainBinding.total.setText(b.getString("total", ""));

        mainBinding.voltar.setOnClickListener(v -> startActivity(new Intent(this, VisualizarProdutosActivity.class)));

    }
}