package com.jrlcst.couponapi.shared.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration class for customizing the {@link ThreadPoolTaskExecutor} with MDC support.
 *
 * <p>This configuration ensures that the logging context (MDC) is propagated across threads in the
 * thread pool by using {@link MDCTaskDecorator}.
 */
@Configuration
public class MDCThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

  /**
   * Creates and configures a {@link ThreadPoolTaskExecutor} with an MDC-aware task decorator.
   *
   * <p>- Uses {@link MDCTaskDecorator} to ensure logging context propagation in async tasks. -
   * Calls {@code initialize()} to properly set up the thread pool before use.
   *
   * @return A configured instance of {@link ThreadPoolTaskExecutor}.
   */
  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setTaskDecorator(new MDCTaskDecorator());
    executor.initialize();
    return executor;
  }
}
