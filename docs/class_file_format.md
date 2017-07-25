 - [a.b.c] validation rule
 - `<a.b.c>` data and grammar item
 - `<!NAME>` constant item
 - only rules for single file validation go into this file

#### [1.0] `class` file format
 - [1.0.1] `class` file contains definition of exactly one interface or class.
 - [1.0.2] `class` file consists of a stream of 8-bit values.
 - [1.0.3] 16, 32 and 64-bit data types are stored in big-endian order. 
 - [1.0.4] `x` is a valid index into the `constant_pool <1.1.5>` iff `x > 0 && x < constant_pool_count <1.1.4>` and if `x-1 > 0`, the type of `constant_pool <1.1.5>` entry at index `x-1` is not `Double_info <1.3.27>` or `Long_info <1.3.24>`.
 - [1.0.5] `x` is an unqualified name iff `size(x) > 0` and `('.'|';'|'['|'/') !in x`
 - [1.0.6] `x` is a method name iff it is an unqualified name [1.0.5] and (`('<'|'>') !in x` or `x in ("<init>", "<clinit>")`)
 - [1.0.7] `x` is a binary (fully qualified) name iff `size(x.split('/')) > 0` and `x.split('/').all { it is unqualified name [1.0.5] }`
 - [1.0.8] `x` is an identifier iff `('.'|';'|'['|'/'|'<'|'>'|':') !in x` and `size(x) > 0`.
 
#### [1.1] `class` data object

