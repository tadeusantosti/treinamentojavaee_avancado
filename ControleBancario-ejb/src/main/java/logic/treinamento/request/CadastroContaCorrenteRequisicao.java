package logic.treinamento.request;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SalvarCadastroContaCorrenteRequisicao")
@XmlAccessorType(XmlAccessType.FIELD)
public class CadastroContaCorrenteRequisicao implements Serializable {

    @XmlElement
    private String titular;

    @XmlElement
    private int agencia;

    @XmlElement
    private int banco;

    @XmlElement
    private BigDecimal saldo = BigDecimal.ZERO;

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public int getBanco() {
        return banco;
    }

    public void setBanco(int banco) {
        this.banco = banco;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
