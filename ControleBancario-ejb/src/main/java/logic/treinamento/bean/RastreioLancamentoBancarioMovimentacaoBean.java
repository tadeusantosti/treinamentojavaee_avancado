package logic.treinamento.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Startup;
import javax.inject.Singleton;
import logic.treinamento.model.Lancamento;

/**
 * Classe responsavel pela gestão do Log de rastreio dos lancamentos que
 * impactaram no saldo da conta corrente.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
@Singleton
@Startup
public class RastreioLancamentoBancarioMovimentacaoBean implements RastreioLancamentoBancarioMovimentacaoLocal {

    public static Map<Long, List<Lancamento>> mapaContasLancamentos = new HashMap<>();
    public static List<Lancamento> lancamentos;

    /**
     * Método para salvar o log de rastreio com o lancamento que impactou no
     * saldo da conta corrente.
     *
     * @author Tadeu
     * @param lanc Lancamento - Dados do lancamento bancario que impactou no
     * saldo da conta corrente.
     */
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

    /**
     * Método para obter os dados do log de rastreio.
     *
     * @author Tadeu
     * @return Map<Long, List<Lancamento>> - Objeto que contem os dados do log
     * de rastreio, tendo como chave o ID da conta corrente e como valor todos
     * os lancamentos que impactaram no saldo da conta corrente.
     *
     */
    @Override
    public Map<Long, List<Lancamento>> getMapaContasLancamentos() {
        return mapaContasLancamentos;
    }

}