```
ClassFile {                                         <1.1.0>
    u4                          magic               <1.1.1>
    u2                          minor_version       <1.1.2>
    u2                          major_version       <1.1.3>
    u2                          constant_pool_count <1.1.4>
    cp_info<1.3.39>[]           constant_pool       <1.1.5>
    u2                          access_flags        <1.1.6>
    u2                          this_class          <1.1.7>
    u2                          super_class         <1.1.8>
    u2                          interfaces_count    <1.1.9>
    u2[]                        interfaces          <1.1.10>
    u2                          fields_count        <1.1.11>
    field_info<1.4.0>[]         fields              <1.1.12>
    u2                          methods_count       <1.1.13>
    method_info<1.5.0>[]        methods             <1.1.14>
    u2                          attributes_count    <1.1.15>
    attribute_info<1.6.0>[]     attributes          <1.1.16>
}
```

 - [1.1.1] `magic <1.1.1>` equals `0xCAFEBABE <!MAGIC>`
 - [1.1.2] `constant_pool <1.1.5>` has size `constant_pool_count <1.1.4> - 1`
 - [1.1.3] `access_flags <1.1.6>` is a bit mask of:
    - `0x0001 <!PUBLIC>`
    - `0x0010 <!FINAL>`
    - `0x0020 <!SUPER>`
    - `0x0200 <!INTERFACE>`
    - `0x0400 <!ABSTRACT>`
    - `0x1000 <!SYNTHETIC>`
    - `0x2000 <!ANNOTATION>`
    - `0x4000 <!ENUM>`
 - [1.1.4] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!ABSTRACT>` must be set.
 - [1.1.5] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!FINAL>` must *not* be set.
 - [1.1.6] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!SUPER>` must *not* be set.
 - [1.1.7] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!ENUM>` must *not* be set.
 - [1.1.8] If `access_flags <1.1.6>` has `<!ANNOTATION>` set, `<!INTERFACE>` must be set.
 - [1.1.10] `access_flags <1.1.6>` must *not* have both `<!ABSTRACT>` and `<!FINAL>` set.
 - [1.1.11] `this_class <1.1.7>` must be a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.1.12] `constant_pool <1.1.5>` entry at index `this_class <1.1.7>` must by of type `Class_info <1.3.0>`.
 - [1.1.13] `super_class <1.1.8>` is either `0`, or a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.1.14] `constant_pool <1.1.5>` entry at index `super_class <1.1.8>` (if non-zero), must be of type `Class_info <1.3.0>`.
 - [1.1.15] If `super_class <1.1.8>` is `0`, `constant_pool <1.1.5>` entry at index `this_class <1.1.7>` represents the `java.lang.Object` class.
 - [1.1.16] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `super_class <1.1.8>` is not `0`.
 - [1.1.17] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `constant_pool <1.1.5>` entry at index `super_class <1.1.8>` represents the `java.lang.Object` class.
 - [1.1.18] `interfaces <1.1.10>` has size `interfaces_count <1.1.9>`.
 - [1.1.19] Each entry in `interfaces <1.1.10>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.1.20] forall `i in 0..interfaces_count - 1`, the `constant_pool <1.1.5>` entry at index `interfaces[i] <1.1.10>` must be of type `Class_info <1.3.0>`.
 - [1.1.21] `fields <1.1.12>` has size `fileds_count <1.1.11>`.
 - [1.1.22] `methods <1.1.14>` has size `methods_count <1.1.14>`.
 - [1.1.23] `attributes <1.1.16>` has size `attributes_count <1.1.15>`.

#### [1.2] `FieldDescriptor` and `MethodDescriptor` grammar

```
FieldDescriptor : FieldType<1.2.1>                                      <1.2.0>
FieldType : BaseType<1.2.2> | ObjectType<1.2.3> | ArrayType<1.2.4>      <1.2.1>
BaseType : B | C | D | F | I | J | S | Z                                <1.2.2>
ObjectType : L binary_name[1.0.7] ;                                     <1.2.3>
ArrayType : [ FieldType<1.2.1>                                          <1.2.4>

MethodDescriptor : ( Parameter<1.2.6>* ) Return<1.2.1>                  <1.2.5>
Parameter : FieldType<1.2.1>                                            <1.2.6>
Return : FieldType<1.2.1> | V                                           <1.2.7>

Return {
    Void
    FieldType { // FieldDescriptor, Parameter
        BaseType { B | C | D | F | I | J | S | Z }
        ObjectType
        ArrayType
    }
}

MethodDescriptor

JavaType {
    BaseType { B | C | D | F | I | J | S | Z }
    ReferenceType {
        ClassType
        TypeVariable
        ArrayType
    }
}

JavaType : ReferenceType<1.2.9> | BaseType<1.2.2>                               <1.2.8>
ReferenceType : ClassType<1.2.10> | TypeVariable<1.2.20> | ArrayType<1.2.16>   <1.2.9>

ClassType : L PackageSpecifier<1.2.11>? 
            SimpleClassType<1.2.12> ClassTypeSuffix<1.2.15>*            <1.2.10>
PackageSpecifier : identifier[1.0.8] / PackageSpecifier<1.2.11>*        <1.2.11>
SimpleClassType : identifier[1.0.8] TypeArguments<1.2.13>?              <1.2.12>
TypeArguments : < TypeArgument<1.2.14> TypeArgument<1.2.14>+ >          <1.2.13>
TypeArgument : (+|-)? ReferenceType<1.2.9> | *                          <1.2.14>
ClassTypeSuffix : . SimpleClassType<1.2.12>                             <1.2.15>
ArrayType : [ JavaType<1.2.8>                                           <1.2.16>

ClassSignature : TypeParameters<1.2.18>? ClassType<1.2.10> ClassType<1.2.10>*   <1.2.17>
TypeParameters : < TypeParameter<1.2.19> TypeParameter<1.2.19>* >               <1.2.18>
TypeParameter : identifier[1.0.8] : ReferenceType<1.2.9>? (: ReferenceType<1.2.9>)* <1.2.19>

TypeVariable : T identifier[1.0.8] ;                                            <1.2.20>
MethodSignature :   TypeParameters<1.2.18>? ( JavaType<1.2.8>* ) 
                    Result<1.2.22> Throws<1.2.23>                               <1.2.21>
Result : (JavaType<1.2.8> | V)                                                  <1.2.22>
Throws : ^ (ClassType<1.2.10> | TypeVariable<1.2.20>)                           <1.2.23>

FieldSignature : ReferenceType<1.2.9>           <1.2.24>
  
```

 - `ArrayType <1.2.4>` must have fewer than `256` dimensions (number of `[`).
 - The parameter list in `MethodDescriptor <1.2.5>` must have size less than `256` where size includes `this` (for non-static methods) and parameters of type `long` and `double` count as two positions. 

#### [1.3] `cp_info` data format

```
//cp_info is a union of the following structures:
cp_info:            <1.3.49>

Class_info {        <1.3.0>
    u1 tag          <1.3.1>
    u2 name_index   <1.3.2>
}

Fieldref_info {             <1.3.3>
    u1 tag                  <1.3.4>
    u2 class_index          <1.3.5>
    u2 name_and_type_index  <1.3.6>
}

Methodref_info {            <1.3.7>
    u1 tag                  <1.3.8>
    u2 class_index          <1.3.9>
    u2 name_and_type_index  <1.3.10>
}

InterfaceMethodref_info {   <1.3.11>
    u1 tag                  <1.3.12>
    u2 class_index          <1.3.13>
    u2 name_and_type_index  <1.3.14>
}

String_info {               <1.3.15>
    u1 tag                  <1.3.16>
    u2 string_index         <1.3.17>
}

Integer_info {              <1.3.18>
    u1 tag                  <1.3.19>
    int bytes               <1.3.20>
}

Float_info {                <1.3.21>
    u1 tag                  <1.3.22>
    float bytes             <1.3.23>
}

Long_info {                 <1.3.24>
    u1 tag                  <1.3.25>
    long bytes              <1.3.26>
}

Double_info {               <1.3.27>
    u1 tag                  <1.3.28>
    double bytes            <1.3.29>
}

NameAndType_info {          <1.3.30>
    u1 tag                  <1.3.31>
    u2 name_index           <1.3.32>
    u2 descriptor_index     <1.3.33>
}

Utf8_info {                 <1.3.34>
    u1 tag                  <1.3.35>
    u2 length               <1.3.36>
    u1[] bytes              <1.3.37>
}

MethodHandle_info {         <1.3.38>
    u1 tag                  <1.3.39>
    u2 reference_kind       <1.3.40>
    u2 reference_index      <1.3.41>
}

MethodType_info {           <1.3.42>
    u1 tag                  <1.3.43>
    u2 descriptor_index     <1.3.44>
}

InvokeDynamic_info {        <1.3.45>
    u1 tag                  <1.3.46>
    u2 bootstrap_method_attr_index  <1.3.47>
    u2 name_and_type_index  <1.3.48>
}

```
 - [1.3.1] `tag <1.3.35>` has the value `1 <!CONST_Utf8>`.
 - [1.3.2] `tag <1.3.19>` has the value `3 <!CONST_Integer>`.
 - [1.3.3] `tag <1.3.22>` has the value `4 <!CONST_Float>`.
 - [1.3.4] `tag <1.3.25>` has the value `5 <!CONST_Long>`.
 - [1.3.5] `tag <1.3.28>` has the value `6 <!CONST_Double>`.
 - [1.3.6] `tag <1.3.1>` has the value `7 <!CONST_Class>`.
 - [1.3.7] `tag <1.3.16>` has the value `8 <!CONST_String>`.
 - [1.3.8] `tag <1.3.4>` has the value `9 <!CONST_Fieldref>`.
 - [1.3.9] `tag <1.3.8>` has the value `10 <!CONST_Methodref>`.
 - [1.3.10] `tag <1.3.12>` has the value `11 <!CONST_InterfaceMethodref>`.
 - [1.3.11] `tag <1.3.31>` has the value `12 <!CONST_NameAndType>`.
 - [1.3.12] `tag <1.3.39>` has the value `15 <!CONST_MethodHandle>`.
 - [1.3.13] `tag_<1.3.43>` has the value `16 <!CONST_MethodType>`.
 - [1.3.14] `tag_<1.3.44>` has the value `18 <!CONST_InvokeDynamic>`.
 - [1.3.15] `name_index <1.3.2><1.3.32>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.16] `constant_pool <1.1.5>` entry at index `name_index <1.3.2><1.3.32>` must be of type `Utf8_info <1.3.34>` .
 - [1.3.17] `constant_pool <1.1.5>` entry at index `name_index <1.3.2>` must represent a fully quantified binary name [1.0.7] or an `ArrayType <1.2.4>`. 
 - [1.3.18] `constant_pool <1.1.5>` entry at index `name_index <1.3.32>` must represent a valid unqualified name [1.0.5].
 - [1.3.19] `class_index <1.3.5><1.3.9><1.3.13>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.20] `constant_pool <1.1.5>` entry at index `class_index <1.3.5><1.3.9><1.3.13>` must be of type `Class_info <1.3.0>`.
 - [1.3.21] `name_and_type_index <1.3.6><1.3.10><1.3.14><1.3.48>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.22] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.6><1.3.10><1.3.14><1.3.48>` must be of type `NameAndType_info <1.3.30>`.
 - [1.3.23] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.6>` must have a `FieldDescriptor <1.2.0>` as type.
 - [1.3.24] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.10><1.3.14><1.3.48>` must have a `MethodDescriptor <1.2.5>` as type.
 - [1.3.25] `string_index <1.3.17>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.26] `constant_pool <1.1.5>` entry at `string_index <1.3.17>` must be of type `Utf8_info <1.3.34>`.
 - [1.3.27] `descriptor_index <1.3.33><1.3.44>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.28] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.33><1.3.44>` must be of type `Utf8_info <1.3.34>`.
 - [1.3.29] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.33>` must represent a valid `FieldDescriptor <1.2.0>` or `MethodDescriptor <1.2.5>`.
 - [1.3.30] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.44>` must represent a valid `MethodDescriptor <1.2.5>`.
 - [1.3.31] `bytes <1.3.37>` is of size `length <1.3.36>`.
 - [1.3.32] `bytes <1.3.37` represent a *modified UTF-8 string* (definition needed).
 - [1.3.33] `reference_kind <1.3.40>` must be in range `1..9`.
 - [1.3.34] `reference_index <1.3.41>` must be a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.35] If `reference_kind <1.3.40>` is in range `1..4`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Fieldref_info <1.3.3>`.
 - [1.3.36] If `reference_kind <1.3.40>` is `5` or `8`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Methodref_info <1.3.7>`.
 - [1.3.37] If `reference_kind <1.3.40>` is `6` or `7` and `major_version <1.1.2>` is less than `52`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Methodref_info <1.3.7>`. If `major_version <1.1.2> >= 52`, it can also be `InterfaceMethodref_info <1.3.11>`.
 - [1.3.38] If `reference_kind <1.3.40>` is `9`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `InterfaceMethodref_info <1.3.11>`.
 - [1.3.39] If `reference_kind <1.3.40>` is in `[5,6,7,9]`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must not have a name `<init>` or `<clinit>`.
 - [1.3.40] If `reference_kind <1.3.40>` is `8`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must have a name `<init>`.
 - [1.3.41] `bootstrap_method_attr_index <1.3.47>` is a valid index into the `bootstrap_methods <1.28.4>`.
 
#### [1.4] `field_info` data format

```
field_info {                                        <1.4.0>
    u2                          access_flags        <1.4.1>
    u2                          name_index          <1.4.2>
    u2                          descriptor_index    <1.4.3>
    u2                          attributes_count    <1.4.4>
    attribute_info<1.6.0>[]     attributes          <1.4.5>
}
```

 - [1.4.1] `access_flags <1.4.1>` is a bit mask of:
    - `0x0001 <!PUBLIC>`
    - `0x0002 <!PRIVATE>`
    - `0x0004 <!PROTECTED>`
    - `0x0008 <!STATIC>`
    - `0x0010 <!FINAL>`
    - `0x0040 <!VOLATILE>`
    - `0x0080 <!TRANSIENT>`
    - `0x1000 <!SYNTHETIC>`
    - `0x4000 <!ENUM>`
    
 - [1.4.2] In `access_flags <1.4.1>`, `<!VOLATILE>` and `<!FINAL>` *cannot* be set at the same time.
 - [1.4.3] In `access_flags <1.4.1>`, at most one of `<!PUBLIC>`, `<!PRIVATE>`, `<!PROTECTED>` can be set.
 - [1.4.4] If `access_flags <1.1.6>` (class) has `<!INTERFACE>` set, then in `access_flags <1.4.1>`, `<!PUBLIC>`, `<!STATIC>` and `<!FINAL>` *must* be set.
 - [1.4.5] If `access_flags <1.1.6>` (class) has `<!INTERFACE>` set, then in `access_flags <1.4.1>`, only `<!PUBLIC>`, `<!STATIC>`, `<!FINAL>` and `<!SYNTHETIC>` *can* be set.
 - [1.4.6] `name_index <1.4.2>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.4.7] `constant_pool <1.1.5>` entry at index `name_index <1.4.2>` must be of type `Utf8_info <1.3.34>`.
 - [1.4.8] `constant_pool <1.1.5>` entry at index `name_index <1.4.2>` must represent a valid unqualified name [1.0.5].
 - [1.4.9] `descriptor_index <1.4.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
- [1.4.7] `constant_pool <1.1.5>` entry at index `descriptor_index <1.4.3>` must be of type `Utf8_info <1.3.34>`.
 - [1.4.8] `constant_pool <1.1.5>` entry at index `descriptor_index <1.4.3>` must represent a valid `FieldDescriptor <1.2.0>`.
 - [1.4.9] `attributes <1.4.5>` has size `attributes_count <1.4.4>`.
 
#### [1.5] `method_info` data format

```
method_info {                                       <1.5.0>
    u2                          access_flags        <1.5.1>
    u2                          name_index          <1.5.2>
    u2                          descriptor_index    <1.5.3>
    u2                          attributes_count    <1.5.4>
    attributes_info<1.6.0>[]    attributes          <1.5.5>
}
```

 - [1.5.1] `access_flags <1.5.1>` is a bit mask of:
    - `0x0001 <!PUBLIC>`
    - `0x0002 <!PRIVATE>`
    - `0x0004 <!PROTECTED>`
    - `0x0008 <!STATIC>`
    - `0x0010 <!FINAL>`
    - `0x0020 <!SYNCHRONIZED>`
    - `0x0040 <!BRIDGE>`  
    - `0x0080 <!VARARGS>`
    - `0x0100 <!NATIVE>`
    - `0x0400 <!ABSTRACT>`
    - `0x0800 <!STRICT>`
    - `0x1000 <!SYNTHETIC>`

 - [1.5.2] In `access_flags <1.5.1>`, at most one of `<!PUBLIC>`, `<!PRIVATE>`, `<!PROTECTED>` can be set.
 - [1.5.3] If `access_flags <1.1.6>` has `<!INTERFACE>` set, then in `access_flags <1.5.1>`, `<!PROTECTED>`, `<!FINAL>`, `<!SYNCHRONIZED>`, `<!NATIVE>` must *not* be set.
 - [1.5.5] If `access_flags <1.1.6>` has `<!INTERFACE>` set and `major_version <1.1.3> < 52`, then in `access_flags <1.5.1>`, `<!PUBLIC>` and `<!ABSTRACT>` must be set. If `major_version <1.1.3> >= 52`, `<!PUBLIC>` or `<!PRIVATE>` must be set. 
 - [1.5.6] If `access_flags <1.5.1>` has `<!ABSTRACT>` set, `<!PRIVATE>`, `<!STATIC>`, `<!FINAL>`, `<!SYNCHRONIZED>`, `<!NATIVE>` and `<!STRICT>` must *not* be set.
 - [1.5.7] If `constant_pool <1.1.5>` entry at index `name_index <1.5.2>` represents the name `<init>`, only `<!PRIVATE>`, `<!PUBLIC>`, `<!PROTECTED>`, `<!VARARGS>`, `<!STRICT>` and `<!SYNTHETIC>` flags *can* be set.
 - [1.5.8] `name_index <1.5.2>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.5.9] `constant_pool <1.1.5>` entry at index `name_index <1.5.2>` must be of type `Utf8_info <1.3.34>`.
 - [1.5.10] `constant_pool <1.1.5>` entry at index `name_index <1.5.2>` must represent a valid unqualified name [1.0.5].
 - [1.5.11] `descriptor_index <1.5.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.5.12] `constant_pool <1.1.5>` entry at index `descriptor_index <1.5.3>` must be of type `Utf8_info <1.3.34>`.
 - [1.5.13] `constant_pool <1.1.5>` entry at index `descriptor_index <1.5.3>` must represent a valid `MethodDescriptor <1.2.5>`.
 - [1.5.14] `attributes <1.5.5>` has size `attributes_count <1.5.4>`.
  
#### [1.6] General attribute

Applies to all attributes, even unrecognized ones:

```
attribute_info {                    <1.6.0>
    u2      attribute_name_index    <1.6.1>
    u4      attribute_length        <1.6.2>
    u1[]    info                    <1.6.3>
}
```

 - [1.6.1] `attribute_name_index <1.6.1>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.6.2] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.6.1>` must be of type `Utf8_info <1.3.34>`.
 - [1.6.3] `info <1.6.3>` has size `attribute_length <1.6.2>`. 
 
#### [1.7] `ConstantValue` attribute

```
ConstantValue {                     <1.7.0>
    u2      attribute_name_index    <1.7.1>
    u4      attribute_length        <1.7.2>
    u2      constantvalue_index     <1.7.3>
}
```

 - [1.7.1] All rules from [1.6] apply.
 - [1.7.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.7.3] Appears in `attributes <1.4.5>`.
 - [1.7.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.7.1>` must be `ConstantValue <!CONSTANT_VALUE>`.
 - [1.7.6] There may be at most one attribute of this type. 
 - [1.7.6] `constantvalue_index <1.7.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.7.7] `contant_pool <1.1.5>` entry at index `constantvalue_index <1.7.3>` has a type matching the `FieldDescriptor <1.2.0>` of this field (int, short, char, byte and boolean are all represented as integers).   

#### [1.8] `Code` attribute
```
Code {                                                          <1.8.0>
    u2                              attribute_name_index        <1.8.1>
    u4                              attribute_length            <1.8.2>
    u2                              max_stack                   <1.8.3>
    u2                              max_locals                  <1.8.4>
    u4                              code_length                 <1.8.5>
    u1[]                            code                        <1.8.6>
    u2                              exception_table_length      <1.8.7>
    exception_table_entry<1.8.11>[] exception_table             <1.8.8>
    u2                              attributes_count            <1.8.9>
    attribute_info<1.6.0>[]         attributes                  <1.8.10>
}

