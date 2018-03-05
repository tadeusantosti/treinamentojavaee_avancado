package logic.treinamento.bean;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import logic.treinamento.model.Lancamento;

@Local
public interface RastreioLancamentoBancarioMovimentacaoLocal {
     public void registrarAlteracaoContaCorrente(Lancamento lanc);
     
     public Map<Long, List<Lancamento>> getMapaContasLancamentos();
}
