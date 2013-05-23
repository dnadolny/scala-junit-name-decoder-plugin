package org.jenkinsci.plugins.scalajunitnamedecoder;

import static org.junit.Assert.assertEquals;
import hudson.tasks.junit.SuiteResult;
import hudson.tasks.junit.TestResult;
import hudson.tasks.junit.CaseResult;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class ScalaTestNameTransformerJenkinsTest {
    public @Rule JenkinsRule j = new JenkinsRule();
    
    @Test public void extension_changes_display_name_for_test_and_package_names() throws Exception {
        SuiteResult suite = parseOne(getDataFile("junit.xml"));
        CaseResult result1 = suite.getCases().get(0);
        CaseResult result2 = suite.getCases().get(1);
        CaseResult result3 = suite.getCases().get(2);
        CaseResult result4 = suite.getCases().get(3);
        assertDetailsMatch(result1, "testWithNormalName", "testWithNormalName", "foo.bar.SomeTest.testWithNormalName", "foo.bar.SomeTest.testWithNormalName");
        assertDetailsMatch(result2, "test$u0020from$u0020default$u0020package", "test from default package", "DefaultPackageTest.test$u0020from$u0020default$u0020package", "DefaultPackageTest.`test from default package`");
        assertDetailsMatch(result3, "testName", "testName", "foo.bar$u0020baz.PackageHasUnicodeTest.testName", "foo.`bar baz`.PackageHasUnicodeTest.testName");
        assertDetailsMatch(result4, "test$u0020name", "test name", "foo.bar$u0020baz.Package$u0020And$u0020Class$u0020Has$u0020Unicode$u0020Test.test$u0020name", "foo.`bar baz`.`Package And Class Has Unicode Test`.`test name`");
    }

    private void assertDetailsMatch(CaseResult result, String name, String displayName, String fullName, String fullDisplayName) {
        assertEquals(name, result.getName());
        assertEquals(displayName, result.getDisplayName());
        assertEquals(fullName, result.getFullName());
        assertEquals(fullDisplayName, result.getFullDisplayName());
    }
    
    private File getDataFile(String name) throws URISyntaxException {
        return new File(ScalaTestNameTransformerJenkinsTest.class.getResource(name).toURI());
    }

    private SuiteResult parseOne(File file) throws Exception {
        TestResult testResult = new TestResult();
        testResult.parse(file);
        Collection<SuiteResult> suites = testResult.getSuites();
        assertEquals(1, suites.size());
        return suites.iterator().next();
    }
}
