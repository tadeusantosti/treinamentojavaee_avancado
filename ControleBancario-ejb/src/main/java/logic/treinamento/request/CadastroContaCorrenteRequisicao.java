package logic.treinamento.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import logic.treinamento.model.Lancamento;

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
    List<Lancamento> lancamentos;

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

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }
}
