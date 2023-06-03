package io.github.kindcensor.ksp.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes.*
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.net.URLClassLoader

class ProcessorTest {

    companion object {

        private const val PARENT_CLASS_NAME = "Parent"

        private const val CLASS_NAME = "TestData"

        private const val PACKAGE_NAME = "org.test"

        private val HEADER = """
                package $PACKAGE_NAME    
                import io.github.kindcensor.annotation.GenerateToString
                import io.github.kindcensor.annotation.ToStringHide
                import io.github.kindcensor.annotation.ToStringInitial
                import io.github.kindcensor.annotation.ToStringMaskBeginning
                import io.github.kindcensor.annotation.ToStringMaskEmail
                import io.github.kindcensor.annotation.ToStringMaskEnding      
                import io.github.kindcensor.ksp.TestStringer
        """.trimIndent()

        private val CLASS_CONTENT = """
            (
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

        private const val EXPECTED_TEMPLATE =
            "%s(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.org,phone=#########00,password=********,data=random)"
    }

    @Test
    fun `test generation for data class`() {
        val result = compile(
            """
                $HEADER
                @GenerateToString
                data class $CLASS_NAME$CLASS_CONTENT
            """.trimIndent()
        )

        val data = getObject("$PACKAGE_NAME.$CLASS_NAME", result)
        assertThat(data.toString()).isEqualTo(EXPECTED_TEMPLATE.format(CLASS_NAME))
    }

    @Test
    fun `test generation for child data class`() {
        val result = compile(
            """
                $HEADER
                class $PARENT_CLASS_NAME {
                    @GenerateToString
                    data class $CLASS_NAME$CLASS_CONTENT
                }
            """.trimIndent()
        )

        val data = getObject("$PACKAGE_NAME.$PARENT_CLASS_NAME\$$CLASS_NAME", result)
        assertThat(data.toString()).isEqualTo(EXPECTED_TEMPLATE.format("$PARENT_CLASS_NAME.$CLASS_NAME"))
    }

    @Test
    fun `test generation for regular class`() {
        val result = compile(
            """
                $HEADER
                @GenerateToString
                class $CLASS_NAME$CLASS_CONTENT
            """.trimIndent()
        )

        val data = getObject("$PACKAGE_NAME.$CLASS_NAME", result)
        assertThat(data.toString()).isEqualTo(EXPECTED_TEMPLATE.format(CLASS_NAME))
    }

    private fun getObject(className: String, classLoader: ClassLoader): Any {
        val kClazz = classLoader.loadClass(className)
        return kClazz.constructors[0].newInstance(
            "Vladimir",
            "Petrovich",
            "Ivanov",
            "v.p.ivanov@mail.org",
            88002000600L,
            "Swordfish",
            "random"
        )
    }

    private fun compile(@Language("kotlin") classContent: String): URLClassLoader {
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

        val result = compilation.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        return result.classLoader
    }

}