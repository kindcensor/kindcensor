package org.x27.datamasker

import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.x27.datamasker.annotation.*
import java.lang.reflect.Field

/**
 * Implements string builder, based on [ReflectionToStringBuilder].
 * Taking into account annotations like [ToStringInitial], [ToStringMaskBeginning], [ToStringMaskEnding],
 * [ToStringMaskEmail], [ToStringHide], etc.
 *
 * Note: as reflection based code it will be ~10-20 slower then regular hand-written [toString] method, and 1.5-2 times
 * slower then [org.apache.commons.lang3.builder.ToStringBuilder].
 *
 *      Benchmark                                           Mode  Cnt     Score    Error  Units
 *      ToStringBenchmark.apacheReflectionToString          avgt    5  1484.542 ± 28.615  ns/op
 *      ToStringBenchmark.apacheToString                    avgt    5   922.272 ± 52.431  ns/op
 *      ToStringBenchmark.manualObjectsToString             avgt    5   140.707 ±  2.457  ns/op
 *      ToStringBenchmark.manualToString                    avgt    5   118.061 ±  2.196  ns/op
 *
 * Source https://github.com/consoleau/kassava
 *
 * See also https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/builder/ReflectionToStringBuilder.html
 *
 * @constructor
 * @param obj The Object to be output
 * @param style The style of the [toString] to create, may be `null`
 * @param buffer The [StringBuffer] to populate
 * @param reflectUpToClass  The superclass to reflect up to (inclusive), may be `null`
 * @param outputTransients Whether to include transient fields
 * @param outputStatics Whether to include static fields
 * @param excludeNullValues Whether to exclude fields whose values are `null`
 */
class MaskedReflectionToStringBuilder @JvmOverloads constructor(
    obj: Any,
    style: ToStringStyle? = null,
    buffer: StringBuffer? = StringBuffer(512),
    reflectUpToClass: Class<in Any>? = null,
    outputTransients: Boolean = false,
    outputStatics: Boolean = false,
    excludeNullValues: Boolean = false
) : ReflectionToStringBuilder(
    obj,
    style,
    buffer,
    reflectUpToClass,
    outputTransients,
    outputStatics,
    excludeNullValues
) {

    override fun getValue(field: Field): Any? {
        val value = super.getValue(field)

        for (ann in field.annotations) {
            when (ann) {
                is ToStringMaskBeginning -> return DataMasker.maskBeginning(value, ann.maxOpen, ann.minHidden, ann.mask)
                is ToStringMaskEnding ->  return DataMasker.maskEnding(value, ann.maxOpen, ann.minHidden, ann.mask)
                is ToStringInitial ->  return DataMasker.initial(value, ann.withPeriodEnding)
                is ToStringMaskEmail ->  return DataMasker.maskEmail(value?.toString(), ann.mask)
                is ToStringHide ->  return DataMasker.hide(value, ann.preserveLength, ann.preserveNull, ann.mask)
            }
        }

        return value
    }
}