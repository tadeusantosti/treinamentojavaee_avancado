package logic.treinamento.request;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe responsavel pela gestão dos dados recebidos via WebService que dizem
 * respeito a remoção dos dados de um lancamento bancario.
 *
 * @since 1.0
 * @author Tadeu
 * @version 2.0
 */
@XmlRootElement(name = "AtualizarLancamentoRequisicao")
@XmlAccessorType(XmlAccessType.FIELD)
public class LancamentoBancarioExclusaoRequisicao implements Serializable {

    @XmlElement
    private long idLancamento;


    public long getIdLancamento() {
        return idLancamento;
    }

    public void setIdLancamento(long idLancamento) {
        this.idLancamento = idLancamento;
    }
}
