---
layout: default
---

### Conditional Statements

The conditional statement is used to execute one or more statements only when a given condition is true.

#### Declaring Conditional Statements

A conditional statement is declared using the `if` keyword, the condition expression is provided as an argument inside
parenthesis, and the conditionally executed statements are provided as a code block, or another control statement.

```
if (true) {
	console << "condition was met"
}
```
{: .executable}

#### Contra-Conditional blocks

It may be desirable to have a set of statements executed if the condition is false, this can be specified using the
`else` keyword

```
if (false) {
	console << "condition was met"
} else {
	console << "condition was not met"
}
```
{: .executable}

#### Using Optional Variables

Variables with an optional type must be tested using the existence operator before they can be used.

```
var Text? output = "Hello"
if (?output) {
	console << output.uppercase()
}
```
{: .executable}