package org.kindcensor.reflection;

import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.kindcensor.annotation.ToStringHide;
import org.kindcensor.annotation.ToStringInitial;
import org.kindcensor.annotation.ToStringMaskBeginning;
import org.kindcensor.annotation.ToStringMaskEmail;
import org.kindcensor.annotation.ToStringMaskEnding;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MaskedReflectionToStringBuilderJavaApiTest {

    @Test
    void testToStringMaskedReflectionToStringBuilder() {
        TestDataMaskedReflectionToStringBuilder data = new TestDataMaskedReflectionToStringBuilder(
                "Vladimir",
                "Petrovich",
                "Ivanov",
                "v.p.ivanov@mail.ru",
                88002000600L,
                "Swordfish",
                "random"
        );
        assertThat(data.toString())
                .isEqualTo("MaskedReflectionToStringBuilderJavaApiTest.TestDataMaskedReflectionToStringBuilder[data=random,email=********ov@m***.ru,firstName=V.,lastName=Iv****,middleName=P.,password=********,phone=#########00]");
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
