package com.github.daemontus.classfile.validate

import com.github.daemontus.classfile.*

private val java8 = 52
private val java7 = 51
private val java6 = 50

private val minSupported = java6
private val maxSupported = java8

fun logValidate(string: String) {
    println(string)
}

@Throws(InvalidClassFileException::class)
fun ClassFile.validate() {

    val classFile = this

    logValidate(" - start validating class file")

    if (version.major < minSupported || version.major > maxSupported) {
        throw InvalidClassFileException("Class files with version $version are not supported.")
    }

    logValidate(" - version validation passed")

    this.access.validate()

    logValidate(" - access validation passed")

    this.constantPool.validate(this)

    logValidate(" - constant pool validation passed")

    this.constantPool.run {
        check(thisClass)

        logValidate(" - this_class validation passed")

        superClass?.let {
            check(it)
        } ?: run {
            if (this[this[thisClass].name].value != "java/lang/Object") {
                throw InvalidClassFileException("Only java/lang/Object can have no super class.")
            }
        }

        logValidate(" - super_class validation passed")

        interfaces.forEach {
            check(it)
        }

        logValidate(" - interfaces validation passed")

        fields.forEach {

            check(it.name)
            check(it.descriptor)
            it.access.validate(classFile)

            it.attributes.forEach(Attribute::validate)

            logValidate(" - field validation passed")
        }
    }
}

fun ClassFile.Access.validate() {

    if (isInterface && !isAbstract)
        throw InvalidClassFileException("Interface must be abstract. Current access: $this")
    if (isInterface && isFinal)
        throw InvalidClassFileException("Interface can't be final. Current access: $this")
    if (isInterface && isEnum)
        throw InvalidClassFileException("Interface can't be enum. Current access: $this")
    if (isInterface && hasSuperFlag)
        throw InvalidClassFileException("Interface can't have super. Current access: $this")

    if (isAbstract && isFinal)
        throw InvalidClassFileException("Abstract classes can't be final. Current access: $this")

    if (isAnnotation && !isInterface)
        throw InvalidClassFileException("Only interfaces can be annotations. Current access: $this")

}

fun ClassFile.FieldInfo.Access.validate(classFile: ClassFile) {

    if (isFinal && isVolatile)
        throw InvalidClassFileException("Field can't be both volatile and final. Current access: $this")

    if (isPublic && isPrivate)
        throw InvalidClassFileException("Field can't be both public and private. Current access: $this")
    if (isPrivate && isProtected)
        throw InvalidClassFileException("Field can't be both private and protected. Current access: $this")
    if (isProtected && isPublic)
        throw InvalidClassFileException("Field can't be both public and protected. Current access: $this")

    if (classFile.access.isInterface) {
        if (!isPublic)
            throw InvalidClassFileException("Interface fields must be public. Current access: $this")
        if (!isFinal)
            throw InvalidClassFileException("Interface fields must be final. Current access: $this")
        if (!isStatic)
            throw InvalidClassFileException("Interface fields must be static. Current access: $this")

        if (isTransient)
            throw InvalidClassFileException("Interface fields can't be transient. Current access: $this")
    }

    if (isEnum && !classFile.access.isEnum)
        throw InvalidClassFileException("Enum fields are only allowed in enum classes. Current access: $this")

}

fun Attribute.validate() {

}