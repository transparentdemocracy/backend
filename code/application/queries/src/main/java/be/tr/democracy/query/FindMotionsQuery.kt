package be.tr.democracy.query

import be.tr.democracy.api.FindMotions
import be.tr.democracy.api.GetMotionGroup
import be.tr.democracy.vocabulary.motion.MotionGroup
import be.tr.democracy.vocabulary.page.Page
import be.tr.democracy.vocabulary.page.PageRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.Objects
import java.util.Optional

class FindMotionsQuery(motionGroupReadModel: MotionGroupReadModel) : FindMotions, GetMotionGroup {
    private val motionGroupReadModel: MotionGroupReadModel
    private val logger: Logger = LoggerFactory.getLogger(FindMotionsQuery::class.java)

    init {
        Objects.requireNonNull(motionGroupReadModel)
        this.motionGroupReadModel = motionGroupReadModel
    }

    override fun findMotions(searchTerm: String, pageRequest: PageRequest): Page<MotionGroup> {
        val motionPage = motionGroupReadModel.find(searchTerm, pageRequest)
        val sorted = motionPage.sortedPage(motionComparator)
        logger.trace("Loaded page {} motionsGroups from database for searchTerm {}", sorted.pageNr, searchTerm)
        return sorted
    }

    override fun getMotionGroup(motionId: String): Optional<MotionGroup> {
        return Optional.ofNullable(motionGroupReadModel.getMotionGroup(motionId))
    }

    companion object {
        private val motionComparator = java.util.Comparator { o1: MotionGroup, o2: MotionGroup ->
            val first = LocalDate.parse(o1.voteDate)
            val second = LocalDate.parse(o2.voteDate)
            second.compareTo(first)
        }
    }
}
