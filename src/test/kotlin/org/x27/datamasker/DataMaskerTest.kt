package org.x27.datamasker

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.x27.datamasker.DataMasker.DEFAULT_HIDDEN_STRING
import org.x27.datamasker.DataMasker.DEFAULT_MASK
import org.x27.datamasker.DataMasker.hide
import org.x27.datamasker.DataMasker.initial
import org.x27.datamasker.DataMasker.maskBeginning
import org.x27.datamasker.DataMasker.maskEmail
import org.x27.datamasker.DataMasker.maskEnding

@Suppress("SpellCheckingInspection")
class DataMaskerTest {

    @Test
    fun testMaskBegining() {
        assertThat(maskBeginning(null)).isNull()
        assertThat(maskBeginning("")).isEqualTo("****")
        assertThat(maskBeginning("V")).isEqualTo("****")
        assertThat(maskBeginning("Vl")).isEqualTo("****")
        assertThat(maskBeginning("Vla")).isEqualTo("****")
        assertThat(maskBeginning("Vlad")).isEqualTo("****")
        assertThat(maskBeginning("Vladi")).isEqualTo("****i")
        assertThat(maskBeginning("Vladim")).isEqualTo("****im")
        assertThat(maskBeginning("Vladimi")).isEqualTo("*****mi")
        assertThat(maskBeginning("Vladimir")).isEqualTo("******ir")

        assertThat(maskBeginning(null, 3)).isNull()
        assertThat(maskBeginning("", 3)).isEqualTo("****")
        assertThat(maskBeginning("V", 3)).isEqualTo("****")
        assertThat(maskBeginning("Vl", 3)).isEqualTo("****")
        assertThat(maskBeginning("Vla", 3)).isEqualTo("****")
        assertThat(maskBeginning("Vlad", 3)).isEqualTo("****")
        assertThat(maskBeginning("Vladi", 3)).isEqualTo("****i")
        assertThat(maskBeginning("Vladim", 3)).isEqualTo("****im")
        assertThat(maskBeginning("Vladimi", 3)).isEqualTo("****imi")
        assertThat(maskBeginning("Vladimir", 3)).isEqualTo("*****mir")

        assertThat(maskBeginning(null, 3, 3)).isNull()
        assertThat(maskBeginning("", 3, 3)).isEqualTo("***")
        assertThat(maskBeginning("V", 3, 3)).isEqualTo("***")
        assertThat(maskBeginning("Vl", 3, 3)).isEqualTo("***")
        assertThat(maskBeginning("Vla", 3, 3)).isEqualTo("***")
        assertThat(maskBeginning("Vlad", 3, 3)).isEqualTo("***d")
        assertThat(maskBeginning("Vladi", 3, 3)).isEqualTo("***di")
        assertThat(maskBeginning("Vladim", 3, 3)).isEqualTo("***dim")
        assertThat(maskBeginning("Vladimi", 3, 3)).isEqualTo("****imi")
        assertThat(maskBeginning("Vladimir", 3, 3)).isEqualTo("*****mir")

        assertThat(maskBeginning(null, 3, 3, '#')).isNull()
        assertThat(maskBeginning("", 3, 3, '#')).isEqualTo("###")
        assertThat(maskBeginning("V", 3, 3, '#')).isEqualTo("###")
        assertThat(maskBeginning("Vl", 3, 3, '#')).isEqualTo("###")
        assertThat(maskBeginning("Vla", 3, 3, '#')).isEqualTo("###")
        assertThat(maskBeginning("Vlad", 3, 3, '#')).isEqualTo("###d")
        assertThat(maskBeginning("Vladi", 3, 3, '#')).isEqualTo("###di")
        assertThat(maskBeginning("Vladim", 3, 3, '#')).isEqualTo("###dim")
        assertThat(maskBeginning("Vladimi", 3, 3, '#')).isEqualTo("####imi")
        assertThat(maskBeginning("Vladimir", 3, 3, '#')).isEqualTo("#####mir")

        assertThat(maskBeginning(8800200600L)).isEqualTo("********00")
    }

