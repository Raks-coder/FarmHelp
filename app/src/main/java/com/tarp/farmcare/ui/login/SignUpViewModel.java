package com.tarp.farmcare.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tarp.farmcare.R;
import com.tarp.farmcare.data.LoginRepository;
import com.tarp.farmcare.data.Result;
import com.tarp.farmcare.data.SignUpRepository;
import com.tarp.farmcare.data.model.LoggedInUser;

public class SignUpViewModel extends ViewModel {

    private MutableLiveData<SignUpFormState> signUpFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private SignUpRepository signUpRepository;

    SignUpViewModel(SignUpRepository loginRepository) {
        this.signUpRepository = loginRepository;
    }

    LiveData<SignUpFormState> getSignUpFormState() {
        return signUpFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void signUp(String firstName, String lastName, String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = signUpRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void signUpDataChanged(String firstName, String lastName, String username, String password) {
        if (!isUserNameValid(username)) {
            signUpFormState.setValue(new SignUpFormState(null, null, R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            signUpFormState.setValue(new SignUpFormState(null, null,null, R.string.invalid_password));
        } else if (!isFirstNameValid(firstName)) {
            signUpFormState.setValue(new SignUpFormState(R.string.invalid_first, null,null, null));
        } else if (!isLastNameValid(firstName)) {
            signUpFormState.setValue(new SignUpFormState(null, R.string.invalid_last,null, null));
        } else {
            signUpFormState.setValue(new SignUpFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isFirstNameValid(String password) {
        return password != null && password.trim().length() > 1;
    }

    private boolean isLastNameValid(String password) {
        return password != null && password.trim().length() > 1;
    }
}
