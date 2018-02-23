package logic.treinamento.model;

public enum AgenciaEnum {
    OSASCO(1),
    SAOPAULO(2),
    ARARAS(3);

    private final int idAgencia;

    AgenciaEnum(int id) {
        this.idAgencia = id;
    }

    public int getId() {
        return idAgencia;
    }

    public static AgenciaEnum getByCodigo(int codigoID) {
        if (codigoID <= 0) {
            return null;
        }
        for (AgenciaEnum agencia : AgenciaEnum.values()) {
            if (agencia.idAgencia == codigoID) {
                return agencia;
            }
        }
        return null;
    }
}
