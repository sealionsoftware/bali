---
layout: default
---

### Conditional Loops

The conditional loop statement is used to execute one or more statements repeatedly while a given condition is true.

#### Declaring A Conditional Loop

A conditional loop is declared using the `while` keyword. The condition expression must be provided as an argument inside
parenthesis, and the conditionally executed statements are provided as a code block, or another control statement.

```
var Integer i = 0
while (i < 3) {
	console << "condition was met"
	i = ++i
}
```
{: .executable}

### Iterative Loops

The iterative loop statement is used to execute one or more statements for each item in a Group.

#### Declaring An Iterative Loop

An iterative loop is declared using the `for` keyword, the name for the current item of the iteration must be supplied
as the first argument, and an expression which yeilds a Group to iterate over must be provided as a second argument,
and the statements to execute for each item are provided as a code block, or another control statement.

```
for (word : ["one", "two", "three"]) {
	console << word
}
```
{: .executable}