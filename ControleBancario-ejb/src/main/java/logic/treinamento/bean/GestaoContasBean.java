package logic.treinamento.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import logic.treinamento.dao.InterfaceContaCorrente;
import logic.treinamento.dao.InterfaceLancamentoDao;
import logic.treinamento.model.AgenciaEnum;
import logic.treinamento.model.BancoEnum;
import logic.treinamento.model.ContaCorrente;
import logic.treinamento.model.Lancamento;
import logic.treinamento.model.TipoLancamentoEnum;
import logic.treinamento.request.AtualizarCadastroContaCorrenteRequisicao;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioExclusaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;
import utilitarios.Formatadores;

/**
 * Classe responsavel pela gestão do controle das contas correntes e seus
 * respectivos lancamentos.
 *
 * @since 1.0
 * @author Tadeu
 * @version 2.0
 */
@Stateless
public class GestaoContasBean implements InterfaceGestaoContas, Serializable {

    @Inject
    private InterfaceLancamentoDao lancamentoDao;

    @Inject
    private InterfaceContaCorrente contaCorrenteDao;

    @Inject
    private RastreioLancamentoBancarioMovimentacaoLocal rastreio;

    /**
     * Método para salvar o lancamento bancario.
     *
     * @author Tadeu
     * @param ContasDoMesRequisicao LancamentoBancarioRequisicao - Dados do
     * lancamento bancario que sera salvo.
     * @throws java.lang.Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvarLancamentoBancario(@Observes LancamentoBancarioRequisicao ContasDoMesRequisicao) throws Exception {

        Lancamento lanc = new Lancamento();
        lanc.setObservacao(ContasDoMesRequisicao.getObservacao());
        lanc.setValor(ContasDoMesRequisicao.getValor());
        lanc.setTipoLancamento(TipoLancamentoEnum.getByCodigo(ContasDoMesRequisicao.getIdTipoLancamento()));
        lanc.setData(Formatadores.validarDatasInformadas(ContasDoMesRequisicao.getData()).get(0));
        lanc.setIdContaCorrente(ContasDoMesRequisicao.getIdContaCorrente());

        String retornoValidacao = validarCamposObrigatorios(lanc);

        if (retornoValidacao.isEmpty()) {
            lancamentoDao.salvarLancamentoBancario(lanc);
            atualizarSaldoContaCorrente(lanc);
            rastreio.registrarAlteracaoContaCorrente(lanc);
        }
    }

    /**
     * Método para atualizar os dados de um lancamento bancario.
     *
     * @author Tadeu
     * @param atualizarLancamentoRequisicao
     * LancamentoBancarioAtualizacaoRequisicao - Dados do lancamento bancario
     * que sera atualizado.
     * @throws java.lang.Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void atualizarLancamentoBancario(@Observes LancamentoBancarioAtualizacaoRequisicao atualizarLancamentoRequisicao) throws Exception {

        Lancamento lanc = new Lancamento();
        lanc.setId(atualizarLancamentoRequisicao.getId());
        lanc.setObservacao(atualizarLancamentoRequisicao.getObservacaoAtualizada());
        lanc.setData(Formatadores.validarDatasInformadas(atualizarLancamentoRequisicao.getDataAtualizada()).get(0));
        lanc.setIdContaCorrente(atualizarLancamentoRequisicao.getIdContaCorrente());
        lanc.setValor(BigDecimal.ZERO);
        String retornoValidacao = validarCamposObrigatoriosAtualizacao(lanc);

        if (retornoValidacao.equals("")) {
            lancamentoDao.atualizarLancamentoBancario(lanc);            
            atualizarSaldoContaCorrente(lanc);
            rastreio.registrarAlteracaoContaCorrente(lanc);
        }
    }

    /**
     * Método para excluir os dados de um lancamento bancario.
     *
     * @author Tadeu
     * @param idLancamento long - ID do lancamento bancario que sera atualizado.
     * @throws java.sql.SQLException
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void excluirLancamentoBancario(@Observes LancamentoBancarioExclusaoRequisicao lancamentoRemocao) throws SQLException {
        if (lancamentoRemocao.getIdLancamento() >= 0) {
            lancamentoDao.excluirLancamento(lancamentoRemocao.getIdLancamento());
            System.out.println("Lancamento Excluido com Sucesso!");
        } else {
            System.out.println("E necessario informar o codigo do lancamento!");
        }
    }

    /**
     * Método para consultar lancamentos bancarios atraves de um determinado
     * periodo.
     *
     * @author Tadeu
     * @param dataInicial String - Data de inicio do periodo que sera pesquisado
     * os lancamentos.
     * @param dataFinal String - Data Final do periodo que sera pesquisado os
     * lancamentos.
     * @return List<Lancamento> - Objeto que contem os lancamentos que foram
     * localizados na consulta.
     * @throws java.lang.Exception
     */
    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorPeriodo(String dataInicial, String dataFinal) throws Exception {
        List<Date> datas = Formatadores.validarDatasInformadas(dataInicial, dataFinal);
        return lancamentoDao.pesquisarLancamentoBancarioPorPeriodo(datas.get(0), datas.get(1));
    }

