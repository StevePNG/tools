package com.stvpng.tool.example.querydsl;

import com.stvpng.tool.querydsl.extension.CustomRepositoryFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication
@Configurable
@EnableJpaRepositories(basePackages = {"com.stvpng.tool.example.querydsl"}, repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
@EntityScan(basePackages = {"com.stvpng.tool.example.domain"})
@Log
public class Configuration {

    @Bean(destroyMethod = "stop")
    @Profile("test-pg")
    public PostgresProcess postgresProcess() throws IOException {
        log.info("Starting embedded Postgres...");

        String tempDir = System.getProperty("java.io.tmpdir");
        String binariesDir = tempDir + "/pg_bin";

        PostgresConfig postgresConfig = new PostgresConfig(
                Version.V9_6_11,
                new AbstractPostgresConfig.Net("localhost", Network.getFreeServerPort()),
                new AbstractPostgresConfig.Storage("vortex"),
                new AbstractPostgresConfig.Timeout(60_000),
                new AbstractPostgresConfig.Credentials("usr1", "pwd1")
        );

        IRuntimeConfig iRuntimeConfig = EmbeddedPostgres.cachedRuntimeConfig(Paths.get(binariesDir));


        PostgresStarter<PostgresExecutable, PostgresProcess> runtime =
                PostgresStarter
                        .getInstance(iRuntimeConfig);
        PostgresExecutable exec = runtime.prepare(postgresConfig);
        PostgresProcess process = exec.start();

        return process;
    }

    @Bean(destroyMethod = "close")
    @DependsOn("postgresProcess")
    @Profile("test-pg")
    DataSource dataSource(PostgresProcess postgresProcess) {
        PostgresConfig postgresConfig = postgresProcess.getConfig();

        val config = new HikariConfig();
        config.setUsername(postgresConfig.credentials().username());
        config.setPassword(postgresConfig.credentials().password());
        config.setJdbcUrl("jdbc:postgresql://localhost:" + postgresConfig.net().port() + "/" + postgresConfig.storage().dbName());

        return new HikariDataSource(config);
    }
}
