package projeto.tcc.cafeteiriagoumertdelivery;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;

import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityConfigProdutoBinding;
import projeto.tcc.cafeteiriagoumertdelivery.databinding.ActivityMainBinding;
import projeto.tcc.cafeteiriagoumertdelivery.model.ProdutoModel;

public class ConfigProdutoActivity extends AppCompatActivity {

    private Dialog dialogCarregando;

    private String imageUrl = "";

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private ProdutoModel produtoModel;
    private ActivityConfigProdutoBinding mainBinding;
    private DatabaseReference cupcakesRef = FirebaseDatabase.getInstance().getReference("cupcakes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityConfigProdutoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Produto");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // quando for atualizar produto lembrar de setar o link com o link

        try {
            Bundle b = getIntent().getExtras();
            produtoModel = new Gson().fromJson(
                    b.getString("produto", ""),
                    new TypeToken<ProdutoModel>() {
                    }.getType()
            );
            imageUrl = produtoModel.getImagem();

            Picasso.get().load(imageUrl).into(mainBinding.produtoImg);
            mainBinding.nomeProdutoEdt.setText(produtoModel.getNome());
            mainBinding.descricaoProdutoEdt.setText(produtoModel.getDescricao());
            mainBinding.precoProdutoEdt.setText(produtoModel.getPreco());
        } catch (Exception ignored) {
            produtoModel = new ProdutoModel();
        }

        mainBinding.produtoImg.setOnClickListener(v -> openGallery());
        mainBinding.salvarProduto.setOnClickListener(v -> {

            String nome = mainBinding.nomeProdutoEdt.getText().toString();
            String descricao = mainBinding.descricaoProdutoEdt.getText().toString();
            String preco = mainBinding.precoProdutoEdt.getText().toString();

            if (produtoModel.getId().isEmpty()) {
                produtoModel.setId(UUID.randomUUID().toString());
            }

            produtoModel.setNome(nome);
            produtoModel.setImagem(imageUrl);
            Log.d("mayara", "onCreate: " + imageUrl);
            produtoModel.setDescricao(descricao);
            produtoModel.setPreco(preco);

            cupcakesRef.child(produtoModel.getId()).setValue(produtoModel).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Toast.makeText(this, "Salvo com Êxito!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Tente Novamente!", Toast.LENGTH_SHORT).show();
            });

        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione a Imagem"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            mainBinding.produtoImg.setImageURI(imageUri);
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("Carregando...");
            b.setCancelable(false);
            dialogCarregando = b.create();
            dialogCarregando.show();

            // Referência ao Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("imagens/" + UUID.randomUUID().toString());

            // Faz o upload da imagem para o Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Obtém o link da imagem após o upload
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUrl = uri.toString();
                            dialogCarregando.dismiss();
                        });
                    })
                    .addOnFailureListener(e -> {
                        dialogCarregando.dismiss();
                    });
        } else {
            dialogCarregando.dismiss();
            Toast.makeText(this, "Nenhuma imagem selecionada!", Toast.LENGTH_SHORT).show();
        }
    }

}