exception_table_entry {                                 <1.8.11>
    u2                      start_pc                    <1.8.12>
    u2                      end_pc                      <1.8.13>
    u2                      handler_pc                  <1.8.14>
    u2                      catch_type                  <1.8.15>
}
```

 - [1.8.1] All rules from [1.6] apply.
 - [1.8.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.8.2] Appears in `attributes <1.5.5>`.
 - [1.8.3] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.8.1>` must be `Code <!CODE>`.
 - [1.8.4] If this methods `access_flags <1.5.1>` have `0x0100 <!NATIVE>` or `<!ABSTRACT>` set, no code attribute is allowed. Otherwise exactly one code attribute must be present.
 - [1.8.5] `code_length <1.8.5>` is in `1..65535`.
 - [1.8.6] `code <1.8.6>` has size `code_length <1.8.5>`.
 - [1.8.7] `exception_table <1.8.8>` has size `exception_table_length <1.8.7>`.
 - [1.8.8] `attributes <1.8.10>` have size `attributes_count <1.8.9>`.
 - [1.8.9] `start_pc <1.8.12>` and `end_pc <1.8.13>` form a right-exclusive range in the `code <1.8.6>`, i.e. `start_pc <1.8.12> < end_pc <1.8.13>` and `start_pc <1.8.12> >= 0 && start_pc <1.8.12> < code_length <1.8.5>` and `end_pc <1.8.13> > 0 end_pc <1.8.13> <= code_length <1.8.5>`.
 - [1.8.10] `handler_pc <1.8.14>` must be a valid index into the `code <1.8.6>`.
 - [1.8.11] `catch_type <1.8.15>` is either `0` or a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.8.12] If not `0`, `constant_pool <1.1.5>` entry at index `catch_type <1.8.15>` must be of type `Class_info <1.3.0>`. 

