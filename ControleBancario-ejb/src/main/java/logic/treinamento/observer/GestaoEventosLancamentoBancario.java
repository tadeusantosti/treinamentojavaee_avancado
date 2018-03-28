package logic.treinamento.observer;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import logic.treinamento.bean.InterfaceGestaoContas;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioExclusaoRequisicao;
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
public class GestaoEventosLancamentoBancario implements Serializable {

    @Inject
    Event<LancamentoBancarioRequisicao> eventoSalvarLancamentoBancario;

    @Inject
    Event<LancamentoBancarioAtualizacaoRequisicao> eventoAtualizarLancamentoBancario;

    @Inject
    Event<LancamentoBancarioExclusaoRequisicao> eventoExcluirLancamentoBancario;

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
     * @param lancamentoRemocao LancamentoBancarioExclusaoRequisicao -
     * lancamento bancario que sera excluido.
     */
    public void excluirLancamentoBancario(LancamentoBancarioExclusaoRequisicao lancamentoRemocao) {
        eventoExcluirLancamentoBancario.fire(lancamentoRemocao);
    }

}
