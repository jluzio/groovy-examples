package com.example.groovy.playground.lang

import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

class StringTest {

  @Test
  void join() {
    def values = ["a", "b", "c"]
    def output = values.collect {"!" + it }.join(",")
    assertThat(output).isEqualTo("!a,!b,!c")
  }

  @Test
  void filter_join() {
    def values = ["a", "b", "c"]
    def output = values.findAll { it != "a" }.collect {"!" + it }.join(",")
    assertThat(output).isEqualTo("!b,!c")
  }

}
