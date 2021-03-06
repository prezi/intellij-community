/*
 * Copyright 2000-2011 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.psi.formatter.java;


import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;

/**
 * @author Denis Zhdanov
 * @since 1/18/11 3:11 PM
 */
public class JavadocFormatterTest extends AbstractJavaFormatterTest {

  public void testRightMargin() throws Exception {
    getSettings().getRootSettings().getCommonSettings(JavaLanguage.INSTANCE).WRAP_LONG_LINES = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 35;//      |
    doTextTest(
      "/** Here is one-line java-doc comment */" +
      "class Foo {\n" +
      "}",
      "/**\n" +
      " * Here is one-line java-doc \n" +
      " * comment\n" +
      " */\n" +
      "class Foo {\n" +
      "}");

  }

  public void testLineFeedsArePreservedDuringWrap() {
    // Inspired by IDEA-61895
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().JD_PRESERVE_LINE_FEEDS = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 48;
    
    doTextTest(
      "/**\n" +
      " * This is a long comment that spans more than one\n" +
      " * line\n" +
      " */\n" +
      "class Test {\n" +
      "}",
      "/**\n" +
      " * This is a long comment that spans more than\n" +
      " * one\n" +
      " * line\n" +
      " */\n" +
      "class Test {\n" +
      "}"
    );
  }
  
  public void testSCR11296() throws Exception {
    final CommonCodeStyleSettings settings = getSettings();
    settings.getRootSettings().RIGHT_MARGIN = 50;
    settings.getRootSettings().WRAP_COMMENTS = true;
    settings.getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    settings.getRootSettings().JD_P_AT_EMPTY_LINES = false;
    settings.getRootSettings().JD_KEEP_EMPTY_LINES = false;
    doTest();
  }

  public void testSCR2632() throws Exception {
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 20;

    doTextTest("/**\n" + " * <p />\n" + " * Another paragraph of the description placed after blank line.\n" + " */\n" + "class A{}",
               "/**\n" +
               " * <p/>\n" +
               " * Another paragraph\n" +
               " * of the description\n" +
               " * placed after\n" +
               " * blank line.\n" +
               " */\n" +
               "class A {\n" +
               "}");
  }
  
  public void testParagraphTagGeneration() {
    // Inspired by IDEA-61811
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().JD_P_AT_EMPTY_LINES = true;
    doTextTest(
      "/**\n" +
      " * line 1\n" +
      " *\n" +
      " * line 2\n" +
      " * <pre>\n" +
      " *   line 3\n" +
      " *\n" +
      " *   line 4\n" +
      " * </pre>\n" +
      " */\n" +
      "class Test {\n" +
      "}",
      "/**\n" +
      " * line 1\n" +
      " * <p/>\n" +
      " * line 2\n" +
      " * <pre>\n" +
      " *   line 3\n" +
      " *\n" +
      " *   line 4\n" +
      " * </pre>\n" +
      " */\n" +
      "class Test {\n" +
      "}"
    );
  }

  public void testParameterDescriptionNotOnNewLine() throws Exception {
    // IDEA-107383
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().JD_ALIGN_PARAM_COMMENTS = true;

    doClassTest(
      "/**\n" +
      " @param protocolId protocol id\n" +
      " @param connectedUserIdHandlerFromServer user id\n" +
      " @return\n" +
      
      " */\n" +
      "public void register(int protocolId, int connectedUserIdHandlerFromServer) {\n" +
      "}",

      "/**\n" +
      " * @param protocolId                       protocol id\n" +
      " * @param connectedUserIdHandlerFromServer user id\n" +
      " * @return\n" +
      " */\n" +
      "public void register(int protocolId, int connectedUserIdHandlerFromServer) {\n" +
      "}");
  }