    /**
     * Método para consultar lancamentos bancarios atraves da descricao da
     * observacao do lancamento.
     *
     * @author Tadeu
     * @param observacao String - Observacao do lancamento bancario.
     * @return List<Lancamento> - Objeto que contem os lancamentos que foram
     * localizados na consulta.
     * @throws java.sql.SQLException
     */
    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorObservacao(@Observes String observacao) throws SQLException {
        if (!observacao.isEmpty()) {
            return lancamentoDao.pesquisarLancamentoBancarioPorObservacao(observacao);
        } else {
            return null;
        }
    }

    /**
     * Método para consultar lancamentos bancarios atraves do tipo do lancamento
     * bancario.
     *
     * @author Tadeu
     * @param idtipolancamento int - ID do tipo do lancamento bancario.
     * @return List<Lancamento> - Objeto que contem os lancamentos que foram
     * localizados na consulta.
     * @throws java.sql.SQLException
     */
    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorTipoDeLancamento(int idtipolancamento) throws SQLException {
        if (!validarTipoLancamentoInformado(TipoLancamentoEnum.getByCodigo(idtipolancamento))) {
            return lancamentoDao.pesquisarLancamentoBancarioPorTipoDeLancamento(TipoLancamentoEnum.getByCodigo(idtipolancamento));
        } else {
            return null;
        }
    }

    /**
     * Método para validar os campos obrigatorios para salvar o lancamento
     * bancario.
     *
     * @author Tadeu
     * @param lanc Lancamento - Objeto que carrega as informacoes do lancamento
     * bancario.
     * @return String - Objeto que contem o resultado da validacao.
     */
    @Override
    public String validarCamposObrigatorios(Lancamento lanc) {

        if (lanc.getObservacao() == null || lanc.getObservacao().isEmpty()) {
            return "E necessario informar uma observacao para o lancamento !";
        } else if (lanc.getValor() == null || lanc.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            return "E necessario informar um valor !";
        } else if (lanc.getTipoLancamento() != TipoLancamentoEnum.DEPOSITO
                && lanc.getTipoLancamento() != TipoLancamentoEnum.SAQUE
                && lanc.getTipoLancamento() != TipoLancamentoEnum.TRANSFERENCIA) {
            return "E necessario informar um tipo de lancamento Valido !";
        } else if (lanc.getData() == null) {
            return "E necessario informar a data do lancamento !";
        } else if (lanc.getIdContaCorrente() <= 0) {
            return "E necessario informar o codigo da sua conta corrente !";
        } else {
            return "";
        }

    }

    /**
     * Método para validar os campos obrigatorios para atualizar o lancamento
     * bancario.
     *
     * @author Tadeu
     * @param lanc Lancamento - Objeto que carrega as informacoes do lancamento
     * bancario.
     * @return String - Objeto que contem o resultado da validacao.
     */
    private String validarCamposObrigatoriosAtualizacao(Lancamento lanc) {

        if (lanc.getId() < 0) {
            return "E necessario informar o codigo do lancamento !";
        } else if (lanc.getObservacao().isEmpty()) {
            return "E necessario informar uma observacao para o lancamento !";
        } else if (lanc.getData() == null) {
            return "E necessario informar a data do lancamento !";
        } else {
            return "";
        }

    }

