package logic.treinamento.bean;

import java.math.BigDecimal;
import java.sql.Date;
import javax.inject.Inject;
import junit.framework.TestCase;
import logic.treinamento.dao.InterfaceLancamentoDao;
import logic.treinamento.dao.LancamentoDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import logic.treinamento.model.Lancamento;
import logic.treinamento.dao.TipoLancamentoEnum;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.StatelessBean;
import utilitarios.Formatadores;

@RunWith(WeldJUnit4Runner.class)
public class GestaoContasValidacoesTest extends TestCase {

    @Inject
    public InterfaceGestaoContas gestaoContaBean;

    @Inject
    InterfaceLancamentoDao gestaoContasDao;

    @org.apache.openejb.testing.Module
    public EjbJar beans() {
        EjbJar ejbJar = new EjbJar();
        ejbJar.addEnterpriseBean(new StatelessBean(GestaoContasBean.class));
        ejbJar.addEnterpriseBean(new StatelessBean(LancamentoDao.class));
        ejbJar.addEnterpriseBean(new StatelessBean(Formatadores.class));
        return ejbJar;
    }

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
        assertEquals("E necessario informar o nome !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setNome("Jair Rillo Junior");
        assertEquals("E necessario informar um valor !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setNome("Jair Rillo Junior");
        lanc.setValor(BigDecimal.valueOf(1200.05D));
        assertEquals("E necessario informar um tipo de lancamento Valido !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setNome("Jair Rillo Junior");
        lanc.setValor(BigDecimal.valueOf(1200.05D));
        lanc.setTipoLancamento(TipoLancamentoEnum.DEPOSITO);
        assertEquals("E necessario informar a data do lancamento !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setNome("Jair Rillo Junior");
        lanc.setValor(BigDecimal.valueOf(1200.05D));
        lanc.setTipoLancamento(TipoLancamentoEnum.DEPOSITO);
        lanc.setData(new Date(new java.util.Date().getTime()));
        assertEquals("", gestaoContaBean.validarCamposObrigatorios(lanc));

    }

}
