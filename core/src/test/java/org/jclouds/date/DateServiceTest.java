/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
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
 * ====================================================================
 */
package org.jclouds.date;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jclouds.PerformanceTest;
import org.jclouds.date.DateService;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/* 
 * TODO: Scrap any non-DateService references (eg Joda & Amazon) if/when
 * we confirm that the DateService is fast enough.
 */

/**
 * Compares performance of date operations
 * 
 * @author Adrian Cole
 * @author James Murty
 */
// NOTE:without testName, this will fail w/NPE during surefire
@Test(groups = "performance", singleThreaded = true, timeOut = 2 * 60 * 1000, testName = "DateServiceTest")
public class DateServiceTest extends PerformanceTest {
   protected DateService dateService;

   @BeforeTest
   protected void createDateService() {
      Injector i = Guice.createInjector();
      dateService = i.getInstance(DateService.class);
   }

   protected TestData[] testData;

   protected class TestData {
      public final String iso8601DateString;
      public final String iso8601DateStringTz;

      public final String iso8601SecondsDateString;
      public final String rfc822DateString;
      public final String cDateString;

      public final Date date;

      TestData(String iso8601, String iso8601DateStringTz, String iso8601Seconds, String rfc822, String cDateString,
               Date dateTime) {
         this.iso8601DateString = iso8601;
         this.iso8601DateStringTz = iso8601DateStringTz;
         this.iso8601SecondsDateString = iso8601Seconds;
         this.rfc822DateString = rfc822;
         this.cDateString = cDateString;
         this.date = dateTime;
      }
   }

   public DateServiceTest() {
      // Constant time test values, each TestData item must contain matching times!
      testData = new TestData[] {
               new TestData("2009-03-12T02:00:07.000Z", "2009-03-12T02:00:07-04:00", "2009-03-12T02:00:07Z",
                        "Thu, 12 Mar 2009 02:00:07 GMT", "Thu Mar 12 02:00:07 +0000 2009", new Date(1236823207000l)),
               new TestData("2009-03-12T02:00:07.000Z", "2009-03-12T02:00:07+04:00", "2009-03-12T02:00:07Z",
                        "Thu, 12 Mar 2009 02:00:07 GMT", "Thu Mar 12 02:00:07 +0000 2009", new Date(1236823207000l)),
               new TestData("2009-03-14T04:00:07.000Z", "2009-03-14T04:00:07Z+04:00", "2009-03-14T04:00:07Z",
                        "Sat, 14 Mar 2009 04:00:07 GMT", "Thu Mar 14 04:00:07 +0000 2009", new Date(1237003207000l)),
               new TestData("2009-03-16T06:00:07.000Z", "2009-03-16T06:00:07Z+04:00", "2009-03-16T06:00:07Z",
                        "Mon, 16 Mar 2009 06:00:07 GMT", "Thu Mar 16 06:00:07 +0000 2009", new Date(1237183207000l)),
               new TestData("2009-03-18T08:00:07.000Z", "2009-03-18T08:00:07Z+04:00", "2009-03-18T08:00:07Z",
                        "Wed, 18 Mar 2009 08:00:07 GMT", "Thu Mar 18 08:00:07 +0000 2009", new Date(1237363207000l)),
               new TestData("2009-03-20T10:00:07.000Z", "2009-03-20T10:00:07Z+04:00", "2009-03-20T10:00:07Z",
                        "Fri, 20 Mar 2009 10:00:07 GMT", "Thu Mar 20 10:00:07 +0000 2009", new Date(1237543207000l)) };
   }

   @Test
   public void testIso8601DateParse() throws ExecutionException, InterruptedException {
      Date dsDate = dateService.iso8601DateParse(testData[0].iso8601DateString);
      assertEquals(dsDate, testData[0].date);
   }

   @Test
   public void testIso8601DateParseTz() throws ExecutionException, InterruptedException {
      Date dsDate = dateService.iso8601SecondsDateParse(testData[0].iso8601DateStringTz);
      assertEquals(dsDate, testData[0].date);
   }

   @Test
   public void testIso8601SecondsDateParse() throws ExecutionException, InterruptedException {
      Date dsDate = dateService.iso8601SecondsDateParse(testData[0].iso8601SecondsDateString);
      assertEquals(dsDate, testData[0].date);
   }

   @Test
   public void testCDateParse() throws ExecutionException, InterruptedException {
      Date dsDate = dateService.cDateParse(testData[0].cDateString);
      assertEquals(dsDate, testData[0].date);
   }

