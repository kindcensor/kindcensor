package org.kindcensor

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.kindcensor.core.DataMasker

class ToStringTest {

    @Test
    fun testToStringSimple() {
        val data = TestDataSimple(
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
                "TestData(firstName='V.', middleName='P.', lastName='Iv****', email='********ov@m***.ru', phone='*********00', password='********', data='random')"
            )
    }

    @Test
    fun testToStringToStringBuilder() {
        val data = TestDataToStringBuilder(
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
                "ToStringTest.TestDataToStringBuilder[firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.ru,phone=*********00,password=********,data=random]"
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

}