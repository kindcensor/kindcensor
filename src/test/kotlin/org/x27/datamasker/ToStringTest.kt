package org.x27.datamasker

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.x27.datamasker.annotation.*

class ToStringTest {

    @Test
    fun testToStringSimple() {
        val data = TestDataSimple(
            "Vladimir",
            "Vladimirovich",
            "Medvedev",
            "v.v.medvev@mail.ru",
            88002000600,
            "Swordfish",
            "random"
        )
        Assertions.assertThat(data.toString())
            .isEqualTo(
                "TestData(firstName='V.', middleName='V.', lastName='Me******', email='********ev@m***.ru', phone='*********00', password='********', data='random')"
            )
    }

    @Test
    fun testToStringToStringBuilder() {
        val data = TestDataToStringBuilder(
            "Vladimir",
            "Vladimirovich",
            "Medvedev",
            "v.v.medvev@mail.ru",
            88002000600,
            "Swordfish",
            "random"
        )
        Assertions.assertThat(data.toString())
            .isEqualTo(
                "ToStringTest.TestDataToStringBuilder[firstName=V.,middleName=V.,lastName=Me******,email=********ev@m***.ru,phone=*********00,password=********,data=random]"
            )
    }


    @Test
    fun testToStringReflectionToStringBuilder() {
        val data = TestDataMaskedReflectionToStringBuilder(
            "Vladimir",
            "Vladimirovich",
            "Medvedev",
            "v.v.medvev@mail.ru",
            88002000600,
            "Swordfish",
            "random"
        )
        Assertions.assertThat(data.toString())
            .isEqualTo(
                "ToStringTest.TestDataMaskedReflectionToStringBuilder[data=random,email=********ev@m***.ru,firstName=V.,lastName=Me******,middleName=V.,password=********,phone=*********00]"
            )
    }

    private data class TestDataSimple(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val email: String,
        val phone: Long,
        val password: String,
        val data: String
    ) {

        override fun toString(): String {
            return "TestData(" +
                    "firstName='${DataMasker.initial(firstName)}', " +
                    "middleName='${DataMasker.initial(middleName)}', " +
                    "lastName='${DataMasker.maskEnding(lastName)}', " +
                    "email='${DataMasker.maskEmail(email)}', " +
                    "phone='${DataMasker.maskBeginning(phone)}', " +
                    "password='${DataMasker.hide(password)}', " +
                    "data='$data'" +
                    ")"
        }
    }

    private data class TestDataToStringBuilder(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val email: String,
        val phone: Long,
        val password: String,
        val data: String
    ) {

        override fun toString(): String {
            return ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("firstName", DataMasker.initial(firstName))
                .append("middleName", DataMasker.initial(middleName))
                .append("lastName", DataMasker.maskEnding(lastName))
                .append("email", DataMasker.maskEmail(email))
                .append("phone", DataMasker.maskBeginning(phone))
                .append("password", DataMasker.hide(password))
                .append("data", data)
                .toString()
        }
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