package logic.treinamento.request;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe responsavel pela gestão dos dados recebidos via WebService que dizem
 * respeito ao cadastro de um novo lancamento bancario.
 *
 * @since 1.0
 * @author Tadeu
 * @version 2.0
 */
@XmlRootElement(name = "SalvarLancamentoBancarioRequisicao")
@XmlAccessorType(XmlAccessType.FIELD)
public class LancamentoBancarioRequisicao implements Serializable {

    @XmlElement
    private String observacao;

    @XmlElement
    private BigDecimal valor;

    @XmlElement
    private int idTipoLancamento;

    @XmlElement
    private String data;

    @XmlElement
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

    public int getIdTipoLancamento() {
        return idTipoLancamento;
    }

    public void setIdTipoLancamento(int idTipoLancamento) {
        this.idTipoLancamento = idTipoLancamento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getIdContaCorrente() {
        return idContaCorrente;
    }

    public void setIdContaCorrente(long idContaCorrente) {
        this.idContaCorrente = idContaCorrente;
    }
}
