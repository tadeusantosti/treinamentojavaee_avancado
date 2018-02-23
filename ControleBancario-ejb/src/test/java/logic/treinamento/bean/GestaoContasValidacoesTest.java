package logic.treinamento.bean;

import java.math.BigDecimal;
import java.sql.Date;
import javax.inject.Inject;
import junit.framework.TestCase;
import logic.treinamento.dao.InterfaceLancamentoDao;
import logic.treinamento.model.AgenciaEnum;
import logic.treinamento.model.BancoEnum;
import logic.treinamento.model.ContaCorrente;
import org.junit.Test;
import org.junit.runner.RunWith;
import logic.treinamento.model.Lancamento;
import logic.treinamento.model.TipoLancamentoEnum;
import utilitarios.Formatadores;

@RunWith(WeldJUnit4Runner.class)
public class GestaoContasValidacoesTest extends TestCase {

    @Inject
    public InterfaceGestaoContas gestaoContaBean;

    @Inject
    InterfaceLancamentoDao gestaoContasDao;

    /**
     * Teste responsavel por validar o metodo que verifica o campo de data
     * recebido no serviço.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi informado uma data vazia.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Metodo validador retorna uma mensagem ao requisitor:
     * A data deve ser informada!
     *
     * @throws Exception
     */
    @Test
    public void testValidarDataVazia() throws Exception {
        try {
            Formatadores.validarDatasInformadas("");
        } catch (Exception ex) {
            assertEquals("A data deve ser informada!", ex.getMessage());
        }
    }

    /**
     * Teste responsavel por validar o metodo que verifica o campo de data
     * recebido no serviço.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi informado uma data nula.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Metodo validador retorna uma mensagem ao requisitor:
     * A data deve ser informada!
     *
     * @throws Exception
     */
    @Test
    public void testValidarDataNula() throws Exception {
        try {
            Formatadores.validarDatasInformadas(null);
        } catch (Exception ex) {
            assertEquals("A data deve ser informada!", ex.getMessage());
        }
    }

    /**
     * Teste responsavel por validar o metodo que verifica o campo de data
     * recebido no serviço.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi informado uma data em um formato nao esperado
     * pelo serviço.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Metodo validador retorna uma mensagem ao requisitor:
     * Data no formato invalido!
     *
     * @throws Exception
     */
    @Test
    public void testValidarFormatoData() throws Exception {
        try {
            Formatadores.validarDatasInformadas("31-12-2017");
        } catch (Exception ex) {
            assertEquals("Data no formato invalido!", ex.getMessage());
        }
    }

    /**
     * Teste responsavel por validar o metodo que verifica os campos de data
     * inicial e final recebidas no serviço.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foi informado uma data inicial superior à data
     * final.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Metodo validador retorna uma mensagem ao requisitor:
     * A data inicial nao pode ser maior que a data final!!
     *
     * @throws Exception
     */
    @Test
    public void testValidarPeriodoData() throws Exception {
        try {
            Formatadores.validarDatasInformadas("31/12/2017", "01/12/2017");
        } catch (Exception ex) {
            assertEquals("A data inicial nao pode ser maior que a data final!", ex.getMessage());
        }
    }

    /**
     * Teste responsavel por validar o metodo que verifica os campos
     * obrigatorios recebidos pelo serviço.
     * ------------------------------------------------------------------------
     * Preparacao do cenario: -Foram informados sequencialmente todos os campos
     * nulos ou vazis que sao obrigatorios para o uso de alguns serviços do
     * sistema.
     * ------------------------------------------------------------------------
     * Resultado Esperado: -Metodo validador retorna uma mensagem ao requisitor
     * de acordo com cada campo validado.
     *
     */
    @Test
    public void testValidarCamposObrigatorios() {
        Lancamento lanc = new Lancamento();
        assertEquals("E necessario informar uma observacao para o lancamento !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setObservacao("Jair Rillo Junior");
        assertEquals("E necessario informar um valor !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setValor(BigDecimal.valueOf(1200.05D));
        assertEquals("E necessario informar um tipo de lancamento Valido !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setTipoLancamento(TipoLancamentoEnum.DEPOSITO);
        assertEquals("E necessario informar a data do lancamento !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setData(new Date(new java.util.Date().getTime()));
        assertEquals("E necessario informar o codigo da sua conta corrente !",  gestaoContaBean.validarCamposObrigatorios(lanc));
        
        lanc.setIdContaCorrente(3);
        assertEquals("", gestaoContaBean.validarCamposObrigatorios(lanc));
    }

    @Test
    public void testValidarCamposObrigatoriosCadastrarContaCorrente() {
        ContaCorrente conta = new ContaCorrente();
        assertEquals("E necessario informar o nome do titular da conta!", gestaoContaBean.validarCamposObrigatoriosCadastrarContaCorrente(conta));

        conta.setTitular("Son Goku");
        assertEquals("E necessario informar um codigo de banco Valido !", gestaoContaBean.validarCamposObrigatoriosCadastrarContaCorrente(conta));

        conta.setBanco(BancoEnum.BRADESCO);
        assertEquals("E necessario informar um codigo de agenica Valido !", gestaoContaBean.validarCamposObrigatoriosCadastrarContaCorrente(conta));

        conta.setAgencia(AgenciaEnum.OSASCO);
        assertEquals("", gestaoContaBean.validarCamposObrigatoriosCadastrarContaCorrente(conta));
    }

}
