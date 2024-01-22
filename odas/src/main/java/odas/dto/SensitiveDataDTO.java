package odas.dto;

public class SensitiveDataDTO {
    private String cardNumber;
    private String idCardNumber;
    public SensitiveDataDTO() {
    }
    public SensitiveDataDTO(String cardNumber, String idCardNumber) {
        this.cardNumber = cardNumber;
        this.idCardNumber = idCardNumber;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getIdCardNumber() {
        return idCardNumber;
    }
    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }
}