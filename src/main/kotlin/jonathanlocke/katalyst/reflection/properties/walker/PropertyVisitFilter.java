package jonathanlocke.katalyst.reflection.properties.walker;

import jonathanlocke.katalyst.reflection.properties.Property;

import java.util.function.Predicate;

public interface PropertyVisitFilter extends Predicate<Property<?>>
{
}
