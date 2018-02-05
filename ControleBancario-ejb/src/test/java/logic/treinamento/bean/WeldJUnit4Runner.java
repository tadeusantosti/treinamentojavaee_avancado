package logic.treinamento.bean;
 
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;



public class WeldJUnit4Runner extends BlockJUnit4ClassRunner {
    public WeldJUnit4Runner(final Class<Object> clazz) throws InitializationError {
        super(clazz);
    }
    @Override
    protected Object createTest() {
        return WeldContext.getInstance().getBean(getTestClass().getJavaClass());
    }
    
}