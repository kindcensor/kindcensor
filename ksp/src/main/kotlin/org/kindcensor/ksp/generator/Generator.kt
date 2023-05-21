package org.kindcensor.ksp.generator

import org.kindcensor.ksp.ir.ClassIR

fun interface Generator {

    fun generate(classesIR: List<ClassIR>): GeneratorResult?

}