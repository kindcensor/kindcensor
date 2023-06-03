package io.github.kindcensor.ksptest

import io.github.kindcensor.annotation.GenerateToString
import io.github.kindcensor.annotation.ToStringHide
import io.github.kindcensor.annotation.ToStringInitial
import io.github.kindcensor.annotation.ToStringMaskBeginning
import io.github.kindcensor.annotation.ToStringMaskEmail
import io.github.kindcensor.annotation.ToStringMaskEnding
import io.github.kindcensor.ksp.Stringer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
            "%s(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.org,phone=#########00,password=********,data=random)"

        private const val EXPECTED_NO_ANNOTATIONS_TEMPLATE =
            "%s(firstName=Vladimir,middleName=Petrovich,lastName=Ivanov,email=v.p.ivanov@mail.org,phone=88002000600,password=Swordfish,data=random)"
    }

    @Test
    fun `top level data class`() = assertForClass<Data>(EXPECTED_TEMPLATE, "Data")

    @Test
    fun `top level regular class`() = assertForClass<Regular>(EXPECTED_TEMPLATE, "Regular")

    @Test
    fun `top level regular class without annotations on fields`() =
        assertForClass<RegularNoFieldAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE, "RegularNoFieldAnnotations")

    @Test
    fun `top level data class without annotations on fields`() =
        assertForClass<DataNoFieldAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE, "DataNoFieldAnnotations")

    @Test
    fun `top level regular class without any annotations`() =
        assertForClass<RegularNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE, "RegularNoAnnotations")

    @Test
    fun `top level data class without any annotations`() =
        assertForClass<DataNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE, "DataNoAnnotations")

    @Test
    fun `nested data class`() = assertForClass<Nested.Data>(EXPECTED_TEMPLATE, "Nested.Data")

    @Test
    fun `nested regular class`() = assertForClass<Nested.Regular>(EXPECTED_TEMPLATE, "Nested.Regular")

    @Test
    fun `nested regular class without annotations on fields`() =
        assertForClass<Nested.RegularNoFieldAnnotations>(
            EXPECTED_NO_ANNOTATIONS_TEMPLATE,
            "Nested.RegularNoFieldAnnotations"
        )

    @Test
    fun `nested data class without annotations on fields`() =
        assertForClass<Nested.DataNoFieldAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE, "Nested.DataNoFieldAnnotations")

    @Test
    fun `nested regular class without any annotations`() =
        assertForClass<Nested.RegularNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE, "Nested.RegularNoAnnotations")

    @Test
    fun `nested data class without any annotations`() =
        assertForClass<Nested.DataNoAnnotations>(EXPECTED_NO_ANNOTATIONS_TEMPLATE, "Nested.DataNoAnnotations")

    private inline fun <reified T> assertForClass(expectedTemplate: String, classNameInToString: String) {
        val data = T::class.java.constructors[0].newInstance(
            "Vladimir",
            "Petrovich",
            "Ivanov",
            "v.p.ivanov@mail.org",
            88002000600L,
            "Swordfish",
            "random"
        )
        assertThat(data.toString()).isEqualTo(expectedTemplate.format(classNameInToString))
    }
}