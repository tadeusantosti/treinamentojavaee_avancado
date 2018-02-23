package logic.treinamento.model;

public enum BancoEnum {
    BRADESCO(1),
    ITAU(2),
    SANTANDER(3);

    private final int idBanco;

    BancoEnum(int id) {
        this.idBanco = id;
    }

    public int getId() {
        return idBanco;
    }

    public static BancoEnum getByCodigo(int codigoID) {
        if (codigoID <= 0) {
            return null;
        }
        for (BancoEnum banco : BancoEnum.values()) {
            if (banco.idBanco == codigoID) {
                return banco;
            }
        }
        return null;
    }
}
