package org.x27.datamasker;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.x27.datamasker.annotation.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ToStringJavaApiTest {

    @Test
    void testToStringSimple() {
        TestDataSimple data = new TestDataSimple(
                "Vladimir",
                "Vladimirovich",
                "Medvedev",
                "v.v.medvev@mail.ru",
                88002000600L,
                "Swordfish",
                "random"
        );
        assertThat(data.toString())
                .isEqualTo("TestDataSimple{firstName='V.', middleName='V.', lastName='Me******', email='********ev@m***.ru', phone=*********00, password=********, data='random'}");
    }

    @Test
    void testToStringToStringBuilder() {
        TestDataToStringBuilder data = new TestDataToStringBuilder(
                "Vladimir",
                "Vladimirovich",
                "Medvedev",
                "v.v.medvev@mail.ru",
                88002000600L,
                "Swordfish",
                "random"
        );
        assertThat(data.toString())
                .isEqualTo("ToStringJavaApiTest.TestDataToStringBuilder[firstName=V.,middleName=V.,lastName=Me******,email=********ev@m***.ru,phone=*********00,password=********,data=random]");
    }

    @Test
    void testToStringMaskedReflectionToStringBuilder() {
        TestDataMaskedReflectionToStringBuilder data = new TestDataMaskedReflectionToStringBuilder(
                "Vladimir",
                "Vladimirovich",
                "Medvedev",
                "v.v.medvev@mail.ru",
                88002000600L,
                "Swordfish",
                "random"
        );
        assertThat(data.toString())
                .isEqualTo("ToStringJavaApiTest.TestDataMaskedReflectionToStringBuilder[data=random,email=********ev@m***.ru,firstName=V.,lastName=Me******,middleName=V.,password=********,phone=#########00]");
    }

    private static class TestDataSimple {

        protected final String firstName;

        protected final String middleName;

        protected final String lastName;

        protected final String email;

        protected final long phone;

        protected final String password;

        protected final String data;

        public TestDataSimple(
                String firstName,
                String middleName,
                String lastName,
                String email,
                long phone,
                String password,
                String data
        ) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.data = data;
        }

        @Override
        public String toString() {
            return "TestDataSimple{" +
                    "firstName='" + DataMasker.initial(firstName) + '\'' +
                    ", middleName='" + DataMasker.initial(middleName) + '\'' +
                    ", lastName='" + DataMasker.maskEnding(lastName) + '\'' +
                    ", email='" + DataMasker.maskEmail(email) + '\'' +
                    ", phone=" + DataMasker.maskBeginning(phone) +
                    ", password=" + DataMasker.hide(password) +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

    private static class TestDataToStringBuilder extends TestDataSimple {

        public TestDataToStringBuilder(
                String firstName,
                String middleName,
                String lastName,
                String email,
                long phone,
                String password,
                String data
        ) {
            super(firstName, middleName, lastName, email, phone, password, data);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("firstName", DataMasker.initial(firstName))
                    .append("middleName", DataMasker.initial(middleName))
                    .append("lastName", DataMasker.maskEnding(lastName))
                    .append("email", DataMasker.maskEmail(email))
                    .append("phone", DataMasker.maskBeginning(phone))
                    .append("password", DataMasker.hide(password))
                    .append("data", data)
                    .toString();
        }
    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private static class TestDataMaskedReflectionToStringBuilder {

        @ToStringInitial
        private final String firstName;

        @ToStringInitial
        private final String middleName;

        @ToStringMaskEnding
        private final String lastName;

        @ToStringMaskEmail
        private final String email;

        @ToStringMaskBeginning(mask = '#')
        private final long phone;

        @ToStringHide
        private final String password;

        protected final String data;

        public TestDataMaskedReflectionToStringBuilder(
                String firstName,
                String middleName,
                String lastName,
                String email,
                long phone,
                String password,
                String data
        ) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.data = data;
        }

        @Override
        public String toString() {
            return new MaskedReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
        }
    }
}
