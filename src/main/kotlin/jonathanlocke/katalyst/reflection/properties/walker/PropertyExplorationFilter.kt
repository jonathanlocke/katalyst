package jonathanlocke.katalyst.reflection.properties.walker;

import jonathanlocke.katalyst.reflection.properties.Property;

import java.util.function.Predicate;

public interface PropertyExplorationFilter extends Predicate<Property<?>>
{
    default PropertyExplorationFilter and(PropertyExplorationFilter that) {
        return property -> test(property) && that.test(property);
    }
}
