const datePattern = '^(((0[1-9]|[12]\\d|3[01]).(0[13578]|1[02]).((19|[2-9]\\d)\\d{2}))' +
    '|((0[1-9]|[12]\\d|30).(0[13456789]|1[012]).((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8]).02.((19|[2-9]\\d)' +
    '\\d{2}))|(29.02.((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$';
const timePattern = '^(((0[1-9]|[12]\\d|3[01]).(0[13578]|1[02]).((19|[2-9]\\d)\\d{2}))' +
    '|((0[1-9]|[12]\\d|30).(0[13456789]|1[012]).((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8]).02.((19|[2-9]\\d)' +
    '\\d{2}))|(29.02.((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)))) ' +
    '(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)$';

let colors = [];

function validatePropById(prop) {
    let selectorProp = document.querySelector('#' + prop);
    selectorProp.classList.remove('correct-field');
    selectorProp.classList.add('incorrect-field');
    if (selectorProp.classList.contains('date')) {
        let currentDate = new Date();
        let inputDateByString = selectorProp.value;

        let pattern = new RegExp(datePattern);
        if (!pattern.test(inputDateByString)) {
            return
        }
        const [day, month, year] = inputDateByString.split(".");
        let inputDate = new Date(year, month - 1, day);
        if (inputDate <= currentDate) {
            selectorProp.classList.remove('incorrect-field');
            selectorProp.classList.add('correct-field');
        }
    } else if (selectorProp.classList.contains('dateTime')) {
        let currentDate = new Date();
        let inputDateTimeByString = selectorProp.value;
        let pattern = new RegExp(timePattern);
        if (!pattern.test(inputDateTimeByString)) {
            return
        }
        let splitDateTimeByString = inputDateTimeByString.split(" ");
        const [day, month, year] = splitDateTimeByString[0].split(".");
        const [hour, minutes, seconds] = splitDateTimeByString[1].split(":");
        let inputDateTime = new Date(year, month - 1, day, hour, minutes, seconds);
        if (inputDateTime <= currentDate) {
            selectorProp.classList.remove('incorrect-field');
            selectorProp.classList.add('correct-field');
        }
    }

}


function findEntityWithId(idNode, entity) {
    let errorMessage = idNode.parentNode.querySelector('.error-message');
    if (errorMessage !== null) {
        errorMessage.parentNode.removeChild(errorMessage);
    }
    if (idNode.value === '') {
        idNode.classList.remove('correct-field');
        return
    }
    if (idNode.validity.valid) {
        api.get(entity + '/' + idNode.value)
            .then(() => {
                idNode.classList.remove('incorrect-field');
                idNode.classList.add('correct-field');
            })
            .catch(() => {
                idNode.classList.remove('correct-field');
                idNode.classList.add('incorrect-field');
                let errorMessage = document.createElement('div');
                errorMessage.innerHTML = entity + ' with id \'' + idNode.value + '\' doesn\'t exist!';
                errorMessage.classList.add('error-message');
                idNode.parentNode.appendChild(errorMessage);
            });
    }
}

function initProps(prop) {
    let response = document.querySelector('#response');
    let list;
    let queryProp;
    switch (prop) {
        case 'type':
            list = document.querySelector('.device-type-list');
            queryProp = 'type';
            break;
        case 'color':
            list = document.querySelector('.device-color-list');
            queryProp = 'color';
            break;
        case 'colorRGB':
            list = document.querySelector('.device-colorRGB-list');
            queryProp = 'color';
            break;
    }
    let property = list.lastElementChild;
    while (property) {
        list.removeChild(property);
        property = list.lastElementChild;
    }
    api.get('device/' + queryProp)
        .then(response => {
            let namesOfProp = Array.from(response.data);
            if (prop === 'color') {
                colors = namesOfProp;
            }
            for (let i = namesOfProp.length - 1; i >= 0; i--) {
                let newPropName = document.createElement('option');
                switch (prop) {
                    case 'color':
                        newPropName.innerHTML = namesOfProp[i].colorName;
                        break;
                    case 'colorRGB':
                        newPropName.innerHTML = namesOfProp[i].colorRGB;
                        break;
                    case 'type':
                        newPropName.innerHTML = namesOfProp[i];
                        break;
                }
                list.insertBefore(newPropName, list.firstElementChild);
            }
        }).catch(error => {
        if (error.response) {
            response.textContent = JSON.stringify(error.response.data, null, 2)
        } else {
            response.textContent = error.toString()
        }
    });
}

function ifColorExistLockRGB(query) {
    let colorRGBNode = document.querySelector('#colorRGB');
    colorRGBNode.removeAttribute('disabled');
    colorRGBNode.removeAttribute('required');
    colorRGBNode.value = '';
    for (let i = 0; i < colors.length; i++) {
        if (colors[i].colorName === document.querySelector('#colorName').value) {
            colorRGBNode.value = colors[i].colorRGB;
            colorRGBNode.setAttribute('disabled', 'disabled');
            break;
        }
    }
    if (query === 'post') {
        colorRGBNode.setAttribute('required', 'required')
    }
}

function checkValidityOfFields() {
    return (document.querySelectorAll(':invalid').length > 0
        || document.querySelectorAll('.incorrect-field').length > 0);
}