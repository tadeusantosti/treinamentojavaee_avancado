package logic.treinamento.model;

/**
 * Classe enumeradora que representa os codigos e descricoes dos tipos de
 * lancamentos bancarios.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
public enum TipoLancamentoEnum {
    TRANSFERENCIA(1),
    SAQUE(2),
    DEPOSITO(3);

    private final int idTipoLancamento;

    TipoLancamentoEnum(int id) {
        this.idTipoLancamento = id;
    }

    public int getId() {
        return idTipoLancamento;
    }

    /**
     * Método para recuperar o enumerador do tipo de lancamento bancario com
     * base no codigo informado
     *
     * @author Tadeu
     * @param codigoID int - codigo da agencia.
     * @return TipoLancamentoEnum - Objeto que contem o banco referente ao
     * codigo informado
     */
    public static TipoLancamentoEnum getByCodigo(int codigoID) {
        if (codigoID <= 0) {
            return null;
        }
        for (TipoLancamentoEnum tipoLanc : TipoLancamentoEnum.values()) {
            if (tipoLanc.idTipoLancamento == codigoID) {
                return tipoLanc;
            }
        }
        return null;
    }
}
