
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent

    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        enableExperimentalRules.set(false)

        reporters {
            reporter(ReporterType.HTML)
            reporter(ReporterType.CHECKSTYLE)
        }

        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }

        dependencies {
            ktlintRuleset("io.nlopez.compose.rules:ktlint:0.3.21")
        }
    }
}

// Registers a task to copy git hooks
tasks.register("copyGitHooks", Copy::class.java) {
    description = "Copies the git hooks from /git-hooks to the .git folder."
    group = "git hooks"
    from("$rootDir/.hooks/pre-commit") // Source directory for git hooks
    into("$rootDir/.git/hooks/") // Destination directory for git hooks
}

// Registers a task to install git hooks
tasks.register("installGitHooks", Exec::class.java) {
    description = "Installs the pre-commit git hooks from /git-hooks."
    group = "git hooks"
    workingDir = rootDir // Working directory for the task
    commandLine = listOf("chmod") // Command to change file permissions
    args("-R", "+x", ".git/hooks/") // Arguments for the chmod command
    dependsOn("copyGitHooks") // Sets dependency on the copyGitHooks task
    doLast {
        logger.info(
            "Git hook installed successfully.",
        ) // Logs a message after the task is completed
    }
}

// Ensures git hooks are installed before the preBuild task
afterEvaluate {
    tasks.getByPath(":app:preBuild").dependsOn(":installGitHooks")
}