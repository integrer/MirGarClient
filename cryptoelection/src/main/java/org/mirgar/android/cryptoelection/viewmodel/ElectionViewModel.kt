package org.mirgar.android.cryptoelection.viewmodel

import androidx.lifecycle.MutableLiveData
import com.exonum.binding.common.crypto.KeyPair
import org.mirgar.android.common.model.PollModel
import org.mirgar.android.cryptoelection.data.ElectionsStorage
import org.mirgar.android.cryptoelection.data.SharedPreferencesManager
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.operations.OperationResult
import org.mirgar.android.cryptoelection.usecase.GetElectionResultsUseCase
import org.mirgar.android.cryptoelection.usecase.VoteUseCase
import org.mirgar.android.cryptoelection.utils.toPublicKey
import org.mirgar.android.common.viewmodel.PollViewModel as BasePollViewModel

class ElectionViewModel(
    override val id: String, electionsStorage: ElectionsStorage,
    private val voteUseCase: VoteUseCase, private val handler: BaseOperationHandler,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val getElectionResultsUseCase: GetElectionResultsUseCase
) : BasePollViewModel<String, Int>() {
    private val _model = MutableLiveData<PollModel<Int>>(
        (electionsStorage[id] ?: throw IllegalStateException()).body
    )
    override val model = _model

    override fun update() {
        TODO("Not yet implemented")
    }

    fun updateVotes(setVotedYet: Boolean = false) {
        handler.withHandler {
            getElectionResultsUseCase.key = id
            val result = getElectionResultsUseCase()
            if (result is OperationResult.ElectionResultsObtained) {
                val election = _model.value ?: throw IllegalStateException()
                _model.postValue(Companion.update(election, result.value, setVotedYet))
                return@withHandler OperationResult.Updated
            }
            throw IllegalStateException("Unexpected returning value")
        }
    }

    override fun vote(optionId: Int) {
        voteUseCase.electionId = id
        voteUseCase.optionId = optionId
        val privateKey = sharedPreferencesManager.privateKey ?: throw IllegalStateException()
        voteUseCase.keys = KeyPair.newInstance(privateKey, privateKey.toPublicKey())
        handler.withHandler { voteUseCase() }
    }

    companion object {
        private fun update(
            model: PollModel<Int>, results: Map<Int, Int>, setVotedYet: Boolean
        ): PollModel<Int> {
            val options = model.optionModels.map {
                val result = results.getOrDefault(it.id, 0)
                it.copy(votes = result, isNormalized = false)
            }
            val newModel = model.copy(optionModels = options)
            if (setVotedYet) return newModel.copy(showResults = true, canVote = false)
            return newModel
        }
    }
}