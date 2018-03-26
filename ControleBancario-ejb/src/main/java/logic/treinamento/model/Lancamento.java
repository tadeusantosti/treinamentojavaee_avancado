package logic.treinamento.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe responsavel por representar os dados da entidade lancamento
 * persistidos no banco de dados.
 *
 * @since 1.0
 * @author Tadeu
 * @version 2.0
 */
@Entity
@Table(name = "Lancamento")
public class Lancamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String observacao, dataGUI;
    private Date data;
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private TipoLancamentoEnum tipoLancamento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_contacorrente")
    private ContaCorrente conta;

    private long idContaCorrente;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TipoLancamentoEnum getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(TipoLancamentoEnum tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataGUI() {
        return dataGUI;
    }

    public void setDataGUI(String dataGUI) {
        this.dataGUI = dataGUI;
    }

    public ContaCorrente getConta() {
        return conta;
    }

    public void setConta(ContaCorrente conta) {
        this.conta = conta;
    }

    public long getIdContaCorrente() {
        return idContaCorrente;
    }

    public void setIdContaCorrente(long idContaCorrente) {
        this.idContaCorrente = idContaCorrente;
    }

}
