---
layout: default
---

### Text Values

Text values are sequences of characters used to construct and store messages to the user, for storing information in
human readable files and often for passing messages between different computer programs.

#### Literals

A text object can easily be defined using double quotes, `"Hello World"`

#### Upper Case

The uppercase version of any text value can be obtained using the `uppercase()` method, this is often useful for
comparing strings for equality independent of their case

```
"Hello World".uppercase()
```
{: .evaluable }

#### Equality

Like Logic and Number, Text is a kind of Value. A Text can be compared to another Text
expression with `==` to check if they represent the same value.

```
"Hello" == "Goodbye"
```
{: .evaluable}

#### Inequality

The Text type has the `!=` to operator that all Values declare.

```
"Hello" != "Goodbye"
```
{: .evaluable}

#### Characters

Unlike Logic and Number, Text is made up of a sequence of more basic values of type Character. It is a kind of Group
and implements all the Group methods.

```
"Hello".isEmpty()
```
{: .evaluable}
```
"Hello".size()
```
{: .evaluable}
```
"Hello"#3
```
{: .evaluable}
