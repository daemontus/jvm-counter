package com.github.daemontus.classfile.validate

import com.github.daemontus.classfile.*

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
            if (this[this[thisClass].name].value != JAVA_LANG_OBJECT) {
                throw InvalidClassFileException("Only $JAVA_LANG_OBJECT can have no super class.")
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

        methods.forEach {
            check(it.name)
            check(it.descriptor)
            it.access.validate(classFile)

            // instance initialization has special requirements
            if (this[it.name].value == INSTANCE_INITIALIZER) {
                if (it.access.isStatic)
                    throw InvalidClassFileException("Instance initializer can't be static. Current access: ${it.access}")
                if (it.access.isFinal)
                    throw InvalidClassFileException("Instance initializer can't be final. Current access: ${it.access}")
                if (it.access.isSynchronized)
                    throw InvalidClassFileException("Instance initializer can't be synchronized. Current access: ${it.access}")
                if (it.access.isBridge)
                    throw InvalidClassFileException("Instance initializer can't be bridge. Current access: ${it.access}")
                if (it.access.isNative)
                    throw InvalidClassFileException("Instance initializer can't be native. Current access: ${it.access}")
                if (it.access.isAbstract)
                    throw InvalidClassFileException("Instance initializer can't be abstract. Current access: ${it.access}")
            }

            logValidate(" - method validation passed")
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

fun ClassFile.MethodInfo.Access.validate(classFile: ClassFile) {

    if (isPublic && isPrivate)
        throw InvalidClassFileException("Method can't be both public and private. Current access: $this")
    if (isPrivate && isProtected)
        throw InvalidClassFileException("Method can't be both private and protected. Current access: $this")
    if (isProtected && isPublic)
        throw InvalidClassFileException("Method can't be both public and protected. Current access: $this")

    if (classFile.access.isInterface) {
        if (isProtected)
            throw InvalidClassFileException("Interface method can't be protected. Current access: $this")
        if (isNative)
            throw InvalidClassFileException("Interface method can't be native. Current access: $this")
        if (isFinal)
            throw InvalidClassFileException("Interface method can't be final. Current access: $this")
        if (isSynchronized)
            throw InvalidClassFileException("Interface method can't be synchronized. Current access: $this")

        if (classFile.version.major < java8) {
            if (!isPublic)
                throw InvalidClassFileException("Prior to Java 8, all interface methods must be public. Current access: $this")
            if (!isAbstract)
                throw InvalidClassFileException("Prior to Java 8, all interface methods must be abstract. Current access: $this")
        }

        if (classFile.version.major >= java8) {
            if (!isPublic && !isPrivate)
                throw InvalidClassFileException("Only public and private methods are allowed in interfaces. Current access: $this")
        }
    }

    if (isAbstract) {
        if (isPrivate)
            throw InvalidClassFileException("Abstract method can't be private. Current access: $this")
        if (isStatic)
            throw InvalidClassFileException("Abstract method can't be static. Current access: $this")
        if (isFinal)
            throw InvalidClassFileException("Abstract method can't be final. Current access: $this")
        if (isSynchronized)
            throw InvalidClassFileException("Abstract method can't be synchronized. Current access: $this")
        if (isNative)
            throw InvalidClassFileException("Abstract method can't be native. Current access: $this")
        if (isStrict)
            throw InvalidClassFileException("Abstract method can't be strict. Current access: $this")
    }

}

fun Attribute.validate() {

}