package org.kindcensor.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import org.kindcensor.annotation.bind.AnnotationRegistry

//TODO own representation of a code
internal fun generateCensoredToStringFunctionsFile(byClasses: Map<KSDeclaration, List<Pair<KSPropertyDeclaration, KSAnnotation>>>): FileSpec {
    val classesToFunctions = byClasses.map { (clazz, properties) ->
        clazz to generateToString(clazz as KSClassDeclaration, properties)
    }
    return FileSpec.builder("org.kindcensor.ksp.generated", "ToStringFunctions")
        .also { builder -> classesToFunctions.forEach { (_, f) -> builder.addFunction(f) } }
        .addType(
            TypeSpec.objectBuilder("Initializer")
                .addInitializerBlock(
                    CodeBlock.builder()
                        .also { builder ->
                            classesToFunctions.forEach { (clazz, function) ->
                                val className = clazz.simpleName.getShortName()
                                builder.addStatement(
                                    "%T.register(%L::class, ::%N)",
                                    Stringer::class,
                                    className,
                                    function.name
                                )
                            }
                        }
                        .build()
                )
                .build()
        )
        .build()
}

private fun generateToString(
    clazz: KSClassDeclaration,
    properties: List<Pair<KSPropertyDeclaration, KSAnnotation>>
): FunSpec {
    val qualifiedName = clazz.qualifiedName ?: error("Failed to get qualifiedName for $clazz")
    val parameterClassName = ClassName(qualifiedName.getQualifier(), qualifiedName.getShortName())
    return FunSpec.builder(makeName(qualifiedName))
        .returns(String::class)
        .addParameter("subject", parameterClassName)
        .beginControlFlow("return buildString")
        .addStatement("append(%S)", qualifiedName.getShortName() + '(')
        .also { builder ->
            properties.forEachIndexed { index, (property, annotation) ->
                appendProperty(builder, property, annotation)
                if (index != properties.lastIndex) {
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
    property: KSPropertyDeclaration,
    annotation: KSAnnotation
) {
    val propertyName = property.simpleName.getShortName()
    builder.addStatement("""append("${property.simpleName.getShortName()}=")""")
        .addCode("append(%T.apply(", AnnotationRegistry::class)
        .also { appendAnnotationValue(it, annotation) }
        .addCode(", subject.$propertyName))\n")
}

fun appendAnnotationValue(builder: FunSpec.Builder, annotation: KSAnnotation) {
    val name = annotation.shortName.getShortName()
    builder.addCode("%T(", AnnotationRegistry.bySimpleNames[name])
    annotation.arguments.forEachIndexed { index, argument ->
        builder.addCode("%N=%L", argument.name?.getShortName(), formatArgumentValue(argument.value))
        if (index != annotation.arguments.lastIndex) {
            builder.addCode(", ")
        }
    }
    builder.addCode(")")
}

private fun formatArgumentValue(value: Any?) = when (value) {
    is Char -> "'$value'"
    is String -> """"$value""""
    else -> value
}


private fun makeName(qualifiedName: KSName): String = "toString_${qualifiedName.asString().replace(',', '_')}"