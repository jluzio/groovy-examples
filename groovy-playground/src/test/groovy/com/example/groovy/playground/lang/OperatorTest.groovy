package com.example.groovy.playground.lang

import groovy.transform.EqualsAndHashCode
import org.junit.jupiter.api.Test

class OperatorTest {

  @EqualsAndHashCode
  class Creature { String type }

  @Test
  void logicalOperators() {
    def cat = new Creature(type: 'cat')
    def copyCat = cat
    def lion = new Creature(type: 'cat')

    assert cat.equals(lion) // Java logical equality
    assert cat == lion      // Groovy shorthand operator

    assert cat.is(copyCat)  // Groovy identity
    assert cat === copyCat  // operator shorthand
    assert cat !== lion     // negated operator shorthand
  }

}
