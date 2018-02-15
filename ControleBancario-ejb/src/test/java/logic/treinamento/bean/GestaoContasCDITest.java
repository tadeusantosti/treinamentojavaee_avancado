package logic.treinamento.bean;

import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import logic.treinamento.dao.InterfaceLancamentoDao;
import logic.treinamento.dao.TipoLancamentoEnum;
import logic.treinamento.model.Lancamento;
import logic.treinamento.observer.EventosGestaoContas;
import logic.treinamento.request.LancarContasDoMesRequisicao;
import org.junit.Test;
import org.junit.runner.RunWith;
import utilitarios.Formatadores;

@RunWith(WeldJUnit4Runner.class)
public class GestaoContasCDITest {

    @Inject
    public InterfaceGestaoContas gestaoContaBean;

    @Inject
    InterfaceLancamentoDao gestaoContasDao;

    @Inject
    EventosGestaoContas eventosGestaoContas;

    @Test
    public void testeEvento() throws Exception {

        List<Lancamento> lixo = gestaoContaBean.pesquisarLancamentoPorNome("Albert");
        if (!lixo.isEmpty()) {
            for (Lancamento lancamento : lixo) {
            gestaoContaBean.excluirLancamento(lancamento.getId());                
            }            
        }
                
        LancarContasDoMesRequisicao lancRequisicao = new LancarContasDoMesRequisicao();
        lancRequisicao.setNome("Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoPorNome("Albert");

        if (!lancNovo.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancNovo) {
                assertEquals(lancRequisicao.getNome(), lancamentoConsultado.getNome());
                assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
            }
        } else {
            fail("O lancamento bancario nao foi salvo!");
        }

        gestaoContaBean.excluirLancamento(lancNovo.get(0).getId());
    }
}
