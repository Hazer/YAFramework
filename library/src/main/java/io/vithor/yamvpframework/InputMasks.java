package io.vithor.yamvpframework;

import android.support.annotation.StringDef;

public final class InputMasks {
    public static final String RG_MASK = "##.###.###-#";
    public static final String CPF_MASK = "###.###.###-##";
    public static final String CNPJ_MASK = "##.###.###/####-##";
    public static final String CEP_MASK = "##.###-###";

    public static final String BANK_AGENCY_ACCOUNT_MASK = "####.#####-#";
    public static final String BANK_ACCOUNT_ONLY_MASK = "#####-#";

    public static final String DDD_NUMBER_MASK = "(##)";
    public static final String BRAZIL_PHONE_NUMBER_MASK = "####-####";
    public static final String BRAZIL_PHONE_NUMBER_9DIGITS_MASK = "#####-####";
    public static final String BRAZIL_REGION_PHONE_NUMBER_MASK = "(##) ####-####";
    public static final String BRAZIL_REGION_PHONE_NUMBER_9DIGITS_MASK = "(##) #####-####";
    public static final String BRAZIL_LICENSE_PLATE = "###-####";

    @StringDef({
            RG_MASK,
            CPF_MASK,
            CNPJ_MASK,
            CEP_MASK,
            BANK_AGENCY_ACCOUNT_MASK,
            BANK_ACCOUNT_ONLY_MASK,
            DDD_NUMBER_MASK,
            BRAZIL_LICENSE_PLATE,
            BRAZIL_PHONE_NUMBER_MASK,
            BRAZIL_PHONE_NUMBER_9DIGITS_MASK,
            BRAZIL_REGION_PHONE_NUMBER_MASK,
            BRAZIL_REGION_PHONE_NUMBER_9DIGITS_MASK})
    public @interface Mask {

    }
}
