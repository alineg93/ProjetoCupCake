package projeto.tcc.cafeteiriagoumertdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityRegistrarUsuarioBinding;
import projeto.tcc.cafeteiriagoumertdelivery.model.UsuarioModel;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private ActivityRegistrarUsuarioBinding mainBinding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usersCupCake");
    private UsuarioModel usuarioModel;

    private boolean fazerCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRegistrarUsuarioBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Novo Registro");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        usuarioModel = new UsuarioModel();
        usuarioModel.setId(UUID.randomUUID().toString());
        fazerCadastro = true;


        mainBinding.botaoAcao.setOnClickListener(v -> {
            String nome, email, senha, endereco;

            nome = mainBinding.nome.getText().toString();
            email = mainBinding.email.getText().toString();
            senha = mainBinding.senha.getText().toString();
            endereco = mainBinding.endereco.getText().toString();

            if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {

                usuarioModel.setEmail(email);
                usuarioModel.setNome(nome);
                usuarioModel.setSenha(senha);
                usuarioModel.setEndereco(endereco);


                if (fazerCadastro) {
                    auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            usersRef.child(usuarioModel.getId()).setValue(usuarioModel).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful())
                                    startActivity(new Intent(this, VisualizarProdutosActivity.class));
                                Toast.makeText(this, "Informações salvas no banco!", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            Toast.makeText(this, "Essas informações não podem ser cadastradas!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }
}