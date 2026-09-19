# Repository Guidelines

## Project Structure & Module Organization

This is a Kotlin Multiplatform project. The published library is in `library/`:

- `library/src/commonMain/kotlin` contains shared form, field, component, formatter, and validator code.
- `library/src/commonTest/kotlin` contains the library’s Kotlin tests.
- `app/composeApp` contains the Compose Multiplatform demonstration app, with platform-specific code in `androidMain`, `iosMain`, and `desktopMain` and shared code in `commonMain`.
- `app/iosApp` contains the native iOS application entry point.
- `images/` and `screenshots/` contain README and project assets.

Keep reusable behavior in `library`; update the demo only when demonstrating or validating library behavior.

## Build, Test, and Development Commands

Use the checked-in Gradle wrapper from the repository root:

```bash
./gradlew build
./gradlew :library:build
```

`build` performs the configured project build, while the library task focuses on the publishable module and its tests. Open the project in Android Studio or IntelliJ IDEA for Compose Multiplatform and iOS development; the iOS target also requires Xcode and a macOS environment.

## Coding Style & Naming Conventions

Use Kotlin conventions: four-space indentation, trailing commas in multiline declarations, `UpperCamelCase` for classes and composables, and `lowerCamelCase` for functions, properties, and local variables. Name validators with the `Validator` suffix and platform source sets with the existing `commonMain`, `androidMain`, `iosMain`, and `desktopMain` patterns. Follow the surrounding code and Gradle Kotlin DSL style; no separate formatter or linter configuration is currently checked in.

## Testing Guidelines

Add unit tests under `library/src/commonTest/kotlin`, mirroring the production package structure and using descriptive `*Test.kt` names (for example, `EmailValidatorTest.kt`). Extend tests for every behavior change, especially validation edge cases. Run `./gradlew :library:build` before submitting changes.

## Commit & Pull Request Guidelines

Recent commits use short, imperative summaries such as `Clean up`, `Rename to camper`, and `Update project with upstream`. Keep commits focused and use the same concise style. Pull requests should explain the user-visible or library-facing change, identify verification commands run, link related issues when applicable, and include screenshots or recordings for UI changes.

## Security & Configuration Tips

Do not commit credentials or repository URLs containing secrets. Publishing uses local Gradle properties such as `project.repoUsername`, `project.repoPassword`, `project.snapshotUrl`, and `project.releaseUrl`; keep those values in local configuration only.
