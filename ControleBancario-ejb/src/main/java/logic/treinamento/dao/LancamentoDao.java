package logic.treinamento.dao;

import java.io.Serializable;
import logic.treinamento.model.Lancamento;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import utilitarios.Formatadores;

/**
 *
 * @author tadpi
 */
@Stateless
@Named
public class LancamentoDao implements InterfaceLancamentoDao, Serializable {

    /**
     *
     */
    @Inject    
    private EntityManager em;

    @Override
    public void salvarContasDoMes(Lancamento lanc) throws SQLException {
        em.getTransaction().begin();
        em.persist(lanc);
        em.getTransaction().commit();
    }

    @Override
    public void atualizarLancamento(Lancamento lanc) throws SQLException {
        em.getTransaction().begin();
        em.merge(lanc);
        em.getTransaction().commit();
    }

    @Override
    public void excluirLancamento(long idLancamento) throws SQLException {
        em.getTransaction().begin();
        em.remove(em.getReference(Lancamento.class, idLancamento));
        em.getTransaction().commit();
    }

    @Override
    public List<Lancamento> pesquisarLancamentoPorPeriodo(Date dataInicial, Date dataFinal) throws SQLException {
        StringBuilder sql = new StringBuilder();
        em.getTransaction().begin();
        sql.append("\n SELECT l FROM Lancamento l");
        sql.append(" WHERE l.data BETWEEN '").append(Formatadores.formatoDataBanco.format(dataInicial)).append("' AND '").append(Formatadores.formatoDataBanco.format(dataFinal)).append("' ORDER BY l.id");
        String jpql = sql.toString();
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }

    @Override
    public List<Lancamento> pesquisarLancamentoPorNome(String nome) throws SQLException {
        StringBuilder sql = new StringBuilder();
        em.getTransaction().begin();
        sql.append("\n SELECT l FROM Lancamento l");
        sql.append(" WHERE l.nome LIKE '%").append(nome).append("%' ORDER BY l.id");
        String jpql = sql.toString();
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }

    @Override
    public List<Lancamento> pesquisarLancamentoPorTipoDeLancamento(TipoLancamentoEnum tipoLancamento) throws SQLException {
        StringBuilder sql = new StringBuilder();
        em.getTransaction().begin();
        sql.append("\n SELECT l FROM Lancamento l");
        sql.append(" WHERE l.tipoLancamento = '").append(tipoLancamento).append("' ORDER BY l.id");
        String jpql = sql.toString();
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }

}
