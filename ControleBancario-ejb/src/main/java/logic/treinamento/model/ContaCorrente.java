package logic.treinamento.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Classe responsavel por representar os dados da entidade conta corrente
 * persistidos no banco de dados.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
@Entity
@Table(name = "ContaCorrente")
public class ContaCorrente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idContaCorrente", unique = true, nullable = false)
    private long id;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Lancamento> lancamento;

    private BigDecimal saldo = BigDecimal.ZERO;
    private String titular;
    private boolean situacao = true;

    @Enumerated(EnumType.ORDINAL)
    private AgenciaEnum agencia;

    @Enumerated(EnumType.ORDINAL)
    private BancoEnum banco;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Lancamento> getLancamento() {
        return lancamento;
    }

    public void setLancamento(List<Lancamento> lancamento) {
        this.lancamento = lancamento;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public AgenciaEnum getAgencia() {
        return agencia;
    }

    public void setAgencia(AgenciaEnum agencia) {
        this.agencia = agencia;
    }

    public BancoEnum getBanco() {
        return banco;
    }

    public void setBanco(BancoEnum banco) {
        this.banco = banco;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

}
