# NEUROVAULT: Chemistry Memory Universe

**NeuroVault** is a sci-fi inspired, interactive chemistry learning and memory training Android application designed for foundational pharmacy students (B.Pharm) and STEM enthusiasts. The application bridges active recall, 2D particle simulation, ionic compound synthesis, and syllabus mastery based on the Pharmacy Council of India (PCI) B.Pharm curriculum.

---

## 🚀 Key Highlights & Architecture

1. **Deterministic Daily Cognitive Quest**
   - Reproducible daily puzzle generated from the device's local calendar date (`yyyy-MM-dd`).
   - Strict one-time reward (+250 Pts) enforcement in persistent storage (`StorageManager.completeDailyChallenge()`).
   - Anti-exploit protection against recomposition, device rotation, screen replay, or date rollback.

2. **Formula Forge Ionic Synthesis**
   - Synthesize chemically neutral compounds from cations and anions with live electrical neutrality balance (`Σ q⁺ + Σ q⁻ = 0`).
   - Strict chemical formula parsing and validation via `ChemistryEngine`.
   - Normalization of Unicode vs. ASCII representations (e.g., `MgCl₂` vs. `MgCl2`) to prevent duplicate point farming.
   - Distinct, one-time reward (+50 Pts) per unique valid synthesized compound.

3. **Spaced Repetition Review (SuperMemo-2 / Leitner Hybrid)**
   - Algorithmic spaced retrieval schedule managed by `AdaptiveEngine.calculateNextReview()`.
   - Dynamic intervals (`1 day -> 3 days -> 7 days -> 14 days -> 30 days`) with rapid 4-hour re-test upon forgotten recall.
   - Only schedules concepts the student has actively encountered in lessons and quizzes.
   - Active recall flow: prompt and question presented first before answer/formula reveal.
   - Independent bookmarking system that never alters or interferes with spaced review intervals.

4. **Pharmacy Council of India (PCI) Syllabus Alignment**
   - Aligned with the PCI curriculum (Semester I - III) per NEP 2020:
     - **BP106T (VERIFIED)**: Pharmaceutical Inorganic and Analytical Chemistry (Impurities, Acid-Base Titrations, Antacids, Electrolytes & Saline).
     - **BP202T (PROVISIONAL)**: Pharmaceutical Organic Chemistry I.
     - **BP302T (PROVISIONAL)**: Physical Pharmaceutics I (pH, Buffers, Tonicity).
   - Full bilingual support in English and Bengali (বাংলা).

5. **2D Interactive Simulations & Visual Learning**
   - Interactive Atom Assembler (Canvas-rendered proton/neutron/electron orbital simulation).
   - Visual calculation of Atomic Number ($Z$), Mass Number ($A$), net ionic charge, and electron shells ($K, L, M$).

---

## 🛠️ Tech Stack

- **Platform**: Android (Min SDK 24, Target SDK 36)
- **UI Framework**: Jetpack Compose with Material 3 & Custom Neon Sci-Fi Design System
- **Language**: Kotlin 2.0+ (Coroutines, StateFlow, Serialization)
- **Architecture**: Single-Activity Jetpack Compose with deterministic State Hoisting
- **Storage**: SharedPreferences & JSON state serialization (`StorageManager`)
- **Testing**: JUnit 4, Robolectric 4.14+ (SDK 36 unit and integration test suite)
- **Build System**: Gradle Kotlin DSL (`build.gradle.kts`) with Secrets Gradle Plugin

---

## 🧪 Verification & Unit Tests

The test suite covers:
- **`ChemistryEngineTest`**:
  - Valid chemical formula parsing (e.g., `NaCl`, `MgCl₂`, `Al₂(SO₄)₃`, `Ca(OH)₂`).
  - Strict invalid formula rejection (zero counts, unmatched parentheses, unknown element symbols, illegal characters).
  - Reaction stoichiometry balancing verification.
- **`SpacedReviewTest`**:
  - Success progression of intervals and repetition counts.
  - Mistake handling and interval reset.
  - Due card filtering based on current timestamp.
- **`ExampleRobolectricTest`**:
  - Anti-exploit verification for Daily Challenge rewards.
  - Multi-day calendar lifecycle and restored state verification.
  - Unique Formula Forge synthesis rewards and canonical Unicode/ASCII deduplication.
  - Encountered concept progression and bookmark independence.
  - App settings persistence.

To execute tests locally:
```bash
./gradlew testDebugUnitTest
```

To build the debug APK:
```bash
./gradlew assembleDebug
```
The resulting APK is generated at `app/build/outputs/apk/debug/app-debug.apk` and mirrored to `.build-outputs/app-debug.apk`.
