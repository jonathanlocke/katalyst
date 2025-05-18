
# Problems

## Use Patterns

There are two ways for an object to handle problems. The first is by accepting a 
ProblemHandler in its constructor and then using it to handle problems. The second
is for an object to implement ProblemSource (which extends ProblemHandler), 
typically by implementing the ProblemSourceMixin.

### 1. Pass ProblemHandler to constructor

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

The advantage of this pattern is that it's not possible to create a Rocket object
without being aware that it handles problems through the ProblemHandler interface.
Failing to pass in a ProblemHandler to the constructor will result in a compile time
error. The downside is that it's slightly more verbose than the second option.

### 2. Implement ProblemHandler

Rocket could also look like this:

```kotlin
class Rocket : ProblemSourceMixin {
    
    fun launch() {
        if (!startEngine()) {
            error("Couldn't blast off")        
        }
    }
}
```

Use of Rocket then looks like this:

```kotlin
class Launchpad : ProblemSourceMixin {
    
    fun launchRocket() {
        val rocket = handleProblemsFrom(Rocket())
        rocket.launch()
    }
}
```

The advantage is that there's less code involved. The result is cleaner. The downside
is that it's possible to create a Rocket and use it without capturing the problems 
that it is transmitting. If Rocket calls error() and the client that created it has
not attached a ProblemHandler to the Rocket, a runtime error will result due to 
this code similar to this in ProblemSource:

```kotlin
    fun handlers(): MutableList<ProblemHandler>

    override fun handle(problem: Problem) {
        if (handlers().isEmpty()) {
            logger.warning("Unhandled problem: $problem")
        }
        super.handle(problem)
    }
```

Any time the list of handlers is empty and an attempt is made to handle a problem,
the attempt will be logged as a warning. The problem is not trivial because there 
may be problems that are rarely transmitted by a ProblemSource. This might not be 
caught without adequate testing.
