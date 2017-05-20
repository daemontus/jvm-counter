 - [a.b.c] validation rule
 - `<a.b.c>` data and grammar item
 - `<!NAME>` constant item
 - only rules for single file validation go into this file

#### [1.0] `class` file format
 - [1.0.1] `class` file contains definition of exactly one interface or class.
 - [1.0.2] `class` file consists of a stream of 8-bit values.
 - [1.0.3] 16, 32 and 64-bit data types are stored in big-endian order. 
 - [1.0.4] `x` is a valid index into the `constant_pool <1.1.5>` iff `x > 0 && x < constant_pool_count <1.1.4>` and if `x-1 > 0`, the type of `constant_pool <1.1.5>` entry at index `x-1` is not `Double_info <TODO>` or `Long_info <TODO>`.
 - [1.0.5] `x` is an unqualified name iff `size(x) > 0` and `('.'|';'|'['|'/') !in x`
 - [1.0.6] `x` is a method name iff it is an unqualified name [1.0.5] and (`('<'|'>') !in x` or `x in ("<init>", "<clinit>")`)
 - [1.0.7] `x` is a binary (fully qualified) name iff `size(x.split('/')) > 0` and `x.split('/').all { it is unqualified name [1.0.5] }` 
 
#### [1.1] `class` data object

```
ClassFile {                                 <1.1.0>
    u4                  magic               <1.1.1>
    u2                  minor_version       <1.1.2>
    u2                  major_version       <1.1.3>
    u2                  constant_pool_count <1.1.4>
    cp_info[]           constant_pool       <1.1.5>
    u2                  access_flags        <1.1.6>
    u2                  this_class          <1.1.7>
    u2                  super_class         <1.1.8>
    u2                  interfaces_count    <1.1.9>
    u2[]                interfaces          <1.1.10>
    u2                  fields_count        <1.1.11>
    field_info[]        fields              <1.1.12>
    u2                  methods_count       <1.1.13>
    method_info[]       methods             <1.1.14>
    u2                  attributes_count    <1.1.15>
    attribute_info[]    attributes          <1.1.16>
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
 - [1.1.9] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `<!ABSTRACT>` must be set.
 - [1.1.10] `access_flags <1.1.6>` must *not* have both `<!ABSTRACT>` and `<!FINAL>` set.
 - [1.1.11] `this_class <1.1.7>` must be a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.1.12] `constant_pool <1.1.5>` entry at index `this_class <1.1.7>` must by of type `Class_info <TODO>`.
 - [1.1.13] `super_class <1.1.8>` is either `0`, or a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.1.14] `constant_pool <1.1.5>` entry at index `super_class <1.1.8>` (if non-zero), must be of type `Class_info <TODO>`.
 - [1.1.15] If `super_class <1.1.8>` is `0`, `constant_pool <1.1.5>` entry at index `this_class <1.1.7>` represents the `java.lang.Object` class.
 - [1.1.16] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `super_class <1.1.8>` is not `0`.
 - [1.1.17] If `access_flags <1.1.6>` has `<!INTERFACE>` set, `constant_pool <1.1.5>` entry at index `super_class <1.1.8>` represents the `java.lang.Object` class.
 - [1.1.18] `interfaces <1.1.10>` has size `interfaces_count <1.1.9>`.
 - [1.1.19] Each entry in `interfaces <1.1.10>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.
 - [1.1.20] forall `i in 0..interfaces_count - 1`, the `constant_pool <1.1.5>` entry at index `interfaces[i] <1.1.10>` must be of type `Class_info <TODO>`.
 - [1.1.21] `fields <1.1.12>` has size `fileds_count <1.1.11>`.
 - [1.1.22] `methods <1.1.14>` has size `methods_count <1.1.14>`.
 - [1.1.23] `attributes <1.1.16>` has size `attributes_count <1.1.15>`.

#### [1.2] `FieldDescriptor` and `MethodDescriptor` grammar

