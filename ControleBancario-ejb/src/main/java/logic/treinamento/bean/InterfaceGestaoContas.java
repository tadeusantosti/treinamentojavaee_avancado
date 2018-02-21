package logic.treinamento.bean;

import java.util.List;
import javax.ejb.Local;
import logic.treinamento.model.Lancamento;
import logic.treinamento.request.AtualizarLancamentoRequisicao;
import logic.treinamento.request.LancarContasDoMesRequisicao;

@Local
public interface InterfaceGestaoContas {
    
    public void salvarLancamentoBancario(LancarContasDoMesRequisicao lancarContasDoMesRequisicao) throws Exception;
    
    public List<Lancamento> pesquisarLancamentoBancarioPorTipoDeLancamento(int idtipolancamento) throws Exception;

    public List<Lancamento> pesquisarLancamentoBancarioPorNome(String nome) throws Exception;

    public List<Lancamento> pesquisarLancamentoBancarioPorPeriodo(String dataInicial, String dataFinal) throws Exception;

    public void excluirLancamentoBancario(long idLancamento) throws Exception;

    public void atualizarLancamentoBancario(AtualizarLancamentoRequisicao atualizarLancamentoRequisicao) throws Exception;

    public String validarCamposObrigatorios(Lancamento lanc);


}
