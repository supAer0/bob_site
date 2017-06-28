function ProductId(productId) {
    var self = this;
    self.productId=productId;
}

function ProductValue(id,name,value) {
    var self=this;
    self.id=id;
    self.idProduct=productId;
    self.name=ko.observable(name);
    self.value = ko.observable(value);
}

function ProductValuesViewModel() {
    var self = this;
    self.productValues = ko.observableArray([]);

    self.id = ko.observable(null);
    self.name = ko.observable("");
    self.value = ko.observable("");
    console.log(productId);
    var productIdenty = new ProductId(productId);
    var jsonData = ko.toJSON(productIdenty);
    console.log(jsonData);
    self.load = function () {
        jsRoutes.controllers.Admin.productValuesJson().ajax({
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function (data) {
                console.log("Успешно обработан json запрос. Записи загружены");
                console.log(data);
                for (var i = 0; i < data.length; i++) {
                    self.productValues.push(new ProductValue(data[i].id, data[i].name, data[i].value));
                }
            },
            error: function (data) {
                alert("error! " + data.error);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });
    };

    self.reload = function () {
        self.productValues.destroyAll();
        self.load();
    };

    // self.saveProductValues = function () {
    //     var productValue = new Product();
    //     product.id = self.id();
    //     product.name(self.name());
    //     product.description(self.description());
    //     product.cost(self.cost());
    //     product.amount(self.amount());
    //     var jsonData = ko.toJSON(product);
    //     console.log(jsonData);
    //
    //     jsRoutes.controllers.Admin.saveProductJson().ajax({
    //         dataType: 'json',   //
    //         contentType: 'application/json; charset=utf-8',
    //         data: jsonData,
    //         success: function (data) {
    //             console.log("Успешно обработан ajax запрос. Запись добавлена в DB");
    //             console.log(data);
    //             var isNew = false;
    //             if (product.id == null)
    //                 isNew = true;
    //             if (isNew) { /*создание нового */
    //                 product.id = data.id;
    //                 self.products.push(product);
    //             } else {
    //                 //todo редактирование - ищем и обновляем
    //                 console.log(self.products());
    //                 for (var i = 0; i < self.products().length; i++) {
    //                     var found = false;
    //                     if (product.id == self.products()[i].id) {
    //                         self.products()[i].name(data.name);
    //                         self.products()[i].description(data.description);
    //                         self.products()[i].cost(data.cost);
    //                         self.products()[i].amount(data.amount);
    //                         console.log(self.products()[i]);
    //                         break;
    //                     }
    //                 }
    //             }
    //             self.cleanForm();
    //             console.log(product.id);
    //
    //         },    error: function (data) {
    //             alert(data.responseText);
    //             console.log('error! Не могу отправить json запрос');
    //             console.log(data);
    //         }
    //     });
    //
    // };

    self.reload();
}
//создаем экземпляр ViewModel
var productValuesViewModel = new ProductValuesViewModel();
ko.applyBindings(productValuesViewModel);
































//
//
// function ProductValue(id,idProduct,name,value) {
//     var self=this;
//     self.id=id;
//     self.idProduct=idProduct;
//     self.name=ko.observable(name);
//     self.value = ko.observable(value);
// }
//
// // function ProductValue(id,idAttribute,idProduct,value) {
// //     var self=this;
// //     self.id=id;
// //     self.idAttribute = idAttribute;
// //     self.idProduct = idProduct;
// //     self.value=ko.observable(value)
// // }
//
//
//
// self.productValueId = ko.observable(null);
// self.productValueName = ko.observable("");
// self.productValueValue = ko.observable("");
//
//
// jsRoutes.controllers.Admin.productValuesJson().ajax({
//     dataType: 'json',
//     contentType: 'application/json; charset=utf-8',
//     data: jsonData,
//     success: function (data) {
//         console.log("Успешно обработан json запрос. Записи загружены");
//         for (var i = 0; i < data.length; i++) {
//             self.productValues.push(new ProductValue(data[i].id, data[i].idProduct, data[i].name, data[i].value==null ? "" : data[i].value));
//         }
//     },
//     error: function (data) {
//         alert("error! " + data.error);
//         console.log('error! Не могу отправить json запрос на получение данных');
//         console.log(data);
//     }
// });
//
//
//
//
//
// var productValue = new ProductValue();
// productValue.id = self.productValueId();
// product.idProduct = self.id();
// product.name(self.productValueName());
// product.value(self.productValueValue());
// var jsonData = ko.toJSON(productValue);
// console.log(productValue);
// jsRoutes.controllers.Admin.saveProductValueJson().ajax({
//     dataType: 'json',   //
//     contentType: 'application/json; charset=utf-8',
//     data: jsonData,
//     success: function (data) {
//         console.log("Успешно обработан ajax запрос. Запись добавлена в DB");
//         console.log(data);
//         var isNew = false;
//         if (productValue.id == null)
//             isNew = true;
//         if (isNew) { /*создание нового */
//             productValue.id = data.id;
//             self.productValues.push(product);
//         } else {
//             //todo редактирование - ищем и обновляем
//             console.log(self.productValues());
//             for (var i = 0; i < self.productValues().length; i++) {
//                 if (productValue.id == self.productValues()[i].id) {
//                     self.productValues()[i].name(data.name);
//                     self.productValues()[i].value(data.value);
//                     console.log(self.productValues()[i]);
//                     break;
//                 }
//             }
//         }
//         self.cleanForm();
//         console.log(productValue.id);
//
//     },    error: function (data) {
//         alert(data.responseText);
//         console.log('error! Не могу отправить json запрос');
//         console.log(data);
//     }
// });