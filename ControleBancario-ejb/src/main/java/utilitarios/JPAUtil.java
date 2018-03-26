package utilitarios;

import java.io.Serializable;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe utilitaria que possui metodos responsaveis por criar uma conexao com o
 * banco de dados atraves de um EntityManager.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
public class JPAUtil implements Serializable {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("dbControleBancario");

    /**
     * Método responsavel por produzir/criar um EntityManager para acesso ao
     * banco de dados;
     *
     * @author Tadeu
     * @return EntityManager - Objeto EntityManager que permite a comunicacao
     * com o banco de dados.
     */
    @Produces
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Método responsavel por fechar uma conexao aberta com o banco de dados;
     *
     * @author Tadeu
     * @param em EntityManager - Objeto EntityManager que possui a oonexao com o
     * banco de dados.
     */
    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}
