package org.kindcensor.ksptest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kindcensor.annotation.ToStringHide
import org.kindcensor.annotation.ToStringInitial
import org.kindcensor.annotation.ToStringMaskBeginning
import org.kindcensor.annotation.ToStringMaskEmail
import org.kindcensor.annotation.ToStringMaskEnding
import org.kindcensor.ksp.Stringer

data class TestDataTopLevel(
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

class TestRegularTopLevel(
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

class Outer {
    data class TestDataNested(
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

    data class TestRegularNested(
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
}

class KspTest {

    companion object {
        private const val EXPECTED_TEMPLATE = "%s(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.ru,phone=#########00,password=********,data=random)"
    }

    @Test
    fun `test toString by KSP for top level data class`() {
        val data = TestDataTopLevel(
            firstName = "Vladimir",
            middleName = "Petrovich",
            lastName = "Ivanov",
            email = "v.p.ivanov@mail.ru",
            phone = 88002000600L,
            password = "Swordfish",
            data = "random"
        )
        assertThat(data.toString()).isEqualTo(EXPECTED_TEMPLATE.format(TestDataTopLevel::class.simpleName))
    }

    @Test
    fun `test toString by KSP for top level regular class`() {
        val data = TestRegularTopLevel(
            firstName = "Vladimir",
            middleName = "Petrovich",
            lastName = "Ivanov",
            email = "v.p.ivanov@mail.ru",
            phone = 88002000600L,
            password = "Swordfish",
            data = "random"
        )
        assertThat(data.toString()).isEqualTo(EXPECTED_TEMPLATE.format(TestRegularTopLevel::class.simpleName))
    }

    @Test
    fun `test toString by KSP for nested data class`() {
        val data = Outer.TestDataNested(
            firstName = "Vladimir",
            middleName = "Petrovich",
            lastName = "Ivanov",
            email = "v.p.ivanov@mail.ru",
            phone = 88002000600L,
            password = "Swordfish",
            data = "random"
        )
        assertThat(data.toString()).isEqualTo(EXPECTED_TEMPLATE.format(Outer.TestDataNested::class.simpleName))
    }

    @Test
    fun `test toString by KSP for nested regular class`() {
        val data = Outer.TestRegularNested(
            firstName = "Vladimir",
            middleName = "Petrovich",
            lastName = "Ivanov",
            email = "v.p.ivanov@mail.ru",
            phone = 88002000600L,
            password = "Swordfish",
            data = "random"
        )
        assertThat(data.toString()).isEqualTo(EXPECTED_TEMPLATE.format(Outer.TestRegularNested::class.simpleName))
    }
}