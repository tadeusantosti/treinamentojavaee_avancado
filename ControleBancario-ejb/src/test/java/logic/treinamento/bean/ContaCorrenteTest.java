package logic.treinamento.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import logic.treinamento.dao.InterfaceContaCorrente;
import logic.treinamento.dao.InterfaceLancamentoDao;
import logic.treinamento.model.AgenciaEnum;
import logic.treinamento.model.BancoEnum;
import logic.treinamento.model.TipoLancamentoEnum;
import logic.treinamento.model.ContaCorrente;
import logic.treinamento.model.Lancamento;
import logic.treinamento.observer.GestaoEventosContaCorrente;
import logic.treinamento.observer.GestaoEventosLancamentoBancario;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioExclusaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import utilitarios.Formatadores;

@RunWith(WeldJUnit4Runner.class)
public class ContaCorrenteTest {

    @Inject
    public InterfaceGestaoContas gestaoContaBean;

    @Inject
    InterfaceLancamentoDao gestaoContasDao;

    @Inject
    GestaoEventosLancamentoBancario eventoLancamentoBancario;

    @Inject
    GestaoEventosContaCorrente eventosLancamentoContaCorrente;

    @Inject
    InterfaceContaCorrente contaCorrenteDao;

    @Inject
    RastreioLancamentoBancarioMovimentacaoLocal rastreioBean;

    @Before
    public void setup() throws Exception {
        LancamentoBancarioExclusaoRequisicao lancamentoRemocao = new LancamentoBancarioExclusaoRequisicao();
        List<Lancamento> registrosPararemoverAntesTeste = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");
        if (!registrosPararemoverAntesTeste.isEmpty()) {
            for (Lancamento lancamento : registrosPararemoverAntesTeste) {
                lancamentoRemocao.setIdLancamento(lancamento.getId());
                eventoLancamentoBancario.excluirLancamentoBancario(lancamentoRemocao);
            }
        }

        List<Lancamento> registrosPararemoverAntesTeste1 = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Charles Darwin");
        if (!registrosPararemoverAntesTeste1.isEmpty()) {
            for (Lancamento lancamento : registrosPararemoverAntesTeste1) {
                lancamentoRemocao.setIdLancamento(lancamento.getId());
                eventoLancamentoBancario.excluirLancamentoBancario(lancamentoRemocao);
            }
        }

        List<ContaCorrente> registrosContaCorrente = contaCorrenteDao.pesquisarTodasContasCorrentes();
        if (!registrosContaCorrente.isEmpty()) {
            for (ContaCorrente contaCorrente : registrosContaCorrente) {
                eventosLancamentoContaCorrente.excluirContaCorrente(contaCorrente.getId());
            }
        }
    }

    /** <H3> Teste de Criação e Atualizacao dos dados de uma Conta Corrente
     * </H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Garantir que o sistema esteja criando e atualizando os
     * dados de uma conta corrente.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi criado um objeto contaCorrente contendo os dados ficticios.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Cadastrar uma nova conta corrente. <i><br>
     * Resultado esperado: Sistema casdastrou uma nova conta corrente,
     * persistindo os dados em banco. <br>
     * <li> <i> Cenário 2: Atualizar os dados da conta corrente que foi criada.
     * <i> <li>
     * Resultado esperado: Sistema atualizou os dados da conta corrente com
     * sucesso. <br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testCriacaoAtualizacaoContaCorrente() throws Exception {
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosLancamentoContaCorrente.salvarContaCorrente(cc);

        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        if (!contas.isEmpty()) {
            for (ContaCorrente conta : contas) {
                assertTrue(BigDecimal.ZERO.compareTo(conta.getSaldo()) == 0);
                assertEquals(cc.getAgencia(), conta.getAgencia().getId());
                assertEquals(cc.getBanco(), conta.getBanco().getId());
                assertEquals(cc.getTitular(), conta.getTitular());
                assertTrue(conta.isSituacao());
            }
        } else {
            fail("A conta corrente nao foi cadastrada!");
        }

        LancamentoBancarioRequisicao lancRequisicaoDeposito = new LancamentoBancarioRequisicao();
        lancRequisicaoDeposito.setObservacao("Deposito na conta corrente de Albert Einstein");
        lancRequisicaoDeposito.setValor(new BigDecimal(1234.56));
        lancRequisicaoDeposito.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicaoDeposito.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        lancRequisicaoDeposito.setIdContaCorrente(contas.get(0).getId());
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicaoDeposito);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");
        ContaCorrente conta = gestaoContaBean.pesquisarContasCorrentesPorId(lancNovo.get(0).getIdContaCorrente());

        if (!lancNovo.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancNovo) {
                assertEquals(lancRequisicaoDeposito.getObservacao(), lancamentoConsultado.getObservacao());
                assertEquals(lancRequisicaoDeposito.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicaoDeposito.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicaoDeposito.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
                assertEquals(contas.get(0).getId(), lancRequisicaoDeposito.getIdContaCorrente());
                assertTrue(conta.getSaldo().compareTo(BigDecimal.valueOf(lancamentoConsultado.getValor().doubleValue())) == 0);
            }
        } else {
            fail("O lancamento bancario nao foi salvo!");
        }

        Map<Long, List<Lancamento>> mapa = rastreioBean.getMapaContasLancamentos();
        Assert.assertNotNull(mapa);
        assertEquals(1, mapa.size());
        assertTrue(conta.getSaldo().compareTo(mapa.get(contas.get(0).getId()).get(0).getValor().setScale(2, RoundingMode.HALF_UP)) == 0);

        LancamentoBancarioRequisicao lancRequisicaoSaque = new LancamentoBancarioRequisicao();
        lancRequisicaoSaque.setObservacao("Saque na conta corrente de Albert Einstein");
        lancRequisicaoSaque.setValor(new BigDecimal(1000.00));
        lancRequisicaoSaque.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicaoSaque.setIdTipoLancamento(TipoLancamentoEnum.SAQUE.getId());
        lancRequisicaoSaque.setIdContaCorrente(contas.get(0).getId());
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicaoSaque);

        ContaCorrente contaAtualizada = gestaoContaBean.pesquisarContasCorrentesPorId(lancNovo.get(0).getIdContaCorrente());
        assertEquals(contaAtualizada.getSaldo(), lancRequisicaoDeposito.getValor().subtract(lancRequisicaoSaque.getValor()).setScale(2, RoundingMode.HALF_UP));

        mapa = rastreioBean.getMapaContasLancamentos();
        Assert.assertNotNull(mapa);
        assertEquals(2, mapa.get(conta.getId()).size());

    }
}
