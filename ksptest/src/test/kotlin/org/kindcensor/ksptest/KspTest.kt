package org.kindcensor.ksptest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kindcensor.annotation.GenerateToString
import org.kindcensor.annotation.ToStringHide
import org.kindcensor.annotation.ToStringInitial
import org.kindcensor.annotation.ToStringMaskBeginning
import org.kindcensor.annotation.ToStringMaskEmail
import org.kindcensor.annotation.ToStringMaskEnding
import org.kindcensor.ksp.Stringer

@GenerateToString
data class Data(
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

@GenerateToString
class Regular(
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

@GenerateToString
class RegularNoFieldAnnotations(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val phone: Long,
    val password: String,
    val data: String
) {
    override fun toString(): String = Stringer.toString(this)
}

@GenerateToString
class DataNoFieldAnnotations(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val phone: Long,
    val password: String,
    val data: String
) {
    override fun toString(): String = Stringer.toString(this)
}

@GenerateToString
class RegularNoAnnotations(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val phone: Long,
    val password: String,
    val data: String
) {
    override fun toString(): String = Stringer.toString(this)
}

@GenerateToString
class DataNoAnnotations(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val phone: Long,
    val password: String,
    val data: String
) {
    override fun toString(): String = Stringer.toString(this)
}

class Nested {

    @GenerateToString
    data class Data(
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

    @GenerateToString
    data class Regular(
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

    @GenerateToString
    class RegularNoFieldAnnotations(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val email: String,
        val phone: Long,
        val password: String,
        val data: String
    ) {
        override fun toString(): String = Stringer.toString(this)
    }

    @GenerateToString
    class DataNoFieldAnnotations(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val email: String,
        val phone: Long,
        val password: String,
        val data: String
    ) {
        override fun toString(): String = Stringer.toString(this)
    }

    @GenerateToString
    class RegularNoAnnotations(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val email: String,
        val phone: Long,
        val password: String,
        val data: String
    ) {
        override fun toString(): String = Stringer.toString(this)
    }

    @GenerateToString
    class DataNoAnnotations(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val email: String,
        val phone: Long,
        val password: String,
        val data: String
    ) {
        override fun toString(): String = Stringer.toString(this)
    }

}

class KspTest {

    companion object {
        private const val EXPECTED_TEMPLATE =
            "%s(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.ru,phone=#########00,password=********,data=random)"

        private const val EXPECTED_NO_ANNOTATIONS_TEMPLATE =
            "%s(firstName=Vladimir,middleName=Petrovich,lastName=Ivanov,email=v.p.ivanov@mail.ru,phone=88002000600,password=Swordfish,data=random)"
    }

    @Test
    fun `top level data class`() = assertForClass<Data>(EXPECTED_TEMPLATE)

    @Test
    fun `top level regular class`() = assertForClass<Regular>(EXPECTED_TEMPLATE)

    @Test
    fun `top level regular class without annotations on fields`() =
        assertForClass<RegularNoFieldAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    @Test
    fun `top level data class without annotations on fields`() =
        assertForClass<DataNoFieldAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    @Test
    fun `top level regular class without any annotations`() =
        assertForClass<RegularNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    @Test
    fun `top level data class without any annotations`() =
        assertForClass<DataNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    @Test
    fun `nested data class`() = assertForClass<Nested.Data>(EXPECTED_TEMPLATE)

    @Test
    fun `nested regular class`() = assertForClass<Nested.Regular>(EXPECTED_TEMPLATE)

    @Test
    fun `nested regular class without annotations on fields`() =
        assertForClass<Nested.RegularNoFieldAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    @Test
    fun `nested data class without annotations on fields`() =
        assertForClass<Nested.DataNoFieldAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    @Test
    fun `nested regular class without any annotations`() =
        assertForClass<Nested.RegularNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    @Test
    fun `nested data class without any annotations`() =
        assertForClass<Nested.DataNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE)

    private inline fun <reified T> assertForClass(expectedTemplate: String) {
        val data = T::class.java.constructors[0].newInstance(
            "Vladimir",
            "Petrovich",
            "Ivanov",
            "v.p.ivanov@mail.ru",
            88002000600L,
            "Swordfish",
            "random"
        )
        assertThat(data.toString()).isEqualTo(expectedTemplate.format(T::class.simpleName))
    }
}