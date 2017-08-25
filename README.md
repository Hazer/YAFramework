# YAFramework [ ![Download](https://api.bintray.com/packages/hazer/maven/yaf/images/download.svg) ](https://bintray.com/hazer/maven/yaf/_latestVersion)[![Build Status](https://www.bitrise.io/app/4088ff7976b995e8/status.svg?token=V0Xvf2jmpBy443zHTsKhYw&branch=master)](https://www.bitrise.io/app/4088ff7976b995e8)

Built upon  [![Kotlin](https://img.shields.io/badge/kotlin-1.1.2--5-blue.svg)](http://kotlinlang.org)[ ![Anko](https://img.shields.io/badge/anko-0.9.1a-blue.svg) ](https://bintray.com/jetbrains/anko/anko/0.9.1a/link)

Yet Another Framework for Android

# Using Yaf
This project is not yet release to jcenter, but it is already on Bintray, during this period use the project pointing to the private repository:
```Gradle
repositories {
  maven { url "https://dl.bintray.com/hazer/maven" }
}
```

Yaf has a meta-dependency which plugs in all available features into your project at once:

```Gradle
dependencies {
  compile "io.vithor.frameworks.yaf:all:$yaf_version"
}
```

If you only need some of the features, you can reference any of Yaf's modules:
```Gradle
dependencies {
  // Base dependency
  compile "io.vithor.frameworks.yaf:core:$yaf_version"
  // Gson helpers
  compile "io.vithor.frameworks.yaf:gson:$yaf_version"
  // OrmLite Migration Manager
  compile "io.vithor.frameworks.yaf:migration:$yaf_version"
  // Model View Presenter abstraction
  compile "io.vithor.frameworks.yaf:mvp:$yaf_version"
  // Kotlin-friendly Permission Requests
  compile "io.vithor.frameworks.yaf:permissions:$yaf_version"
  // Rest Abstraction based on Retrofit2
  compile "io.vithor.frameworks.yaf:rest:$yaf_version"
  // SQLite helpers and DSL
  compile "io.vithor.frameworks.yaf:sqlite:$yaf_version"
  // UI widgets, components, custom listeners and extensions
  compile "io.vithor.frameworks.yaf:ui:$yaf_version"
  // Form-like Validation DSL
  compile "io.vithor.frameworks.yaf:validation:$yaf_version"
}
```
