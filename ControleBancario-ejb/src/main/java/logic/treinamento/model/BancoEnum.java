package logic.treinamento.model;

/**
 * Classe enumeradora que representa os codigos e descricoes dos bancos.
 *
 * @since 1.0
 * @author Tadeu
 * @version 1.0
 */
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

     /**
     * Método para recuperar o enumerador do banco com base no codigo
     * informado
     *
     * @author Tadeu
     * @param codigoID int - codigo da agencia.
     * @return BancoEnum - Objeto que contem o banco referente ao codigo
     * informado
     */
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
