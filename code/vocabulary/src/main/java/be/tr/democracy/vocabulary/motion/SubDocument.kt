package be.tr.democracy.vocabulary.motion

data class SubDocument(
    val documentNr: Int,
    val documentSubNr: Int,
    val summaryNL: String,
    val summaryFR: String,
) {
    val documentPdfURL: String
        get() = "https://www.dekamer.be/FLWB/PDF/55/%04d/55K%04d%03d.pdf".format(documentNr, documentNr, documentSubNr)
}
