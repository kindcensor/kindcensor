package org.kindcensor.ksp.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class ProcessorTest {

    companion object {
        private const val CLASS_NAME = "TestData"

        private const val PACKAGE_NAME = "org.test"

        private val HEADER = """
                package $PACKAGE_NAME      
                import org.kindcensor.annotation.ToStringHide
                import org.kindcensor.annotation.ToStringInitial
                import org.kindcensor.annotation.ToStringMaskBeginning
                import org.kindcensor.annotation.ToStringMaskEmail
                import org.kindcensor.annotation.ToStringMaskEnding      
                import org.kindcensor.ksp.TestStringer
        """.trimIndent()
    }

    @Test
    fun `test generation for data class`() {
        val result = compile(
            """
                $HEADER
                data class $CLASS_NAME(
                    @field:ToStringInitial val firstName: String,
                    @field:ToStringInitial val middleName: String,
                    @field:ToStringMaskEnding val lastName: String,
                    @field:ToStringMaskEmail val email: String,
                    @field:ToStringMaskBeginning(mask = '#') val phone: Long,
                    @field:ToStringHide val password: String,
                    val data: String
                    ) {               
                    override fun toString(): String = TestStringer.toString(this)
                }
            """.trimIndent()
        )

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        val data = getObject(
            result,
            "Vladimir",
            "Petrovich",
            "Ivanov",
            "v.p.ivanov@mail.ru",
            88002000600L,
            "Swordfish",
            "random"
        )
        AssertionsForClassTypes.assertThat(data.toString())
            .isEqualTo("TestData(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.ru,phone=#########00,password=********,data=random)")
    }

    @Test
    fun `test generation for regular class`() {
        val result = compile(
            """
                $HEADER
                class $CLASS_NAME(
                    @field:ToStringInitial val firstName: String,
                    @field:ToStringInitial val middleName: String,
                    @field:ToStringMaskEnding val lastName: String,
                    @field:ToStringMaskEmail val email: String,
                    @field:ToStringMaskBeginning(mask = '#') val phone: Long,
                    @field:ToStringHide val password: String,
                    val data: String
                    ) {               
                    override fun toString(): String = TestStringer.toString(this)
                }
            """.trimIndent()
        )

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        val data = getObject(
            result,
            "Vladimir",
            "Petrovich",
            "Ivanov",
            "v.p.ivanov@mail.ru",
            88002000600L,
            "Swordfish",
            "random"
        )
        AssertionsForClassTypes.assertThat(data.toString())
            .isEqualTo("TestData(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.ru,phone=#########00,password=********,data=random)")
    }

    private fun getObject(result: KotlinCompilation.Result, vararg constructorArguments: Any?): Any {
        val kClazz = result.classLoader.loadClass("$PACKAGE_NAME.$CLASS_NAME")
        return kClazz.constructors[0].newInstance(*constructorArguments)
    }

    private fun compile(@Language("kotlin") classContent: String): KotlinCompilation.Result {
        val source = SourceFile.kotlin("$CLASS_NAME.kt", classContent)

        val compilation = KotlinCompilation().apply {
            sources = listOf(source)
            symbolProcessorProviders = listOf(ProcessorProvider())
            kspWithCompilation = true
            inheritClassPath = true
            kspIncremental = true
            messageOutputStream = System.out
            verbose = true
            kspArgs = mutableMapOf("logGeneratedCode" to "true")
        }

        return compilation.compile()
    }

}