package org.poo.visitors;

/**
 * Visitable classes should accept visitors.
 */
public interface Visitable {
    /**
     * Accepts a visitor and returns the data collected by it.
     * @param visitor
     * @return
     */
    String accept(Visitor visitor);
}
