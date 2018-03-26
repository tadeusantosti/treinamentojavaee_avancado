package logic.treinamento.request;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe responsavel pela gestão dos dados recebidos via WebService que dizem
 * respeito a atualizacao dos dados de um lancamento bancario.
 *
 * @since 1.0
 * @author Tadeu
 * @version 2.0
 */
@XmlRootElement(name = "AtualizarLancamentoRequisicao")
@XmlAccessorType(XmlAccessType.FIELD)
public class LancamentoBancarioAtualizacaoRequisicao implements Serializable {

    @XmlElement
    private long id;
    
    @XmlElement
    private long IdContaCorrente;

    @XmlElement
    private String observacaoAtualizada;

    @XmlElement
    private String dataAtualizada;

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

    public String getDataAtualizada() {
        return dataAtualizada;
    }

    public void setDataAtualizada(String dataAtualizada) {
        this.dataAtualizada = dataAtualizada;
    }

    public long getIdContaCorrente() {
        return IdContaCorrente;
    }

    public void setIdContaCorrente(long IdContaCorrente) {
        this.IdContaCorrente = IdContaCorrente;
    }
}
