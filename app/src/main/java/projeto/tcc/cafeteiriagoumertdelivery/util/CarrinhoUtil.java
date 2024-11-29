package projeto.tcc.cafeteiriagoumertdelivery.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import projeto.tcc.cafeteiriagoumertdelivery.model.ProdutoModel;

public class CarrinhoUtil {

    public static String prefs = "Carrinho";
    public static String data = "produtos";

    public static void saveCarrinho(List<ProdutoModel> lista, Activity c) {
        SharedPreferences preferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(data, new Gson().toJson(lista)).apply();
    }

    public static List<ProdutoModel> returnCarrinho(Activity c) {
        SharedPreferences preferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);

        List<ProdutoModel> carrinho = new Gson().fromJson(preferences.getString(data, ""), new TypeToken<List<ProdutoModel>>() {
        }.getType());

        if (carrinho == null) return new ArrayList<>();

        return carrinho;
    }
}
