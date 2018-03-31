package logic.treinamento.bean;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Local;
import javax.enterprise.event.Observes;
import logic.treinamento.model.ContaCorrente;
import logic.treinamento.model.Lancamento;
import logic.treinamento.request.AtualizarCadastroContaCorrenteRequisicao;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioExclusaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;

@Local
public interface InterfaceGestaoContas {
    
    public void salvarLancamentoBancario(LancamentoBancarioRequisicao lancarContasDoMesRequisicao) throws Exception;
    
    public List<Lancamento> pesquisarLancamentoBancarioPorTipoDeLancamento(int idtipolancamento) throws Exception;

    public List<Lancamento> pesquisarLancamentoBancarioPorObservacao(String nome) throws Exception;

    public List<Lancamento> pesquisarLancamentoBancarioPorPeriodo(String dataInicial, String dataFinal) throws Exception;

    public void excluirLancamentoBancario(LancamentoBancarioExclusaoRequisicao lancamentoRemocao) throws Exception;

    public void atualizarLancamentoBancario(LancamentoBancarioAtualizacaoRequisicao atualizarLancamentoRequisicao) throws Exception;

    public String validarCamposObrigatorios(Lancamento lanc);

    public void salvarContaCorrente(CadastroContaCorrenteRequisicao contaCorrenteRequisicao) throws Exception;

    public String validarCamposObrigatoriosCadastrarContaCorrente(ContaCorrente conta);

    public void excluirContaCorrente(long idContaCorrente) throws Exception;

    public BigDecimal verSaldoContaCorrente(long idContaCorrente) throws Exception;

    public void atualizarSaldoContaCorrente(Lancamento lanc) throws SQLException;

    public ContaCorrente pesquisarContasCorrentesPorId(long idContaCorrente) throws SQLException;

    public void atualizarDadosContaCorrente(AtualizarCadastroContaCorrenteRequisicao contaCorrenteRequisicao) throws Exception;

    public List<Lancamento> obterLancametosBancariosVinculadosContaCorrenteAtravesRastreio(long idContaCorrente);

    public String validarCamposObrigatoriosAtualizacao(Lancamento lanc);

    public String validarDadosAntesAtualizarContaCorrente(AtualizarCadastroContaCorrenteRequisicao contaCorrenteRequisicao) throws Exception;

    public List<Lancamento> consultarLancametosBancariosVinculadosContaCorrente(long idContaCorrente) throws SQLException;
}
