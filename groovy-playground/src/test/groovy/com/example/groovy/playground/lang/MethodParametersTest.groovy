package com.example.groovy.playground.lang

import groovy.util.logging.Slf4j
import org.junit.jupiter.api.Test

import java.util.function.Consumer
import java.util.function.Function

import static org.assertj.core.api.Assertions.assertThat

@Slf4j
class MethodParametersTest {

  class TestBean {

    String defaultParams(String target = "world") {
      "Hello ${target}!"
    }

    String optional(String target) {
      "Hello ${target ?: 'default'}!"
    }

  }

  @Test
  void test() {
    def tester = new TestBean()
    Consumer<Object> logIt = v -> log.debug("{}", v)
    assertThat(tester.defaultParams())
        .satisfies(logIt)
        .isEqualTo("Hello world!")
    assertThat(tester.optional())
        .satisfies(logIt)
        .isEqualTo("Hello default!")
    assertThat(tester.optional('opt'))
        .satisfies(logIt)
        .isEqualTo("Hello opt!")
  }


}
