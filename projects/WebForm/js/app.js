const firstnameEl = document.querySelector('#firstname');
const lastnameEl = document.querySelector('#lastname');
const suburbEl = document.querySelector('#suburb');
const phoneEl = document.querySelector('#phone');
const emailEl = document.querySelector('#email');

const form = document.querySelector('#signup');



/* Reusable utility functions */
// Required field
const isRequired = value => value === '' ? false : true;

// The length of a field is between min and max.
const isBetween = (length, min, max) => length < min || length > max ? false : true;

// The email is in a valid format.
const isEmailValid = (email) => {
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
};

const isPhoneValid = (phone) => {
    const phoneno = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
    return phoneno.test(phone);
};




// Highlights the border of the input field and displays an error message if the input field is invalid.
const showError = (input, message) => {
    // get the form-field element
    const formField = input.parentElement;
    // add the error class
    formField.classList.remove('success');
    formField.classList.add('error');

    // show the error message
    const error = formField.querySelector('small');
    error.textContent = message;
};

// Highlights the border of the input field and displays a success message if the input field is valid.
const showSuccess = (input) => {
    // get the form-field element
    const formField = input.parentElement;

    // remove the error class
    formField.classList.remove('error');
    formField.classList.add('success');

    // hide the error message
    const error = formField.querySelector('small');
    error.textContent = '';
}




// Validate the firstname field
const checkFirstName = () => {

    let valid = false;
    const min = 3,
        max = 25;
    const firstname = firstnameEl.value.trim();

    if (!isRequired(firstname)) {
        showError(firstnameEl, 'First name cannot be blank.');
    } else if (!isBetween(firstname.length, min, max)) {
        showError(firstnameEl, `First name must be between ${min} and ${max} characters.`)
    } else {
        showSuccess(firstnameEl);
        valid = true;
    }
    return valid;
}

// Validate the lastname field
const checkLastName = () => {

    let valid = false;
    const min = 3,
        max = 25;
    const lastname = lastnameEl.value.trim();

    if (!isRequired(lastname)) {
        showError(lastnameEl, 'Last name cannot be blank.');
    } else if (!isBetween(lastname.length, min, max)) {
        showError(lastnameEl, `Last name must be between ${min} and ${max} characters.`)
    } else {
        showSuccess(lastnameEl);
        valid = true;
    }
    return valid;
}

// Validate the suburb field
const checkSuburb = () => {

    let valid = false;
    const min = 3,
        max = 25;
    const suburb = suburbEl.value.trim();

    if (!isRequired(suburb)) {
        showError(suburbEl, 'Suburb cannot be blank.');
    } else if (!isBetween(suburb.length, min, max)) {
        showError(suburbEl, `Suburb must be between ${min} and ${max} characters.`)
    } else {
        showSuccess(suburbEl);
        valid = true;
    }
    return valid;
}

// Validate the suburb field
const checkPhone = () => {

    let valid = false;
    const min = 10,
        max = 20;
    const phone = phoneEl.value.trim();

    if (!isRequired(phone)) {
        showError(phoneEl, 'Phone number cannot be blank.');
    } else if (!isPhoneValid(phone)) {
        showError(phoneEl, `Phoner number is not valid.`)
    } else if (!isBetween(phone.length, min, max)) {
        showError(phoneEl, `Phone must be between ${min} and ${max} characters.`)
    }
    else {
        showSuccess(phoneEl);
        valid = true;
    }
    return valid;
}

// Validate the email field
const checkEmail = () => {
    let valid = false;
    const email = emailEl.value.trim();
    if (!isRequired(email)) {
        showError(emailEl, 'Email cannot be blank.');
    } else if (!isEmailValid(email)) {
        showError(emailEl, 'Email is not valid.')
    } else {
        showSuccess(emailEl);
        valid = true;
    }
    return valid;
}

// Prevent the form from submitting once the submit button is clicked.
form.addEventListener('submit', function (e) {
    // prevent the form from submitting
    e.preventDefault();

    // validate forms
    let isFirstNameValid = checkFirstName(),
        isLastNameValid = checkLastName(),
        isSuburbValid = checkSuburb(),
        isPhoneValid = checkPhone(),
        isEmailValid = checkEmail();

    let isFormValid = isFirstNameValid &&
        isLastNameValid &&
        isSuburbValid &&
        isPhoneValid &&
        isEmailValid;

    // submit to the server if the form is valid and redirect to Photo with Santa page.
    if (isFormValid) {
        var form = document.getElementById("signup");

            event.preventDefault();
            var status = document.getElementById("my-form-status");
            var data = new FormData(event.target);
            fetch(event.target.action, {
                method: form.method,
                body: data,
                headers: {
                    'Accept': 'application/json'
                }
            }).then(response => {
                status.innerHTML = "Thanks for your submission!";
                window.location.href = "http://photo.mallmedia.net/"
            }).catch(error => {
                status.innerHTML = "Oops! There was a problem submitting your form."
            });
        }
        form.addEventListener("submit", handleSubmit)

});

// Wait for the users to pause the typing for a small amount of time or stop typing before validating the input.
const debounce = (fn, delay = 900) => {
    let timeoutId;
    return (...args) => {
        // cancel the previous timer
        if (timeoutId) {
            clearTimeout(timeoutId);
        }
        // setup a new timer
        timeoutId = setTimeout(() => {
            fn.apply(null, args)
        }, delay);
    };
};

// Check which input user is at and use debounce function.
form.addEventListener('input', debounce(function (e) {
    switch (e.target.id) {
        case 'firstname':
            checkFirstName();
            break;
        case 'lastname':
            checkLastName();
            break;
        case 'suburb':
            checkSuburb();
            break;
        case 'phone':
            checkPhone();
            break;
        case 'email':
            checkEmail();
            break;
    }
}));