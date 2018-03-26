package logic.treinamento.dao;

import logic.treinamento.model.TipoLancamentoEnum;
import logic.treinamento.model.Lancamento;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface InterfaceLancamentoDao {

    public void salvarLancamentoBancario(Lancamento lanc) throws SQLException;

    public void atualizarLancamentoBancario(Lancamento lanc) throws SQLException;

    public void excluirLancamento(long idLancamento) throws SQLException;

    public List<Lancamento> pesquisarLancamentoBancarioPorPeriodo(Date dataInicial, Date dataFinal) throws SQLException;

    public List<Lancamento> pesquisarLancamentoBancarioPorObservacao(String observacao) throws SQLException;

    public List<Lancamento> pesquisarLancamentoBancarioPorTipoDeLancamento(TipoLancamentoEnum tipoLancamento) throws SQLException;

    public List<Lancamento> pesquisarLancamentoBancarioPorContaBancaria(long idContaCorrente) throws SQLException;
}