   @Test
   public void testRfc822DateParse() throws ExecutionException, InterruptedException {
      Date dsDate = dateService.rfc822DateParse(testData[0].rfc822DateString);
      assertEquals(dsDate, testData[0].date);
   }

   @Test
   public void testIso8601DateFormat() throws ExecutionException, InterruptedException {
      String dsString = dateService.iso8601DateFormat(testData[0].date);
      assertEquals(dsString, testData[0].iso8601DateString);
   }

   @Test
   public void testIso8601SecondsDateFormat() throws ExecutionException, InterruptedException {
      String dsString = dateService.iso8601SecondsDateFormat(testData[0].date);
      assertEquals(dsString, testData[0].iso8601SecondsDateString);
   }

   @Test
   public void testCDateFormat() throws ExecutionException, InterruptedException {
      String dsString = dateService.cDateFormat(testData[0].date);
      assertEquals(dsString, testData[0].cDateString);
   }

   @Test
   public void testRfc822DateFormat() throws ExecutionException, InterruptedException {
      String dsString = dateService.rfc822DateFormat(testData[0].date);
      assertEquals(dsString, testData[0].rfc822DateString);
   }

   @Test
   void testIso8601DateFormatResponseTime() throws ExecutionException, InterruptedException {
      for (int i = 0; i < LOOP_COUNT; i++)
         dateService.iso8601DateFormat();
   }

   @Test
   void testFromSeconds() throws ExecutionException, InterruptedException {
      long seconds = 1254008225;
      Date date = dateService.fromSeconds(seconds);
      assertEquals(dateService.rfc822DateFormat(date), "Sat, 26 Sep 2009 23:37:05 GMT");
   }

   @Test
   void testTzWithExtraZ() throws ExecutionException, InterruptedException {
      assertEquals(dateService.iso8601SecondsDateParse("2011-05-26T06:14:13-04:00").toString(),
               "Wed May 25 23:14:13 PDT 2011");
      assertEquals(dateService.iso8601SecondsDateParse("2011-05-26T06:14:13-04:00Z").toString(),
               "Wed May 25 23:14:13 PDT 2011");
   }

   @Test
   void testRfc822DateFormatResponseTime() throws ExecutionException, InterruptedException {
      for (int i = 0; i < LOOP_COUNT; i++)
         dateService.rfc822DateFormat();
   }

   @Test
   void testCDateFormatResponseTime() throws ExecutionException, InterruptedException {
      for (int i = 0; i < LOOP_COUNT; i++)
         dateService.cDateFormat();
   }

   @Test
   void testFormatIso8601DateCorrectnessInParallel() throws Throwable {
      List<Runnable> tasks = new ArrayList<Runnable>(testData.length);
      for (final TestData myData : testData) {
         tasks.add(new Runnable() {
            public void run() {
               String dsString = dateService.iso8601DateFormat(myData.date);
               assertEquals(dsString, myData.iso8601DateString);
            }
         });
      }
      executeMultiThreadedCorrectnessTest(tasks);
   }

   @Test
   void testFormatIso8601DatePerformanceInParallel() throws Throwable {
      List<Runnable> tasks = new ArrayList<Runnable>(testData.length);
      for (final TestData myData : testData) {
         tasks.add(new Runnable() {
            public void run() {
               dateService.iso8601DateFormat(myData.date);
            }
         });
      }
      executeMultiThreadedPerformanceTest("testFormatIso8601DatePerformanceInParallel", tasks);
   }

   @Test
   void testParseIso8601DateSerialResponseTime() throws ExecutionException, InterruptedException {
      for (int i = 0; i < LOOP_COUNT; i++)
         dateService.iso8601DateParse(testData[0].iso8601DateString);
   }

   @Test
   void testParseIso8601DateCorrectnessInParallel() throws Throwable {
      List<Runnable> tasks = new ArrayList<Runnable>(testData.length);
      for (final TestData myData : testData) {
         tasks.add(new Runnable() {
            public void run() {
               Date dsDate = dateService.iso8601DateParse(myData.iso8601DateString);
               assertEquals(dsDate, myData.date);
            }
         });
      }
      executeMultiThreadedCorrectnessTest(tasks);
   }

   @Test
   void testParseIso8601DatePerformanceInParallel() throws Throwable {
      List<Runnable> tasks = new ArrayList<Runnable>(testData.length);
      for (final TestData myData : testData) {
         tasks.add(new Runnable() {
            public void run() {
               dateService.iso8601DateParse(myData.iso8601DateString);
            }
         });
      }
      executeMultiThreadedPerformanceTest("testParseIso8601DatePerformanceInParallel", tasks);
   }

}