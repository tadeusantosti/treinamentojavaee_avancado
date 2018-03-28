package logic.treinamento.bean;
 
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;


/**
 * Classe obrigatoria do frameWork Wekd para a execução correta dos eventos
 * controlados pelo CDI nos testes.
 *
 * @since 2.0
 * @author Tadeu
 * @version 1.0
 */
public class WeldJUnit4Runner extends BlockJUnit4ClassRunner {
    public WeldJUnit4Runner(final Class<Object> clazz) throws InitializationError {
        super(clazz);
    }
    @Override
    protected Object createTest() {
        return WeldContext.getInstance().getBean(getTestClass().getJavaClass());
    }
    
}