  public void testWrappedParameterDescription() throws Exception {
    // Inspired by IDEA-13072
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().JD_PARAM_DESCRIPTION_ON_NEW_LINE = true;
    doClassTest(
      "/**\n" +
      " * test description\n" +
      " * @param first first description\n" +
      " * @param second\n" +
      " * @param third third\n" +
      " *              description\n" +
      " * @param forth\n" +
      " *          forth description\n" +
      " */\n" +
      "void test(int first, int second, int third, int forth) {\n" +
      "}",
      "/**\n" +
      " * test description\n" +
      " *\n" +
      " * @param first\n" +
      " *         first description\n" +
      " * @param second\n" +
      " * @param third\n" +
      " *         third description\n" +
      " * @param forth\n" +
      " *         forth description\n" +
      " */\n" +
      "void test(int first, int second, int third, int forth) {\n" +
      "}"
    );
  }

  public void testExceptionAlignmentCorrect() {
    getCurrentCodeStyleSettings().ENABLE_JAVADOC_FORMATTING = true;
    getCurrentCodeStyleSettings().JD_ALIGN_EXCEPTION_COMMENTS = true;

    String before =
      "public class Controller {\n" +
      "\n" +
      "    /**\n" +
      "     * @throws NoSearchServersConfiguredException If no search engine servers are configured.\n" +
      "     * @throws SearchServerUnavailableException If the search engine server is not accessible.\n" +
      "     * @throws InvalidSearchServerResponseException If the search engine server response was invalid.\n" +
      "     * @throws NotificationEncodingException If the request could not be encoded to UTF-8.\n" +
      "     * @throws NotificationUnavailableException If the notification server is not available or sent back an invalid response code.\n" +
      "     */\n" +
      "    public int superDangerousMethod() {\n" +
      "        return 68;\n" +
      "    }\n" +
      "}";

    String after =
      "public class Controller {\n" +
      "\n" +
      "    /**\n" +
      "     * @throws NoSearchServersConfiguredException   If no search engine servers are configured.\n" +
      "     * @throws SearchServerUnavailableException     If the search engine server is not accessible.\n" +
      "     * @throws InvalidSearchServerResponseException If the search engine server response was invalid.\n" +
      "     * @throws NotificationEncodingException        If the request could not be encoded to UTF-8.\n" +
      "     * @throws NotificationUnavailableException     If the notification server is not available or sent back an invalid response code.\n" +
      "     */\n" +
      "    public int superDangerousMethod() {\n" +
      "        return 68;\n" +
      "    }\n" +
      "}";

    doTextTest(before, after);
  }

  public void testReturnTagAlignment() throws Exception {
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 80;
    getSettings().getRootSettings().JD_LEADING_ASTERISKS_ARE_ENABLED = true;
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().getCommonSettings(JavaLanguage.INSTANCE).WRAP_LONG_LINES = true;

    String before = "    /**\n" +
                    "     * @return this is a return value documentation with a very long description that is longer than the right margin. It is more than 200 characters long, not including the comment indent and the asterisk characters, which should be greater than any sane right margin.\n" +
                    "     */\n" +
                    "    public int method(int parameter) {\n" +
                    "        return 0;\n" +
                    "    }\n";

    String after = "/**\n" +
                   " * @return this is a return value documentation with a very long description\n" +
                   " * that is longer than the right margin. It is more than 200 characters\n" +
                   " * long, not including the comment indent and the asterisk characters, which\n" +
                   " * should be greater than any sane right margin.\n" +
                   " */\n" +
                   "public int method(int parameter) {\n" +
                   "    return 0;\n" +
                   "}\n";

    doClassTest(before, after);
  }


