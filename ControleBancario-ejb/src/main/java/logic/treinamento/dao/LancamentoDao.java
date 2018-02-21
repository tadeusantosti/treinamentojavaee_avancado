package logic.treinamento.dao;

import logic.treinamento.model.Lancamento;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import utilitarios.Formatadores;

@Stateless
public class LancamentoDao implements InterfaceLancamentoDao {

    @Inject
    private EntityManager em;

    @Override
    public void salvarLancamentoBancario(Lancamento lanc) throws SQLException {
        try {
            em.getTransaction().begin();
            em.persist(lanc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public void atualizarLancamentoBancario(Lancamento lanc) throws SQLException {
        try {
            em.getTransaction().begin();
            em.merge(lanc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public void excluirLancamento(long idLancamento) throws SQLException {
        try {
            em.getTransaction().begin();
            em.remove(em.getReference(Lancamento.class, idLancamento));
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorPeriodo(Date dataInicial, Date dataFinal) throws SQLException {
        StringBuilder sql = new StringBuilder();
        List<Lancamento> resultados = null;

        sql.append("\n SELECT l FROM Lancamento l");
        sql.append(" WHERE l.data BETWEEN '").append(Formatadores.formatoDataBanco.format(dataInicial)).append("' AND '").append(Formatadores.formatoDataBanco.format(dataFinal)).append("' ORDER BY l.id");
        String jpql = sql.toString();
        Query query = em.createQuery(jpql);
        resultados = query.getResultList();
        return resultados;
    }

    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorNome(String nome) throws SQLException {

        StringBuilder sql = new StringBuilder();
        List<Lancamento> resultados = null;

        try {
            sql.append("\n SELECT l FROM Lancamento l");
            sql.append(" WHERE l.nome LIKE '%").append(nome).append("%' ORDER BY l.id");
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
    public List<Lancamento> pesquisarLancamentoBancarioPorTipoDeLancamento(TipoLancamentoEnum tipoLancamento) throws SQLException {

        StringBuilder sql = new StringBuilder();
        List<Lancamento> resultados = null;

        try {
            sql.append("\n SELECT l FROM Lancamento l");
            sql.append(" WHERE l.tipoLancamento =:tipoLancamento");
            String jpql = sql.toString();
            Query query = em.createQuery(jpql).setParameter("tipoLancamento", tipoLancamento);
            resultados = query.getResultList();
            return resultados;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
