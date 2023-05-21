package org.kindcensor.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test

@Suppress("NO_REFLECTION_IN_CLASS_PATH")
class ProcessorProviderTest {

    companion object {
        private const val CLASS_NAME = "TestData"
    }

    @Test
    fun test() {
        val source = SourceFile.kotlin(
            "$CLASS_NAME.kt", """
                import org.kindcensor.annotation.ToStringHide
                import org.kindcensor.annotation.ToStringInitial
                import org.kindcensor.annotation.ToStringMaskBeginning
                import org.kindcensor.annotation.ToStringMaskEmail
                import org.kindcensor.annotation.ToStringMaskEnding      
                import org.kindcensor.ksp.Stringer

                data class $CLASS_NAME(
                    @field:ToStringInitial val firstName: String,
                    @field:ToStringInitial val middleName: String,
                    @field:ToStringMaskEnding val lastName: String,
                    @field:ToStringMaskEmail val email: String,
                    @field:ToStringMaskBeginning(mask = '#') val phone: Long,
                    @field:ToStringHide val password: String,
                    // val data: String TODO
                ) {               
                    override fun toString(): String = Stringer.toString(this)
                }
        """.trimIndent()
        )

        val compilation = KotlinCompilation().apply {
            sources = listOf(source)
            symbolProcessorProviders = listOf(ProcessorProvider())
            kspWithCompilation = true
            inheritClassPath = true
            kspIncremental = true
            messageOutputStream = System.out
            verbose = true
        }

        val result = compilation.compile()
        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        result.compiledClassAndResourceFiles.forEach { println(it) }
        val kClazz = result.classLoader.loadClass(CLASS_NAME)
        result.classLoader.loadClass("org.kindcensor.ksp.generated.Initializer").kotlin.objectInstance
        val data = kClazz.constructors[0].newInstance(
            "Vladimir", "Petrovich", "Ivanov", "v.p.ivanov@mail.ru", 88002000600L, "Swordfish"
        )
        AssertionsForClassTypes.assertThat(data.toString())
            .isEqualTo("TestData(firstName=V.,middleName=P.,lastName=Iv****,email=********ov@m***.ru,phone=#########00,password=********)")
    }


}