#### [1.9] `StackMapTable` attribute

```
StackMapTable {                                         <1.9.0>
    u2                          attribute_name_index    <1.9.1>
    u4                          attribute_length        <1.9.2>
    u2                          number_of_entries       <1.9.3>
    stack_map_frame<1.9.52>[]   entries                 <1.9.4>
}

// stack_map_frame is a union of the following structures:
stack_map_frame:                                        <1.9.52>

same_frame {                                            <1.9.5>
    u1                  frame_type                      <1.9.6>
}

same_locals_1_stack_item_frame {                        <1.9.7>
    u1                          frame_type              <1.9.8>
    verification_type<1.9.51>   stack                   <1.9.9>
}

same_locals_1_stack_item_frame_extended {               <1.9.10>
    u1                          frame_type              <1.9.11>
    u2                          offset_delta            <1.9.12>
    verification_type<1.9.51>   stack                   <1.9.13>
}

chop_frame {                                            <1.9.14>
    u1                  frame_type                      <1.9.15>
    u2                  offset_delta                    <1.9.16>
}

same_frame_extended {                                   <1.9.17>
    u1                  frame_type                      <1.9.18>
    u2                  offset_delta                    <1.9.19>
}

append_frame {                                          <1.9.20>
    u1                          frame_type              <1.9.21>
    u2                          offset_delta            <1.9.22>
    verification_type<1.9.51>[] locals                  <1.9.23>
}

full_frame {                                            <1.9.24>
    u1                          frame_type              <1.9.25>
    u2                          offset_delta            <1.9.26>
    u2                          number_of_locals        <1.9.27>
    verification_type<1.9.51>[] locals                  <1.9.28>
    u2                          number_of_stack_items   <1.9.29>
    verification_type<1.9.51>[] stack                   <1.9.30>
}

// verification_type is a union of the following structures:
verification_type:                                      <1.9.51>    
 
Top {                                                   <1.9.31>
    u1                  tag                             <1.9.32>
}

Integer {                                               <1.9.33>
    u1                  tag                             <1.9.34>
}

Float {                                                 <1.9.35>
    u1                  tag                             <1.9.36>
}

Null {                                                  <1.9.37>
    u1                  tag                             <1.9.38>
}

UninitializedThis {                                     <1.9.39>
    u1                  tag                             <1.9.40>
}

Object {                                                <1.9.41>
    u1                  tag                             <1.9.42>
    u2                  cpool_index                     <1.9.43>
}

UnitializedVariable {                                   <1.9.44>
    u1                  tag                             <1.9.45>
    u2                  offset                          <1.9.46>
}

Long {                                                  <1.9.47>
    u1                  tag                             <1.9.48>
}

Double {                                                <1.9.49>
    u1                  tag                             <1.9.50>
}


```
 - [1.9.1] All rules from [1.6] apply.
 - [1.9.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 50.0`.
 - [1.9.3] Appears in `attributes <1.8.10>`.
 - [1.9.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.9.1>` must be `StackMapTable <!STACK_MAP_TABLE>`.
 - [1.9.5] At most one stack map table can be present for each method.
 - [1.9.6] `entries <1.9.4>` has size `number_of_entries <1.9.3>`.
 - [1.9.7] `tag <1.9.32>` is `0x0 <!TOP>`.
 - [1.9.8] `tag <1.9.34>` is `0x1 <!INTEGER>`.
 - [1.9.9] `tag <1.9.36>` is `0x2 <!FLOAT>`.
 - [1.9.10] `tag <1.9.50>` is `0x3 <!DOUBLE>`.
 - [1.9.11] `tag <1.9.48>` is `0x4 <!LONG>`.
 - [1.9.12] `tag <1.9.38>` is `0x5 <!NULL>`.
 - [1.9.13] `tag <1.9.40>` is `0x6 <!U_THIS>`.
 - [1.9.14] `tag <1.9.42>` is `0x7 <!OBJECT>`.
 - [1.9.15] `tag <1.9.45>` is `0x8 <!U_VAR>`.
 - [1.9.16] `cpool_index <1.9.43>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.9.17] `constant_pool <1.1.5>` entry at index `cpool_index <1.9.43>` must be of type `Class_info <1.3.0>`.
 - [1.9.18] `offset <1.9.46>` must be a valid index into the `code <1.8.6>` of parent code attribute.
 - [1.9.19] `frame_type <1.9.6>` is in range `0..63`.
 - [1.9.20] `frame_type <1.9.8>` is in range `64..127`.
 - [1.9.21] `frame_type <1.9.11>` is `247`.
 - [1.9.22] `frame_type <1.9.15>` is in range `248..250`.
 - [1.9.23] `frame_type <1.9.18>` is `251`.
 - [1.9.24] `frame_type <1.9.21>` is in range `252..254`.
 - [1.9.25] `frame_type <1.9.25>` is `255`.
 - [1.9.26] The size of `locals <1.9.23>` is `frame_type <1.9.21> - 251`.
 - [1.9.27] The size of `locals <1.9.28>` is `number_of_locals <1.9.27>`.
 - [1.9.28] The size of `stack <1.9.30>` is `numer_of_stack_items <1.9.29>`.
 

#### [1.10] `Exceptions` attribute

```
Exceptions {                            <1.10.0>
    u2      attribute_name_index        <1.10.1>
    u4      attribute_length            <1.10.2>
    u2      number_of_exceptions        <1.10.3>
    u2[]    exception_index_table       <1.10.4>
}
```

 - [1.10.1] All rules from [1.6] apply.
 - [1.10.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.10.3] Appears in `attributes <1.5.5>`.
 - [1.10.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.10.1>` must be `Exceptions <!EXCEPTIONS>`.
 - [1.10.5] At most one exceptions attribute can be present for each method.
 - [1.10.6] Size of the `exception_index_table <1.10.4>` is `number_of_exceptions <1.10.3>`.
 - [1.10.7] Every `exception_index_table <1.10.4>` entry is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.10.8] Every `exception_index_table <1.10.4>` entry corresponds to a `constant_pool <1.1.5>` entry of type `Class_info <1.3.0>`.

#### [1.11] `InnerClasses` attribute

```
InnerClasses {                                          <1.11.0>
    u2                      attribute_name_index        <1.11.1>
    u4                      attribute_length            <1.11.2>
    u2                      number_of_classes           <1.11.3>
    inner_class<1.11.9>[]   classes                     <1.11.4>
}

inner_class {                                   <1.11.9>
    u2              inner_class_info_index      <1.11.5>              
    u2              outer_class_info_index      <1.11.6>
    u2              inner_name_index            <1.11.7>
    u2              inner_class_access_flags    <1.11.8>
}
```

 - [1.11.1] All rules from [1.6] apply.
 - [1.11.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.11.3] Appears in `attributes <1.1.16>`.
 - [1.11.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.11.1>` must be `InnerClasses <!INNER_CLASSES>`.
 - [1.11.5] At most one inner classes attribute can be present for each class.
 - [1.11.6] Size of the `classes <1.11.4>` is `number_of_classes <1.11.3>`.
 - [1.11.7] `inner_class_info_index <1.11.5>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.11.8] `constant_pool <1.1.5>` entry at index `inner_class_info_index <1.11.5>` must be of type `Class_info <1.3.0>`.
 - [1.11.9] `outer_class_info_index <1.11.6>` is either `0` or a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.11.10] If not zero, index `outer_class_info_index <1.11.6>` points to a `constant_pool <1.1.5>` entry of type `Class_info <1.3.0>`.
 - [1.11.11] `inner_name_index <1.11.7>` is either `0` or a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.11.12] If not zero, index `inner_name_index <1.11.7>` points to a `constant_pool <1.1.5>` entry of type `Utf8_info <1.3.34>`.
 - [1.11.13] `inner_class_access_flags <1.11.8>` is bit mask of:
    - `0x0001 <!PUBLIC>`
    - `0x0002 <!PRIVATE>`
    - `0x0004 <!PROTECTED>`
    - `0x0008 <!STATIC>`
    - `0x0010 <!FINAL>`
    - `0x0200 <!INTERFACE>`
    - `0x0400 <!ABSTRACT>`
    - `0x1000 <!SYNTHETIC>`
    - `0x2000 <!ANNOTATION>`
    - `0x4000 <!ENUM>`
 - [1.11.14] If `major_version.minor_version <1.1.3>.<1.1.2> >= 51.0` and `inner_name_index <1.11.7>` is `0`, `outer_class_info_index <1.11.6>` must be `0` too.
 