```
FieldDescriptor : FieldType                     <1.2.0>
FieldType : BaseType | ObjectType | ArrayType   <1.2.1>
BaseType : B | C | D | F | I | J | S | Z        <1.2.2>
ObjectType : L binary_name[1.0.7] ;             <1.2.3>
ArrayType : [ FieldType                         <1.2.4>

MethodDescriptor : ( Parameter* ) Return        <1.2.5>
Parameter : FieldType<1.2.1>                    <1.2.6>
Return : FieldType<1.2.1> | V                   <1.2.7>
```

 - `ArrayType <1.2.4>` must have fewer than `256` dimensions (number of `[`).
 - The parameter list in `MethodDescriptor <1.2.5>` must have size less than `256` where size includes `this` (for non-static methods) and parameters of type `long` and `double` count as two positions. 

#### [1.3] `cp_info` data format

```
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
 - [1.3.16] `constant_pool <1.1.5>` entry at index `name_index <1.3.2><1.3.32>` must be of type `Utf8_info <TODO>` .
 - [1.3.17] `constant_pool <1.1.5>` entry at index `name_index <1.3.2>` must represent a fully quantified binary name [1.0.7] or an `ArrayType <1.2.4>`. 
 - [1.3.18] `constant_pool <1.1.5>` entry at index `name_index <1.3.32>` must represent a valid unqualified name [1.0.5].
 - [1.3.19] `class_index <1.3.5><1.3.9><1.3.13>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.20] `constant_pool <1.1.5>` entry at index `class_index <1.3.5><1.3.9><1.3.13>` must be of type `Class_info <1.3.0>`.
 - [1.3.21] `name_and_type_index <1.3.6><1.3.10><1.3.14><1.3.48>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.22] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.6><1.3.10><1.3.14><1.3.48>` must be of type `NameAndType_info <TODO>`.
 - [1.3.23] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.6>` must have a `FieldDescriptor <1.2.0>` as type.
 - [1.3.24] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.10><1.3.14><1.3.48>` must have a `MethodDescriptor <1.2.5>` as type.
 - [1.3.25] `string_index <1.3.17>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.26] `constant_pool <1.1.5>` entry at `string_index <1.3.17>` must be of type `Utf8_info <TODO>`.
 - [1.3.27] `descriptor_index <1.3.33><1.3.44>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.28] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.33><1.3.44>` must be of type `Utf8_info <TODO>`.
 - [1.3.29] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.33>` must represent a valid `FieldDescriptor <1.2.0>` or `MethodDescriptor <1.2.5>`.
 - [1.3.30] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.44>` must represent a valid `MethodDescriptor <1.2.5>`.
 - [1.3.31] `bytes <1.3.37>` is of size `length <1.3.36>`.
 - [1.3.32] `bytes <1.3.37` represent a *modified UTF-8 string* (definition needed).
 - [1.3.33] `reference_kind <1.3.40>` must be in range `1..9`.
 - [1.3.34] `reference_index <1.3.41>` must be a valid index [1.0.4] into `constant_pool <1.1.5>`.
 - [1.3.35] If `reference_kind <1.3.40>` is in range `1..4`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Fieldref_info <TODO>`.
 - [1.3.36] If `reference_kind <1.3.40>` is `5` or `8`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Methodref_info <TODO>`.
 - [1.3.37] If `reference_kind <1.3.40>` is `6` or `7` and `major_version <1.1.2>` is less than `52`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Methodref_info <TODO>`. If `major_version <1.1.2> >= 52`, it can also be `InterfaceMethodref_info <TODO>`.
 - [1.3.38] If `reference_kind <1.3.40>` is `9`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `InterfaceMethodref_info <TODO>`.
 - [1.3.39] If `reference_kind <1.3.40>` is in `[5,6,7,9]`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must not have a name `<init>` or `<clinit>`.
 - [1.3.40] If `reference_kind <1.3.40>` is `8`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must have a name `<init>`.
 - [1.3.41] `bootstrap_method_attr_index <1.3.47>` is a valid index into the `bootstrap_methods <TODO>`.
 