package org.kindcensor.reflection

import org.apache.commons.lang3.builder.ToStringStyle
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.kindcensor.annotation.ToStringHide
import org.kindcensor.annotation.ToStringInitial
import org.kindcensor.annotation.ToStringMaskBeginning
import org.kindcensor.annotation.ToStringMaskEmail
import org.kindcensor.annotation.ToStringMaskEnding

class MaskedReflectionToStringBuilderTest {

    @Test
    fun testToStringReflectionToStringBuilder() {
        val data = TestDataMaskedReflectionToStringBuilder(
            firstName = "Vladimir",
            middleName = "Petrovich",
            lastName = "Ivanov",
            email = "v.p.ivanov@mail.ru",
            phone = 88002000600,
            password = "Swordfish",
            data = "random"
        )
        Assertions.assertThat(data.toString())
            .isEqualTo(
                "MaskedReflectionToStringBuilderTest.TestDataMaskedReflectionToStringBuilder[data=random,email=********ov@m***.ru,firstName=V.,lastName=Iv****,middleName=P.,password=********,phone=*********00]"
            )
    }

    private data class TestDataMaskedReflectionToStringBuilder(
        @field:ToStringInitial val firstName: String,
        @field:ToStringInitial val middleName: String,
        @field:ToStringMaskEnding val lastName: String,
        @field:ToStringMaskEmail val email: String,
        @field:ToStringMaskBeginning val phone: Long,
        @field:ToStringHide val password: String,
        val data: String
    ) {

        override fun toString(): String =
            MaskedReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString()
    }
}