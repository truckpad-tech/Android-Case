package br.com.viniciuscoscia.truckpad.entity

import br.com.viniciuscoscia.truckpad.common.SearchResult
import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults

data class RepoResult(
    val searchResult: SearchResult,
    val calcResults: CalcResults? = null,
    val message: String = ""
)