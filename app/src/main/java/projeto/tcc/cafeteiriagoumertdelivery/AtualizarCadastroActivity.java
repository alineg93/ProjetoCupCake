package projeto.tcc.cafeteiriagoumertdelivery;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityAtualizarCadastroBinding;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityMainBinding;
import projeto.tcc.cafeteiriagoumertdelivery.model.ProdutoModel;
import projeto.tcc.cafeteiriagoumertdelivery.model.UsuarioModel;

public class AtualizarCadastroActivity extends AppCompatActivity {

    private ActivityAtualizarCadastroBinding mainBinding;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usersCupCake");


    private UsuarioModel usuarioAtual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityAtualizarCadastroBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Atualizar");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dado : snapshot.getChildren()) {
                    UsuarioModel usuarioModel = dado.getValue(UsuarioModel.class);
                    if (usuarioModel.getEmail().equals(user.getEmail())) {
                        usuarioAtual = usuarioModel;
                        mainBinding.nomeUsuario.setText(usuarioModel.getNome());
                        mainBinding.enderecoUsuario.setText(usuarioModel.getEndereco());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mainBinding.btnSalvar.setOnClickListener(v -> {
            String novoNome = mainBinding.nomeUsuario.getText().toString();
            String novoEndereco = mainBinding.nomeUsuario.getText().toString();

            if (usuarioAtual != null) {
                usuarioAtual.setNome(novoNome);
                usuarioAtual.setEndereco(novoEndereco);
                usersRef.child(usuarioAtual.getId()).setValue(usuarioAtual).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Salvo!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}