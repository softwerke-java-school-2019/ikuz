const api = axios.create({
    baseURL: '/api/',
    timeout: 5000
});

const deviceFilterOptions = ['modelName', 'modelNamePrefix', 'deviceType', 'colorName', 'colorRGB', 'pageItems', 'manufactureDate', 'price',
    'manufacturer', 'manufacturerPrefix', 'priceFrom', 'priceTo', 'manufactureDateFrom', 'manufactureDateTo'];
const deviceOrderOptions = ['modelName', 'deviceType', 'manufacturer', 'manufactureDate', '-manufactureDate', 'price', '-price'];

const customerFilterOptions = ['firstName', 'firstNamePrefix', 'middleName', 'middleNamePrefix', 'lastName', 'lastNamePrefix', 'pageItems',
    'birthdate', 'birthdateFrom', 'birthdateTo'];
const customerOrderOptions = ['firstName', 'middleName', 'lastName', 'birthdate', '-birthdate'];

const billFilterOptions = ['customerId', 'purchaseDateTime', 'purchaseDateTimeFrom', 'purchaseDateTimeTo', 'totalPrice', 'totalPriceFrom',
    'totalPriceTo', 'pageItems', 'deviceId', 'quantity', 'quantityFrom', 'quantityTo', 'price', 'priceFrom', 'priceTo'];
const billOrderOptions = ['customerId', 'purchaseDateTime', '-purchaseDateTime', 'totalPrice', '-totalPrice'];


function lockOtherProps(entity, isChosenId) {
    let inputForms = document.querySelectorAll('.form-input');
    let selectForms = document.querySelectorAll('.form-select');
    let buttons = document.querySelectorAll('.order-type-button');
    let idForm = document.querySelector('#' + entity + '-id');
    if (isChosenId) {
        for (let i = 0; i < inputForms.length; i++) {
            if (inputForms[i] !== idForm) {
                inputForms[i].setAttribute('disabled', 'disabled');
            }
        }
        for (let i = 0; i < selectForms.length; i++) {
            selectForms[i].setAttribute('disabled', 'disabled');
        }
        for (let i = 0; i < buttons.length; i++) {
            buttons[i].setAttribute('disabled', 'disabled');
        }
        idForm.removeAttribute('disabled');
        let datesToValidate = document.body.querySelectorAll('.date');
        let timesToValidate = document.body.querySelectorAll('.dateTime');
        for (let i = 0; i < datesToValidate.length; i++) {
            datesToValidate[i].classList.remove('correct-field');
            datesToValidate[i].classList.remove('incorrect-field');
        }
        for (let i = 0; i < timesToValidate.length; i++) {
            timesToValidate[i].classList.remove('correct-field');
            timesToValidate[i].classList.remove('incorrect-field');
        }
    } else {
        for (let i = 0; i < inputForms.length; i++) {
            if (inputForms[i] !== idForm) {
                inputForms[i].removeAttribute('disabled');
            }
            for (let i = 0; i < selectForms.length; i++) {
                selectForms[i].removeAttribute('disabled');
            }
            for (let i = 0; i < buttons.length; i++) {
                buttons[i].removeAttribute('disabled');
            }
        }
        idForm.setAttribute('disabled', 'disabled');
        validateGetProperty('date');
        validateGetProperty('time');
    }
}

function validateGetProperty(prop) {
    let propsToValidate;
    if (prop === 'time') {
        propsToValidate = document.body.querySelectorAll('.dateTime');
    } else if (prop === 'date') {
        propsToValidate = document.body.querySelectorAll('.date');
    }
    for (let i = 0; i < propsToValidate.length; i++) {
        validatePropById(propsToValidate[i].getAttribute('id'));
        if (propsToValidate[i].value === '') {
            propsToValidate[i].classList.add('correct-field');
            propsToValidate[i].classList.remove('incorrect-field');
        }
    }
}

function addOrderType(entity, buttonNode) {
    let newOrderType = document.createElement('div');
    let entityOrderOptions;
    switch (entity) {
        case 'device':
            entityOrderOptions = deviceOrderOptions;
            break;
        case 'customer':
            entityOrderOptions = customerOrderOptions;
            break;
        case 'bill':
            entityOrderOptions = billOrderOptions;
            break;
    }
    let orderOptionsHTML = '';
    for (let i = 0; i < entityOrderOptions.length; i++) {
        orderOptionsHTML += ('<option>' + entityOrderOptions[i] + '</option>');
    }
    newOrderType.innerHTML = `
            <br>
            <label class="title">Order Type:
                <select class="form-select order-type" autocomplete="off">`
                    + orderOptionsHTML +
                `</select>
            </label>`;
    newOrderType.classList.add('order-type-wrapper');
    buttonNode.parentNode.insertBefore(newOrderType, buttonNode);
}

function removeOrderType(buttonNode) {
    let orderTypes = buttonNode.parentNode.querySelectorAll('.order-type-wrapper');
    if (orderTypes.length > 0) {
        orderTypes[orderTypes.length - 1].remove();
    }
}

function sendGetFrom(entity) {
    if (checkValidityOfFields()) {
        alert('Invalid data entry detected!');
        return;
    }
    let queryString;
    let typeOfGetQueryNode = document.body.querySelector('.form-select-main');
    let typeOfGetQuery = typeOfGetQueryNode.options[typeOfGetQueryNode.selectedIndex].value;
    let responseOnQuery = document.querySelector('#response');
    let waitingResponse = document.querySelector('#waiting-for-answer');
    waitingResponse.classList.add('loading');
    if (typeOfGetQuery === 'Filter by Id') {
        queryString = entity + '/' + document.body.querySelector('#' + entity + '-id').value;
    } else {
        queryString = entity + '?';
        let pageSelectNode = document.body.querySelector('.page-offset-select');
        let pageOffsetNode = document.body.querySelector('.page-offset');
        let ampersand = '';
        if (pageOffsetNode.value !== '') {
            if (pageSelectNode.options[pageSelectNode.selectedIndex].value === 'Page') {
                queryString += 'page';
            } else {
                queryString += 'offset';
            }
            queryString += ('=' + pageOffsetNode.value);
            ampersand = '&';
        }
        let filterOptions;
        switch (entity) {
            case 'device':
                filterOptions = deviceFilterOptions;
                break;
            case 'customer':
                filterOptions = customerFilterOptions;
                break;
            case 'bill':
                filterOptions = billFilterOptions;
                break;
        }
        for (let i = 0; i < filterOptions.length; i++) {
            let filterValue = document.body.querySelector('#' + filterOptions[i]).value;
            if (filterValue !== '') {
                queryString += (ampersand + filterOptions[i] + '=');
                queryString += filterValue;
                ampersand = '&';
            }
        }
        let orderType = document.body.querySelectorAll('.order-type');
        if (orderType.length !== 0) {
            queryString += (ampersand + 'orderBy=');
            let comma = '';
            for (let j = 0; j < orderType.length; j++) {
                queryString += (comma + orderType[j].options[orderType[j].selectedIndex].value);
                comma = ',';
            }
        }
        if (queryString[queryString.length - 1] === '?') {
            queryString = queryString.substring(0, queryString.length - 1);
        }
    }
    api.get(queryString).then(response => {
        responseOnQuery.textContent = JSON.stringify(response.data, null, 2);
    }).catch(error => {
        if (error.response) {
            responseOnQuery.textContent = JSON.stringify(error.response.data, null, 2)
        } else {
            responseOnQuery.textContent = error.toString()
        }
    }).finally(() => {
            waitingResponse.classList.remove('loading')
        }
    )
}