  public void testReturnTagAlignmentWithPreTagOnFirstLine() throws Exception {
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 80;
    getSettings().getRootSettings().JD_LEADING_ASTERISKS_ARE_ENABLED = true;
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().getCommonSettings(JavaLanguage.INSTANCE).WRAP_LONG_LINES = true;

    String before = "    /**\n" +
                    "     * @return <pre>this is a return value documentation with a very long description\n" +
                    "     * that is longer than the right margin.</pre>\n" +
                    "     */\n" +
                    "    public int method(int parameter) {\n" +
                    "        return 0;\n" +
                    "    }";

    String after = "/**\n" +
                   " * @return <pre>this is a return value documentation with a very long\n" +
                   " * description\n" +
                   " * that is longer than the right margin.</pre>\n" +
                   " */\n" +
                   "public int method(int parameter) {\n" +
                   "    return 0;\n" +
                   "}";

    doClassTest(before, after);
  }

  public void testSeeTagAlignment() throws Exception {
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 80;
    getSettings().getRootSettings().JD_LEADING_ASTERISKS_ARE_ENABLED = true;
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().getCommonSettings(JavaLanguage.INSTANCE).WRAP_LONG_LINES = true;

    String before = "    /**\n" +
                    "     * @see this is an additional documentation with a very long description that is longer than the right margin. It is more than 200 characters long, not including the comment indent and the asterisk characters which should be greater than any sane right margin\n" +
                    "     */\n" +
                    "    public int method(int parameter) {\n" +
                    "        return 0;\n" +
                    "    }";

    String after = "/**\n" +
                   " * @see this is an additional documentation with a very long description\n" +
                   " * that is longer than the right margin. It is more than 200 characters\n" +
                   " * long, not including the comment indent and the asterisk characters which\n" +
                   " * should be greater than any sane right margin\n" +
                   " */\n" +
                   "public int method(int parameter) {\n" +
                   "    return 0;\n" +
                   "}";

    doClassTest(before, after);
  }

  public void testDummySinceTagAlignment() throws Exception {
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 80;
    getSettings().getRootSettings().JD_LEADING_ASTERISKS_ARE_ENABLED = true;
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().getCommonSettings(JavaLanguage.INSTANCE).WRAP_LONG_LINES = true;

    String before = "    /**\n" +
                    "     * @since this is an additional documentation with a very long description that is longer than the right margin. It is more than 200 characters long, not including the comment indent and the asterisk characters which should be greater than any sane right margin\n" +
                    "     */\n" +
                    "    public int method(int parameter) {\n" +
                    "        return 0;\n" +
                    "    }";

    String after = "/**\n" +
                   " * @since this is an additional documentation with a very long description\n" +
                   " * that is longer than the right margin. It is more than 200 characters\n" +
                   " * long, not including the comment indent and the asterisk characters which\n" +
                   " * should be greater than any sane right margin\n" +
                   " */\n" +
                   "public int method(int parameter) {\n" +
                   "    return 0;\n" +
                   "}";

    doClassTest(before, after);
  }

  public void testDummyDeprecatedTagAlignment() throws Exception {
    getSettings().getRootSettings().ENABLE_JAVADOC_FORMATTING = true;
    getSettings().getRootSettings().RIGHT_MARGIN = 80;
    getSettings().getRootSettings().JD_LEADING_ASTERISKS_ARE_ENABLED = true;
    getSettings().getRootSettings().WRAP_COMMENTS = true;
    getSettings().getRootSettings().getCommonSettings(JavaLanguage.INSTANCE).WRAP_LONG_LINES = true;

    String before = "    /**\n" +
                    "     * @deprecated this is an additional documentation with a very long description that is longer than the right margin. It is more than 200 characters long, not including the comment indent and the asterisk characters which should be greater than any sane right margin\n" +
                    "     */\n" +
                    "    public int method(int parameter) {\n" +
                    "        return 0;\n" +
                    "    }";

    String after = "/**\n" +
                   " * @deprecated this is an additional documentation with a very long\n" +
                   " * description that is longer than the right margin. It is more than 200\n" +
                   " * characters long, not including the comment indent and the asterisk\n" +
                   " * characters which should be greater than any sane right margin\n" +
                   " */\n" +
                   "public int method(int parameter) {\n" +
                   "    return 0;\n" +
                   "}";

    doClassTest(before, after);
  }
}
