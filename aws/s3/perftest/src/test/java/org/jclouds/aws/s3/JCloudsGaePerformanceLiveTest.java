/**
 *
 * Copyright (C) 2009 Global Cloud Specialists, Inc. <info@globalcloudspecialists.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.aws.s3;

import java.io.File;
import java.io.InputStream;

import org.jclouds.gae.config.URLFetchServiceClientModule;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;
import com.google.inject.Module;

@Test(sequential = true, testName = "s3.JCloudsGaePerformanceLiveTest", groups = { "live" })
public class JCloudsGaePerformanceLiveTest extends BaseJCloudsPerformance {

   @Override
   protected boolean putByteArray(String bucket, String key, byte[] data, String contentType)
            throws Exception {
      setupApiProxy();
      return super.putByteArray(bucket, key, data, contentType);
   }

   @Override
   protected boolean putFile(String bucket, String key, File data, String contentType)
            throws Exception {
      setupApiProxy();
      return super.putFile(bucket, key, data, contentType);
   }

   @Override
   protected boolean putInputStream(String bucket, String key, InputStream data, String contentType)
            throws Exception {
      setupApiProxy();
      return super.putInputStream(bucket, key, data, contentType);
   }

   @Override
   protected boolean putString(String bucket, String key, String data, String contentType)
            throws Exception {
      setupApiProxy();
      return super.putString(bucket, key, data, contentType);
   }

   @Override
   protected void deleteEverything() throws Exception {
      setupApiProxy();
      super.deleteEverything();
   }

   @BeforeMethod
   void setupApiProxy() {
      ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
      ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")) {
      });
   }

   class TestEnvironment implements ApiProxy.Environment {
      public String getAppId() {
         return "Unit Tests";
      }

      public String getVersionId() {
         return "1.0";
      }

      public void setDefaultNamespace(String s) {
      }

      public String getRequestNamespace() {
         return null;
      }

      public String getDefaultNamespace() {
         return null;
      }

      public String getAuthDomain() {
         return null;
      }

      public boolean isLoggedIn() {
         return false;
      }

      public String getEmail() {
         return null;
      }

      public boolean isAdmin() {
         return false;
      }
   }

   @Override
   protected Module createHttpModule() {
      return new URLFetchServiceClientModule();
   }
}