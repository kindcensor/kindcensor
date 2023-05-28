package org.kindcensor.ksp.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import org.kindcensor.ksp.Options
import org.kindcensor.ksp.generator.KotlinPoetOutputGenerator

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        Processor(environment, Options.fromEnvironmentOptions(environment.options), KotlinPoetOutputGenerator)
}