///usr/bin/env jbang "$0" "$@" ; exit $?

@Grab("org.apache.commons:commons-lang3:3.12.0")

class grabtest {

  public static void main(String[] args) {
    System.out.printf("%s%n", org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10));
  }
}