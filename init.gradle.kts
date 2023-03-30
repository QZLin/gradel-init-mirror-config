import java.net.*

val replaceDict = mapOf(
        "jcenter.bintray.com" to "https://maven.aliyun.com/repository/jcenter",//
        "maven.google.com" to "https://maven.aliyun.com/repository/google",//
        "plugins.gradle.org/m2" to "https://maven.aliyun.com/repository/gradle-plugin",//
//        "repo.maven.apache.org/maven2/" to "https://maven.aliyun.com/mvn/search",//
        "repo1.maven.org/maven2" to "https://maven.aliyun.com/repository/central",//
        "repository.apache.org/snapshots" to "https://maven.aliyun.com/repository/apache-snapshots",//

        "repo.grails.org/grails/core" to "https://maven.aliyun.com/repository/grails-core",//

        "repo.spring.io/libs-milestone" to "https://maven.aliyun.com/repository/spring",//
        "repo.spring.io/plugins-release" to "https://maven.aliyun.com/repository/spring-plugin",//
)
val appendList: List<String> = listOf()

val logTag = "init.gradle.kts"
println("[$logTag]")

val repoConfig: RepositoryHandler.() -> Unit = {
    this.forEach { repo: ArtifactRepository ->
        if (!(repo is MavenArtifactRepository)) return@forEach
        val repo_url = repo.url.toString()
        logger.info("[$logTag] \t${repo.name}(\"$repo_url\")")
        replaceDict.forEach { k, v ->
            if (!repo_url.contains(k)) return@forEach
            println("[$logTag] ${repo.name}(\"${repo.url}\") -> $v")
            repo.setUrl(URI.create(v))
        }

    }
}

settingsEvaluated {
    this.pluginManagement {
        repoConfig(repositories)
    }
    this.dependencyResolutionManagement {
        repoConfig(repositories)
    }
}
allprojects {
    buildscript {
        repoConfig(repositories)
    }
    repoConfig(repositories)
}