    @Test
    fun testMaskEnding() {
        assertThat(maskEnding(null)).isNull()
        assertThat(maskEnding("")).isEqualTo("****")
        assertThat(maskEnding("V")).isEqualTo("****")
        assertThat(maskEnding("Vl")).isEqualTo("****")
        assertThat(maskEnding("Vla")).isEqualTo("****")
        assertThat(maskEnding("Vlad")).isEqualTo("****")
        assertThat(maskEnding("Vladi")).isEqualTo("V****")
        assertThat(maskEnding("Vladim")).isEqualTo("Vl****")
        assertThat(maskEnding("Vladimi")).isEqualTo("Vl*****")
        assertThat(maskEnding("Vladimir")).isEqualTo("Vl******")

        assertThat(maskEnding(null, 3)).isNull()
        assertThat(maskEnding("", 3)).isEqualTo("****")
        assertThat(maskEnding("V", 3)).isEqualTo("****")
        assertThat(maskEnding("Vl", 3)).isEqualTo("****")
        assertThat(maskEnding("Vla", 3)).isEqualTo("****")
        assertThat(maskEnding("Vlad", 3)).isEqualTo("****")
        assertThat(maskEnding("Vladi", 3)).isEqualTo("V****")
        assertThat(maskEnding("Vladim", 3)).isEqualTo("Vl****")
        assertThat(maskEnding("Vladimi", 3)).isEqualTo("Vla****")
        assertThat(maskEnding("Vladimir", 3)).isEqualTo("Vla*****")

        assertThat(maskEnding(null, 3, 3)).isNull()
        assertThat(maskEnding("", 3, 3)).isEqualTo("***")
        assertThat(maskEnding("V", 3, 3)).isEqualTo("***")
        assertThat(maskEnding("Vl", 3, 3)).isEqualTo("***")
        assertThat(maskEnding("Vla", 3, 3)).isEqualTo("***")
        assertThat(maskEnding("Vlad", 3, 3)).isEqualTo("V***")
        assertThat(maskEnding("Vladi", 3, 3)).isEqualTo("Vl***")
        assertThat(maskEnding("Vladim", 3, 3)).isEqualTo("Vla***")
        assertThat(maskEnding("Vladimi", 3, 3)).isEqualTo("Vla****")
        assertThat(maskEnding("Vladimir", 3, 3)).isEqualTo("Vla*****")

        assertThat(maskEnding(null, 3, 3, '#')).isNull()
        assertThat(maskEnding("", 3, 3, '#')).isEqualTo("###")
        assertThat(maskEnding("V", 3, 3, '#')).isEqualTo("###")
        assertThat(maskEnding("Vl", 3, 3, '#')).isEqualTo("###")
        assertThat(maskEnding("Vla", 3, 3, '#')).isEqualTo("###")
        assertThat(maskEnding("Vlad", 3, 3, '#')).isEqualTo("V###")
        assertThat(maskEnding("Vladi", 3, 3, '#')).isEqualTo("Vl###")
        assertThat(maskEnding("Vladim", 3, 3, '#')).isEqualTo("Vla###")
        assertThat(maskEnding("Vladimi", 3, 3, '#')).isEqualTo("Vla####")
        assertThat(maskEnding("Vladimir", 3, 3, '#')).isEqualTo("Vla#####")

        assertThat(maskEnding(8800200600L)).isEqualTo("88********")
    }

    @Test
    fun testInitial() {
        assertThat(initial(null)).isNull()
        assertThat(initial("")).isEqualTo("")
        assertThat(initial(" ")).isEqualTo("")
        assertThat(initial("  ")).isEqualTo("")
        assertThat(initial("V")).isEqualTo("V.")
        assertThat(initial("Vladimir")).isEqualTo("V.")
        assertThat(initial("Vladimir", withPeriodEnding = false)).isEqualTo("V")
    }

    @Test
    fun testMaskEmail() {
        assertThat(maskEmail(null)).isNull()
        assertThat(maskEmail("")).isEqualTo("****")
        assertThat(maskEmail("a.borovkov@ftc.ru")).isEqualTo("********ov@f**.ru")
        assertThat(maskEmail("a.borovkov@gmail.com")).isEqualTo("********ov@g****.com")
        assertThat(maskEmail("a.borovkov@ftc.ru", mask = '#')).isEqualTo("########ov@f##.ru")
    }

    @Test
    fun testHide() {
        val defaultLengthSharps =
            buildString(DataMasker.DEFAULT_HIDDEN_LENGTH) { repeat(DataMasker.DEFAULT_HIDDEN_LENGTH) { append('#') } }
        val secretLengthSharps = buildString(DataMasker.DEFAULT_HIDDEN_LENGTH) { repeat(6) { append('#') } }
        val secretLengthAstrics = buildString(DataMasker.DEFAULT_HIDDEN_LENGTH) { repeat(6) { append('*') } }

        assertThat(hide(null, preserveLength = false, preserveNull = true, mask = DEFAULT_MASK)).isNull()
        assertThat(hide(null, preserveLength = true, preserveNull = true, mask = DEFAULT_MASK)).isNull()
        assertThat(hide(null, preserveLength = true, preserveNull = true, mask = '#')).isNull()

        assertThat(hide(null, preserveLength = false, preserveNull = false, mask = DEFAULT_MASK))
            .isEqualTo(DEFAULT_HIDDEN_STRING)
        assertThat(hide(null, preserveLength = true, preserveNull = false, mask = DEFAULT_MASK))
            .isEqualTo(DEFAULT_HIDDEN_STRING)
        assertThat(hide(null, preserveLength = true, preserveNull = false, mask = '#')).isEqualTo(defaultLengthSharps)

        assertThat(hide("secret", preserveLength = false, preserveNull = false, mask = DEFAULT_MASK))
            .isEqualTo(DEFAULT_HIDDEN_STRING)
        assertThat(hide("secret", preserveLength = true, preserveNull = false, mask = DEFAULT_MASK))
            .isEqualTo(secretLengthAstrics)
        assertThat(hide("secret", preserveLength = true, preserveNull = false, mask = '#'))
            .isEqualTo(secretLengthSharps)
    }

}