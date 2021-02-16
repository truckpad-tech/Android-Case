package br.com.viniciuscoscia.truckpad.domain.usecases

import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults
import br.com.viniciuscoscia.truckpad.domain.repositories.ITruckPadRepository

class FindCalcResultsHistoryUseCase(private val iTruckPadRepository: ITruckPadRepository) :
    BaseUseCase<List<CalcResults>>() {
    override suspend fun execute(): List<CalcResults> {
        return iTruckPadRepository.getCalcResultsHistory()
    }
}