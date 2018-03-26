package logic.treinamento.request;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe responsavel pela gestão dos dados recebidos via WebService que dizem
 * respeito ao cadastro de uma nova conta corrente.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
@XmlRootElement(name = "SalvarCadastroContaCorrenteRequisicao")
@XmlAccessorType(XmlAccessType.FIELD)
public class CadastroContaCorrenteRequisicao implements Serializable {

    @XmlElement
    private String titular;

    @XmlElement
    private int agencia;

    @XmlElement
    private int banco;

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
}
