/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.boot;

import java.util.logging.LogManager;

import org.springframework.boot.logging.java.JavaLoggingSystem;
import org.springframework.nativex.extension.InitializationInfo;
import org.springframework.nativex.extension.InitializationTime;
import org.springframework.nativex.extension.MethodInfo;
import org.springframework.nativex.extension.NativeImageConfiguration;
import org.springframework.nativex.extension.NativeImageHint;
import org.springframework.nativex.extension.ResourcesInfo;
import org.springframework.nativex.extension.TypeInfo;
import org.springframework.nativex.type.AccessBits;

@NativeImageHint(
	trigger=kotlin.Unit.class,
	resourcesInfos= { 
		@ResourcesInfo(patterns= { "META-INF/.*.kotlin_module$", ".*.kotlin_builtins" })
	}
)
@NativeImageHint(
	resourcesInfos = {
		@ResourcesInfo(patterns= {
			"db/.*", // TODO should be conditional on database active?
			"messages/.*",
			"banner.txt",
			"META-INF/spring.components",
			"application.*.yml",
			"application.*.yaml",
			"^git.properties",
			"^META-INF/build-info.properties",
			"application.*.properties",
			// This one originally added for kotlin but covers many other scenarios too - is it too many files?
			"META-INF/services/.*",
		    // Do these two catch the logging/<eachDirectory>/*.properties?
		    "org/springframework/boot/logging/.*.properties",
		    "org/springframework/boot/logging/.*.xml",
		    "logging.properties",
		    "org/springframework/boot/logging/java/logging.properties"
			}),
		@ResourcesInfo(patterns = {
			"messages/messages"	
			},isBundle = true)
	},
	typeInfos = { 
		@TypeInfo(types = {
			SpringBootConfiguration.class,
			LogManager.class,
			JavaLoggingSystem.class
		},access=AccessBits.LOAD_AND_CONSTRUCT|AccessBits.PUBLIC_METHODS)
	}
)
@NativeImageHint(
		typeInfos = {
				@TypeInfo(types= SpringApplication.class, methods = {
						@MethodInfo(name="setBannerMode", parameterTypes = Banner.Mode.class) // Enables property control of banner mode
				})
		}
//		initializationInfos = {
//			@InitializationInfo(initTime = InitializationTime.BUILD, typeNames = "org.springframework.nativex.buildtools.StaticSpringFactories")
//		}
		)
public class SpringApplicationHints implements NativeImageConfiguration {
}
