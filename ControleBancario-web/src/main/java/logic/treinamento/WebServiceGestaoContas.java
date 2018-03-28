package logic.treinamento;

import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.ws.rs.core.Response;
import logic.treinamento.bean.InterfaceGestaoContas;
import logic.treinamento.model.Lancamento;
import logic.treinamento.observer.GestaoEventosContaCorrente;
import logic.treinamento.observer.GestaoEventosLancamentoBancario;
import logic.treinamento.request.AtualizarCadastroContaCorrenteRequisicao;
import logic.treinamento.request.CadastroContaCorrenteRequisicao;
import logic.treinamento.request.LancamentoBancarioAtualizacaoRequisicao;
import logic.treinamento.request.LancamentoBancarioExclusaoRequisicao;
import logic.treinamento.request.LancamentoBancarioRequisicao;

@WebService(name = "gestaoContas")
public class WebServiceGestaoContas {

    @Inject
    private InterfaceGestaoContas gestaoContaBean;
    @Inject
    GestaoEventosLancamentoBancario eventosLancamentoBancario;
    @Inject
    GestaoEventosContaCorrente eventosContaCorrente;

    @WebMethod(operationName = "cadastrarLancamentoBancario")
    @WebResult(name = "respostaCadastro")
    public String salvarLancamentoBancario(@WebParam(name = "requisicao") LancamentoBancarioRequisicao cadastroLancamentoBancarioRequisicao) throws Exception {
        try {
            eventosLancamentoBancario.salvarLacamentoBancario(cadastroLancamentoBancarioRequisicao);
            return Response.Status.OK.getReasonPhrase();
        } catch (Exception e) {
            return Response.Status.EXPECTATION_FAILED.getReasonPhrase();
        }
    }

    @WebMethod(operationName = "atualizarLancamentoBancario")
    @WebResult(name = "respostaAtualizacaoCadastro")
    public String atualizarLancamentoBancario(@WebParam(name = "nome") LancamentoBancarioAtualizacaoRequisicao atualizarLancamentoBancarioRequisicao) throws Exception {
        try {
            eventosLancamentoBancario.atualizarLancamentoBancario(atualizarLancamentoBancarioRequisicao);
            return Response.Status.OK.getReasonPhrase();
        } catch (Exception e) {
            return Response.Status.EXPECTATION_FAILED.getReasonPhrase();
        }
    }

    @WebMethod(operationName = "excluirLancamentoBancario")
    @WebResult(name = "respostaExclusaoCadastro")
    public String excluirLancamentoBancario(@WebParam(name = "idLancamento") int idLancamento) throws Exception {
        try {
            LancamentoBancarioExclusaoRequisicao lancamento = new LancamentoBancarioExclusaoRequisicao();
            lancamento.setIdLancamento(idLancamento);
            eventosLancamentoBancario.excluirLancamentoBancario(lancamento);
            return Response.Status.OK.getReasonPhrase();
        } catch (Exception e) {
            return Response.Status.EXPECTATION_FAILED.getReasonPhrase();
        }

    }

    @WebMethod(operationName = "pesquisarLancamentoBancarioPorTipo")
    @WebResult(name = "Lancamento")
    public List<Lancamento> pesquisaLancamentoBancarioPorTipoLancamento(@WebParam(name = "tipoLancamento") int idtipolancamento) throws Exception {
        return gestaoContaBean.pesquisarLancamentoBancarioPorTipoDeLancamento(idtipolancamento);
    }

    @WebMethod(operationName = "pesquisarLancamentoBancarioPorPeriodo")
    @WebResult(name = "Lancamentos")
    public List<Lancamento> pesquisaLancamentoBancarioPorPeriodo(@WebParam(name = "dataInicial") String dataInicial, @WebParam(name = "dataFinal") String dataFinal) throws Exception {
        return gestaoContaBean.pesquisarLancamentoBancarioPorPeriodo(dataInicial, dataFinal);
    }

    @WebMethod(operationName = "pesquisarLancamentoBancarioPorObservacao")
    @WebResult(name = "Lancamentos")
    public List<Lancamento> pesquisaLancamentoBancarioPorObservacao(@WebParam(name = "observacao") String observacao) throws Exception {
        return gestaoContaBean.pesquisarLancamentoBancarioPorObservacao(observacao);
    }

    @WebMethod(operationName = "cadastrarContaContaCorrente")
    @WebResult(name = "resposta")
    public String salvarContaCorrente(@WebParam(name = "requisicao") CadastroContaCorrenteRequisicao cadastroContaCorrenteRequisicao) throws Exception {
        try {
            eventosContaCorrente.salvarContaCorrente(cadastroContaCorrenteRequisicao);
            return Response.Status.OK.getReasonPhrase();
        } catch (Exception e) {
            return Response.Status.EXPECTATION_FAILED.getReasonPhrase();
        }
    }

    @WebMethod(operationName = "atualizarContaContaCorrente")
    @WebResult(name = "resposta")
    public String atualizarCadastroContaCorrente(@WebParam(name = "requisicao") AtualizarCadastroContaCorrenteRequisicao cadastroContaCorrenteRequisicao) throws Exception {
        try {
            eventosContaCorrente.atualizarDadosContaCorrente(cadastroContaCorrenteRequisicao);
            return Response.Status.OK.getReasonPhrase();
        } catch (Exception e) {
            return Response.Status.EXPECTATION_FAILED.getReasonPhrase();
        }
    }

    @WebMethod(operationName = "excluirContaContaCorrente")
    @WebResult(name = "resposta")
    public String excluirCadastroContaCorrente(@WebParam(name = "idContaCorrente") int idContaCorrente) throws Exception {
        try {
            eventosContaCorrente.excluirContaCorrente(idContaCorrente);
            return Response.Status.OK.getReasonPhrase();
        } catch (Exception e) {
            return Response.Status.EXPECTATION_FAILED.getReasonPhrase();
        }
    }

    @WebMethod(operationName = "inativarContaContaCorrente")
    @WebResult(name = "resposta")
    public String inativarCadastroContaCorrente(@WebParam(name = "idContaCorrente") int idContaCorrente) throws Exception {
        try {
            eventosContaCorrente.inativarContaCorrente(idContaCorrente);
            return Response.Status.OK.getReasonPhrase();
        } catch (Exception e) {
            return Response.Status.EXPECTATION_FAILED.getReasonPhrase();
        }
    }

    @WebMethod(operationName = "verSaldoContaCorrente")
    @WebResult(name = "Saldo")
    public BigDecimal visualizarSadoAtualContaCorrentePorID(@WebParam(name = "codigoContaCorrente") long codigoContaCorrente) throws Exception {
        return gestaoContaBean.verSaldoContaCorrente(codigoContaCorrente);
    }

    @WebMethod(operationName = "consultarLogContaCorrente")
    @WebResult(name = "LogLancamentosBancarios")
    public List<Lancamento> consultarLancametosBancariosVinculadosContaCorrente(@WebParam(name = "codigoContaCorrente") long codigoContaCorrente) throws Exception {
        return gestaoContaBean.consultarLancametosBancariosVinculadosContaCorrente(codigoContaCorrente);
    }
}