#### [1.12] `EnclosingMethod` attribute

```
EnclosingMethod {               <1.12.0>
    u2  attribute_name_index    <1.12.1>
    u4  attribute_length        <1.12.2>
    u2  class_index             <1.12.3>
    u2  method_index            <1.12.4>
}
```

 - [1.12.1] All rules from [1.6] apply.
 - [1.12.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.12.3] Appears in `attributes <1.1.16>`.
 - [1.12.4] At most one enclosing method attribute can be present for each class.
 - [1.12.5] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.12.1>` must be `EnclosingMethod <!ENCLOSING_METHOD>`.
 - [1.12.6] `class_index <1.12.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.12.7] `constant_pool <1.1.5>` entry at index `class_index <1.12.3>` must be of type `Class_info <1.3.0>`.
 - [1.12.8] `method_index <1.12.4>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.12.9] `constant_pool <1.1.5>` entry at index `method_index <1.12.4>` must be of type `NameAndType_info <1.3.30>`.
  
#### [1.13] `Synthetic` attribute

```
Synthetic {                     <1.13.0>
    u2  attribute_name_index    <1.13.1>
    u4  attribute_length        <1.13.2>
}
```

 - [1.13.1] All rules from [1.6] apply.
 - [1.13.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.13.3] Appears in `attributes <1.1.16><1.4.5><1.5.5><1.8.10>`.
 - [1.13.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.13.1>` must be `Synthetic <!SYNTHETIC>`.
 - [1.13.5] `attribute_length <1.13.2>` must be `0`.
 - TODO: Check if synthetic is allowed multiple times in practice

#### [1.14] `Signature` attribute

```
Signature {                     <1.14.0>
    u2  attribute_name_index    <1.14.1>
    u4  attribute_length        <1.14.2>
    u2  signature_index         <1.14.3>
}  
```

 - [1.14.1] All rules from [1.6] apply.
 - [1.14.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.14.3] Appears in `attributes <1.1.16><1.4.5><1.5.5>`.
 - [1.14.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.14.1>` must be `Signature <!Signature>`.
 - [1.14.5] `signature_index <1.14.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.14.6] `constant_pool <1.1.5>` entry at index `signature_index <1.14.3>` must be of type `Utf8_info <1.3.34>`.
 - [1.14.7] `constant_pool <1.1.5>` entry at index `signature_index <1.14.3>` must represent a `ClassSignature <1.1.17>`, `MethodSignature <1.1.21>` or `FieldSignature <1.1.24>` in accordance with the location of this attribute.
 - TODO: Probably should appear only once - but it's not part of the spec. 

#### [1.15] `SourceFile` attribute
```
SourceFile {                    <1.15.0>
    u2  attribute_name_index    <1.15.1>
    u4  attribute_length        <1.15.2>
    u2  sourcefile_index        <1.15.3>
}
```
 - [1.15.1] All rules from [1.6] apply.
 - [1.15.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.15.3] Appears in `attributes <1.1.16>`.
 - [1.15.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.15.1>` must be `SourceFile <!SOURCE_FILE>`.
 - [1.15.5] `sourcefile_index <1.15.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.15.6] `constant_pool <1.1.5>` entry at index `sourcefile_index <1.15.3>` must be of type `Utf8_info <1.3.34>`.
 - [1.15.7] At most one source file attribute can be present for each class. 

