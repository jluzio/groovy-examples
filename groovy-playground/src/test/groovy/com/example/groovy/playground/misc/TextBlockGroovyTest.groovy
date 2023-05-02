package com.example.groovy.playground.misc

import groovy.util.logging.Slf4j
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

@Slf4j
class TextBlockGroovyTest {

  @Test
  void test() {
    var text = """
      line 1
      line 2
      """
    log.info "normal: ${text}"
    log.info "stripIndent: ${text.stripIndent()}"
    log.info "stripIndent + stripLeading: ${text.stripIndent().stripLeading()}"
    log.info "stripLeading + stripIndent: ${text.stripLeading().stripIndent()}"
    def expected = "line 1\nline 2\n"
    assertThat(text)
        .isNotEqualTo(expected)
    assertThat(text.stripIndent())
        .isNotEqualTo(expected)
    assertThat(text.stripLeading().stripIndent())
        .isNotEqualTo(expected)
    assertThat(text.stripIndent().stripLeading())
        .isEqualTo(expected)
  }

}
