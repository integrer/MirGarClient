package org.mirgar.android.cryptoelection.viewmodel

import android.util.Patterns.EMAIL_ADDRESS
import android.util.Patterns.PHONE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import org.mirgar.android.common.viewmodel.MessagingViewModel
import org.mirgar.android.cryptoelection.model.CreateParticipant
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.usecase.CreateParticipantUseCase
import java.util.regex.Pattern

class RegistrationViewModel(
    private val useCase: CreateParticipantUseCase,
    private val operationHandler: BaseOperationHandler
) : MessagingViewModel() {

    private fun initializeRequiredFields() {
        name.value ?: name.setValue("")
        email.value ?: email.setValue("")
        passCode.value ?: passCode.setValue("")
        phoneNumber.value ?: phoneNumber.setValue("")
    }

    enum class ValidationError {
        EmptyField,
        InvalidEmail,
        InvalidPhone,
    }

    lateinit var mapErr: (ValidationError) -> CharSequence?

    val name = MutableLiveData<String?>()
    val nameError: LiveData<CharSequence?>

    val email = MutableLiveData<String?>()
    val emailError: LiveData<CharSequence?>

    val passCode = MutableLiveData<String?>()
    val passCodeError: LiveData<CharSequence?>

    val phoneNumber = MutableLiveData<String?>()
    val phoneNumberError: LiveData<CharSequence?>

    //residence

    init {
        fun CharSequence.matches(pattern: Pattern) = pattern.matcher(this).matches()

        fun <T> mapErr(v: T?, check: (T) -> ValidationError?) = v?.let(check)?.let(mapErr)

        fun checkIsEmpty(it: CharSequence) = if (it.isBlank()) ValidationError.EmptyField else null

        fun CharSequence.matchOr(pattern: Pattern, err: ValidationError) =
            checkIsEmpty(this) ?: if (!matches(pattern)) err else null

        nameError = name.map { mapErr(it, ::checkIsEmpty) }
        emailError = email.map { input ->
            mapErr(input) { it.matchOr(EMAIL_ADDRESS, ValidationError.InvalidEmail) }
        }
        passCodeError = passCode.map { mapErr(it, ::checkIsEmpty) }
        phoneNumberError = phoneNumber.map { input ->
            mapErr(input) { it.matchOr(PHONE, ValidationError.InvalidEmail) }
        }
    }


    private val hasErrors
        get() = nameError.value != null || emailError.value != null
                || passCodeError.value != null || phoneNumberError.value != null

    val model: LiveData<CreateParticipant> = MediatorLiveData<CreateParticipant>().apply {
        fun <T> addSource(source: LiveData<T>, map: (T, CreateParticipant) -> CreateParticipant) {
            this.addSource(source) { value = map(it, value ?: CreateParticipant()) }
        }
        addSource(name) { v, orig -> v?.let { orig.copy(name = it) } ?: orig }
        addSource(email) { v, orig -> v?.let { orig.copy(email = it) } ?: orig }
        addSource(passCode) { v, orig -> v?.let { orig.copy(passCode = it) } ?: orig }
        addSource(phoneNumber) { v, orig -> v?.let { orig.copy(phoneNumber = it) } ?: orig }
    }

    fun register() {
        initializeRequiredFields()
        if (hasErrors) return
        useCase.model = model.value ?: throw IllegalStateException("Model must be initialized")

        operationHandler.withHandler { useCase() }
    }
}