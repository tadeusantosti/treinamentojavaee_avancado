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
import logic.treinamento.request.AtualizarCadastroContaCorrenteRequisicao;
import utilitarios.Formatadores;

@RunWith(WeldJUnit4Runner.class)
public class GestaoContasValidacoesTest extends TestCase {

    @Inject
    public InterfaceGestaoContas gestaoContaBean;

    @Inject
    InterfaceLancamentoDao gestaoContasDao;

    /** <H3>Teste de Validacao do campo DATA Vazia</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica o
     * campo de data recebido no serviço.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi informado uma data vazia.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Informar uma data vazia ao metodo validador. <i><br>
     * Resultado esperado: Metodo validador retorna uma mensagem ao requisitor:
     * A data deve ser informada!<br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testValidarDataVazia() throws Exception {
        try {
            Formatadores.validarDatasInformadas("");
        } catch (Exception ex) {
            assertEquals("A data deve ser informada!", ex.getMessage());
        }
    }

    /** <H3>Teste de Validacao do campo DATA Nula</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica o
     * campo de data recebido no serviço.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi informado uma data nula.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Informar uma data nula ao metodo validador. <i><br>
     * Resultado esperado: Metodo validador retorna uma mensagem ao requisitor:
     * A data deve ser informada!<br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testValidarDataNula() throws Exception {
        try {
            Formatadores.validarDatasInformadas(null);
        } catch (Exception ex) {
            assertEquals("A data deve ser informada!", ex.getMessage());
        }
    }

    /** <H3>Teste de Validacao do formato da DATA recebida no campo DATA no
     * WebService</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica o
     * campo de data recebido no serviço.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi informado uma data com um formato invalido.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Informar uma data com um formato nao esperado ao
     * metodo validador. <i><br>
     * Resultado esperado: Metodo validador retorna uma mensagem ao requisitor:
     * Data no formato invalido!<br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testValidarFormatoData() throws Exception {
        try {
            Formatadores.validarDatasInformadas("31-12-2017");
        } catch (Exception ex) {
            assertEquals("Data no formato invalido!", ex.getMessage());
        }
    }

    /** <H3>Teste de Validacao das data informadas para a consulta por
     * periodo</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica os
     * campos de data inicial e final recebidas no serviço.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foi informado uma data inicial superior à data final..</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Informar uma data inicial superior a data final do
     * periodo. <i><br>
     * Resultado esperado: Metodo validador retorna uma mensagem ao requisitor:
     * A data inicial nao pode ser maior que a data final!<br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testValidarPeriodoData() throws Exception {
        try {
            Formatadores.validarDatasInformadas("31/12/2017", "01/12/2017");
        } catch (Exception ex) {
            assertEquals("A data inicial nao pode ser maior que a data final!", ex.getMessage());
        }
    }

    /** <H3>Teste de Validacao dos campos obrigatorios para o cadastro de um
     * novo lancamento bancario</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica os
     * campos obrigatorios recebidos pelo serviço.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foram informados sequencialmente todos os campos nulos ou vazis que sao
     * obrigatorios para o cadastro de um novo lancamento bancario.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Nao informar nada no campo OBSERVACAO. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar uma observacao para o lancamento ! <br>
     * <li> <i> Cenário 2: Nao informar nada no campo VALOR. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar um valor ! <br>
     * <li> <i> Cenário 3: Nao informar nada no campo TIPO LANCAMENTO. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar um tipo de lancamento Valido ! <br>
     * <li> <i> Cenário 4: Nao informar nada no campo DATA. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar a data do lancamento ! <br>
     * <li> <i> Cenário 5: Nao informar nada no campo ID CONTA CORRENTE. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar o codigo da sua conta corrente ! <br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
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
        assertEquals("E necessario informar o codigo da sua conta corrente !", gestaoContaBean.validarCamposObrigatorios(lanc));

        lanc.setIdContaCorrente(3);
        assertEquals("", gestaoContaBean.validarCamposObrigatorios(lanc));
    }

    /** <H3>Teste de Validacao dos campos obrigatorios para o cadastro de uma
     * nova Conta Corrente</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica os
     * campos obrigatorios recebidos pelo serviço.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foram informados sequencialmente todos os campos nulos ou vazis que sao
     * obrigatorios para o cadastro de uma nova conta corrente.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Nao informar nada no campo TITULAR. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar o nome do titular da conta! <br>
     * <li> <i> Cenário 2: Nao informar nada no campo CODIGO DO BANCO. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar um codigo de banco Valido ! <br>
     * <li> <i> Cenário 3: Nao informar nada no campo CODIGO DA AGENCIA. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar um codigo de agenica Valido ! <br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
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

    /** <H3>Teste de Validacao dos campos obrigatorios para a atualização de um
     * lancamento bancario</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica os
     * campos obrigatorios recebidos pelo serviço para atualizacao do lancamento
     * bancario.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foram informados sequencialmente todos os campos nulos ou vazis que sao
     * obrigatorios para o cadastro de uma nova conta corrente.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Informar um ID menor ou igual a zeroa. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar o codigo do lancamento ! <br>
     * <li> <i> Cenário 2: Nao informar nada no campo OBSERVACAO. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar uma observacao para o lancamento ! <br>
     * <li> <i> Cenário 3: Nao informar nada no campo DATA. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar a data do lancamento ! <br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testValidarCamposObrigatoriosAtualizacao() {
        Lancamento lanc = new Lancamento();
        lanc.setId(0);

        assertEquals("E necessario informar o codigo do lancamento !", gestaoContaBean.validarCamposObrigatoriosAtualizacao(lanc));

        lanc.setId(1);
        lanc.setObservacao("");
        assertEquals("E necessario informar uma observacao para o lancamento !", gestaoContaBean.validarCamposObrigatoriosAtualizacao(lanc));

        lanc.setId(1);
        lanc.setObservacao("Teste de validacao");
        assertEquals("E necessario informar a data do lancamento !", gestaoContaBean.validarCamposObrigatoriosAtualizacao(lanc));
    }

    /** <H3>Teste de Validacao dos campos obrigatorios para a atualização dos
     * dados de uma conta corrente</H3>
     * <br>
     * <br>
     * <p>
     * Objetivo do teste: Teste responsavel por validar o metodo que verifica os
     * campos obrigatorios recebidos pelo serviço para atualizacao dos dados de
     * uma conta corrente.</p>
     * <br>
     * <p>
     * <b>Configuração inicial para a realização dos testes: </b> <br>
     * Foram informados sequencialmente todos os campos nulos ou vazis que sao
     * obrigatorios para o cadastro de uma nova conta corrente.</p>
     * <br>
     * <p>
     * <b>Relação de cenários com sua descrição, os passos executados e os
     * resultados esperados. *</b> </p>
     * <ul>
     * <li> <i> Cenário 1: Informar um ID menor ou igual a zeroa. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar o ID da conta! <br>
     * <li> <i> Cenário 2: Nao informar nada no campo TITULAR. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar o nome do titular da conta! <br>
     * <li> <i> Cenário 3: Nao informar nada no campo BANCO. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar um codigo de banco Valido ! <br>
     * <li> <i> Cenário 4: Nao informar nada no campo AGENCIA. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem: E
     * necessario informar um codigo de agenica Valido ! <br>
     * <li> <i> Cenário 5: Informar todos os campos necessarios. <i><br>
     * Resultado esperado: Metodo validador retorna a seguinte mensagem
     * vazia<br>
     * </ul>
     * <br>
     * <p>
     * @since 1.0
     * @author Tadeu
     * @version 2.0 </p>
     */
    @Test
    public void testValidarDadosAntesAtualizarContaCorrente() throws Exception {
        AtualizarCadastroContaCorrenteRequisicao contaCorrenteRequisicao = new AtualizarCadastroContaCorrenteRequisicao();

        contaCorrenteRequisicao.setIdContaCorrente(0);
        assertEquals("E necessario informar o ID da conta!", gestaoContaBean.validarDadosAntesAtualizarContaCorrente(contaCorrenteRequisicao));

        contaCorrenteRequisicao.setIdContaCorrente(99);
        assertEquals("E necessario informar o nome do titular da conta!", gestaoContaBean.validarDadosAntesAtualizarContaCorrente(contaCorrenteRequisicao));

        contaCorrenteRequisicao.setIdContaCorrente(99);
        contaCorrenteRequisicao.setTitular("Son Gohan");
        contaCorrenteRequisicao.setBanco(0);
        assertEquals("E necessario informar um codigo de banco Valido !", gestaoContaBean.validarDadosAntesAtualizarContaCorrente(contaCorrenteRequisicao));

        contaCorrenteRequisicao.setIdContaCorrente(99);
        contaCorrenteRequisicao.setTitular("Son Gohan");
        contaCorrenteRequisicao.setBanco(BancoEnum.SANTANDER.getId());
        contaCorrenteRequisicao.setAgencia(0);
        assertEquals("E necessario informar um codigo de agenica Valido !", gestaoContaBean.validarDadosAntesAtualizarContaCorrente(contaCorrenteRequisicao));

        contaCorrenteRequisicao.setIdContaCorrente(99);
        contaCorrenteRequisicao.setTitular("Son Gohan");
        contaCorrenteRequisicao.setBanco(BancoEnum.SANTANDER.getId());
        contaCorrenteRequisicao.setAgencia(AgenciaEnum.OSASCO.getId());
        assertEquals("", gestaoContaBean.validarDadosAntesAtualizarContaCorrente(contaCorrenteRequisicao));
    }

}
