package com.jrlcst.couponapi.shared.infraestructure.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * A {@link TaskDecorator} implementation for propagating MDC (Mapped Diagnostic Context)
 * information to asynchronous tasks.
 *
 * <p>This ensures that logging context (such as request IDs or user identifiers) is preserved
 * across different threads when executing tasks in an async executor.
 */
public class MDCTaskDecorator implements TaskDecorator {

  /**
   * Captures the current MDC context and applies it to the decorated task. Retrieves a copy of the
   * current MDC context. - Wraps the provided {@link Runnable} so that the MDC context is restored
   * before execution and cleared afterward.
   *
   * @param runnable The original task to be executed.
   * @return A decorated {@link Runnable} that maintains the MDC context.
   */
  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> contextMap = MDC.getCopyOfContextMap();
    return () -> {
      try {
        MDC.setContextMap(contextMap);
        runnable.run();
      } finally {
        MDC.clear();
      }
    };
  }
}
