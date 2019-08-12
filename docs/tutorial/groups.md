---
layout: default
---

### Groups

A Group is an object that stores references to a group of other values.

#### Literals

You can can create a Group from variables or literals using square brackets, delimiting the values with commas.
For example: `[1, true, "Hello"]` gives you a group of three values, the number one, the logical value true, and the
text "Hello". An empty group would be constructed with the expression: `[]`.

#### Emptiness

You can test whether the group contains any values using the `isEmpty()` method.

```
["Hello", "World"].isEmpty()
```
{: .evaluable}

#### Size

The `size()` method returns an integer showing the number of objects in the group

```
["Hello", "World"].size()
```
{: .evaluable}

#### Get

The `#` operator accesses the object at a specified place in the group

```
["Hello", "World"] # 2
```
{: .evaluable}

#### Iterating

The `iterator()` method returns an object which is used to navigate through the group by accessing each item in turn
using the methods `next()` and `hasNext()`

```
var Iterator i  = ["Hello", "World"].iterator()
var currentItem = i.next()
var hasNext = i.hasNext()
```
{: .executable}

#### Typed Groups

Groups can be restricted to contain only objects of a certain type. Incompatible objects will cause errors when
used as arguments to a literal expression that is being assigned to a Group variable or parameter.

```
var Group[Text] helloWorldWords = ["Hello", "World"]
console << helloWorldWords # 1
console << helloWorldWords # 2
```
{: .executable}
```
var Group[Text] words = ["Hello", 1]
```
{: .executable}

When the group is typed, the return values of the `#` operation, and the `next()` method of the iterator will return
values of the group type so they can be assigned to typed variables and have their type specific methods invoked

```
var Iterator[Text] i  = ["Hello", "World"].iterator()
console << i.next()
console << i.next().uppercase()
```
{: .executable}
