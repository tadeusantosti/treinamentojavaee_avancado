package logic.treinamento.dao;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import logic.treinamento.model.ContaCorrente;

/**
 * Classe responsavel pela gestão das persistencias e consultas no banco de
 * dados para processos relacionados a conta corrente.
 *
 * @since 2.0
 * @author Tadeu
 * @version 1.0
 */
@Stateless
public class ContaCorrenteDao implements InterfaceContaCorrente {

    @Inject
    private EntityManager em;

    /**
     * Método para persistir os dados de uma nova conta corrente
     *
     * @author Tadeu
     * @param conta ContaCorrente - Dados da conta corrente que sera persistida.
     * @throws java.sql.SQLException
     */
    @Override
    public void salvarContaCorrente(ContaCorrente conta) throws SQLException {
        try {
            em.getTransaction().begin();
            em.persist(conta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    /**
     * Método para excluir os dados de uma nova conta corrente persistida
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente.
     * @throws java.sql.SQLException
     */
    @Override
    public void excluirContaCorrente(long idContaCorrente) throws SQLException {
        try {
            em.getTransaction().begin();
            em.remove(em.getReference(ContaCorrente.class, idContaCorrente));
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    /**
     * Método para consultar uma conta corrente no banco de dados atraves de seu
     * ID
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente.
     * @return ContaCorrente - Objeto que contem os dados da conta corrente
     * pesquisada.
     * @throws java.sql.SQLException
     */
    @Override
    public ContaCorrente pesquisarContasCorrentesPorId(long idContaCorrente) throws SQLException {
        ContaCorrente conta = em.find(ContaCorrente.class, idContaCorrente);
        em.merge(conta);
        em.refresh(conta);
        return conta;
    }

    /**
     * Método para consultar todas as contas correntes persistidas em banco.
     *
     * @author Tadeu
     * @return List<ContaCorrente> - Objeto que contem os dados de todas as
     * contas correntes persistidas.
     * @throws java.sql.SQLException
     */
    @Override
    public List<ContaCorrente> pesquisarTodasContasCorrentes() throws SQLException {
        StringBuilder sql = new StringBuilder();
        List<ContaCorrente> resultado = null;

        sql.append("SELECT cc FROM ContaCorrente cc");
        String jpql = sql.toString();
        Query query = em.createQuery(jpql);
        resultado = query.getResultList();
        return resultado;
    }

    /**
     * Método para inativar uma conta corrente.
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente que sera inativada
     * @throws java.sql.SQLException
     */
    @Override
    public void inativarContaCorrente(long idContaCorrente) throws SQLException {
        try {
            ContaCorrente contaCorrente = pesquisarContasCorrentesPorId(idContaCorrente);
            contaCorrente.setSituacao(false);

            em.getTransaction().begin();
            em.merge(contaCorrente);
            em.getTransaction().commit();
        } catch (SQLException ex) {
            em.getTransaction().rollback();
        }
    }

    /**
     * Método para atualizar dados de uma conta corrente.
     *
     * @author Tadeu
     * @param conta ContaCorrente - Objeto que contem as informacoes da conta
     * que sera atualizada
     * @throws java.sql.SQLException
     */
    @Override
    public void atualizarDadosContaCorrente(ContaCorrente conta) throws SQLException {
        try {
            em.getTransaction().begin();
            em.merge(conta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

}
