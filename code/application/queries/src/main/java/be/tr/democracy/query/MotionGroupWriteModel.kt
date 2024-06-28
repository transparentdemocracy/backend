package be.tr.democracy.query

import be.tr.democracy.vocabulary.motion.MotionGroup

interface MotionGroupWriteModel {
    // TODO bulk upserts for performance
    fun upsert(motionGroup: MotionGroup)
    fun deleteByPlenaryId(id: String) {
        TODO()
    }
}
