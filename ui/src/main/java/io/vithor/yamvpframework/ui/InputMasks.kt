package io.vithor.yamvpframework.ui

import android.support.annotation.StringDef

object InputMasks {
    const val RG_MASK = "##.###.###-#############"
    const val CPF_MASK = "###.###.###-##"
    const val CNPJ_MASK = "##.###.###/####-##"
    const val CEP_MASK = "##.###-###"

    const val BANK_AGENCY_ACCOUNT_MASK = "####.#####-#"
    const val BANK_ACCOUNT_ONLY_MASK = "#####-#"

    const val DDD_NUMBER_MASK = "(##)"
    const val BRAZIL_PHONE_NUMBER_MASK = "####-####"
    const val BRAZIL_PHONE_NUMBER_9DIGITS_MASK = "#####-####"
    const val BRAZIL_REGION_PHONE_NUMBER_MASK = "(##) ####-####"
    const val BRAZIL_REGION_PHONE_NUMBER_9DIGITS_MASK = "(##) #####-####"
    const val BRAZIL_LICENSE_PLATE = "###-####"

    @StringDef(RG_MASK, CPF_MASK, CNPJ_MASK, CEP_MASK, BANK_AGENCY_ACCOUNT_MASK, BANK_ACCOUNT_ONLY_MASK, DDD_NUMBER_MASK, BRAZIL_LICENSE_PLATE, BRAZIL_PHONE_NUMBER_MASK, BRAZIL_PHONE_NUMBER_9DIGITS_MASK, BRAZIL_REGION_PHONE_NUMBER_MASK, BRAZIL_REGION_PHONE_NUMBER_9DIGITS_MASK)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Mask
}