#### [1.16] `SourceDebugExtensions` attribute
```
SourceDebugExtensions {             <1.16.0>
    u2      attribute_name_index    <1.16.1>
    u4      attribute_length        <1.16.2>
    u1[]    debug_extension         <1.16.3>
}
```
 - [1.16.1] All rules from [1.6] apply.
 - [1.16.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.16.3] Appears in `attributes <1.1.16>`.
 - [1.16.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.16.1>` must be `SourceDebugExtensions <!SOURCE_DEBUG_EXTENSIONS>`.
 - [1.16.5] Size of the `debug_extension <1.16.3>` is `attribute_length <1.16.2>`.
 - [1.16.6] At most one source debug extension attribute can be present for each class. 

#### [1.17] `LineNumberTable` attribute
```
LineNumberTable {                               <1.17.0>
    u2              attribute_name_index        <1.17.1>
    u4              attribute_length            <1.17.2>
    u2              line_number_table_length    <1.17.3>
    entry<1.17.5>[] line_number_table           <1.17.4>
}

entry {                                 <1.17.5>
    u2  start_pc                        <1.17.6>
    u2  line_number                     <1.17.7>
}
```

 - [1.17.1] All rules from [1.6] apply.
 - [1.17.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.17.3] Appears in `attributes <1.8.10>`.
 - [1.17.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.17.1>` must be `LineNumberTable <!LINE_NUMBER_TABLE>`.
 - [1.17.5] Size of the `line_number_table <1.17.4>` is `line_number_table_length <1.17.3>`.
 - [1.17.6] `start_pc <1.17.6>` is a valid instruction index in the corresponding `code <1.8.6>`

#### [1.18] `LocalVariableTable` attribute
```
LocalVariableTable {                                <1.18.0>
    u2              attribute_name_index            <1.18.1>
    u4              attribute_length                <1.18.2>
    u2              local_variable_table_length     <1.18.3>
    entry<1.18.5>[] local_variable_table            <1.18.4>
}

entry {                                     <1.18.5>
    u2  start_pc                            <1.18.6>
    u2  length                              <1.18.7>
    u2  name_index                          <1.18.8>
    u2  descriptor_index                    <1.18.9>
    u2  index                               <1.18.10>
}
```
 - [1.18.1] All rules from [1.6] apply.
 - [1.18.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - [1.18.3] Appears in `attributes <1.8.10>`.
 - [1.18.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.18.1>` must be `LocalVariableTable <!LOCAL_VARIABLE_TABLE>`.
 - [1.18.5] There can be at most one local variable table *per local variable* (whatever that means).
 - [1.18.6] Size of the `local_variable_table <1.18.4>` is `local_variable_table_length <1.18.3>`.
 - [1.18.7] `start_pc <1.18.6>` is a valid instruction index in the corresponding `code <1.8.6>`.
 - [1.18.8] `start_pc <1.18.6> + length <1.18.7>` is either a valid instruction index in the corresponding `code <1.8.6>`, or equal to `code_length <1.8.5>`.
 - [1.18.9] `name_index <1.18.8>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.18.10] `constant_pool <1.1.5>` entry at index `name_index <1.18.8>` is of type `Utf8_info <1.3.34>`.
 - [1.18.11] `constant_pool <1.1.5>` entry at index `name_index <1.18.8>` is a valid unqualified name [1.0.5].
 - [1.18.12] `descriptor_index <1.18.9>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.18.13] `constant_pool <1.1.5>` entry at index `descriptor_index <1.18.9>` is of type `Utf8_info <1.3.34>`.
 - [1.18.14] `constant_pool <1.1.5>` entry at index `descriptor_index <1.18.9>` is a valid `FieldDescriptor<1.2.0>`.
 - [1.18.15] `index <1.18.10>` is a valid index into the local variable array, i.e. `index <1.18.10> + 1 < max_locals <1.8.4>` if the descriptor is of type long/double and without the `+1` otherwise.

#### [1.19] `LocalVariableTypeTable` attribute
```
LocalVariableTypeTable {                                <1.19.0>
    u2              attribute_name_index                <1.19.1>
    u4              attribute_length                    <1.19.2>
    u2              local_variable_type_table_length    <1.19.3>
    entry<1.19.5>[] local_variable_type_table           <1.19.4>
}

entry {                                     <1.19.5>
    u2  start_pc                            <1.19.6>
    u2  length                              <1.19.7>
    u2  name_index                          <1.19.8>
    u2  signature_index                     <1.19.9>
    u2  index                               <1.19.10>
}
```
 - [1.19.1] All rules from [1.6] apply.
 - [1.19.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.19.3] Appears in `attributes <1.8.10>`.
 - [1.19.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.19.1>` must be `LocalVariableTypeTable <!LOCAL_VARIABLE_TYPE_TABLE>`.
 - [1.19.5] There can be at most one local variable table *per local variable* (whatever that means).
 - [1.19.6] Size of the `local_variable_type_table <1.19.4>` is `local_variable_type_table_length <1.19.3>`.
 - [1.19.7] `start_pc <1.19.6>` is a valid instruction index in the corresponding `code <1.8.6>`.
 - [1.19.8] `start_pc <1.19.6> + length <1.19.7>` is either a valid instruction index in the corresponding `code <1.8.6>`, or equal to `code_length <1.8.5>`.
 - [1.19.9] `name_index <1.19.8>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.19.10] `constant_pool <1.1.5>` entry at index `name_index <1.19.8>` is of type `Utf8_info <1.3.34>`.
 - [1.19.11] `constant_pool <1.1.5>` entry at index `name_index <1.19.8>` is a valid unqualified name [1.0.5].
 - [1.19.12] `siganture_index <1.19.9>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.19.13] `constant_pool <1.1.5>` entry at index `signature_index <1.19.9>` is of type `Utf8_info <1.3.34>`.
 - [1.19.14] `constant_pool <1.1.5>` entry at index `signature_index <1.19.9>` is a valid `FieldDescriptor<1.2.0>`.
 - [1.19.15] `index <1.19.10>` is a valid index into the local variable array, i.e. `index <1.19.10> + 1 < max_locals <1.8.4>` if the descriptor is of type long/double and without the `+1` otherwise.


#### [1.20] `Deprecated` attribute
```
Deprecated {                        <1.20.0>
    u2  attribute_name_index        <1.20.1>
    u4  attribute_length            <1.20.2>
}
```
 - All rules from [1.6] apply.
 - appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.
 - appears in `attributes <1.1.16><1.4.5><1.5.5>`.
 - [1.9.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.20.1>` must be `Deprecated <!DEPRECATED>`.
 - [1.13.5] `attribute_length <1.20.2>` must be `0`.
 - TODO: Check if the attribute can be present more than once in practice.

#### [1.21] `RuntimeVisibleAnnotations` attribute
```
RuntimeVisibleAnnotations {                         <1.21.0>
    u2                      attribute_name_index    <1.21.1>
    u4                      attribute_length        <1.21.2>
    u2                      num_annotations         <1.21.3>
    annotation<1.21.5>[]    annotations             <1.21.4>
}

annotation {                                        <1.21.5>
    u2                  type_index                  <1.21.6>
    u2                  num_element_value_pairs     <1.21.7>
    pair<1.21.9>[]      element_value_pairs         <1.21.8>
}

pair {                                              <1.21.9>
    u2                  element_name_index          <1.21.10>
    element_value       value                       <1.21.11>
}

# Element value is a union of the following types
element_value:                                      <1.21.12>
const_value {                                       <1.21.13>
    u1                          tag                 <1.21.14>
    u2                          const_value_index   <1.21.15>
}

enum_value {                                        <1.21.16>
    u1                          tag                 <1.21.17>
    u2                          type_name_index     <1.21.18>
    u2                          const_name_index    <1.21.19>
}

class_value {                                       <1.21.20>
    u1                          tag                 <1.21.21>
    u2                          class_info_index    <1.21.22>
}

annotation_value {                                  <1.21.23>
    u1                          tag                 <1.21.24>
    annotation                  value               <1.21.25>
}

array_value {                                       <1.21.26>
    u1                          tag                 <1.21.27>
    u2                          num_values          <1.21.28>
    element_value<1.21.12>[]    values              <1.21.29>
}
```

 - [1.21.1] All rules from [1.6] apply.
 - [1.21.2] appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.21.3] appears in `attributes <1.1.16><1.4.5><1.5.5>`.
 - [1.21.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.21.1>` must be `RuntimeVisibleAnnotations <!RUNTIME_VISIBLE_ANNOTATIONS>`.
 - [1.21.5] There can be at most one runtime visible annotations attribute in each attributes table.
 - [1.21.6] Size of the `annotations <1.21.4>` is `num_annotations <1.21.3>`.
 - [1.21.7] Size of the `element_value_pairs <1.21.8>` is `num_element_value_pairs <1.21.7>`.
 - [1.21.8] Size if the `values <1.21.29>` is `num_values <1.21.28>`.
 - [1.21.9] `type_index <1.21.6>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.21.10] `constant_pool <1.1.5>` entry at index `type_index <1.21.6>` must be of type `Utf8_info <1.3.34>`.
 - [1.21.11] `constant_pool <1.1.5>` entry at index `type_index <1.21.6>` must be a valid `FieldDescriptor <1.2.0>`.
 - [1.21.12] `element_name_index <1.21.10>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.21.13] `constant_pool <1.1.5>` entry at index `element_name_index <1.21.10>` must be of type `Utf8_info <1.3.34>`.
 - [1.21.14] `tag <1.21.14>` is one of `B`, `C`, `D`, `F`, `I`, `J`, `S`, `Z` or `s`.
 - [1.21.15] `tag <1.21.17>` is `e`.
 - [1.21.16] `tag <1.21.21>` is `c`.
 - [1.21.17] `tag <1.21.24>` is `@`.
 - [1.21.18] `tag <1.21.27>` is `[`.
 - [1.21.19] `const_value_index <1.21.15>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.21.20] If `tag <1.21.14>` is `B`, `C`, `I`, `S` or `Z`, the `constant_pool <1.1.5>` entry at index `cont_value_index <1.21.15>` must be of type `Integer_info <1.3.18>`.
 - [1.21.21] If `tag <1.21.14>` is `D`, the `constant_pool <1.1.5>` entry at index `cont_value_index <1.21.15>` must be of type `Double_info <1.3.27>`.
 - [1.21.22] If `tag <1.21.14>` is `F`, the `constant_pool <1.1.5>` entry at index `cont_value_index <1.21.15>` must be of type `Float_info <1.3.21>`.
 - [1.21.23] If `tag <1.21.14>` is `J`, the `constant_pool <1.1.5>` entry at index `cont_value_index <1.21.15>` must be of type `Long_info <1.3.24>`.
 - [1.21.24] If `tag <1.21.14>` is `s`, the `constant_pool <1.1.5>` entry at index `cont_value_index <1.21.15>` must be of type `Utf8_info <1.3.34>`. 
 - [1.21.25] `type_name_index <1.21.18>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.21.26] `constant_pool <1.1.5>` entry at index `type_name_index <1.21.18>` must be of type `Utf8_info <1.3.34>`.
 - [1.21.27] `constant_pool <1.1.5>` entry at index `type_name_index <1.21.18>` must represent a valid `FieldDescriptor <1.2.0>`.
 - [1.21.28] `const_name_index <1.21.19>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.21.29] `constant_pool <1.1.5>` entry at index `const_name_index <1.21.19>` must be of type `Utf8_info <1.3.34>`.
 - [1.21.30] `constant_pool <1.1.5>` entry at index `const_name_index <1.21.19>` must be a simple name (probably an unqualified name [1.0.5]).
 - [1.21.31] `class_info_index <1.21.22>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.21.32] `constant_pool <1.1.5>` entry at index `class_info_index <1.21.22>` must be of type `Utf8_info <1.3.34>`.
 - [1.21.33] `constant_pool <1.1.5>` entry at index `class_info_index <1.21.19>` is a valid `Return <1.2.7>` descriptor.


