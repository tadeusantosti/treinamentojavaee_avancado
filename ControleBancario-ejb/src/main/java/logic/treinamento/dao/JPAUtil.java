package logic.treinamento.dao;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class JPAUtil {
    
    @PersistenceUnit
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("dbControleBancario");

    @Produces
    @RequestScoped 
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
