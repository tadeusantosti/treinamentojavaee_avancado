package logic.treinamento.dao;

import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import logic.treinamento.model.ContaCorrente;

@Stateless
public class ContaCorrenteDao implements InterfaceContaCorrente {

    @Inject
    private EntityManager em;

    @Override
    public void salvarContaCorrente(ContaCorrente conta) throws SQLException {
        try {
            em.getTransaction().begin();
            em.persist(conta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }
}
