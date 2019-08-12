---
layout: default
---

### Variables

Variables are used to hold references to values and objects during your programs, while they are being worked on.

#### Declaring a new Variable

Variables are declared using the `var` keyword, and are usually assigned a value as part of the statement.

```
var greeting = "Hello World"
```
{: .executable}

#### Typed Variables

A variable is not very useful unless you know the type of the object it refers to, so that you can use the operations
and methods of the object on the variable reference. You can add a type constraint to the variable by adding the type
name after the `var` keyword.

```
var Text greeting = "Hello World"
console << greeting
```
{: .executable}

The compiler will prevent programs from assigning an expression of the wrong type to a typed variable.

```
var Text greeting = 123
```
{: .executable}

#### Optional Variables

Most variables are required to hold a value that can be used by operations, however sometimes it may be useful to hold
a value which may not exist. An example would be the result of a search that has not found a value. An optional
variable is declared by adding the `?` modifier to the type constraint, and it does not need to have a value assigned to
it when it is declared.

```
var Text? maybeAGreeting
```
{: .executable}
Because an optional variable may not have a value assigned to it, the compiler will return an error if a program
invokes a method on it without first checking if it exists.

```
var Text? maybeAGreeting
var size = maybeAGreeting.size()
```
{: .executable}