package logic.treinamento.model;

/**
 * Classe enumeradora que representa os codigos e descricoes das agencias
 * bancarias.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
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

    /**
     * Método para recuperar o enumerador da agencia com base no codigo
     * informado
     *
     * @author Tadeu
     * @param codigoID int - codigo da agencia.
     * @return AgenciaEnum - Objeto que contem a agencia referente ao codigo
     * informado
     */
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
