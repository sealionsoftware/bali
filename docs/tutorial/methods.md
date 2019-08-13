---
layout: default
---

### Methods

An method is a named fragment of code attached to an object. When the method is called, the code is executed. The code
can use the data held by the object. It may also change the internal data of the object, and it may return a value to 
the caller. 

#### Calling Methods

Methods are called by appending a period, the name of the method and a set of parentheses to an object. In the following
expression, the method 'size' is called on the Text "Hello World". The method contains code that converts the characters 
to uppercase and returns the new Text;

```
"Hello World".uppercase()
```
{: .evaluable}

Methods can also be called on variables that reference an object.

```
var Text greeting = "Hello World"
console << greeting.uppercase()
```
{: .executable}

#### Parameters

When a method is called, it may use the data that is held by its object to compute a return value. Some methods require 
additional data to be passed in the parentheses to compute the value. We say the the method has a parameter. In the 
following statement, the method 'head' is invoked on the Text "Hello World", with the number 5 supplied as the parameter. 
The head method will return the first 5 characters as a new Text object.

```
"Hello World".head(5)
```
{: .evaluable}

#### Method Chaining

When a value is returned by a method, other methods can be called upon the return value. This is called method chaining. 
In the following statement, the methods tail and uppercase are chained together.

```
"Hello World"
    .uppercase()
    .head(5)
```
{: .evaluable}