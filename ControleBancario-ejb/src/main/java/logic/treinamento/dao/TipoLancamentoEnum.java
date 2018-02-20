package logic.treinamento.dao;

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
