package be.tr.democracy.rest.plenary

import java.time.LocalDate

data class UpsertPlenaryJsonRequest(
    val number: Int?,
    val legislature: Int,
    val date: LocalDate,
    val proposal_discussions: List<ProposalDiscussion>?,
    val motion_groups: List<MotionGroup>,
) {

    data class ProposalDiscussion(
        val id: String?,
        val plenary_agenda_item_number: Int?,
        val plenary_id: String?,
        val description_nl: String?,
        val description_nl_tags: List<String>?,
        val description_fr: String?,
        val description_fr_tags: List<String>?,
        val proposals: List<Proposal>?,
    )

    data class Proposal(
        val id: String?,
        val documents_reference: String?,
        val title_nl: String?,
        val title_fr: String?,
    )

    data class MotionGroup(
        val id: String?,
        val plenary_agenda_item_number: Int?,
        val title_nl: String?,
        val title_fr: String?,
        val documents_reference: String?,
        val proposal_discussion_id: String?,
        val motions: List<Motion>?,
    )

    data class Motion(
        val id: String?,
        val sequence_number: Int?,
        val title_nl: String?,
        val title_fr: String?,
        val description: String?,
        val documents_reference: String?,
        val voting_id: String?,
        val proposal_id: String?,
        val cancelled: Boolean?,
    )


}