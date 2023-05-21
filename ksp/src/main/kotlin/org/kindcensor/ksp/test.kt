//package org.kindcensor.ksp
//
//import TestData
//import kotlin.String
//import org.kindcensor.`annotation`.ToStringHide
//import org.kindcensor.`annotation`.ToStringInitial
//import org.kindcensor.`annotation`.ToStringMaskBeginning
//import org.kindcensor.`annotation`.ToStringMaskEmail
//import org.kindcensor.`annotation`.ToStringMaskEnding
//import org.kindcensor.`annotation`.bind.AnnotationRegistry
//import org.kindcensor.ksp.Stringer
//
//public fun censoredToStringFor_TestData(subject: TestData): String = buildString {
//    append("TestData(")
//    append("firstName=")
//    append(AnnotationRegistry.apply(ToStringInitial(withPeriodEnding=true), subject))
//    append(',')
//    append("middleName=")
//    append(AnnotationRegistry.apply(ToStringInitial(withPeriodEnding=true), subject))
//    append(',')
//    append("lastName=")
//    append(AnnotationRegistry.apply(ToStringMaskEnding(maxOpen=2, minHidden=4, mask='*'), subject))
//    append(',')
//    append("email=")
//    append(AnnotationRegistry.apply(ToStringMaskEmail(mask='*'), subject))
//    append(',')
//    append("phone=")
//    append(AnnotationRegistry.apply(ToStringMaskBeginning(mask='#', maxOpen=2, minHidden=4), subject))
//    append(',')
//    append("password=")
//    append(AnnotationRegistry.apply(ToStringHide(preserveLength=false, preserveNull=true, mask='*'),
//        subject))
//    append(')')
//}
//
//public object Initializer {
//    init {
//        Stringer.register(TestData, ::censoredToStringFor_TestData)
//    }
//}