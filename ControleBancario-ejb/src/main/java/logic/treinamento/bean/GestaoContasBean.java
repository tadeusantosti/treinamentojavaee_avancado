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
import logic.treinamento.dao.InterfaceLancamentoDao;
import logic.treinamento.model.Lancamento;
import logic.treinamento.dao.TipoLancamentoEnum;
import logic.treinamento.request.AtualizarLancamentoRequisicao;
import logic.treinamento.request.LancarContasDoMesRequisicao;
import utilitarios.Formatadores;

/**
 *
 * @author tadpi
 */
@Stateless
public class GestaoContasBean implements InterfaceGestaoContas, Serializable {

    @Inject
    private InterfaceLancamentoDao lancamentoDao;

    /**
     *
     * @param lancarContasDoMesRequisicao
     * @throws Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvarLancamentoBancario(@Observes LancarContasDoMesRequisicao lancarContasDoMesRequisicao) throws Exception {

        Lancamento lanc = new Lancamento();
        lanc.setNome(lancarContasDoMesRequisicao.getNome());
        lanc.setValor(lancarContasDoMesRequisicao.getValor());
        lanc.setTipoLancamento(TipoLancamentoEnum.getByCodigo(lancarContasDoMesRequisicao.getIdTipoLancamento()));
        lanc.setData(Formatadores.validarDatasInformadas(lancarContasDoMesRequisicao.getData()).get(0));

        String retornoValidacao = validarCamposObrigatorios(lanc);

        if (retornoValidacao.isEmpty()) {
            lancamentoDao.salvarLancamentoBancario(lanc);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void atualizarLancamentoBancario(@Observes AtualizarLancamentoRequisicao atualizarLancamentoRequisicao) throws Exception {

        Lancamento lanc = new Lancamento();
        lanc.setId(atualizarLancamentoRequisicao.getId());
        lanc.setNome(atualizarLancamentoRequisicao.getNomeAtualizado());
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

        if (lanc.getNome() == null || lanc.getNome().isEmpty()) {
            return "E necessario informar o nome !";
        } else if (lanc.getValor() == null || lanc.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            return "E necessario informar um valor !";
        } else if (lanc.getTipoLancamento() != TipoLancamentoEnum.DEPOSITO
                && lanc.getTipoLancamento() != TipoLancamentoEnum.SAQUE
                && lanc.getTipoLancamento() != TipoLancamentoEnum.TRANSFERENCIA) {
            return "E necessario informar um tipo de lancamento Valido !";
        } else if (lanc.getData() == null) {
            return "E necessario informar a data do lancamento !";
        } else {
            return "";
        }

    }

    private String validarCamposObrigatoriosAtualizacao(Lancamento lanc) {

        if (lanc.getId() < 0) {
            return "E necessario informar o codigo do lancamento !";
        } else if (lanc.getNome().isEmpty()) {
            return "E necessario informar o nome !";
        } else if (lanc.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            return "E necessario informar um valor !";
        } else if (validarTipoLancamentoInformado(lanc.getTipoLancamento())) {
            return "E necessario informar um tipo de lancamento !";
        } else if (lanc.getData() == null) {
            return "E necessario informar a data do lancamento !";
        } else {
            return "";
        }

    }

    private boolean validarTipoLancamentoInformado(TipoLancamentoEnum tipoLancamento) {
        return tipoLancamento.getId() < 0;
    }

}
