---
layout: default
---

### Objects

Bali is an "Object Oriented" language. That means that the program is designed to store its data in small related groups
and that all the program's code is defined in functions that operate on this data called 'methods'. We say that the program is 
composed of Objects, that these Objects store data in their 'fields', and that objects communicate with each other by 
calling their methods. 

Each Object represents a concept within the program, encapsulating the data related to that concept so that the methods 
enforce rules about how that data can be used. There are lots of different types of concept that can usefully be 
represented as an object. They range from the simplest that represent a single value and the operations that can be 
performed upon it, to large complex objects that represent configured program components.

Here are some common examples:

- The number 7
- A list of text snippets: ['The', 'quick', 'brown', 'fox'] 
- A url: 'http://www.sealionsoftware.com'
- A connection to a database: connection.open()
- A record from a database: user.getFirstName()
- A request to a web application: request.getURLPath()
- A component that handles a web request: controller.handle(Request request);
- An automated test of a program component ControllerTest.testGetUser(controller)

In Bali and some other Object oriented programming languages, Objects have a type that defines which data they can hold, 
and what can be done with it. Ideally types are general enough so that they can be reused over and over again for  
different objects. For example there might be lots of objects of type 'URL' in a program.

By composing a program out of objects, we can organise the programs' data through its relationships and
help control the complexity of the program by building up its internal structure. Without objects, our programs would have 
thousands of variables and functions all in the same scope, with no internal rules about who could do what. 

#### Invoking Methods

Methods are invoked by appending a period, the name of the method and a set of parentheses to an object. In the following
statement, the method 'size' is invoked on the Text "Hello World". The method contains code that converts the characters 
to uppercase and returns the new Text;

```
"Hello World".uppercase()
```
{: .evaluable}

Methods can also be invoked on variables that reference the object.

```
var greeting = "Hello World";
greeting.uppercase()
```
{: .executable}

#### Parameters

When a method is invoked, it uses the data that is held by the object to compute a return value. Some methods require 
additional data to be passed in the parentheses to compute the value. We say the the method has a parameter. In the 
following statement, the method 'head' is invoked on the Text "Hello World", with the number 5 supplied as the parameter. 
The head method will return the first 5 characters as a new Text object.

```
"Hello World".head(5);
```
{: .evaluable}

#### Method Chaining

When a value is returned by a method, other methods can be invoked upon the value. This is called method chaining. In 
the following statement, the methods tail and uppercase are chained together.

```
"Hello World"
    .head(5)
    .uppercase();
```
{: .evaluable}