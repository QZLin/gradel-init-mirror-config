val replaceDict = mapOf(
        "jcenter.bintray.com" to "https://maven.aliyun.com/repository/jcenter",//
        "maven.google.com" to "https://maven.aliyun.com/repository/google",//
        "plugins.gradle.org/m2" to "https://maven.aliyun.com/repository/gradle-plugin",//
//        "repo.maven.apache.org/maven2/" to "https://maven.aliyun.com/mvn/search",//
        "repo1.maven.org/maven2" to "https://maven.aliyun.com/repository/central",//
        "repository.apache.org/snapshots" to "https://maven.aliyun.com/repository/apache-snapshots",//

        "repo.grails.org/grails/core" to "https://maven.aliyun.com/repository/spring-plugin",//
        "repo.grails.org/grails/core" to "https://maven.aliyun.com/repository/grails-core",//

        "repo.spring.io/libs-milestone" to "https://maven.aliyun.com/repository/spring",//
        "repo.spring.io/plugins-release" to "https://maven.aliyun.com/repository/spring-plugin",//
)
val appendList: List<String> = listOf()
val logTag = "init.gradle.kts"
println("[$logTag]")

/*initscript {
    repositories { }
    dependencies {}
}*/

/*fun HandleRepos(container: ArtifactRepositoryContainer): List<String> {
    val rmList = mutableListOf<ArtifactRepository>()
    val addList = mutableListOf<String>()
    container.forEach { repo: ArtifactRepository ->
        if (repo is MavenArtifactRepository) {
            val url = repo.url.toString()
            println("[$logTag] ${repo.name}($url)")
            replaceDict.forEach { k, v ->
                if (url.contains(k)) {
                    println("[$logTag] $k -> $v")
                    rmList.add(repo)
                    addList.add(v)
                }
            }
        }
    }
    rmList.forEach {
        container.remove(it)
    }
    return addList
}*/

val repoConfig: RepositoryHandler.() -> Unit = {
    val rmList = mutableListOf<MavenArtifactRepository>()
    val addList = mutableListOf<String>()
    this.forEach { repo: ArtifactRepository ->
        if (!(repo is MavenArtifactRepository)) return@forEach
        val url = repo.url.toString()
        logger.info("[$logTag] \t${repo.name}(\"$url\")")
        replaceDict.forEach { k, v ->
            if (url.contains(k).not()) return@forEach
            logger.info("[$logTag] $k -> $v")
            rmList.add(repo)
            addList.add(v)
        }

    }
    rmList.forEach {
        println("[$logTag] \tRemove: ${it.name}(\"${it.url}\")")
        this.remove(it)
    }
    addList.addAll(appendList)
    addList.forEach {
        println("[$logTag] \t   Add: maven(\"$it\")")
        this.add(this.maven(it))
    }
}
settingsEvaluated {
    this.pluginManagement {
        repositories(repoConfig)
    }
    this.dependencyResolutionManagement {
        repositories(repoConfig)
    }
}
allprojects {

    buildscript {
        repoConfig(repositories)
    }
    repoConfig(repositories)
}