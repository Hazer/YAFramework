package io.vithor.yamvpframework.migration

/**
 * Created by Vithorio Polten on 2/23/16.
 */
class SampleVersionManager private constructor() : AbstractVersionManager() {

    init {
        addMigrations(
                V1_TestMigration())
    }

    companion object {
        /**
         * DO NOT SUBCLASS THIS, SAMPLE ONLY CLASS
         */
        val instance = SampleVersionManager()
    }
}