package logic.treinamento.observer;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import logic.treinamento.request.AtualizarLancamentoRequisicao;
import logic.treinamento.request.LancarContasDoMesRequisicao;

@Stateless
public class EventosGestaoContas implements Serializable {

    @Inject
    Event<LancarContasDoMesRequisicao> eventoLancar;

    @Inject
    Event<AtualizarLancamentoRequisicao> eventoAtualizar;

    @Inject
    Event<Long> eventoExcluir;

    public void salvarLacamentoBancario(LancarContasDoMesRequisicao lancamento) {
        eventoLancar.fire(lancamento);
    }

    public void atualizarLancamentoBancario(AtualizarLancamentoRequisicao atualizarLancamentoRequisicao) {
        eventoAtualizar.fire(atualizarLancamentoRequisicao);
    }

    public void excluirLancamentoBancario(long idLancamento) {
        eventoExcluir.fire(idLancamento);
    }
}
