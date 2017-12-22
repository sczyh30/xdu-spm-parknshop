package io.spm.parknshop.common.state;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple state machine.
 *
 * @author Eric Zhao
 */
public class StateMachine {

  private final List<StateTransformer> transformers;
  private final int defaultState;

  private StateMachine(List<StateTransformer> transformers, int defaultState) {
    this.transformers = transformers;
    this.defaultState = defaultState;
  }

  public static StateMachineBuilder builder() {
    return new StateMachineBuilder();
  }

  public int transform(int state, int event) {
    for (StateTransformer transformer: transformers) {
      if (transformer.accept(state, event)) {
        return transformer.state();
      }
    }
    return defaultState;
  }

  public static class StateMachineBuilder {

    private List<StateTransformer> list = new ArrayList<>();
    private int defaultState = 0;

    public StateMachineBuilder addTransformer(int currentState, int event, int nextState) {
      StateTransformer transformer = new StateTransformer(currentState, event, nextState);
      list.add(transformer);
      return this;
    }

    public StateMachineBuilder withDefaultState(int d) {
      this.defaultState = d;
      return this;
    }

    public StateMachine build() {
      return new StateMachine(list, defaultState);
    }

    private StateMachineBuilder() {}
  }

  private static class StateTransformer {
    private int s;
    private int e;
    private int r;

    StateTransformer(int s, int e, int r) {
      this.s = s;
      this.e = e;
      this.r = r;
    }

    boolean accept(int state, int event) {
      return this.s == state && this.e == event;
    }

    int state() {
      return r;
    }
  }
}