#### [1.22] `RuntimeInvisibleAnnotations` attribute

```
RuntimeInvisibleAnnotations {                       <1.22.0>
    u2                      attribute_name_index    <1.22.1>
    u4                      attribute_length        <1.22.2>
    u2                      num_annotations         <1.22.3>
    annotation<1.21.5>[]    annotations             <1.22.4>
}
```

 - [1.22.1] All rules from [1.6] apply.
 - [1.22.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.22.3] Appears in `attributes <1.1.16><1.4.5><1.5.5>`.
 - [1.22.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.22.1>` must be `RuntimeInvisibleAnnotations <!RUNTIME_INVISIBLE_ANNOTATIONS>`.
 - [1.22.5] There is at most one runtime invisible annotations attribute in each attributes table.
 - [1.22.6] Size of the `annotations <1.22.4>` is `num_annotations <1.22.3>`.

#### [1.23] `RuntimeVisibleParameterAnnotations` attribute

```
RuntimeVisibleParameterAnnotations {            <1.23.0>
    u2          attribute_name_index            <1.23.1>
    u4          attribute_length                <1.23.2>
    u1          num_parameter_annotations       <1.23.3>
    entry[]     parameter_annotations           <1.23.4>
}

entry {                                         <1.23.5>
    u2                      num_annotations     <1.23.6>
    annotation<1.21.5>[]    annotations         <1.23.7>
}
```

 - [1.23.1] All rules from [1.6] apply.
 - [1.23.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.23.3] Appears in `attributes <1.5.5>`.
 - [1.23.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.23.1>` must be `RuntimeVisibleParameterAnnotations <!RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS>`.
 - [1.23.5] Size of `parameter_annotations <1.23.4>` is `num_parameter_annotations <1.23.3>`.
 - [1.23.6] Size of `annotations <1.23.7>` is `num_annotations <1.23.6>`.
 - [1.23.7] There is at most one runtime visible parameter annotations attribute for each method.

#### [1.24] `RuntimeInvisibleParameterAnnotations` attribute

```
RuntimeInvisibleParameterAnnotations {          <1.24.0>
    u2          attribute_name_index            <1.24.1>
    u4          attribute_length                <1.24.2>
    u1          num_parameter_annotations       <1.24.3>
    entry[]     parameter_annotations           <1.24.4>
}

entry {                                         <1.24.5>
    u2                      num_annotations     <1.24.6>
    annotation<1.21.5>[]    annotations         <1.24.7>
}
```

 - [1.24.1] All rules from [1.6] apply.
 - [1.24.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.24.3] Appears in `attributes <1.5.5>`.
 - [1.24.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.24.1>` must be `RuntimeInvisibleParameterAnnotations <!RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS>`.
 - [1.24.5] Size of `parameter_annotations <1.23.4>` is `num_parameter_annotations <1.23.3>`.
 - [1.24.6] Size of `annotations <1.23.7>` is `num_annotations <1.23.6>`.
 - [1.24.7] There is at most one runtime invisible parameter annotations attribute for each method.

#### [1.25] `RuntimeVisibleTypeAnnotations` attribute

```
RuntimeVisibleTypeAnnotations {                         <1.25.51>
    u2                          attribute_name_index    <1.25.0>
    u4                          attribute_length        <1.25.1>
    u2                          num_annotations         <1.25.2>
    type_annotation<1.25.4>[]   annotations             <1.25.3>
}

type_annotation {                                       <1.25.4>
    target          target_info                         <1.25.5>
    type_path       target_path                         <1.25.6>
    u2              type_index                          <1.25.7>
    u2              num_element_value_pairs             <1.25.8>
    pair<1.21.9>[]  element_value_pairs                 <1.25.51>
}

type_path {                                             <1.25.9>
    u1                  path_length                     <1.25.10>
    entry<1.25.12>[]    path                            <1.25.11>
}

entry {                                                 <1.25.12>
    u1  type_path_kind                                  <1.25.13>
    u1  type_argument_index                             <1.25.14>
}

# target is a union of the following types
target:                                                 <1.25.52>

type_parameter_target {                                 <1.25.15>
    u1  target_type                                     <1.25.16>
    u1  type_parameter_index                            <1.25.17>
}

supertype_target {                                      <1.25.18>
    u1 target_type                                      <1.25.19>
    u2 supertype_index                                  <1.25.20>
}

type_parameter_bound_target {                           <1.25.21>
    u1  target_type                                     <1.25.22>
    u1  type_parameter_index                            <1.25.23>
    u1  bound_index                                     <1.25.24>
}

empty_target {                                          <1.25.25>
    u1  target_type                                     <1.25.26>
}

formal_parameter_target {                               <1.25.27>
    u1 target_type                                      <1.25.28>
    u1 formal_parameter_index                           <1.25.29>
}

throws_target {                                         <1.25.30>
    u1 target_type                                      <1.25.31>
    u2 throws_type_index                                <1.25.32>
}

localvar_target {                                       <1.25.33>
    u1      target_type                                 <1.25.34>
    u2      table_length                                <1.25.35>
    entry<1.25.37>[] table                              <1.25.36>
}

entry {                                                 <1.25.37>
    u2  start_pc                                        <1.25.38>
    u2  length                                          <1.25.39>
    u2  index                                           <1.25.40>
}

catch_target {                                          <1.25.41>
    u1  target_type                                     <1.25.42>
    u2  exception_table_index                           <1.25.43>
}

offset_target {                                         <1.25.44>
    u1 target_type                                      <1.25.45>
    u2 offset                                           <1.25.46>
}
    
type_argument_target {                                  <1.25.47>
    u1 target_type                                      <1.25.48>
    u2 offset                                           <1.25.49>
    u1 type_argument_index                              <1.25.50>
}



```

 - [1.25.1] All rules from [1.6] apply.
 - [1.25.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 52.0`.
 - [1.25.3] Appears in `attributes <1.1.16><1.4.5><1.5.5><1.8.10>`.
 - [1.25.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.25.1>` must be `RuntimeVisibleTypeAnnotations <!RUNTIME_VISIBLE_TYPE_ANNOTATIONS>`.
 - [1.25.5] There is at most one runtime visible type annotations attribute in each attribute table.
 - [1.25.6] Size of `annotations <1.25.3>` is `num_annotations <1.25.2>`.
 - [1.25.7] Size of `element_value_pairs <1.25.8>` is `num_element_value_pairs <1.25.8>`.
 - [1.25.8] Size of `path <1.25.11>` is `path_length <1.25.10>`.
 - [1.25.9] `type_index <1.21.6>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.25.10] `constant_pool <1.1.5>` entry at index `type_index <1.21.6>` must be of type `Utf8_info <1.3.34>`.
 - [1.25.11] `constant_pool <1.1.5>` entry at index `type_index <1.21.6>` must be a valid `FieldDescriptor <1.2.0>`.
 - [1.25.12] `type_path_kind <1.25.13>` is in range `0..3`.
 - [1.25.13] If `type_path_kind <1.25.13>` is in range `0..2`, then `type_argument_index <1.25.14>` is `0`.
 - [1.25.14] `target_type <1.25.16>` is either `0x00` or `0x01`.
 - [1.25.15] `target_type <1.25.19>` is either `0x10`.
 - [1.25.16] `target_type <1.25.22>` is either `0x11` or `0x12`.
 - [1.25.17] `target_type <1.25.26>` is in range `0x13..0x15`.
 - [1.25.18] `target_type <1.25.28>` is `0x16`.
 - [1.25.19] `target_type <1.25.31>` is `0x17`.
 - [1.25.20] `target_type <1.25.34>` is either `0x40` or `0x41`.
 - [1.25.21] `target_type <1.25.42>` is `0x42`.
 - [1.25.22] `target_type <1.25.45>` is in range `0x43..0x46`.
 - [1.25.23] `target_type <1.25.48>` is in range `0x47..0x4B`.
 - [1.25.24] If `target_type <1.25.16>` is `0x00`, this attribute appears in `attributes <1.1.16>`.
 - [1.25.25] If `target_type <1.25.16>` is `0x01`, this attribute appears in `attributes <1.5.5>`.
 - [1.25.26] If `target_info <1.25.5>` is `supertype_target <1.25.18>`, this attribute appears in `attributes <1.1.16>`.
 - [1.25.27] If `target_type <1.25.22>` is `0x11`, this attribute appears in `attributes <1.1.16>`.
 - [1.25.28] If `target_type <1.25.22>` is `0x12`, this attribute appears in `attributes <1.5.5>`.
 - [1.25.29] If `target_type <1.25.26>` is `0x13`, this attribute appears in `attributes <1.4.5>`.
 - [1.25.30] If `target_type <1.25.26>` is `0x14` or `0x15`, this attribute appears in `attributes <1.5.5>`.
 - [1.25.31] If `target_info <1.25.5>` is `formal_parameter_target <1.25.27>`, this attribute appears in `attributes <1.5.5>`.
 - [1.25.32] If `target_info <1.25.5>` is `throws_target <1.25.31>`, this attribute appears in `attributes <1.5.5.>`.
 - [1.25.33] If `target_info <1.25.5>` is `localvar_target <1.25.33>`, `catch_target <1.25.41>`, `offset_target <1.25.44>` or `type_argument_target <1.25.47>`, this attribute appears in `attributes <1.8.10>`.
 - [1.25.34] `throws_type_index <1.25.29>` is a valid index into the `exception_index_table <1.10.4>` of the enclosing method.
 - [1.25.35] `exception_table_index <1.25.43>` is a valid index into the `exception_table <1.8.8>` of the enclosing code attribute.
 - [1.25.36] `offset <1.25.46><1.25.49>` is a valid index into the `code <1.8.6>` array of the enclosing code attribute.
 - [1.25.37] Size of `table <1.25.36>` is `table_length <1.25.35>`.
 - [1.25.38] `[start_pc<1.25.38>, start_pc<1.25.38> + length<1.25.39>)` is a valid interval in the `code <1.8.6>` array of the enclosing code attribute.
 - [1.25.39] `index <1.25.40>` is a valid local variable index, i.e. `index <1.25.40> < max_locals <1.8.4>`.
  

#### [1.26] `RuntimeInvisibleTypeAnnotations` attribute
```
RuntimeInvisibleTypeAnnotations {                       <1.26.0>
    u2                          attribute_name_index    <1.26.1>
    u4                          attribute_length        <1.26.2>
    u2                          num_annotations         <1.26.3>
    type_annotation<1.25.4>[]   annotations             <1.26.4>
}
```

 - [1.26.1] All rules from [1.6] apply.
 - [1.26.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 52.0`.
 - [1.26.3] Appears in `attributes <1.1.16><1.4.5><1.5.5><1.8.10>`.
 - [1.26.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.26.1>` must be `RuntimeInvisibleTypeAnnotations <!RUNTIME_INVISIBLE_TYPE_ANNOTATIONS>`.
 - [1.26.5] There is at most one runtime invisible type annotations attribute in each attribute table.
 - [1.26.6] Size of `annotations <1.26.4>` is `num_annotations <1.26.3>`.
 

#### [1.27] `AnnotationDefault` attribute
```
AnnotationDefault {                                 <1.27.0>
    u2                      attribute_name_index    <1.27.1>
    u4                      attribute_length        <1.27.2>
    element_value<1.21.12>  default_value           <1.28.3>
}
```
 - [1.27.1] All rules from [1.6] apply.
 - [1.27.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.
 - [1.27.3] Appears in `attributes <1.5.5>`.
 - [1.27.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.27.1>` must be `AnnotationDefault <!ANNOTATION_DEFAULT>`.
 - [1.27.5] There can be at most one annotation default attribute for a corresponding method.
 - [1.27.6] OPTIONAL: Corresponding mmethod must be a part of an annotation type.

#### [1.28] `BootstrapMethods` attribute

```
BootstrapMethods {                          <1.28.0>
    u2              attribute_name_index    <1.28.1>
    u4              attribute_length        <1.28.2>
    u2              num_bootstrap_methods   <1.28.3>
    entry<1.28.5>[] bootstrap_methods       <1.28.4>
}

entry {                                 <1.28.5>
    u2      bootstrap_method_ref        <1.28.6>
    u2      num_bootstrap_arguments     <1.28.7>
    u2[]    bootstrap_arguments         <1.28.8>
}
```

 - [1.28.1] All rules from [1.6] apply.
 - [1.28.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 51.0`.
 - [1.28.3] Appears in `attributes <1.1.16>`.
 - [1.28.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.28.1>` must be `BootstrapMethods <!BOOTSTRAP_METHODS>`.
 - [1.28.5] If `constant_pool <1.1.5>` contains at least one `InvokeDynamic_info <1.3.45>` constant, then exactly one bootstrap methods attribute must be present for such class.
 - [1.28.6] There can be at most one bootstrap methods attribute for each class.
 - [1.28.7] Size of the `bootstrap_methods <1.28.4>` is `num_bootstrap_methods <1.28.3>`.
 - [1.28.8] `bootstrap_method_ref <1.28.6>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.28.9] `constant_pool <1.1.5>` entry at index `bootstrap_method_ref <1.28.6>` must be of type `MethodHandle_info <1.3.38>`.
 - [1.28.10] Size of `bootstrap_arguments <1.28.8>` is `num_bootstrap_arguments <1.28.7>`.
 - [1.28.11] Each `bootstrap_arguments<1.28.8>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.28.12] `constant_pool <1.1.5>` entry at each `bootstrap_arguments <1.28.8>` must one of `String_info <1.3.15>`, `Class_info <1.3.0>`, `Integer_info <1.3.18>`, `Long_info <1.3.24>`, `Float_info <1.3.21>`, `Double_info <1.3.27>`, `MethodHandle_info <1.3.38>`, `MethodType_info <1.3.42>`. 

#### [1.29] `MethodParameters` attribute

```
MethodParameters {                          <1.29.0>
    u2              attribute_name_index    <1.29.1>
    u4              attribute_length        <1.29.2>
    u1              parameters_count        <1.29.3>
    param<1.29.5>[] parameters              <1.29.4>
}

param {                             <1.29.5>
    u2  name_index                  <1.29.6>
    u2  access_flags                <1.29.7>
}
```

 - [1.29.1] All rules from [1.6] apply.
 - [1.29.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 52.0`.
 - [1.29.3] Appears in `attributes <1.5.5>`.
 - [1.29.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.29.1>` must be `MethodParameters <!METHOD_PARAMETERS>`.
 - [1.29.5] There can be at most one method parameters attribute for each method.
 - [1.29.6] OPTIONAL: `parameters_count <1.29.3>` must correspond to the corresponding `MethodDescriptor <1.2.5>`.
 - [1.29.7] Size of the `parameters <1.29.4>` is `parameters_count <1.29.3>`.
 - [1.29.8] `name_index <1.29.6>` is either `0` or a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.29.9] `contant_pool <1.1.5>` entry at index `name_index <1.29.6>` if not `0` must be of type `Uft8_info <1.3.34>`.
 - [1.29.10] `contant_pool <1.1.5>` entry at index `name_index <1.29.6>` if not `0` must be a valid unqualified name [1.0.5].
 - [1.29.11] `access_flags <1.29.7>` is a bit mask of:
    - `0x0010 <!FINAL>`
    - `0x1000 <!SYNTHETIC>`
    - `0x8000 <!MANDATED>`