package be.tr.democracy.query

import be.tr.democracy.vocabulary.motion.MotionGroup
import be.tr.democracy.vocabulary.page.Page
import be.tr.democracy.vocabulary.page.PageRequest

interface MotionGroupReadModel {
    fun find(searchTerm: String, pageRequest: PageRequest): Page<MotionGroup>

    fun getMotionGroup(motionId: String): MotionGroup?
}
