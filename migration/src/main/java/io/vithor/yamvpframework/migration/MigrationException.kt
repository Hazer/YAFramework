package io.vithor.yamvpframework.migration

/**
 * Created by Vithorio Polten on 7/2/16.
 */
class MigrationException(message: String, val migration: Migration? = null) : IllegalStateException(message) {

}