package org.kindcensor.ksptest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kindcensor.annotation.ToStringHide
import org.kindcensor.annotation.ToStringInitial
import org.kindcensor.annotation.ToStringMaskBeginning
import org.kindcensor.annotation.ToStringMaskEmail
import org.kindcensor.annotation.ToStringMaskEnding
import org.kindcensor.ksp.Stringer

data class TestData(
    @field:ToStringInitial val firstName: String,
    @field:ToStringInitial val middleName: String,
    @field:ToStringMaskEnding val lastName: String,
    @field:ToStringMaskEmail val email: String,
    @field:ToStringMaskBeginning(mask = '#') val phone: Long,
    @field:ToStringHide val password: String,
    val data: String
) {
    override fun toString(): String = Stringer.toString(this)
}

class KspTest {


    @Test
    fun `test toString by KSP`() {
        val data = TestData(
            firstName = "Vladimir",
            middleName = "Petrovich",
            lastName = "Ivanov",
            email = "v.p.ivanov@mail.ru",
            phone = 88002000600L,
            password = "Swordfish",
            data = "random"
        )
        assertThat(data.toString())
            .isEqualTo("TestData(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.ru,phone=#########00,password=********,data=random)")
    }
}