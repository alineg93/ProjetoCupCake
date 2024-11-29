package projeto.tcc.cafeteiriagoumertdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        if (auth.getCurrentUser() != null)
            startActivity(new Intent(this, VisualizarProdutosActivity.class));

        mainBinding.logar.setOnClickListener(v -> {
            String login, senha;

            login = mainBinding.login.getText().toString();
            senha = mainBinding.senha.getText().toString();

            if ( login.equals("gerente") && senha.equals("adm")){
               startActivity(new Intent(this, VisualizarProdutosActivity.class).putExtra("gerente", true));
            }else{
                if (!login.isEmpty() && !senha.isEmpty()) {
                    auth.signInWithEmailAndPassword(login, senha).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Bem-vindo de volta", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, VisualizarProdutosActivity.class));
                        } else {
                            Toast.makeText(this, "Informações incorretas.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


        });

        assert mainBinding.fazerRegistro != null;
        mainBinding.fazerRegistro.setOnClickListener(v -> startActivity(new Intent(this, RegistrarUsuarioActivity.class)));

    }
}