package com.capestart.util.data;

/**
 * Element factory.
 *
 * @param <A>
 *         the type of the element
 */
public interface Creator<A> {
  A create();
}

