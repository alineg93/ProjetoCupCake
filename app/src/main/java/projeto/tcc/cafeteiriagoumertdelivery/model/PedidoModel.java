package projeto.tcc.cafeteiriagoumertdelivery.model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class PedidoModel {

    private String id;
    private List<ProdutoModel> produtos = new ArrayList();
    private String data_hora = "";
    private String total_compra = "";
    private String metodo_pagamento = "";
    private String status = "Concluído";

    public PedidoModel() {
    }

    public String getTotal_compra() {
        return total_compra;
    }

    public String getId() {
        return id;
    }

    public String getMetodo_pagamento() {
        return metodo_pagamento;
    }

    public void setMetodo_pagamento(String metodo_pagamento) {
        this.metodo_pagamento = metodo_pagamento;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTotal_compra(String total_compra) {
        this.total_compra = total_compra;
    }

    public List<ProdutoModel> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoModel> produtos) {
        this.produtos = produtos;
    }

    public String getData_hora() {
        return data_hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }
}
