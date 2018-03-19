package logic.treinamento.request;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AtualizarLancamentoRequisicao")
@XmlAccessorType(XmlAccessType.FIELD)
public class LancamentoBancarioAtualizacaoRequisicao implements Serializable {

    @XmlElement
    private long id;

    @XmlElement
    private String observacaoAtualizada;

    @XmlElement
    private BigDecimal valorAtualizado;

    @XmlElement
    private int idTipoLancamentoAtualizado;

    @XmlElement
    private String dataAtualizada;

    @XmlElement
    private long idContaCorrente;

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObservacaoAtualizada() {
        return observacaoAtualizada;
    }

    public void setObservacaoAtualizada(String nomeAtualizado) {
        this.observacaoAtualizada = nomeAtualizado;
    }

    public BigDecimal getValorAtualizado() {
        return valorAtualizado;
    }

    public void setValorAtualizado(BigDecimal valorAtualizado) {
        this.valorAtualizado = valorAtualizado;
    }

    public int getIdTipoLancamentoAtualizado() {
        return idTipoLancamentoAtualizado;
    }

    public void setIdTipoLancamentoAtualizado(int idTipoLancamentoAtualizado) {
        this.idTipoLancamentoAtualizado = idTipoLancamentoAtualizado;
    }

    public String getDataAtualizada() {
        return dataAtualizada;
    }

    public void setDataAtualizada(String dataAtualizada) {
        this.dataAtualizada = dataAtualizada;
    }

    public long getIdContaCorrente() {
        return idContaCorrente;
    }

    public void setIdContaCorrente(long idContaCorrente) {
        this.idContaCorrente = idContaCorrente;
    }
}
