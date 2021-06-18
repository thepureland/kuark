package io.kuark.ability.rules.context

import org.kie.api.KieBase
import org.kie.api.KieServices
import org.kie.api.builder.KieFileSystem
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.KieSession
import org.kie.internal.io.ResourceFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import java.io.IOException


@Configuration
open class KiaSessionConfig {

    @Bean
    @Throws(IOException::class)
    open fun kieFileSystem(): KieFileSystem {
        val kieFileSystem = kieServices.newKieFileSystem()
        for (file in ruleFiles) {
            kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + file.filename, "UTF-8"))
        }
        return kieFileSystem
    }

    @get:Throws(IOException::class)
    private val ruleFiles: Array<Resource>
        private get() {
            val resourcePatternResolver: ResourcePatternResolver = PathMatchingResourcePatternResolver()
            return resourcePatternResolver.getResources("classpath*:$RULES_PATH**/*.*")
        }

    @Bean
    @Throws(IOException::class)
    open fun kieContainer(): KieContainer {
        val kieRepository = kieServices.repository
        kieRepository.addKieModule { kieRepository.defaultReleaseId }
        val kieBuilder = kieServices.newKieBuilder(kieFileSystem())
        kieBuilder.buildAll()
        return kieServices.newKieContainer(kieRepository.defaultReleaseId)
    }

    private val kieServices: KieServices
        private get() = KieServices.Factory.get()

    @Bean
    @Throws(IOException::class)
    open fun kieBase(): KieBase {
        return kieContainer().kieBase
    }

    @Bean
    @Throws(IOException::class)
    open fun kieSession(): KieSession {
        return kieContainer().newKieSession()
    }

    companion object {
        private const val RULES_PATH = "rules/"
    }

}
