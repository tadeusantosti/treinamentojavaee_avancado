package logic.treinamento.observer;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import logic.treinamento.request.LancarContasDoMesRequisicao;

@Stateless
public class EventosGestaoContas implements Serializable{

    @Inject
    Event<LancarContasDoMesRequisicao> evento;
    
    public void lancarContas(LancarContasDoMesRequisicao lancamento){
        evento.fire(lancamento);
    }

}
