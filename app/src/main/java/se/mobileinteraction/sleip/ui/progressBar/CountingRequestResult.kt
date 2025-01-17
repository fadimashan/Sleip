package se.mobileinteraction.sleip.ui.progressBar

sealed class CountingRequestResult<ResultT> {
    data class Progress<ResultT>(
        val progressFraction: Double
    ) : CountingRequestResult<ResultT>()

    data class Completed<ResultT>(
        val result: ResultT
    ) : CountingRequestResult<ResultT>()
}