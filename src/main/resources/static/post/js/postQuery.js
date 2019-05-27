const api = axios.create({
    baseURL: '/api/',
    timeout: 5000
});

const deviceProps = ['deviceType', 'modelName', 'manufacturer', 'colorName', 'colorRGB', 'manufactureDate', 'price'];
const customerProps = ['firstName', 'middleName', 'lastName', 'birthdate'];
const billProps = ['customerId', 'items'];
const billItemProps = ['deviceId', 'quantity', 'price'];

function sendPostForm(entity) {
    if (checkValidityOfFields()) {
        alert('Invalid data entry detected!');
        return;
    }
    let responseOnQuery = document.querySelector('#response');
    let waitingResponse = document.querySelector('#waiting-for-answer');
    waitingResponse.classList.add('loading');
    let jsonPost = {};
    let entityProps;
    switch (entity) {
        case 'device':
            entityProps = deviceProps;
            break;
        case 'customer':
            entityProps = customerProps;
            break;
        case 'bill':
            entityProps = billProps;
            break;
    }
    for (let i = 0; i < entityProps.length; i++) {
        if (entityProps[i] === 'items') {
            jsonPost[entityProps[i]] = [];
            let billItemList = document.querySelectorAll('.bill-item');
            for (let j = 0; j < billItemList.length; ++j) {
                let billItem = {};
                for (let k = 0; k < billItemProps.length; ++k) {
                    billItem[billItemProps[k]] = billItemList[j].querySelector('.' + billItemProps[k]).value;
                }
                jsonPost[entityProps[i]][j] = billItem;
            }
        } else {
            jsonPost[entityProps[i]] = document.querySelector('#' + entityProps[i]).value;
        }
    }
    api.post(entity, jsonPost).then(response => {
        responseOnQuery.textContent = JSON.stringify(response.data, null, 2);
        if (entity === 'device') {
            initProps('color');
            initProps('type');
        }
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

function addBillItem(buttonNode) {
    let newBillItemNode = document.createElement('ul');
    newBillItemNode.innerHTML = `Item:
                <li class="props-item additional-left-indent">Device's Id:
                    <label class="form-label additional-left-indent">
                        <input class="form-input additional-left-indent deviceId" pattern="0|(^[1-9]\\d*$)" placeholder="0" type="text"
                               onchange="findEntityWithId(this, 'device')" autocomplete="off" required>
                    </label>
                </li>
                <li class="props-item additional-left-indent">Quantity:
                    <label class="form-label additional-left-indent">
                        <input class="form-input additional-left-indent quantity" 
                               placeholder="100" pattern="(^[1-9]\\d*$)" type="text" autocomplete="off" required>
                    </label>
                </li>
                <li class="props-item additional-left-indent">Price:
                    <label class="form-label additional-left-indent">
                        <input class="form-input additional-left-indent price" 
                               placeholder="490" pattern="(^[1-9]\\d*$)" type="text" autocomplete="off" required>
                    </label>
                </li>`;
    newBillItemNode.classList.add('form-label');
    newBillItemNode.classList.add('bill-item');
    buttonNode.parentNode.insertBefore(newBillItemNode, buttonNode);
}

function removeBillItem(buttonNode) {
    let billItems = buttonNode.parentNode.querySelectorAll('.bill-item');
    if (billItems.length > 1) {
        billItems[billItems.length - 1].remove();
    } else {
        alert('At least one bill item must remain on the list!');
    }
}