package be.tr.democracy.query

import be.tr.democracy.vocabulary.motion.MotionGroup

interface MotionGroupWriteModel {
    fun upsert(motionGroup: MotionGroup)
}
