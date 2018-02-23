package logic.treinamento.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
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

        String retornoValidacao = validarCamposObrigatoriosAtualizacao(lanc);

        if (retornoValidacao.equals("")) {
            lancamentoDao.atualizarLancamentoBancario(lanc);
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
            return lancamentoDao.pesquisarLancamentoBancarioPorNome(nome);
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

        String retornoValidacao = validarCamposObrigatoriosCadastrarContaCorrente(cc);

        if (retornoValidacao.isEmpty()) {
            contaCorrenteDao.salvarContaCorrente(cc);
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

}
