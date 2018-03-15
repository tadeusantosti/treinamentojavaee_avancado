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
import logic.treinamento.request.LancamentoBancarioRequisicao;
import utilitarios.Formatadores;

@Stateless
public class GestaoContasBean implements InterfaceGestaoContas, Serializable {

    @Inject
    private InterfaceLancamentoDao lancamentoDao;

    @Inject
    private InterfaceContaCorrente contaCorrenteDao;

    @Inject
    private RastreioLancamentoBancarioMovimentacaoLocal rastreio;

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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void atualizarLancamentoBancario(@Observes LancamentoBancarioAtualizacaoRequisicao atualizarLancamentoRequisicao) throws Exception {

        Lancamento lanc = new Lancamento();
        lanc.setId(atualizarLancamentoRequisicao.getId());
        lanc.setObservacao(atualizarLancamentoRequisicao.getNomeAtualizado());
        lanc.setValor(atualizarLancamentoRequisicao.getValorAtualizado());
        lanc.setTipoLancamento(TipoLancamentoEnum.getByCodigo(atualizarLancamentoRequisicao.getIdTipoLancamentoAtualizado()));
        lanc.setData(Formatadores.validarDatasInformadas(atualizarLancamentoRequisicao.getDataAtualizada()).get(0));
        lanc.setIdContaCorrente(atualizarLancamentoRequisicao.getIdContaCorrente());

        String retornoValidacao = validarCamposObrigatoriosAtualizacao(lanc);

        if (retornoValidacao.equals("")) {
            lancamentoDao.atualizarLancamentoBancario(lanc);
            atualizarSaldoContaCorrente(lanc);
            rastreio.registrarAlteracaoContaCorrente(lanc);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void excluirLancamentoBancario(@Observes long idLancamento) throws SQLException {
        if (idLancamento >= 0) {
            lancamentoDao.excluirLancamento(idLancamento);
            System.out.println("Lancamento Excluido com Sucesso!");
        } else {
            System.out.println("E necessario informar o codigo do lancamento!");
        }
    }

    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorPeriodo(String dataInicial, String dataFinal) throws Exception {
        List<Date> datas = Formatadores.validarDatasInformadas(dataInicial, dataFinal);
        return lancamentoDao.pesquisarLancamentoBancarioPorPeriodo(datas.get(0), datas.get(1));
    }

    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorNome(@Observes String nome) throws SQLException {
        if (!nome.isEmpty()) {
            return lancamentoDao.pesquisarLancamentoBancarioPorObservacao(nome);
        } else {
            return null;
        }
    }

    @Override
    public List<Lancamento> pesquisarLancamentoBancarioPorTipoDeLancamento(int idtipolancamento) throws SQLException {
        if (!validarTipoLancamentoInformado(TipoLancamentoEnum.getByCodigo(idtipolancamento))) {
            return lancamentoDao.pesquisarLancamentoBancarioPorTipoDeLancamento(TipoLancamentoEnum.getByCodigo(idtipolancamento));
        } else {
            return null;
        }
    }

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

    private String validarCamposObrigatoriosAtualizacao(Lancamento lanc) {

        if (lanc.getId() < 0) {
            return "E necessario informar o codigo do lancamento !";
        } else if (lanc.getObservacao().isEmpty()) {
            return "E necessario informar uma observacao para o lancamento !";
        } else if (lanc.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            return "E necessario informar um valor !";
        } else if (validarTipoLancamentoInformado(lanc.getTipoLancamento())) {
            return "E necessario informar um tipo de lancamento !";
        } else if (lanc.getData() == null) {
            return "E necessario informar a data do lancamento !";
        } else if (lanc.getIdContaCorrente() <= 0) {
            return "E necessario informar o codigo da sua conta corrente !";
        } else {
            return "";
        }

    }

    private boolean validarTipoLancamentoInformado(TipoLancamentoEnum tipoLancamento) {
        return tipoLancamento.getId() < 0;
    }

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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void excluirContaCorrente(@Observes long idContaCorrente) throws Exception {
        if (idContaCorrente > 0) {
            contaCorrenteDao.excluirContaCorrente(idContaCorrente);
        }
    }

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

    @Override
    public BigDecimal verSaldoContaCorrente(long idContaCorrente) throws Exception {
        if (idContaCorrente > 0) {
            ContaCorrente conta = contaCorrenteDao.pesquisarContasCorrentesPorId(idContaCorrente);
            return conta.getSaldo();
        } else {
            throw new Exception("E necessario informar o codigo da conta!");
        }
    }

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

    @Override
    public ContaCorrente pesquisarContasCorrentesPorId(long idContaCorrente) throws SQLException {
        return contaCorrenteDao.pesquisarContasCorrentesPorId(idContaCorrente);
    }

    @Override
    public void atualizarSaldoContaCorrente(Lancamento lanc) throws SQLException {
        ContaCorrente conta = pesquisarContasCorrentesPorId(lanc.getIdContaCorrente());
        if (lanc.getTipoLancamento() == TipoLancamentoEnum.SAQUE
                || lanc.getTipoLancamento() == TipoLancamentoEnum.TRANSFERENCIA) {
            conta.setSaldo(conta.getSaldo().subtract(lanc.getValor()));
        } else {
            conta.setSaldo(conta.getSaldo().add(lanc.getValor()));
        }

        contaCorrenteDao.atualizarDadosContaCorrente(conta);
    }

    @Override
    public List<Lancamento> consultarLancametosBancariosVinculadosContaCorrente(@Observes long idContaCorrente) {
        Map<Long, List<Lancamento>> mapaContasLancamentos = rastreio.getMapaContasLancamentos();
        return mapaContasLancamentos.get(idContaCorrente);
    }

}
