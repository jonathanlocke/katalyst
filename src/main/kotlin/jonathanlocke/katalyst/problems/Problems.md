
# Problems

## Use Patterns for ProblemHandler

There are two ways for an object to handle problems. The first is by accepting a 
ProblemHandler in its constructor and then using it to handle problems. The second
is for an object to implement the ProblemHandlerMixin, which provides an implementation
of ProblemHandler.

### 1. Construct with a ProblemHandler

Accepting a ProblemHandler looks like this:

```kotlin
class Rocket(problemHandler: ProblemHandler) {
    fun launch() {
        if (!startEngine()) {
            problemHandler.error("Couldn't blast off") 
        }
    }
}
```

Use of Rocket then looks like this:

```kotlin
class Launchpad(problemHandler: ProblemHandler) {
    fun launchRocket() {
        val rocket = Rocket(problemHandler)
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

