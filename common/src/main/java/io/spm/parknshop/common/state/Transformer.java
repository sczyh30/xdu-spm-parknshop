package io.spm.parknshop.common.state;

/**
 * Transformer interface.
 *
 * @author Eric Zhao
 */
public interface Transformer<S, E> {

  /**
   * Transform to next state from current state via provided event.
   *
   * @param curState current state
   * @param event current event
   * @return next state
   */
  S transform(S curState, E event);
}
