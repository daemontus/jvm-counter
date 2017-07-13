package com.github.daemontus.klassy.classfile.validate

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.MAGIC
import com.github.daemontus.klassy.classfile.Mask
import org.codehaus.groovy.reflection.ClassInfo

fun ClassFile.validate(): List<ValidationProblem> {
    val problems = ArrayList<ValidationProblem>()
    this.validate(problems)
    return problems;
}

private fun ClassFile.validate(problems: MutableList<ValidationProblem>) {
    if (magic != MAGIC) {
        problems.onError("[1.1.1] `magic <1.1.1>` equals `0xCAFEBABE <!MAGIC>`.")
    }
    if (constantPool.size != constantPoolCount - 1) {
        problems.onError("[1.1.2] `constant_pool <1.1.5>` has size `constant_pool_count <1.1.4> - 1`.")
    }
    val expectedFlags = listOf(
            Mask.PUBLIC, Mask.FINAL, Mask.SUPER, Mask.INTERFACE,
            Mask.ABSTRACT, Mask.SYNTHETIC, Mask.ANNOTATION, Mask.ENUM
    ).fold(0) { a, b -> a.or(b) }
    // 0x1=0, 0x0=0, 1x1=0, 1x0=1
    val unexpectedFlags = accessFlags.and(expectedFlags.inv())
    if (unexpectedFlags != 0) {
        problems.onWarning("[1.1.3] Unexpected `access_flags <1.1.6>` ${Integer.toBinaryString(unexpectedFlags)}.")
    }
    if (accessFlags.hasMask(Mask.INTERFACE) && !accessFlags.hasMask(Mask.ABSTRACT)) {
        problems.onError("[1.1.4] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!ABSTRACT>` must be set.")
    }
    if (accessFlags.hasMask(Mask.INTERFACE) && accessFlags.hasMask(Mask.FINAL)) {
        problems.onError("[1.1.5] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!FINAL>` must *not* be set.")
    }
    if (accessFlags.hasMask(Mask.INTERFACE) && accessFlags.hasMask(Mask.SUPER)) {
        problems.onError("[1.1.6] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!SUPER>` must *not* be set.")
    }
    if (accessFlags.hasMask(Mask.INTERFACE) && accessFlags.hasMask(Mask.ENUM)) {
        problems.onError("[1.1.7] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!ENUM>` must *not* be set.")
    }
    if (accessFlags.hasMask(Mask.ANNOTATION) && !accessFlags.hasMask(Mask.INTERFACE)) {
        problems.onError("[1.1.8] If `access_flags <1.1.6>` has `<!ANNOTATION>` set, `<!INTERFACE>` must be set.")
    }
    if (accessFlags.hasMask(Mask.ABSTRACT) && accessFlags.hasMask(Mask.FINAL)) {
        problems.onError("[1.1.10] `access_flags <1.1.6>` must *not* have both `<!ABSTRACT>` and `<!FINAL>` set.")
    }
    if (!constantPool.isCpIndex(thisClass)) {
        problems.onError("[1.1.11] `this_class <1.1.7>` must be a valid index [1.0.4] into `constant_pool <1.1.5>`.")
    }
    if (!constantPool.checkType<CpInfo.ClassInfo>(thisClass)) {
        problems.onError("[1.1.12] `constant_pool <1.1.5>` entry at index `this_class <1.1.7>` must by of type `Class_info <1.3.0>`.")
    }
    if (superClass != 0 && !constantPool.isCpIndex(superClass)) {
        problems.onError("[1.1.13] `super_class <1.1.8>` is either `0`, or a valid index [1.0.4] into `constant_pool <1.1.5>`.")
    }
    if (superClass != 0 && !constantPool.checkType<CpInfo.ClassInfo>(superClass)) {
        problems.onError("[1.1.14] `constant_pool <1.1.5>` entry at index `super_class <1.1.8>` (if non-zero), must be of type `Class_info <1.3.0>`.")
    }
    if (superClass == 0 && !constantPool.isObjectClass(thisClass)) {
        problems.onError("[1.1.15] If `super_class <1.1.8>` is `0`, `constant_pool <1.1.5>` entry at index `this_class <1.1.7>` represents the `java.lang.Object` class.")
    }
    if (accessFlags.hasMask(Mask.INTERFACE) && superClass == 0) {
        problems.onError("[1.1.16] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `super_class <1.1.8>` is not `0`.")
    }
    if (accessFlags.hasMask(Mask.INTERFACE) && !constantPool.isObjectClass(superClass)) {
        problems.onError("[1.1.17] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `constant_pool <1.1.5>` entry at index `super_class <1.1.8>` represents the `java.lang.Object` class.")
    }
    if (interfaces.size != interfacesCount) {
        problems.onError("[1.1.18] `interfaces <1.1.10>` has size `interfaces_count <1.1.9>`.")
    }
    if (interfaces.any { !constantPool.isCpIndex(it) }) {
        problems.onError("[1.1.19] Each entry in `interfaces <1.1.10>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (interfaces.any { !constantPool.checkType<CpInfo.ClassInfo>(it) }) {
        problems.onError("[1.1.20] forall `i in 0..interfaces_count - 1`, the `constant_pool <1.1.5>` entry at index `interfaces[i] <1.1.10>` must be of type `Class_info <1.3.0>`.")
    }
    if (fields.size != fieldsCount) {
        problems.onError("[1.1.21] `fields <1.1.12>` has size `fileds_count <1.1.11>`.")
    }
    if (methods.size != methodsCount) {
        problems.onError("[1.1.22] `methods <1.1.14>` has size `methods_count <1.1.14>`.")
    }
    if (attributes.size != attributesCount) {
        problems.onError("[1.1.23] `attributes <1.1.16>` has size `attributes_count <1.1.15>`.")
    }
}