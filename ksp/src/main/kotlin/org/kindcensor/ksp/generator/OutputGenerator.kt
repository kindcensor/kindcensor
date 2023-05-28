package org.kindcensor.ksp.generator

import org.kindcensor.ksp.ir.ClassIR

/**
 * The output code generator
 */
internal fun interface OutputGenerator {

    /**
     * Generate information about files bases on bunch of [ClassIR]
     */
    fun generate(classesIR: Sequence<ClassIR>): OutputGeneratorResult?

}