package com.example.groovy.playground.misc

import groovy.util.logging.Slf4j
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

@Slf4j
class MavenCommandScriptTest {

  def mvnCmd(Map params = [:]) {
    def goals = params.goals ?: 'clean install'
    def target = params.target ?: '-f pom.xml'
    def profiles = params.profiles ?: ""
    def testSkip = params.testSkip ?: 'false'
    def extraParams = params.extraParams ?: ''
    def testFlags = "-Dmaven.test.skip=${testSkip} -Dskip.integration.tests=${testSkip} -Dskip.unit.tests=${testSkip}"
    return """
      export MAVEN_OPTS="-Xmx1024m"
      mvn ${goals} ${target} ${profiles} ${testFlags} ${extraParams}
      """
  }

  def mvnDockerCmd(Map params = [:]) {
    def dockerRegistry = params.dockerRegistry
    def imageTagKey = params.imageTagKey
    def imageTagValue = params.imageTagValue
    def extraParams = mvnDockerParams(dockerRegistry, imageTagKey, imageTagValue)
    def fullParams = [profiles: "-Pdocker,docker-image", extraParams: extraParams, *: params]
    mvnCmd(fullParams)
  }

  def mvnDockerParams(dockerRegistry, imageTagKey = null, imageTagValue = null) {
    def dockerRegistryPart = dockerRegistry ? "-Ddocker.registry.url=${dockerRegistry}" : ""
    def imageTagPart = imageTagValue ? "${imageTagKey}=\"${imageTagValue}\"" : ""
    "${dockerRegistryPart} ${imageTagPart}"
  }

  @Test
  void test_mvnCmd() {
    log.info "=== default ==="
    def cmdDefault = sanitizeCmdText(mvnCmd())
    def expectedCmdDefault = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false
      """)
    assertThat(cmdDefault)
        .as("default")
        .isEqualTo(expectedCmdDefault)

    log.info "=== goals ==="
    def cmdGoals = sanitizeCmdText(mvnCmd(goals: "compile"))
    def expectedCmdGoals = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn compile -f pom.xml -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false
      """)
    assertThat(cmdGoals)
        .as("goals")
        .isEqualTo(expectedCmdGoals)

