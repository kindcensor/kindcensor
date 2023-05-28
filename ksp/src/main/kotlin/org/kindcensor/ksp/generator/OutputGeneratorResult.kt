package org.kindcensor.ksp.generator

/**
 * The result of generation
 * @property fileName The name of file to write
 * @property packageName The name of the package used in file
 * @property content The content of the file
 */
internal data class OutputGeneratorResult(val fileName: String, val packageName: String, val content: String)
