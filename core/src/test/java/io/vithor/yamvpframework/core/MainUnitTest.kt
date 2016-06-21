package io.vithor.yamvpframework.core

import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

import org.junit.Assert.*
import com.winterbe.expekt.*

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class MainUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        (2 + 2).should.be.equal(4)
    }

    @Test
    fun helloExpekt() {
        23.should.equal(23)
        "Kotlin".should.not.contain("Scala")
        listOf(1, 2, 3).should.have.size.above(1)
    }

    @Test
    fun thisTestFails() {
        3.4.should.be.closeTo(3.2, delta = 0.1)
    }
}