    log.info "=== target ==="
    def cmdTarget = sanitizeCmdText(mvnCmd(target: "-pl submodule"))
    def expectedCmdTarget = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -pl submodule -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false
      """)
    assertThat(cmdTarget)
        .as("target")
        .isEqualTo(expectedCmdTarget)

    log.info "=== profiles ==="
    def cmdProfiles = sanitizeCmdText(mvnCmd(profiles: "-Ptest"))
    def expectedCmdProfiles = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Ptest -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false
      """)
    assertThat(cmdProfiles)
        .as("profiles")
        .isEqualTo(expectedCmdProfiles)

    log.info "=== testSkip ==="
    def cmdTestSkip = sanitizeCmdText(mvnCmd(testSkip: "true"))
    def expectedCmdTestSkip = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Dmaven.test.skip=true -Dskip.integration.tests=true -Dskip.unit.tests=true
      """)
    assertThat(cmdTestSkip)
        .as("testSkip")
        .isEqualTo(expectedCmdTestSkip)

    log.info "=== all ==="
    def cmd_all = sanitizeCmdText(mvnCmd(goals: "compile", target: "-pl submodule", profiles: "-Ptest", testSkip: "true", extraParams: "-Dkey=val"))
    def expectedCmd_all = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn compile -pl submodule -Ptest -Dmaven.test.skip=true -Dskip.integration.tests=true -Dskip.unit.tests=true -Dkey=val
      """)
    assertThat(cmd_all)
        .as("all")
        .isEqualTo(expectedCmd_all)
  }

  @Test
  void test_mvnDockerParams() {
    def params0 = sanitizeText(mvnDockerParams(null))
    def expectedParams0 = sanitizeText("")
    assertThat(params0)
        .isEqualTo(expectedParams0)

    def params1 = sanitizeText(mvnDockerParams("docker-registry-url"))
    def expectedParams1 = sanitizeText("-Ddocker.registry.url=docker-registry-url")
    assertThat(params1)
        .isEqualTo(expectedParams1)

    def params2 = sanitizeText(mvnDockerParams("docker-registry-url", "imgTagKey", "imgTagVal"))
    def expectedParams2 = sanitizeText("-Ddocker.registry.url=docker-registry-url imgTagKey=\"imgTagVal\"")
    assertThat(params2)
        .isEqualTo(expectedParams2)
  }

  @Test
  void test_mvnDockerCmd() {
    log.info "=== default ==="
    def cmdDefault = sanitizeCmdText(mvnDockerCmd())
    def expectedCmdDefault = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Pdocker,docker-image -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false
      """)
    assertThat(cmdDefault)
        .as("default")
        .isEqualTo(expectedCmdDefault)

    log.info "=== dockerRegistry ==="
    def cmdDockerRegistry = sanitizeCmdText(mvnDockerCmd(dockerRegistry: "docker-registry-url"))
    def expectedCmdDockerRegistry = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Pdocker,docker-image -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false -Ddocker.registry.url=docker-registry-url
      """)
    assertThat(cmdDockerRegistry)
        .as("dockerRegistry")
        .isEqualTo(expectedCmdDockerRegistry)

    log.info "=== imageTag ==="
    def cmdImageTag = sanitizeCmdText(mvnDockerCmd(dockerRegistry: "docker-registry-url", imageTagKey: "imgTagKey", imageTagValue: "imgTagVal"))
    def expectedCmdImageTag = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Pdocker,docker-image -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false -Ddocker.registry.url=docker-registry-url imgTagKey=\"imgTagVal\"
      """)
    assertThat(cmdImageTag)
        .as("imageTag")
        .isEqualTo(expectedCmdImageTag)
  }

  @Test
  void test_mvnDockerCmd_params_merge_priority() {
    log.info "=== profilesDefault ==="
    def cmdProfilesDefault = sanitizeCmdText(mvnDockerCmd())
    def expectedCmdProfilesDefault = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Pdocker,docker-image -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false
      """)
    assertThat(cmdProfilesDefault)
        .as("profilesDefault")
        .isEqualTo(expectedCmdProfilesDefault)

    log.info "=== profilesMerge ==="
    def cmdProfilesMerge = sanitizeCmdText(mvnDockerCmd(profiles: "-Pdbs,docker,docker-image"))
    def expectedCmdProfilesMerged = sanitizeExpectedCmdText("""
      export MAVEN_OPTS="-Xmx1024m"
      mvn clean install -f pom.xml -Pdbs,docker,docker-image -Dmaven.test.skip=false -Dskip.integration.tests=false -Dskip.unit.tests=false
      """)
    assertThat(cmdProfilesMerge)
        .as("profilesMerge")
        .isEqualTo(expectedCmdProfilesMerged)
  }

  static String sanitizeExpectedCmdText(String value) {
    log.info("sanitizeExpectedCmdText :: value:\n${value}")
    def sanitizedValue = value
        .stripIndent()
        .stripLeading()
        .trim()
    log.info("sanitizeExpectedCmdText :: sanitizedValue:\n${sanitizedValue}")
    sanitizedValue
  }

  static String sanitizeCmdText(String value) {
    log.info("sanitizeCmdText :: value:\n${value}")
    def sanitizedValue = value
        .stripIndent()
        .stripLeading()
        .replaceAll(" +", " ")
        .trim()
    log.info("sanitizeCmdText :: sanitizedValue:\n${sanitizedValue}")
    sanitizedValue
  }

  static String sanitizeText(String value) {
    log.info("sanitizeText :: value: ${value}")
    def sanitizedValue = value
        .replaceAll(" +", " ")
        .trim()
    log.info("sanitizeText :: sanitizedValue: ${sanitizedValue}")
    sanitizedValue
  }

}
