package logic.treinamento.dao;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    
     @Override
    public List<ContaCorrente> pesquisarTodasContasCorrentes() throws SQLException {

        StringBuilder sql = new StringBuilder();
        List<ContaCorrente> resultados = null;

        try {
            sql.append("\n SELECT cc FROM ContaCorrente cc");            
            String jpql = sql.toString();
            Query query = em.createQuery(jpql);
            resultados = query.getResultList();
            return resultados;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void excluirContaCorrente(long idContaCorrente) throws SQLException {
        try {
            em.getTransaction().begin();
            em.remove(em.getReference(ContaCorrente.class, idContaCorrente));
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }
}
