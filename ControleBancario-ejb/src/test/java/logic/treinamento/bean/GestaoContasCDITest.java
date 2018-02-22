package logic.treinamento.bean;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import logic.treinamento.dao.InterfaceContaCorrente;
import logic.treinamento.dao.InterfaceLancamentoDao;
import logic.treinamento.dao.TipoLancamentoEnum;
import logic.treinamento.model.ContaCorrente;
import logic.treinamento.model.Lancamento;
import logic.treinamento.observer.EventosGestaoContas;
import logic.treinamento.request.AtualizarLancamentoRequisicao;
import logic.treinamento.request.LancarContasDoMesRequisicao;
import org.junit.Before;
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

    @Inject
    InterfaceContaCorrente contaCorrenteDao;

    @Before
    public void setup() throws Exception {
        List<Lancamento> registrosPararemoverAntesTeste = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Albert");
        if (!registrosPararemoverAntesTeste.isEmpty()) {
            for (Lancamento lancamento : registrosPararemoverAntesTeste) {
                eventosGestaoContas.excluirLancamentoBancario(lancamento.getId());
            }
        }

        List<Lancamento> registrosPararemoverAntesTeste1 = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Charles Darwin");
        if (!registrosPararemoverAntesTeste1.isEmpty()) {
            for (Lancamento lancamento : registrosPararemoverAntesTeste1) {
                eventosGestaoContas.excluirLancamentoBancario(lancamento.getId());
            }
        }
        
        List<ContaCorrente> registrosContaCorrente = contaCorrenteDao.pesquisarTodasContasCorrentes();
        if (!registrosContaCorrente.isEmpty()) {
            for (ContaCorrente contaCorrente : registrosContaCorrente) {
                contaCorrenteDao.excluirContaCorrente(contaCorrente.getId());
            }
        }
    }

    /**
     * Teste responsavel por validar o serviço de lançamento de contas do mês e
     * consultar o lançamento salvo através de parte do nome do cliente.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi informado todos os campos principais
     * necessarios para a correta inclusao dos dados
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Sistema persistiu o lancamento de conta em bando de
     * dados e consultou corretamente os dados salvos informando apenas parte do
     * nome do cliente
     *
     * @throws Exception
     */
    @Test
    public void testSalvarLancamentoBancario() throws Exception {

        LancarContasDoMesRequisicao lancRequisicao = new LancarContasDoMesRequisicao();
        lancRequisicao.setNome("Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Albert");

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

        gestaoContaBean.excluirLancamentoBancario(lancNovo.get(0).getId());
    }

    /**
     * Teste responsavel por atualizar os dados de um lancamento persistido na
     * base de dados.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi incluido um lancamento na base de dados.
     * -Foram informados novas informações e requisitado o serviço de
     * atualizacao dos dados no sistema
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Sistema atualizou o lancamento em bando de dados de
     * acordo com a novas informações.
     *
     * @throws Exception
     */
    @Test
    public void testAtualizarDadosLancamentoBancario() throws Exception {

        LancarContasDoMesRequisicao lancRequisicao = new LancarContasDoMesRequisicao();
        lancRequisicao.setNome("Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Albert");

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

        Calendar novaData = Calendar.getInstance();
        novaData.setTime(Formatadores.formatoDataInterface.parse(lancRequisicao.getData()));
        novaData.add(Calendar.DAY_OF_MONTH, 2);

        AtualizarLancamentoRequisicao atualizarLancamentoRequisicao = new AtualizarLancamentoRequisicao();
        atualizarLancamentoRequisicao.setNomeAtualizado("Charles Darwin");
        atualizarLancamentoRequisicao.setValorAtualizado(new BigDecimal(9999.99));
        atualizarLancamentoRequisicao.setDataAtualizada(Formatadores.formatoDataInterface.format(novaData.getTime()));
        atualizarLancamentoRequisicao.setIdTipoLancamentoAtualizado(TipoLancamentoEnum.TRANSFERENCIA.getId());
        eventosGestaoContas.atualizarLancamentoBancario(atualizarLancamentoRequisicao);

        List<Lancamento> lancamentoAtualizado = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Charles");

        if (!lancamentoAtualizado.isEmpty()) {
            for (Lancamento lancAtualizado : lancamentoAtualizado) {
                assertEquals(atualizarLancamentoRequisicao.getNomeAtualizado(), lancAtualizado.getNome());
                assertEquals(atualizarLancamentoRequisicao.getValorAtualizado().doubleValue(), lancAtualizado.getValor().doubleValue());
                assertEquals(atualizarLancamentoRequisicao.getDataAtualizada(), Formatadores.formatoDataInterface.format(lancAtualizado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(atualizarLancamentoRequisicao.getIdTipoLancamentoAtualizado()), lancAtualizado.getTipoLancamento());
            }
        } else {
            fail("O lancamento bancario nao foi atualizado!");
        }

        gestaoContaBean.excluirLancamentoBancario(lancamentoAtualizado.get(0).getId());
    }

    /**
     * Teste responsavel por excluir os dados de um lancamento persistido na
     * base de dados.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi incluido um lancamento na base de dados.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Sistema excluiu os dados do lancamento corretamente.
     *
     * @throws Exception
     */
    @Test
    public void testExcluirLancamentoBancario() throws Exception {

        LancarContasDoMesRequisicao lancRequisicao = new LancarContasDoMesRequisicao();
        lancRequisicao.setNome("Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Albert");

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

        eventosGestaoContas.excluirLancamentoBancario(lancNovo.get(0).getId());

        List<Lancamento> lancExcluido = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Albert");

        if (!lancExcluido.isEmpty()) {
            fail("O lancamento bancario nao foi excluido!");
        }
    }

    /**
     * Teste responsavel por pesquisar os dados de um lancamento persistido na
     * base de dados atraves do tipo do lancamento.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi incluido um lancamento na base de dados com o
     * tipo de lancamento SAQUE.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Sistema consultou corretamente o registro atraves do
     * tipo de lancamento informado (SAQUE)
     *
     * @throws Exception
     */
    @Test
    public void testPesquisarLancamentoBancarioPorTipoDeLancamento() throws Exception {

        LancarContasDoMesRequisicao lancRequisicao = new LancarContasDoMesRequisicao();
        lancRequisicao.setNome("Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.SAQUE.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancamentoDeSaque = gestaoContaBean.pesquisarLancamentoBancarioPorTipoDeLancamento(TipoLancamentoEnum.SAQUE.getId());

        if (!lancamentoDeSaque.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancamentoDeSaque) {
                assertEquals(lancRequisicao.getNome(), lancamentoConsultado.getNome());
                assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
            }
        } else {
            fail("O lancamento bancario nao foi encontrado!");
        }
        eventosGestaoContas.excluirLancamentoBancario(lancamentoDeSaque.get(0).getId());
    }

    /**
     * Teste responsavel por pesquisar os dados de lancamentos persistidos na
     * base de dados informando apenas um periodo.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foram inseridos na base de dados dois lancamentos
     * distintos.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Sistema consultou os lancamentos corretamente
     * atraves do serviço de consulta por periodo.
     *
     * @throws Exception
     */
    @Test
    public void testPesquisarLancamentoBancarioPorPeriodo() throws Exception {

        LancarContasDoMesRequisicao lancRequisicao = new LancarContasDoMesRequisicao();
        lancRequisicao.setNome("Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        Calendar novaData = Calendar.getInstance();
        novaData.setTime(Formatadores.formatoDataInterface.parse(lancRequisicao.getData()));
        novaData.add(Calendar.DAY_OF_MONTH, 5);

        LancarContasDoMesRequisicao lancDoisRequisicao = new LancarContasDoMesRequisicao();
        lancDoisRequisicao.setNome("Charles Darwin");
        lancDoisRequisicao.setValor(new BigDecimal(4242.31));
        lancDoisRequisicao.setData(Formatadores.formatoDataInterface.format(novaData.getTime()));
        lancDoisRequisicao.setIdTipoLancamento(TipoLancamentoEnum.SAQUE.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancDoisRequisicao);

        novaData.add(Calendar.DAY_OF_MONTH, 10);
        List<Lancamento> lancamentoDeSaque = gestaoContaBean.pesquisarLancamentoBancarioPorPeriodo(Formatadores.formatoDataInterface.format(new java.util.Date()), Formatadores.formatoDataInterface.format(novaData.getTime().getTime()));

        if (!lancamentoDeSaque.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancamentoDeSaque) {
                if (lancamentoConsultado.getNome().equals(lancRequisicao.getNome())) {
                    assertEquals(lancRequisicao.getNome(), lancamentoConsultado.getNome());
                    assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                    assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                    assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
                } else if (lancamentoConsultado.getNome().equals(lancDoisRequisicao.getNome())) {
                    assertEquals(lancDoisRequisicao.getNome(), lancamentoConsultado.getNome());
                    assertEquals(lancDoisRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                    assertEquals(lancDoisRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                    assertEquals(TipoLancamentoEnum.getByCodigo(lancDoisRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
                } else {
                    fail("O lancamento bancario nao foi encontrado!");
                }
            }
        } else {
            fail("O lancamento bancario nao foi encontrado!");
        }

        eventosGestaoContas.excluirLancamentoBancario(lancamentoDeSaque.get(0).getId());
        eventosGestaoContas.excluirLancamentoBancario(lancamentoDeSaque.get(1).getId());
    }

    @Test
    public void testContas() throws Exception {
        ContaCorrente cc = new ContaCorrente();
        cc.setSaldo(9999);
        contaCorrenteDao.salvarContaCorrente(cc);
        
        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        LancarContasDoMesRequisicao lancRequisicao = new LancarContasDoMesRequisicao();
        lancRequisicao.setNome("Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        lancRequisicao.setIdContaCorrente(contas.get(0).getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorNome("Albert");

        if (!lancNovo.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancNovo) {
                assertEquals(lancRequisicao.getNome(), lancamentoConsultado.getNome());
                assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());    
                assertEquals(contas.get(0).getId(), lancRequisicao.getIdContaCorrente());
            }
        } else {
            fail("O lancamento bancario nao foi salvo!");
        }

        gestaoContaBean.excluirLancamentoBancario(lancNovo.get(0).getId());
    }

}
