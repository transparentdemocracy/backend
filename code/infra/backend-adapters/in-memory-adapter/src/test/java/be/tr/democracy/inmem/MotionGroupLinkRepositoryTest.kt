package be.tr.democracy.inmem

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MotionGroupLinkRepositoryTest: AbstractRepositoryTest() {

    @Autowired
    lateinit var plenaryRepository: PlenaryRepository

    @Autowired
    lateinit var repository: MotionGroupRepository

    @Test
    fun find() {

    }
}