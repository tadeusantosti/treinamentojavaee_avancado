package logic.treinamento.dao;

import java.sql.SQLException;
import java.util.List;
import logic.treinamento.model.ContaCorrente;

public interface InterfaceContaCorrente {

    public void salvarContaCorrente(ContaCorrente conta) throws SQLException;

    public List<ContaCorrente> pesquisarTodasContasCorrentes() throws SQLException;

    public ContaCorrente pesquisarContasCorrentesPorId(long idContaCorrente) throws SQLException;

    public void inativarContaCorrente(long idContaCorrente) throws SQLException;

    public void excluirContaCorrente(long idContaCorrente) throws SQLException;

    public void atualizarDadosContaCorrente(ContaCorrente conta) throws SQLException;
}
