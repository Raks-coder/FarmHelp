package com.tarp.farmcare.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class SignUpFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    SignUpFormState(@Nullable Integer firstNameError, @Nullable Integer lastNameError, @Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.isDataValid = false;
    }

    SignUpFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.firstNameError = null;
        this.lastNameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getFirstNameError() {
        return firstNameError;
    }

    @Nullable
    Integer getLastNameError() {
        return lastNameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
