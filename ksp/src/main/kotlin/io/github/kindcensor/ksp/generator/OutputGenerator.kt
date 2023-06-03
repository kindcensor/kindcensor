package io.github.kindcensor.ksp.generator

import io.github.kindcensor.ksp.ir.ClassIR

/**
 * The output code generator
 */
internal fun interface OutputGenerator {

    /**
     * Generate information about files bases on bunch of [ClassIR]
     */
    fun generate(classesIR: Iterable<ClassIR>): OutputGeneratorResult?

}