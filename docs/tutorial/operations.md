---
layout: default
---

### Operations

An operation is an expression composed of a target object and a symbol (the "operator") that runs a method of the target
and yields a result. In this respect they are like a method call, except the use of a symbol as a name makes
them more concise, so they are usually used for very frequently called methods.

#### Unary Operators

An operator which does not have a parameter is called an unary operator, in an unary operation the symbol
preceeds the target expression. In the example, the negation operator '!' is applied to the value `true`.

```
!true
```
{: .evaluable}

#### Binary Operators

An operator which has one parameter is called an binary operator, in a binary operation the symbol follows
the target expression, and is followed by the argument that is to be passed. In the example expression, the
addition operator is applied to the value `1` with the argument of `1`.

```
1 + 1
```
{: .evaluable}

#### Existence Operator

There is a special unary operator `?` which can be used to check for the existence of an expression that
may not produce a value. The example expressions access the characters at positions 1 and then 2 of the
Text "H".

```
?("H"#1)
```
{: .evaluable}

```
?("H"#2)
```
{: .evaluable}
