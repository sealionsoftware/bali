---
layout: default
---

### Logical Values

A logical value is one of the truth values of true or false. As well as being the most basic way to store data, they are
important because Logic variables and expressions are used to control the flow of a computer program.

#### Literals

You can specify or pass a value for a logic variable with the keywords `true` and `false`.

#### Negation

You can transform an expression that yields a logic value into its negative using the `!` operator.

```
!true
```
{: .evaluable}

#### Conjunction

You can ask whether two expressions are both true using the `&` operator.

```
true & false
```
{: .evaluable}

#### Disjunction

You can ask whether either of two expressions are true using the `|` operator.

```
true | false
```
{: .evaluable}

#### Equality

The Logic type is a type of Value. As such a logic expression can be compared to another expression to see if they are
the same truth value.

```
true == false
```
{: .evaluable}

#### Inequality

Also as a consequence of being a Value, a logic expression can be compared to test if they are not the same truth value.

```
true != false
```
{: .evaluable}


#### Combining Operations

As with any operations, you can build up more complex expressions by chaining operations together

```
(true & true) != !(true | false)
```
{: .evaluable}