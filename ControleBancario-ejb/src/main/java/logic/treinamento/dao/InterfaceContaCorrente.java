package logic.treinamento.dao;

import java.sql.SQLException;
import logic.treinamento.model.ContaCorrente;

public interface InterfaceContaCorrente {

    public void salvarContaCorrente(ContaCorrente conta) throws SQLException;

}
