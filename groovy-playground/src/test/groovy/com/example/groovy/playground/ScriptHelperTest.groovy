package com.example.groovy.playground

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class ScriptHelperTest {
  def log = LoggerFactory.getLogger(ScriptHelperTest)

  @Test
  void test() {
    log.debug("{}", org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10))
  }

}
