---
layout: default
---

### Numeric Values

Variables with a Number type are used to store quantity information and perform mathematical computations.

#### Literals

Integer numbers can be entered directly into source code as regular base 10 literal values, e.g. `7` or `152`.

#### Negation

Any number can be turned into its negative by prefixing it with the `-` operator.

```
-(637)
```
{: .evaluable }

#### Modulus

You can find the positive value of a Number expression by prefixing it with the modulus operator.

```
| -45
```
{: .evaluable }

#### Addition

Numbers can be added together using the `+` operator.

```
637 + 45
```
{: .evaluable }

#### Subtraction

Similarly, you can subtract one number from another using the `-` operator.

```
637 - 45
```
{: .evaluable }

#### Multiplication

Similarly, you can subtract one number from another using the `*` operator.

```
637 * 45
```
{: .evaluable }

#### Division

And of course, divide one number by another using the `/` operator.

```
637 / 45
```
{: .evaluable}

#### Equality

Like the Logic type, the Number type is a type of Value. A Number can be compared to another Number
expression with `==` to check if they represent the same value.

```
637 == 45
```
{: .evaluable}

#### Inequality

The Number type has the `!=` operator that all Values declare.

```
637 != 45
```
{: .evaluable}

#### Greater Than

Numbers are also a type of Quantified value, which means that they have a `>` operator which checks if they are
bigger than another Number.

```
637 > 45
```
{: .evaluable}

#### Less Than

The Number type also has a '<' operator which checks if it is smaller than another number.

```
637 < 45
```
{: .evaluable}