package logic.treinamento.observer;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import logic.treinamento.bean.InterfaceGestaoContas;
import logic.treinamento.request.AtualizarCadastroContaCorrenteRequisicao;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;

/**
 * Classe responsavel pela gestão dos metodos invocados atraves de eventos
 * controlados pelo CDI.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
@Stateless
public class EventosGestaoContas implements Serializable {

    @Inject
    Event<LancamentoBancarioRequisicao> eventoSalvarLancamentoBancario;

    @Inject
    Event<LancamentoBancarioAtualizacaoRequisicao> eventoAtualizarLancamentoBancario;

    @Inject
    Event<Long> eventoExcluirLancamentoBancario;

    @Inject
    Event<CadastroContaCorrenteRequisicao> eventoSalvarContaCorrente;

    @Inject
    Event<Long> eventoExcluirContaCorrente;

    @Inject
    Event<Long> eventoInativarContaCorrente;

    @Inject
    Event<AtualizarCadastroContaCorrenteRequisicao> eventoAtualizarContaCorrente;

    @Inject
    InterfaceGestaoContas GestaoContasBean;

    /**
     * Método de chamada do evento para salvar um lancamento bancario
     *
     * @author Tadeu
     * @param lancamento LancamentoBancarioRequisicao - Dados do lancamento
     * bancario que sera salvo.
     */
    public void salvarLacamentoBancario(LancamentoBancarioRequisicao lancamento) {
        eventoSalvarLancamentoBancario.fire(lancamento);
    }

    /**
     * Método de chamada do evento para atualizar um lancamento bancario
     *
     * @author Tadeu
     * @param atualizarLancamentoRequisicao
     * LancamentoBancarioAtualizacaoRequisicao - Dados do lancamento bancario
     * que sera atualizado.
     */
    public void atualizarLancamentoBancario(LancamentoBancarioAtualizacaoRequisicao atualizarLancamentoRequisicao) {
        eventoAtualizarLancamentoBancario.fire(atualizarLancamentoRequisicao);
    }

    /**
     * Método de chamada do evento para excluir um lancamento bancario
     *
     * @author Tadeu
     * @param idLancamento long - ID do lancamento bancario que sera excluido.
     */
    public void excluirLancamentoBancario(long idLancamento) {
        eventoExcluirLancamentoBancario.fire(idLancamento);
    }

    /**
     * Método de chamada do evento para salvar uma nova conta corrente
     *
     * @author Tadeu
     * @param contaCorrente CadastroContaCorrenteRequisicao - Dados da conta
     * corrente que sera salva.
     */
    public void salvarContaCorrente(CadastroContaCorrenteRequisicao contaCorrente) {
        eventoSalvarContaCorrente.fire(contaCorrente);
    }

    /**
     * Método de chamada do evento para excluir uma conta corrente
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da cobta corrente que sera excluida.
     */
    public void excluirContaCorrente(long idContaCorrente) {
        eventoExcluirContaCorrente.fire(idContaCorrente);
    }

    /**
     * Método de chamada do evento para inativar uma conta corrente
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da cobta corrente que sera inativada.
     */
    public void inativarContaCorrente(long idContaCorrente) {
        eventoInativarContaCorrente.fire(idContaCorrente);
    }

    /**
     * Método de chamada do evento para atualizar uma conta corrente
     *
     * @author Tadeu
     * @param contaCorrente AtualizarCadastroContaCorrenteRequisicao - Dados da
     * conta corrente que sera atualizada.
     */
    public void atualizarDadosContaCorrente(AtualizarCadastroContaCorrenteRequisicao contaCorrente) {
        eventoAtualizarContaCorrente.fire(contaCorrente);
    }
}
