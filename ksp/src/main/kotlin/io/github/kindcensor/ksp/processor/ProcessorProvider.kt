package io.github.kindcensor.ksp.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import io.github.kindcensor.ksp.Options
import io.github.kindcensor.ksp.generator.KotlinPoetOutputGenerator

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        Processor(environment, Options.fromEnvironmentOptions(environment.options), KotlinPoetOutputGenerator)
}