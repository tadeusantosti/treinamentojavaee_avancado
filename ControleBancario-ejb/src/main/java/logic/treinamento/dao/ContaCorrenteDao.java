package logic.treinamento.dao;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import logic.treinamento.bean.RastreioLancamentoBancarioMovimentacaoLocal;
import logic.treinamento.model.AgenciaEnum;
import logic.treinamento.model.BancoEnum;
import logic.treinamento.model.ContaCorrente;
import logic.treinamento.model.Lancamento;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;

@Stateless
public class ContaCorrenteDao implements InterfaceContaCorrente {

    @Inject
    private EntityManager em;

    @Inject
    private RastreioLancamentoBancarioMovimentacaoLocal rastreio;

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
    public void atualizarSaldoContaCorrente(Lancamento lanc) throws SQLException {
        try {
            rastreio.registrarAlteracaoContaCorrente(lanc);
            ContaCorrente contaCorrente = pesquisarContasCorrentesPorId(lanc.getIdContaCorrente());
            contaCorrente.setSaldo(contaCorrente.getSaldo().add(lanc.getValor()));

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
        ContaCorrente resultado = new ContaCorrente();

        try {
            resultado = em.find(ContaCorrente.class, idContaCorrente);
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

    @Override
    public void inativarContaCorrente(long idContaCorrente) throws SQLException {
        try {
            ContaCorrente contaCorrente = pesquisarContasCorrentesPorId(idContaCorrente);
            contaCorrente.setSituacao(false);

            em.getTransaction().begin();
            em.merge(contaCorrente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public void atualizarDadosContaCorrente(ContaCorrente conta) throws SQLException {
        try {
            em.getTransaction().begin();
            em.merge(conta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        }
    }

}
