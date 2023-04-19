package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

// test
class PolicyServiceTest : BehaviorSpec({
    given("PolicyServiceTest") {
        `when`("context loads") {
            then("context loads successfully") {
                true shouldBe true
            }
        }
    }
})