package io.github.kindcensor.reflection

import io.github.kindcensor.annotation.ToStringHide
import io.github.kindcensor.annotation.ToStringInitial
import io.github.kindcensor.annotation.ToStringMaskBeginning
import io.github.kindcensor.annotation.ToStringMaskEmail
import io.github.kindcensor.annotation.ToStringMaskEnding
import org.apache.commons.lang3.builder.ToStringStyle
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MaskedReflectionToStringBuilderTest {

    @Test
    fun testToStringReflectionToStringBuilder() {
        val data = TestDataMaskedReflectionToStringBuilder(
            firstName = "Vladimir",
            middleName = "Petrovich",
            lastName = "Ivanov",
            email = "v.p.ivanov@mail.org",
            phone = 88002000600,
            password = "Swordfish",
            data = "random"
        )
        Assertions.assertThat(data.toString())
            .isEqualTo(
                "MaskedReflectionToStringBuilderTest.TestDataMaskedReflectionToStringBuilder[data=random,email=********ov@m***.org,firstName=V.,lastName=Iv****,middleName=P.,password=********,phone=*********00]"
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