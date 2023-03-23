package com.example.groovy.playground.lang

import groovy.util.logging.Slf4j
import org.junit.jupiter.api.Test

import java.util.function.Consumer

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

    def multipleDefaultParams(var1 = "val1", var2 = "val2", var3, var4) {
      [var1: var1, var2: var2, var3: var3, var4: var4]
    }

    def multipleDefaultParamsMixedTypes(String var1 = "val1", Integer var2 = 2, String var3, Integer var4) {
      [var1: var1, var2: var2, var3: var3, var4: var4]
    }

    def namedParamsConvention(Map args, Integer var2 = 2) {
      args + [var2: var2]
    }

  }

  def tester = new TestBean()
  Consumer<Object> logIt = v -> log.debug("{}", v)

  @Test
  void test_defaultParams() {
    assertThat(tester.defaultParams())
        .satisfies(logIt)
        .isEqualTo("Hello world!")
  }

  @Test
  void test_optional() {
    assertThat(tester.optional())
        .satisfies(logIt)
        .isEqualTo("Hello default!")
    assertThat(tester.optional('opt'))
        .satisfies(logIt)
        .isEqualTo("Hello opt!")
  }

  @Test
  void test_multipleDefaultParams() {
    assertThat(tester.multipleDefaultParams("val3", "val4"))
        .satisfies(logIt)
        .isEqualTo([var1: "val1", var2: "val2", var3: "val3", var4: "val4"])
    assertThat(tester.multipleDefaultParams("custom_val1", "val3", "val4"))
        .satisfies(logIt)
        .isEqualTo([var1: "custom_val1", var2: "val2", var3: "val3", var4: "val4"])
    assertThat(tester.multipleDefaultParams("custom_val1", "custom_val2", "val3", "val4"))
        .satisfies(logIt)
        .isEqualTo([var1: "custom_val1", var2: "custom_val2", var3: "val3", var4: "val4"])
  }

  @Test
  void test_multipleDefaultParamsMixedTypes() {
    assertThat(tester.multipleDefaultParamsMixedTypes("val3", 4))
        .satisfies(logIt)
        .isEqualTo([var1: "val1", var2: 2, var3: "val3", var4: 4])
    assertThat(tester.multipleDefaultParamsMixedTypes("custom_val1", "val3", 4))
        .satisfies(logIt)
        .isEqualTo([var1: "custom_val1", var2: 2, var3: "val3", var4: 4])
    assertThat(tester.multipleDefaultParamsMixedTypes("custom_val1", 222, "val3", 4))
        .satisfies(logIt)
        .isEqualTo([var1: "custom_val1", var2: 222, var3: "val3", var4: 4])
  }

  @Test
  void test_namedParamsConvention() {
    assertThat(tester.namedParamsConvention(mapVar1: 1, mapVar2: 2, 2))
        .satisfies(logIt)
        .isEqualTo([mapVar1: 1, mapVar2: 2, var2: 2])

    def initialArgs = [mapVar1: 1, mapVar2: 2]
    assertThat(tester.namedParamsConvention(initialArgs, 2))
        .satisfies(logIt)
        .isEqualTo([mapVar1: 1, mapVar2: 2, var2: 2])
        .isNotEqualTo(initialArgs)
  }

}