    /**
     * Método para validar o tipo do lancamento que esta sendo informado.
     *
     * @author Tadeu
     * @param tipoLancamento TipoLancamentoEnum - Tipo do lancamento.
     * @return boolean - Resultado da validacao.
     */
    private boolean validarTipoLancamentoInformado(TipoLancamentoEnum tipoLancamento) {
        return TipoLancamentoEnum.values().equals(tipoLancamento.getId());
    }

    /**
     * Método para salvar a Conta Corrente.
     *
     * @author Tadeu
     * @param contaCorrenteRequisicao CadastroContaCorrenteRequisicao - Dados da
     * conta corrente que sera salva.
     * @throws java.lang.Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvarContaCorrente(@Observes CadastroContaCorrenteRequisicao contaCorrenteRequisicao) throws Exception {

        ContaCorrente cc = new ContaCorrente();
        cc.setAgencia(AgenciaEnum.getByCodigo(contaCorrenteRequisicao.getAgencia()));
        cc.setBanco(BancoEnum.getByCodigo(contaCorrenteRequisicao.getBanco()));
        cc.setTitular(contaCorrenteRequisicao.getTitular());
        cc.setLancamento(new ArrayList<Lancamento>());

        if (validarCamposObrigatoriosCadastrarContaCorrente(cc).isEmpty()) {
            contaCorrenteDao.salvarContaCorrente(cc);
        }
    }

    /**
     * Método para excluir uma Conta Corrente.
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente que sera excluida.
     * @throws java.lang.Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void excluirContaCorrente(@Observes long idContaCorrente) throws Exception {
        if (idContaCorrente > 0) {
            contaCorrenteDao.excluirContaCorrente(idContaCorrente);
        }
    }

    /**
     * Método para inativar uma Conta Corrente.
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente que sera inativada.
     * @throws java.lang.Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void inativarContaCorrente(@Observes long idContaCorrente) throws Exception {
        if (idContaCorrente > 0) {
            contaCorrenteDao.inativarContaCorrente(idContaCorrente);
        }
    }

    /**
     * Método para atualizar dados da Conta Corrente.
     *
     * @author Tadeu
     * @param contaCorrenteRequisicao AtualizarCadastroContaCorrenteRequisicao -
     * Dados da conta corrente que sera atualizada.
     * @throws java.lang.Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void atualizarDadosContaCorrente(@Observes AtualizarCadastroContaCorrenteRequisicao contaCorrenteRequisicao) throws Exception {
        ContaCorrente cc = new ContaCorrente();
        cc.setAgencia(AgenciaEnum.getByCodigo(contaCorrenteRequisicao.getAgencia()));
        cc.setBanco(BancoEnum.getByCodigo(contaCorrenteRequisicao.getBanco()));
        cc.setTitular(contaCorrenteRequisicao.getTitular());
        cc.setSaldo(contaCorrenteRequisicao.getSaldo());
        if (!validarDadosAntesAtualizarContaCorrente(contaCorrenteRequisicao).isEmpty()) {
            contaCorrenteDao.atualizarDadosContaCorrente(cc);
        }
    }

    /**
     * Método para consultar o saldo atual da Conta Corrente.
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente.
     * @return BigDecimal - Saldo da conta corrente
     * @throws java.lang.Exception
     */
    @Override
    public BigDecimal verSaldoContaCorrente(long idContaCorrente) throws Exception {
        if (idContaCorrente > 0) {
            ContaCorrente conta = contaCorrenteDao.pesquisarContasCorrentesPorId(idContaCorrente);
            return conta.getSaldo();
        } else {
            throw new Exception("E necessario informar o codigo da conta!");
        }
    }

