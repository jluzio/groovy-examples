package com.example.groovy.playground.misc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TextBlockJavaTest {
  static final Logger log = LoggerFactory.getLogger(TextBlockJavaTest.class);

  @Test
  void test() {
    var text = """
      line 1
      line 2
      """;
    log.info(text);
    assertThat(text)
        .isEqualTo("line 1\nline 2\n");
  }

}
