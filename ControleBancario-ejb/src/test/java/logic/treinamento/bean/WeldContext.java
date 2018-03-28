package logic.treinamento.bean;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Classe obrigatoria do frameWork Wekd para a execução correta dos eventos
 * controlados pelo CDI nos testes.
 *
 * @since 2.0
 * @author Tadeu
 * @version 1.0
 */
public class WeldContext {

    private final WeldContainer weldContainer;

    private WeldContext() {
        final Weld weld = new Weld();
        this.weldContainer = weld.initialize();
    }

    public static WeldContext getInstance() {
        return WeldContext.Instance.SINGLETON;
    }

    private static final class Instance {

        static final WeldContext SINGLETON = new WeldContext();
    }

    public <T> T getBean(final Class<T> type) {
        return this.weldContainer.instance()
                .select(type)
                .get();
    }
}
