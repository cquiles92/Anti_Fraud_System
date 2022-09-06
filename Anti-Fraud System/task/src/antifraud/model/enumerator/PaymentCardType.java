package antifraud.model.enumerator;

public enum PaymentCardType {
    /**
    https://www.ibm.com/docs/en/order-management-sw/9.3.0?topic=cpms-handling-credit-cards
    Credit Card examples: American Express, MasterCard, Visa, Diners Club and Carte Blanche, Discover, and JCB.
    More cards exist and can be appended.
     */

    AMERICAN_EXPRESS(15, "34", "37"), MASTERCARD(16, "51", "52", "53", "54", "55"),
    VISA_A(13, "4"), VISA_B(16, "4"),
    DINERS_CLUB(14, "36", "38", "300", "301", "302", "303", "304", "305"), DISCOVER(16, "6011"),
    JCB_A(15, "2123", "1800"), JCB_B(16, "3");

    private final String[] prefix;
    private final int cardLength;

    PaymentCardType(int cardLength, String... prefix) {
        this.prefix = prefix;
        this.cardLength = cardLength;
    }

    public String[] getPrefix() {
        return prefix;
    }

    public int getCardLength() {
        return cardLength;
    }
}
