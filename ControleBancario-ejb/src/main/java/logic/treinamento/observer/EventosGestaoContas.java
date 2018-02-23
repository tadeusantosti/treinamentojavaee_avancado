package logic.treinamento.observer;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;

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

    public void salvarLacamentoBancario(LancamentoBancarioRequisicao lancamento) {
        eventoSalvarLancamentoBancario.fire(lancamento);
    }

    public void atualizarLancamentoBancario(LancamentoBancarioAtualizacaoRequisicao atualizarLancamentoRequisicao) {
        eventoAtualizarLancamentoBancario.fire(atualizarLancamentoRequisicao);
    }

    public void excluirLancamentoBancario(long idLancamento) {
        eventoExcluirLancamentoBancario.fire(idLancamento);
    }
    
    public void salvarContaCorrente(CadastroContaCorrenteRequisicao contaCorrente) {
        eventoSalvarContaCorrente.fire(contaCorrente);
    }
}
