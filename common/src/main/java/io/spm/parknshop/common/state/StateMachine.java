package io.spm.parknshop.common.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A simple state machine.
 *
 * @author Eric Zhao
 */
public class StateMachine implements Transformer<Integer, Integer> {

  private final List<StateTransformer> transformers;
  private final MismatchStrategy<Integer> mismatchStrategy;

  private StateMachine(List<StateTransformer> transformers, MismatchStrategy<Integer> mismatchStrategy) {
    this.transformers = transformers;
    this.mismatchStrategy = mismatchStrategy;
  }

  public static StateMachineBuilder builder() {
    return new StateMachineBuilder();
  }

  @Override
  public Integer transform(Integer state, Integer event) {
    Objects.requireNonNull(state);
    Objects.requireNonNull(event);
    for (StateTransformer transformer : transformers) {
      if (transformer.accept(state, event)) {
        return transformer.state();
      }
    }
    return mismatchStrategy.handleMismatchState();
  }

  public static class StateMachineBuilder {

    private final List<StateTransformer> list = new ArrayList<>();
    private int defaultState = 0;
    private StateMismatchStrategy stateMismatchStrategy = StateMismatchStrategy.DEFAULT_STATE;

    private Supplier<RuntimeException> exceptionSupplier;

    public StateMachineBuilder addTransformer(int currentState, int event, int nextState) {
      StateTransformer transformer = new StateTransformer(currentState, event, nextState);
      list.add(transformer);
      return this;
    }

    public StateMachineBuilder withDefaultState(int d) {
      this.defaultState = d;
      return this;
    }

    public StateMachineBuilder withStateMismatchStrategy(StateMismatchStrategy stateMismatchStrategy) {
      this.stateMismatchStrategy = stateMismatchStrategy;
      return this;
    }

    public StateMachineBuilder onMismatchThrow(Supplier<RuntimeException> exceptionSupplier) {
      this.exceptionSupplier = exceptionSupplier;
      return this;
    }

    public StateMachine build() {
      MismatchStrategy<Integer> strategy;
      switch (stateMismatchStrategy) {
        case DEFAULT_STATE:
          strategy = new DefaultStateMismatchStrategy(defaultState);
          break;
        case THROW_EXCEPTION:
          if (Objects.isNull(exceptionSupplier)) {
            exceptionSupplier = () -> new IllegalStateException("This transform flow does not match any state");
          }
          strategy = new ExceptionThrowMismatchStrategy(exceptionSupplier);
          break;
        default:
          throw new IllegalStateException("Unknown strategy");
      }
      return new StateMachine(list, strategy);
    }

    private StateMachineBuilder() {
    }
  }

  private interface MismatchStrategy<S> {
    S handleMismatchState();
  }

  private static class DefaultStateMismatchStrategy implements MismatchStrategy<Integer> {

    private final int defaultValue;

    private DefaultStateMismatchStrategy(int defaultValue) {
      this.defaultValue = defaultValue;
    }

    @Override
    public Integer handleMismatchState() {
      return defaultValue;
    }
  }

  private static class ExceptionThrowMismatchStrategy implements MismatchStrategy<Integer> {

    private final Supplier<RuntimeException> exceptionSupplier;

    private ExceptionThrowMismatchStrategy(Supplier<RuntimeException> exceptionSupplier) {
      this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public Integer handleMismatchState() {
      throw exceptionSupplier.get();
    }
  }

  private static class StateTransformer {
    private final int s;
    private final int e;
    private final int r;

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
