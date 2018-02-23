package logic.treinamento.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import logic.treinamento.model.ContaCorrente;
import logic.treinamento.model.Lancamento;

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

    @Override
    public void atualizarSaldoContaCorrente(long idContaCorrente, BigDecimal saldo) throws SQLException {
        try {
            ContaCorrente contaCorrente = pesquisarContasCorrentesPorId(idContaCorrente);
            contaCorrente.setSaldo(saldo);
            
            em.getTransaction().begin();
            em.merge(contaCorrente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public ContaCorrente pesquisarContasCorrentesPorId(long idContaCorrente) throws SQLException {

        StringBuilder sql = new StringBuilder();
        ContaCorrente resultado = null;

        try {
            sql.append("\n SELECT cc FROM ContaCorrente cc WHERE cc.id = ").append(idContaCorrente);
            String jpql = sql.toString();
            Query query = em.createQuery(jpql);
            resultado = (ContaCorrente) query.getSingleResult();
            return resultado;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ContaCorrente> pesquisarTodasContasCorrentes() throws SQLException {
            StringBuilder sql = new StringBuilder();
        List<ContaCorrente> resultado = null;

        try {
            sql.append("\n SELECT cc FROM ContaCorrente cc");
            String jpql = sql.toString();
            Query query = em.createQuery(jpql);
            resultado = query.getResultList();
            return resultado;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
