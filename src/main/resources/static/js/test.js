// Комментарий на русском

function foo() {
    axios.get('/shop-api/some-entity')
        .then(function (response) {
            alert(response.data[0].value);
        })
        .catch(function (error) {
            console.log(error);
        });
}