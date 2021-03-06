package io.github.binaryfoo

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.File
import java.nio.file.Files

public class RegressionTest {

    @Test
    public fun contactMastercard(): Unit = execute("contact-mastercard")

    @Test
    public fun contactVisa(): Unit = execute("contact-visa")

    @Test
    public fun contactlessMastercard(): Unit = execute("contactless-mastercard")

    @Test
    public fun contactlessMsd(): Unit = execute("contactless-msd", "MSD")

    @Test
    public fun contactAmexCda(): Unit = execute("amex-cda", "Amex")

    @Test
    public fun contactUpi(): Unit = execute("contact-upi", "UPI")

    private fun execute(name: String, meta: String = "EMV") {
        val (input, expectedOutput) = loadCase(name)
        assertThat(decode(input, meta), DecodedAsStringMatcher(expectedOutput))
    }

    private fun loadCase(name: String): Pair<String, String> {
        val input = String(Files.readAllBytes(File("src/test/resources/cases/$name-in.txt").toPath()))
        val expectedOutput = String(Files.readAllBytes(File("src/test/resources/cases/$name-out.txt").toPath()))
        return Pair(input, expectedOutput)
    }

    fun decode(input: String, meta: String): List<DecodedData> {
        return RootDecoder().decode(input, meta, "apdu-sequence")
    }
}

