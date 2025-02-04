package org.example;

import org.testng.TestNG;
import java.util.Collections;

public class TestRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.setTestSuites(Collections.singletonList("sftp_client_tests/src/test/resources/testng.xml"));
        testng.run();
    }
}
