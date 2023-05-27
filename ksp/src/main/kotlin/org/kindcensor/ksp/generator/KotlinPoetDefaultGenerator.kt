package org.kindcensor.ksp.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import org.kindcensor.annotation.bind.AnnotationRegistry
import org.kindcensor.ksp.Binding
import org.kindcensor.ksp.ir.AnnotationIR
import org.kindcensor.ksp.ir.ClassIR

object KotlinPoetDefaultGenerator : Generator {

    private val listOfBindingsTypeName = List::class.asClassName().parameterizedBy(Binding::class.asClassName())

    override fun generate(classesIR: Sequence<ClassIR>): GeneratorResult {
        val classesToFunctions = classesIR.associateWith { generateToString(it) }
        return FileSpec.builder("org.kindcensor.ksp.generated", "ToStringFunctions")
            .also { builder -> classesToFunctions.forEach { (_, f) -> builder.addFunction(f) } }
            .addType(generateInitializer(classesToFunctions))
            .build()
            .let { GeneratorResult(it.name, it.packageName, it.toString()) }
    }

    private fun generateInitializer(classesToFunctions: Map<ClassIR, FunSpec>): TypeSpec {
        val getBindings = FunSpec.builder("getBindings")
            .returns(listOfBindingsTypeName)
            .addCode("return listOf(")
            .also { builder ->
                classesToFunctions.forEach { (clazz, function) ->
                    builder.addCode(
                        "\n%T(%L::class, ::%L as (Any) -> String),",
                        Binding::class,
                        clazz.qualifiedName,
                        function.name
                    )
                }
            }
            .addCode("\n);")
            .build()

        return TypeSpec.classBuilder("Initializer")
            .addFunction(getBindings)
            .build()
    }

    private fun generateToString(clazz: ClassIR): FunSpec {
        val parameterClassName = ClassName(clazz.qualifier, clazz.simpleName)
        return FunSpec.builder(makeName(clazz.qualifiedName))
            .returns(String::class)
            .addParameter("subject", parameterClassName)
            .beginControlFlow("return buildString")
            .addStatement("append(%S)", clazz.simpleName + '(')
            .also { builder ->
                clazz.properties.forEachIndexed { index, (name, annotation) ->
                    appendProperty(builder, name, annotation)
                    if (index != clazz.properties.lastIndex) {
                        builder.addStatement("append(',')")
                    }
                }
            }
            .addStatement("append(')')")
            .endControlFlow()
            .build()
    }

    private fun appendProperty(
        builder: FunSpec.Builder,
        name: String,
        annotation: AnnotationIR?
    ) {
        builder.addStatement("""append("$name=")""")
        appendValue(name, builder, annotation)
    }

    private fun appendValue(name: String, builder: FunSpec.Builder, annotation: AnnotationIR?) {
        if (annotation != null) {
            appendValueWithAnnotation(name, builder, annotation)
        } else {
            appendPlainValue(name, builder)
        }
    }

    private fun appendPlainValue(name: String, builder: FunSpec.Builder) = builder.addStatement("append(subject.$name)")

    private fun appendValueWithAnnotation(name: String, builder: FunSpec.Builder, annotation: AnnotationIR) {
        builder.addCode("append(%T.apply(", AnnotationRegistry::class)
        builder.addCode("%T(", AnnotationRegistry.bySimpleNames[annotation.simpleName])
        annotation.arguments.forEachIndexed { index, argument ->
            builder.addCode("%N=%L", argument.name, formatArgumentValue(argument.value))
            if (index != annotation.arguments.lastIndex) {
                builder.addCode(", ")
            }
        }
        builder.addCode(")")
        builder.addCode(", subject.$name))\n")
    }

    private fun formatArgumentValue(value: Any?) = when (value) {
        is Char -> "'$value'"
        is String -> """"$value""""
        else -> value
    }

    private fun makeName(qualifiedName: String): String = "toString_${qualifiedName.replace('.', '_')}"
}