    /**
     * Método para validar os campos obrigatorios para salvar uma conta
     * corrente.
     *
     * @author Tadeu
     * @param conta ContaCorrente - Objeto que carrega os dados da conta
     * corrente.
     * @return String - Resultado da validacao
     */
    @Override
    public String validarCamposObrigatoriosCadastrarContaCorrente(ContaCorrente conta) {

        if (conta.getTitular() == null || conta.getTitular().isEmpty()) {
            return "E necessario informar o nome do titular da conta!";
        } else if (conta.getBanco() != BancoEnum.BRADESCO
                && conta.getBanco() != BancoEnum.ITAU
                && conta.getBanco() != BancoEnum.SANTANDER) {
            return "E necessario informar um codigo de banco Valido !";
        } else if (conta.getAgencia() != AgenciaEnum.ARARAS
                && conta.getAgencia() != AgenciaEnum.OSASCO
                && conta.getAgencia() != AgenciaEnum.SAOPAULO) {
            return "E necessario informar um codigo de agenica Valido !";
        } else {
            return "";
        }

    }

    /**
     * Método para validar os campos obrigatorios para atualizar uma conta
     * corrente.
     *
     * @author Tadeu
     * @param contaCorrenteRequisicao AtualizarCadastroContaCorrenteRequisicao -
     * Objeto que carrega os dados da conta corrente.
     * @return String - Resultado da validacao
     */
    private String validarDadosAntesAtualizarContaCorrente(AtualizarCadastroContaCorrenteRequisicao contaCorrenteRequisicao) throws Exception {

        if (contaCorrenteRequisicao.getIdContaCorrente() <= 0) {
            return "E necessario informar o ID da conta!";
        } else if (contaCorrenteRequisicao.getTitular() == null || contaCorrenteRequisicao.getTitular().isEmpty()) {
            return "E necessario informar o nome do titular da conta!";
        } else if (contaCorrenteRequisicao.getBanco() != BancoEnum.BRADESCO.getId()
                && contaCorrenteRequisicao.getBanco() != BancoEnum.ITAU.getId()
                && contaCorrenteRequisicao.getBanco() != BancoEnum.SANTANDER.getId()) {
            return "E necessario informar um codigo de banco Valido !";
        } else if (contaCorrenteRequisicao.getAgencia() != AgenciaEnum.ARARAS.getId()
                && contaCorrenteRequisicao.getAgencia() != AgenciaEnum.OSASCO.getId()
                && contaCorrenteRequisicao.getAgencia() != AgenciaEnum.SAOPAULO.getId()) {
            return "E necessario informar um codigo de agenica Valido !";
        } else {
            return "";
        }
    }

    /**
     * Método para pesquisar contas correntes atraves do ID.
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente.
     * @return ContaCorrente - Objeto que contem os dados da conta corrente
     * consultada.
     * @throws java.sql.SQLException
     */
    @Override
    public ContaCorrente pesquisarContasCorrentesPorId(long idContaCorrente) throws SQLException {
        return contaCorrenteDao.pesquisarContasCorrentesPorId(idContaCorrente);
    }

    /**
     * Método para atualizar o saldo da conta corrente.
     *
     * @author Tadeu
     * @param lanc Lancamento - Dados do lancamento que impactou no saldo da
     * conta corrente.
     * @throws java.sql.SQLException
     */
    @Override
    public void atualizarSaldoContaCorrente(Lancamento lanc) throws SQLException {
        ContaCorrente conta = pesquisarContasCorrentesPorId(lanc.getIdContaCorrente());
        if (lanc.getTipoLancamento() == TipoLancamentoEnum.SAQUE || lanc.getTipoLancamento() == TipoLancamentoEnum.TRANSFERENCIA) {
            conta.setSaldo(conta.getSaldo().subtract(lanc.getValor()));
        } else {
            conta.setSaldo(conta.getSaldo().add(lanc.getValor()));
        }
        contaCorrenteDao.atualizarDadosContaCorrente(conta);
    }

    /**
     * Método para consultar os lancamentos bancarios ligados a conta corrente
     *
     * @author Tadeu
     * @param idContaCorrente long - ID da conta corrente.
     * @return List<Lancamento> - Objeto que contem os lancamentos vinculados a
     * conta informada
     */
    @Override
    public List<Lancamento> consultarLancametosBancariosVinculadosContaCorrente(@Observes long idContaCorrente) {
        Map<Long, List<Lancamento>> mapaContasLancamentos = rastreio.getMapaContasLancamentos();
        return mapaContasLancamentos.get(idContaCorrente);
    }

}
