import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.7.10"
}

group = "kr.co.bookand"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	// swagger 추가
	implementation("io.springfox:springfox-boot-starter:3.0.0")
	implementation("io.springfox:springfox-swagger-ui:3.0.0")

	// jwt 추가
	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

	//s3
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
	testImplementation("io.findify:s3mock_2.13:0.2.6")

	// queryDSL
	implementation("com.querydsl:querydsl-jpa:5.0.0")
	kapt("com.querydsl:querydsl-apt:5.0.0")
	kapt("org.springframework.boot:spring-boot-configuration-processor")
	api("com.querydsl:querydsl-jpa")
	kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")

	// kotest
	testImplementation("io.kotest:kotest-runner-junit5:4.6.3")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

	// mockk
	testImplementation("io.mockk:mockk:1.12.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


// kotlin
allOpen {
	annotation("org.springframework.stereotype.Controller")
	annotation("org.springframework.stereotype.Service")
	annotation("org.springframework.stereotype.Repository")
}

noArg {
	annotation("javax.persistence.Entity")
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class){
	kotlin.srcDir("$buildDir/generated/source/kapt/main")
}