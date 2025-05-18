
# Problems

## Use Patterns for ProblemHandler

There are two ways for an object to handle problems. The first is by accepting a 
ProblemHandler in its constructor and then using it to handle problems. The second
is for an object to implement the ProblemHandlerMixin, which provides an implementation
of ProblemHandler.

### 1. Construct with a ProblemHandler

Accepting a ProblemHandler looks like this:

```kotlin
class Rocket(problemHandler: ProblemHandler) : ProblemHandler by problemHandler {
    fun launch() {
        if (!startEngine()) {
            error("Couldn't blast off") 
        }
    }
}
```

Use of Rocket then looks like this:

```kotlin
class Launchpad(problemHandler: ProblemHandler) : ProblemHandler by problemHandler {
    fun launchRocket() {
        val rocket = Rocket(this)
        rocket.launch()
    }
}
```

**Pros**: It's a compile time error to fail to handle errors  
**Cons**: Code is more verbose than using ProblemHandlerMixin

### 2. Implement ProblemHandlerMixin

Rocket could also look like this:

```kotlin
class Rocket : ProblemHandlerMixin {
    fun launch() {
        if (!startEngine()) {
            error("Couldn't blast off")        
        }
    }
}
```

Use of Rocket then looks like this:

```kotlin
class Launchpad : ProblemHandlerMixin {
    fun launchRocket() {
        val rocket = handleProblemsFrom(Rocket())
        rocket.launch()
    }
}
```

**Pros**: Cleaner, more concise code  
**Cons**: Failure to handle problems results in a runtime warning

If Rocket calls error() and the client that created it has not attached a ProblemHandler 
to the Rocket, a runtime error will result due to this code similar to this:

```kotlin
    fun handlers(): MutableList<ProblemHandler>

    override fun handle(problem: Problem) {

        // If there are no handlers,
        if (handlers().isEmpty()) {

            // log a warning because the problem will be lost.
            defaultLoggerFactory.newLogger().warning("Unhandled problem: $problem")
        }
    }
```

## How to Design for ProblemHandler

To allow a method to accept either a ReturnOnError handler or a ThrowOnError handler, it is
necessary to catch exceptions and perform the proper error handling. Two methods in ProblemHandler
make this easy for the two primary cases: (1) the method returns a value or null on failure and
(2) the method returns a boolean value signifying success or failure.

### 1. tryBoolean

In the case of a Boolean return value, the tryBoolean method can be used like this:

```kotlin
    override fun exists() = tryBoolean("File does not exist: $location") {
        Files.exists(location.path)
    }
```

If File.exists() throws an exception this method will return false, *unless* a problem handler attached 
to this object is ThrowOnError, in which case it will trap the exception information and rethrow a 
ProblemException. The message is optional and can be used to capture context information about the problem.

### 2. tryValue

In the case of an optional (nullable) return value, the tryValue method can be used:

```kotlin
    override fun loadSpaceship(): Spaceship? = tryValue() {
        return database.loadSpaceshp(id)
    }
```

Here, if the database is unable to load the spaceship by id, any exceptions will be caught and either
null will be returned (in the case of ReturnOnError) or an exception will be thrown (in the case of
ThrowOnError)

