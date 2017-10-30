
package com.capestart.util.persistence;

import com.capestart.util.data.Either;
import com.capestart.util.data.Function;

import javax.persistence.EntityManager;

/**
 * Persistence environment that handles errors with an either instead of throwing exceptions.
 *
 * @see PersistenceEnv
 */
public interface PersistenceEnv2<F> {
  /** Run code inside a transaction. */
  <A> Either<F, A> tx(Function<EntityManager, A> transactional);

  /** Close the environment and free all associated resources. */
  void close();
}
