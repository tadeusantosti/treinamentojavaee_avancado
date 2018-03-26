package logic.treinamento.bean;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
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
import logic.treinamento.observer.EventosGestaoContas;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import utilitarios.Formatadores;

@RunWith(WeldJUnit4Runner.class)
public class LancamentoBancarioTest {

    @Inject
    public InterfaceGestaoContas gestaoContaBean;

    @Inject
    InterfaceLancamentoDao gestaoContasDao;

    @Inject
    EventosGestaoContas eventosGestaoContas;

    @Inject
    InterfaceContaCorrente contaCorrenteDao;

    @Inject
    RastreioLancamentoBancarioMovimentacaoLocal rastreioBean;

    @Before
    public void setup() throws Exception {

        List<Lancamento> registrosPararemoverAntesTeste = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");
        if (!registrosPararemoverAntesTeste.isEmpty()) {
            for (Lancamento lancamento : registrosPararemoverAntesTeste) {
                eventosGestaoContas.excluirLancamentoBancario(lancamento.getId());
            }
        }

        List<Lancamento> registrosPararemoverAntesTeste1 = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Charles Darwin");
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

        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosGestaoContas.salvarContaCorrente(cc);

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

        LancamentoBancarioRequisicao lancRequisicao = new LancamentoBancarioRequisicao();
        lancRequisicao.setObservacao("Deposito na conta corrente do Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        lancRequisicao.setIdContaCorrente(contas.get(0).getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");

        if (!lancNovo.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancNovo) {
                assertEquals(lancRequisicao.getObservacao(), lancamentoConsultado.getObservacao());
                assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
            }
        } else {
            fail("O lancamento bancario nao foi salvo!");
        }
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
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosGestaoContas.salvarContaCorrente(cc);

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

        LancamentoBancarioRequisicao lancRequisicao = new LancamentoBancarioRequisicao();
        lancRequisicao.setObservacao("Deposito na conta corrente do Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        lancRequisicao.setIdContaCorrente(contas.get(0).getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");

        if (!lancNovo.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancNovo) {
                assertEquals(lancRequisicao.getObservacao(), lancamentoConsultado.getObservacao());
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

        LancamentoBancarioAtualizacaoRequisicao atualizarLancamentoRequisicao = new LancamentoBancarioAtualizacaoRequisicao();
        atualizarLancamentoRequisicao.setId(lancNovo.get(0).getId());        
        atualizarLancamentoRequisicao.setObservacaoAtualizada("Transferencia para a conta corrente do Charles Darwin");
        atualizarLancamentoRequisicao.setDataAtualizada(Formatadores.formatoDataInterface.format(novaData.getTime()));
        eventosGestaoContas.atualizarLancamentoBancario(atualizarLancamentoRequisicao);

        List<Lancamento> lancamentoAtualizado = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Charles");

        if (!lancamentoAtualizado.isEmpty()) {
            for (Lancamento lancAtualizado : lancamentoAtualizado) {
                assertEquals(atualizarLancamentoRequisicao.getObservacaoAtualizada(), lancAtualizado.getObservacao());
                assertEquals(atualizarLancamentoRequisicao.getDataAtualizada(), Formatadores.formatoDataInterface.format(lancAtualizado.getData()));
            }
        } else {
            fail("O lancamento bancario nao foi atualizado!");
        }
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
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosGestaoContas.salvarContaCorrente(cc);

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

        LancamentoBancarioRequisicao lancRequisicao = new LancamentoBancarioRequisicao();
        lancRequisicao.setObservacao("Deposito na conta corrente do Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        lancRequisicao.setIdContaCorrente(contas.get(0).getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancNovo = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");

        if (!lancNovo.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancNovo) {
                assertEquals(lancRequisicao.getObservacao(), lancamentoConsultado.getObservacao());
                assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
            }
        } else {
            fail("O lancamento bancario nao foi salvo!");
        }

        eventosGestaoContas.excluirLancamentoBancario(lancNovo.get(0).getId());

        List<Lancamento> lancExcluido = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");

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
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosGestaoContas.salvarContaCorrente(cc);

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

        LancamentoBancarioRequisicao lancRequisicao = new LancamentoBancarioRequisicao();
        lancRequisicao.setObservacao("Saque realizado da conta corrente de Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.SAQUE.getId());
        lancRequisicao.setIdContaCorrente(contas.get(0).getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        List<Lancamento> lancamentoDeSaque = gestaoContaBean.pesquisarLancamentoBancarioPorTipoDeLancamento(TipoLancamentoEnum.SAQUE.getId());

        if (!lancamentoDeSaque.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancamentoDeSaque) {
                assertEquals(lancRequisicao.getObservacao(), lancamentoConsultado.getObservacao());
                assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
            }
        } else {
            fail("O lancamento bancario nao foi encontrado!");
        }
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
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosGestaoContas.salvarContaCorrente(cc);

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

        LancamentoBancarioRequisicao lancRequisicao = new LancamentoBancarioRequisicao();
        lancRequisicao.setObservacao("Deposito na conta corrente do Albert Einstein");
        lancRequisicao.setValor(new BigDecimal(1234.56));
        lancRequisicao.setData(Formatadores.formatoDataInterface.format(new java.util.Date()));
        lancRequisicao.setIdTipoLancamento(TipoLancamentoEnum.DEPOSITO.getId());
        lancRequisicao.setIdContaCorrente(contas.get(0).getId());
        eventosGestaoContas.salvarLacamentoBancario(lancRequisicao);

        Calendar novaData = Calendar.getInstance();
        novaData.setTime(Formatadores.formatoDataInterface.parse(lancRequisicao.getData()));
        novaData.add(Calendar.DAY_OF_MONTH, 5);

        LancamentoBancarioRequisicao lancDoisRequisicao = new LancamentoBancarioRequisicao();
        lancDoisRequisicao.setObservacao("Saque realizado na conta corrente de Charles Darwin");
        lancDoisRequisicao.setValor(new BigDecimal(4242.31));
        lancDoisRequisicao.setData(Formatadores.formatoDataInterface.format(novaData.getTime()));
        lancDoisRequisicao.setIdTipoLancamento(TipoLancamentoEnum.SAQUE.getId());
        eventosGestaoContas.salvarLacamentoBancario(lancDoisRequisicao);

        novaData.add(Calendar.DAY_OF_MONTH, 10);
        List<Lancamento> lancamentoDeSaque = gestaoContaBean.pesquisarLancamentoBancarioPorPeriodo(Formatadores.formatoDataInterface.format(new java.util.Date()), Formatadores.formatoDataInterface.format(novaData.getTime().getTime()));

        if (!lancamentoDeSaque.isEmpty()) {
            for (Lancamento lancamentoConsultado : lancamentoDeSaque) {
                if (lancamentoConsultado.getObservacao().equals(lancRequisicao.getObservacao())) {
                    assertEquals(lancRequisicao.getObservacao(), lancamentoConsultado.getObservacao());
                    assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                    assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                    assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
                } else if (lancamentoConsultado.getObservacao().equals(lancDoisRequisicao.getObservacao())) {
                    assertEquals(lancDoisRequisicao.getObservacao(), lancamentoConsultado.getObservacao());
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
    }
}
