package logic.treinamento.dao;

import logic.treinamento.model.TipoLancamentoEnum;
import logic.treinamento.model.Lancamento;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import utilitarios.Formatadores;

/**
 * Classe responsavel pela gestão das persistencias e consultas no banco de
 * dados para processos relacionados ao lancamento bancario.
 *
 * @since 1.0
 * @author Tadeu
 * @version 2.0
 */
@Stateless
public class LancamentoDao implements InterfaceLancamentoDao {

    @Inject
    private EntityManager em;

    /**
     * Método para persistir os dados de um novo lancamento bancario
     *
     * @author Tadeu
     * @param lanc Lancamento - Dados do lancamento bancario que sera
     * persistido.
     * @throws java.sql.SQLException
     */
    @Override
    public void salvarLancamentoBancario(Lancamento lanc) throws SQLException {
        try {
            em.getTransaction().begin();
            em.merge(lanc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    /**
     * Método para atualizar os dados de um lancamento bancario que foi
     * persistido
     *
     * @author Tadeu
     * @param lanc Lancamento - Dados do lancamento bancario que sera
     * atualizado.
     * @throws java.sql.SQLException
     */
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

    /**
     * Método para excluir um lancamento bancario atrave de seu ID
     *
     * @author Tadeu
     * @param idLancamento long - ID do lancamento bancario que sera excluido
     * atualizado.
     * @throws java.sql.SQLException
     */
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

    /**
     * Método para pesquisar lancamentos bancarios persistidos dentro de um
     * determinado periodo
     *
     * @author Tadeu
     * @param dataInicial Date - Data do inicio do periodo que sera consultado
     * @param dataFinal Date - Data do termino do periodo que sera consultado
     * atualizado.
     * @return List<Lancamento> - Objeto que contem os lancamentos bancarios
     * consultados dentro do periodo
     * @throws java.sql.SQLException
     */
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

    /**
     * Método para pesquisar lancamentos bancarios persistidos atraves do campo
     * observacao
     *
     * @author Tadeu
     * @param observacao String - Observacao do lancamento bancario
     * @return List<Lancamento> - Objeto que contem os lancamentos bancarios
     * consultados atraves do campo de observacao.
     * @throws java.sql.SQLException
     */
    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorObservacao(String observacao) throws SQLException {
        StringBuilder sql = new StringBuilder();  
        try {
            sql.append("\n SELECT l FROM Lancamento l");
            sql.append(" WHERE l.observacao LIKE '%").append(observacao).append("%' ORDER BY l.id");
            String jpql = sql.toString();            
            TypedQuery<Lancamento> query = em.createQuery(jpql, Lancamento.class); 
            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Método para pesquisar lancamentos bancarios persistidos atraves do tipo
     * do lancamento
     *
     * @author Tadeu
     * @param tipoLancamento TipoLancamentoEnum - Tipo do lancamento bancario
     * @return List<Lancamento> - Objeto que contem os lancamentos bancarios
     * consultados atraves do tipo.
     * @throws java.sql.SQLException
     */
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

    /**
     * Método para pesquisar lancamentos bancarios persistidos atraves da conta
     * corrente
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente
     * @return List<Lancamento> - Objeto que contem os lancamentos bancarios
     * consultados atraves da conta.
     * @throws java.sql.SQLException
     */
    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorContaBancaria(long idContaCorrente) throws SQLException {

        StringBuilder sql = new StringBuilder();
        List<Lancamento> resultados = null;

        try {
            sql.append("\n SELECT l FROM Lancamento l");
            sql.append("\n JOIN ContaCorrente cc ON cc.id = l.id_contacorrente");
            sql.append(" WHERE cc.situacao = TRUE");
            sql.append(" AND cc.id = ").append(idContaCorrente);
            String jpql = sql.toString();
            Query query = em.createQuery(jpql);
            resultados = query.getResultList();
            return resultados;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
