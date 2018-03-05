package logic.treinamento.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Startup;
import javax.inject.Singleton;
import logic.treinamento.model.Lancamento;

@Singleton
@Startup
public class RastreioLancamentoBancarioMovimentacaoBean implements RastreioLancamentoBancarioMovimentacaoLocal {

    public static Map<Long, List<Lancamento>> mapaContasLancamentos = new HashMap<>();
    public static List<Lancamento> lancamentos;

    @Override
    public void registrarAlteracaoContaCorrente(Lancamento lanc) {
        if (!mapaContasLancamentos.containsKey(lanc.getIdContaCorrente())) {
            lancamentos = new ArrayList<>();
            lancamentos.add(lanc);
            mapaContasLancamentos.put(lanc.getIdContaCorrente(), lancamentos);
        } else {
            mapaContasLancamentos.get(lanc.getIdContaCorrente()).add(lanc);
        }
    }

    public Map<Long, List<Lancamento>> getMapaContasLancamentos() {
        return mapaContasLancamentos;
    }

}
