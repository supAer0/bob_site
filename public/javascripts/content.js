function Product(id, name, description, cost, amount, group) {
    var self = this;
    self.name = ko.observable(name);
    self.description = ko.observable(description);
    self.cost = ko.observable(cost);
    self.amount = ko.observable(amount);
    self.group = ko.observable(group);
    self.id = id;
}
function Group(id, name) {
    var self = this;
    self.name = ko.observable(name);
    self.id = id;
}
function ProductsViewModel() {
    var self = this;
    self.products = ko.observableArray([]);
    self.groups = ko.observableArray([]);
    self.groupValue = ko.observableArray([]);

    self.id = ko.observable(null);
    self.name = ko.observable("");
    self.description = ko.observable("");
    self.amount = ko.observable(0);
    self.cost = ko.observable(0);
    self.load = function () {
        jsRoutes.controllers.Admin.productsJson().ajax({
            dataType: 'json',
            success: function (data) {
                console.log("Успешно обработан json запрос. Записи загружены");
                console.log(data);
                for (var i = 0; i < data.length; i++) {
                    self.products.push(new Product(data[i].id, data[i].name, data[i].description, data[i].cost, data[i].amount, data[i].group==null? "" : data[i].group.id));
                }

                console.log(self.products());
            },
            error: function (data) {
                alert("error! " + data.error);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });
        jsRoutes.controllers.Admin.groupsJson().ajax({
            dataType: 'json',
            success: function (data) {
                console.log("Успешно обработан json запрос. Записи загружены");
                for (i = 0; i < data.length; i++) {
                    self.groups.push(new Group(data[i].id, data[i].name));
                }
                $(".chosen-select").trigger("chosen:updated");
            },
            error: function (data) {
                alert("error! " + data.error);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });
    };

    self.reload = function () {
        self.products.removeAll();
        self.load();
    };
    self.cleanForm = function () {
        self.name("");
        self.description("");
        self.cost(0);
        self.amount(0);
        self.id(null);
        console.log(self.groupValue());
        self.groupValue.removeAll();
        $('.chosen-select option:selected').each(function(){this.selected=false;});
        $(".chosen-select").trigger("chosen:updated");
    };
    self.editProduct = function (product) {
        self.cleanForm();
        $('#createOrViewProduct').modal();
        console.log(product);
        self.id(product.id);
        self.name(product.name());
        self.description(product.description());
        self.amount(product.amount());
        self.cost(product.cost());
        console.log("keks");
        console.log(self.groupValue());
        self.groupValue.push(product.group());
        $(".chosen-select").trigger("chosen:updated");
    };
    self.removeProduct = function (product) {
        var jsonData = ko.toJSON(product);
        console.log(jsonData);
        jsRoutes.controllers.Admin.deleteProductJson().ajax({
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function () {
                console.log("Успешно обработан ajax запрос. Запись удалена");
                self.products.remove(product);
            },
            error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });


    };
    self.saveProduct = function () {
        var product = new Product();

        console.log("hellobithes");
        console.log(self.groupValue()[0]);
        product.id = self.id();
        product.name(self.name());
        product.description(self.description());
        product.cost(self.cost());
        product.amount(self.amount());
        product.group(self.groupValue()[0]);
        var jsonData = ko.toJSON(product);
        console.log(jsonData);

        jsRoutes.controllers.Admin.saveProductJson().ajax({
            dataType: 'json',   //
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function (data) {
                console.log("Успешно обработан ajax запрос. Запись добавлена в DB");
                console.log(data);

                if (product.id == null){
                    var isNew = true;}
                else
                {
                    var isNew = false;
                }


                if (isNew) { /*создание нового */
                    product.id = data.id;
                    self.products.push(product);
                } else {
                    //todo редактирование - ищем и обновляем
                    console.log(self.products());
                    for (i = 0; i < self.products().length; i++) {
                        var found = false;
                        if (product.id == self.products()[i].id) {
                            self.products()[i].name(data.name);
                            self.products()[i].description(data.description);
                            self.products()[i].cost(data.cost);
                            self.products()[i].amount(data.amount);


                            console.log(self.products()[i]);
                            break;
                        }
                    }
                }

                self.cleanForm();

                if ($('#checkClose').prop("checked")){
                    $('#createOrViewProduct').modal("hide");
                }

            },    error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });
    };

    self.reload();
}
//создаем экземпляр ViewModel
var productViewModel = new ProductsViewModel();
ko.applyBindings(productViewModel);
