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
import logic.treinamento.observer.GestaoEventosContaCorrente;
import logic.treinamento.observer.GestaoEventosLancamentoBancario;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioExclusaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import utilitarios.Formatadores;

@RunWith(WeldJUnit4Runner.class)
public class LancamentoBancarioTest {

    @Inject
    public InterfaceGestaoContas gestaoContaBean;

    @Inject
    InterfaceLancamentoDao gestaoContasDao;

    @Inject
    GestaoEventosLancamentoBancario eventoLancamentoBancario;

    @Inject
    GestaoEventosContaCorrente eventosContaCorrente;

    @Inject
    InterfaceContaCorrente contaCorrenteDao;

    @Inject
    RastreioLancamentoBancarioMovimentacaoLocal rastreioBean;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
                eventosContaCorrente.excluirContaCorrente(contaCorrente.getId());
            }
        }
    }

    /** <H3>Teste de Criacao e Consulta de um Lançamento Bancario</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Validar o serviço de lançamento de contas do mês e
     * consultar o lançamento salvo através de parte do nome do cliente.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi informado todos os campos principais necessarios para a correta
     * inclusao dos dados</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Salvar um lancamento bancario <i><br>
     * Resultado esperado: Sistema persistiu o lancamento bancario na base.
     * <li> <i> Cenário 2: Consultar o lancamento bancario salvo<i><br>
     * Resultado esperado: Sistema consultou corretamente os dados salvos
     * informando apenas parte da descricao da obserrvacao<br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testSalvarLancamentoBancario() throws Exception {

        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosContaCorrente.salvarContaCorrente(cc);

        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        if (!contas.isEmpty()) {
            for (ContaCorrente conta : contas) {
                assertTrue(BigDecimal.ZERO.compareTo(conta.getSaldo()) == 0);
                assertEquals(cc.getAgencia(), conta.getAgencia().getId());
                assertEquals(cc.getBanco(), conta.getBanco().getId());
                assertEquals(cc.getTitular(), conta.getTitular());
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
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicao);

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

        List<Lancamento> resultadoConsulta = gestaoContaBean.consultarLancametosBancariosVinculadosContaCorrente(lancNovo.get(0).getIdContaCorrente());
        if (!resultadoConsulta.isEmpty()) {
            for (Lancamento lancamentoConsultado : resultadoConsulta) {
                assertEquals(lancRequisicao.getObservacao(), lancamentoConsultado.getObservacao());
                assertEquals(lancRequisicao.getValor().doubleValue(), lancamentoConsultado.getValor().doubleValue());
                assertEquals(lancRequisicao.getData(), Formatadores.formatoDataInterface.format(lancamentoConsultado.getData()));
                assertEquals(TipoLancamentoEnum.getByCodigo(lancRequisicao.getIdTipoLancamento()), lancamentoConsultado.getTipoLancamento());
            }
        } else {
            fail("O lancamento bancario nao foi salvo!");
        }
    }

    /** <H3>Teste de Atualizacao de um Lançamento Bancario</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Atualizar os dados de um lancamento bancario
     * persistido na base de dados.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi incluido um lancamento bancario na base de dados</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Atualizar dados um lancamento bancario <i><br>
     * Resultado esperado: Sistema atualizou os dados do lancamento bancario
     * corretamente.
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testAtualizarDadosLancamentoBancario() throws Exception {
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosContaCorrente.salvarContaCorrente(cc);

        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        if (!contas.isEmpty()) {
            for (ContaCorrente conta : contas) {
                assertTrue(BigDecimal.ZERO.compareTo(conta.getSaldo()) == 0);
                assertEquals(cc.getAgencia(), conta.getAgencia().getId());
                assertEquals(cc.getBanco(), conta.getBanco().getId());
                assertEquals(cc.getTitular(), conta.getTitular());
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
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicao);

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
        atualizarLancamentoRequisicao.setIdContaCorrente(contas.get(0).getId());
        //eventoLancamentoBancario.atualizarLancamentoBancario(atualizarLancamentoRequisicao);        
        gestaoContaBean.atualizarLancamentoBancario(atualizarLancamentoRequisicao);

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

    /** <H3>Teste de Exclusao de um Lançamento Bancario</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Excluir os dados de um lancamento bancario persistido
     * na base de dados.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi incluido um lancamento bancario na base de dados</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Excluir os dados um lancamento bancario <i><br>
     * Resultado esperado: Sistema excluiu os dados do lancamento bancario
     * corretamente.
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testExcluirLancamentoBancario() throws Exception {
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosContaCorrente.salvarContaCorrente(cc);

        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        if (!contas.isEmpty()) {
            for (ContaCorrente conta : contas) {
                assertTrue(BigDecimal.ZERO.compareTo(conta.getSaldo()) == 0);
                assertEquals(cc.getAgencia(), conta.getAgencia().getId());
                assertEquals(cc.getBanco(), conta.getBanco().getId());
                assertEquals(cc.getTitular(), conta.getTitular());
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
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicao);

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
        LancamentoBancarioExclusaoRequisicao lancamentoRemocao = new LancamentoBancarioExclusaoRequisicao();
        lancamentoRemocao.setIdLancamento(lancNovo.get(0).getId());
        eventoLancamentoBancario.excluirLancamentoBancario(lancamentoRemocao);

        List<Lancamento> lancExcluido = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("Albert");

        if (!lancExcluido.isEmpty()) {
            fail("O lancamento bancario nao foi excluido!");
        }
    }

    /** <H3>Teste de Consulta de Lançamentos Bancarios atraves do tipo de
     * lancamento</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Pesquisar os dados de um lancamento persistido na base
     * de dados atraves do tipo do lancamento..</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi incluido um lancamento bancario na base de dados com o tipo de
     * lancamento SAQUE</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Consultar os dados um lancamento bancario atraves do
     * tipo<i><br>
     * Resultado esperado: Sistema recuperou os dados do lancamento bancario
     * atraves do tipo informado.
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testPesquisarLancamentoBancarioPorTipoDeLancamento() throws Exception {
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosContaCorrente.salvarContaCorrente(cc);

        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        if (!contas.isEmpty()) {
            for (ContaCorrente conta : contas) {
                assertTrue(BigDecimal.ZERO.compareTo(conta.getSaldo()) == 0);
                assertEquals(cc.getAgencia(), conta.getAgencia().getId());
                assertEquals(cc.getBanco(), conta.getBanco().getId());
                assertEquals(cc.getTitular(), conta.getTitular());
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
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicao);

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

    /** <H3>Teste de Consulta de Lançamentos Bancarios atraves de um
     * periodo</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Pesquisar os dados de um lancamento persistido na base
     * de dados atraves de um determniado periodo..</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foram inseridos na base de dados dois lancamentos distintos.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Consultar os dados um lancamento bancario atraves do
     * tipo<i><br>
     * Resultado esperado: Sistema recuperou os dados do lancamento bancario
     * atraves do periodo informado.
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testPesquisarLancamentoBancarioPorPeriodo() throws Exception {
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosContaCorrente.salvarContaCorrente(cc);

        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        if (!contas.isEmpty()) {
            for (ContaCorrente conta : contas) {
                assertTrue(BigDecimal.ZERO.compareTo(conta.getSaldo()) == 0);
                assertEquals(cc.getAgencia(), conta.getAgencia().getId());
                assertEquals(cc.getBanco(), conta.getBanco().getId());
                assertEquals(cc.getTitular(), conta.getTitular());
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
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicao);

        Calendar novaData = Calendar.getInstance();
        novaData.setTime(Formatadores.formatoDataInterface.parse(lancRequisicao.getData()));
        novaData.add(Calendar.DAY_OF_MONTH, 5);

        LancamentoBancarioRequisicao lancDoisRequisicao = new LancamentoBancarioRequisicao();
        lancDoisRequisicao.setObservacao("Saque realizado na conta corrente de Charles Darwin");
        lancDoisRequisicao.setValor(new BigDecimal(4242.31));
        lancDoisRequisicao.setData(Formatadores.formatoDataInterface.format(novaData.getTime()));
        lancDoisRequisicao.setIdTipoLancamento(TipoLancamentoEnum.SAQUE.getId());
        eventoLancamentoBancario.salvarLacamentoBancario(lancDoisRequisicao);

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

    /** <H3>Teste de Exceção na Exclusao de um Lançamento Bancario</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Forçar uma falha no processo de excluir os dados de um
     * lancamento bancario persistido na base de dados.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi incluido um lancamento bancario na base de dados</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Não informar o ID do lancamento bancario que deveria
     * ser excluido<i><br>
     * Resultado esperado: Sistema apresentou uma exceçao ao tentar excluir o
     * lancamento bancario sem informar o ID.
     * </ul>
     * <br>
     * <p>
     * @since 2.0
     * @author Tadeu
     * @version 1.0 </p>
     */
    @Test
    public void testValidarCodigoLancamentoInformadoAntesExcluir() throws Exception {
        CadastroContaCorrenteRequisicao cc = new CadastroContaCorrenteRequisicao();
        cc.setAgencia(AgenciaEnum.ARARAS.getId());
        cc.setBanco(BancoEnum.BRADESCO.getId());
        cc.setTitular("Son Gohan");
        eventosContaCorrente.salvarContaCorrente(cc);

        List<ContaCorrente> contas = contaCorrenteDao.pesquisarTodasContasCorrentes();

        if (!contas.isEmpty()) {
            for (ContaCorrente conta : contas) {
                assertTrue(BigDecimal.ZERO.compareTo(conta.getSaldo()) == 0);
                assertEquals(cc.getAgencia(), conta.getAgencia().getId());
                assertEquals(cc.getBanco(), conta.getBanco().getId());
                assertEquals(cc.getTitular(), conta.getTitular());
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
        eventoLancamentoBancario.salvarLacamentoBancario(lancRequisicao);

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
        LancamentoBancarioExclusaoRequisicao lancamentoRemocao = new LancamentoBancarioExclusaoRequisicao();
        lancamentoRemocao.setIdLancamento(0);

        thrown.expect(javax.enterprise.event.ObserverException.class);
        eventoLancamentoBancario.excluirLancamentoBancario(lancamentoRemocao);
    }

    /** <H3>Teste de retorno vazio caso nao seja informado nenhuma
     * observacao</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Garantir que o metodo resposavel por pesquisar
     * lancamentos bancarios retorne uma lista nula caso o usuario nao informe
     * nenhuma informacao no campo de pesquisar (observacao) .</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi executado o metodo de consulta de lancamentos bancarios por
     * observacao</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Executar o metodo de consulta de lancamentos
     * bancarios por observacao nao informando nehuma informacao<i><br>
     * Resultado esperado: Sistema retornou um objeto vazio.
     * </ul>
     * <br>
     * <p>
     * @since 2.0
     * @author Tadeu
     * @version 1.0 </p>
     */
    @Test
    public void testValidarRetornoNuloPesquisarLancamentoBancarioPorObservacaoSemInformarNenhumaDescricao() throws Exception {

        List<Lancamento> resultadoDaPesquisa = gestaoContaBean.pesquisarLancamentoBancarioPorObservacao("");

        if (resultadoDaPesquisa != null) {
            fail("Este metodo nao deveria retornar nada!");
        }
    }

    /** <H3>Teste de retorno vazio caso nao seja informado um tipo de lancamento
     * inexistente</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Garantir que o metodo resposavel por pesquisar
     * lancamentos bancarios retorne uma lista nula caso o usuario informe um
     * codigo inexistente para o tipo de lancamento bancario.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi executado o metodo de consulta de lancamentos bancarios por tipo</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Executar o metodo de pesquisa de lancamentos
     * bancarios informando um tipo de lancamento inexistente<i><br>
     * Resultado esperado: Sistema retornou um objeto vazio.
     * </ul>
     * <br>
     * <p>
     * @since 2.0
     * @author Tadeu
     * @version 1.0 </p>
     */
    @Test
    public void testValidarRetornoNuloPesquisarLancamentoBancarioPorTipoDeLancamentoInformandoIdInexistente() throws Exception {

        List<Lancamento> resultadoDaPesquisa = gestaoContaBean.pesquisarLancamentoBancarioPorTipoDeLancamento(0);

        if (resultadoDaPesquisa != null) {
            fail("Este metodo nao deveria retornar nada!");
        }
    }
}
