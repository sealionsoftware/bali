---
layout: default
---

### Exceptions

Throwing an object as an exception is used to stop the execution of a program and return control to one of
the higher methods in the calling stack, often the top level of a script or a web framework error handler. 
You are basically saying "Something has gone wrong, I can't continue with the current processing".


#### Throwing Objects

Any object can be thrown by using the `throw` keyword, commonly strings are used to return messages on why processing
had to be stopped, or more complex objects can carry fields with typed data that may help the layer responsible for
error handling to recover from the problem.

```
throw "Could not find file"
```
{: .executable}

#### Catching Objects

Thrown objects can be caught using a `catch` block appended to a code block or a control statement like `if`, `while` 
or `for`. For a thrown object to be caught, it must be referenced in the catch block by an argument.

```
{
    throw "Could not find file"
} catch (Text error) {
    console << "An error was thrown"
}
```
{: .executable}

If the catch block argument is of the wrong type, if will not catch the thrown object.

```
{
    throw "Could not find file"
} catch (Logic error) {
    console << "An error was thrown"
}
```
{: .executable}

Catch blocks can be chained together

```
{
    throw "Could not find file"
} catch (Logic logicalError) {
    console << "A logical error was thrown"
} catch (Text textError) {
    console << "A text error was thrown"
}
```
{: .executable}

And subsequent catch blocks will catch objects thrown in previous blocks.

```
{
    throw true;
} catch (Logic logicalError) {
    throw "An error occurred while handling the error";
} catch (Text textError) {
    console << "A text error was thrown"
}
```
{: .executable}

