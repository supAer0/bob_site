function GroupProduct(id, name, transliterationName) {
    var self = this;
    self.name=ko.observable(name);
    self.id = id;
    self.transliterationName = transliterationName;
    self.groupRootId = groupRootId;
}
function GroupRootId(groupRootId) {
    var self = this;
    self.groupRootId=groupRootId;
}
function Attribute(id,name) {
    var self=this;
    self.id=id;
    self.name=ko.observable(name);
    self.groupRootId=groupRootId;
}
function GroupsProductViewModel() {
    var self = this;
    self.groupsProduct = ko.observableArray([]);
    self.attributes = ko.observableArray([]);
    self.id = ko.observable(null);
    self.attributeId = ko.observable(null);
    self.attributeName = ko.observable("");
    self.translitirationName = ko.observable(null);
    self.name = ko.observable("");
    var groupRoot = new GroupRootId(groupRootId);
    var jsonData = ko.toJSON(groupRoot);
    console.log(jsonData);
    self.load = function () {
        jsRoutes.controllers.Admin.groupsProductJson().ajax({
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function (data) {
                console.log("Успешно обработан json запрос. Записи загружены");
                for (var i = 0; i < data.length; i++) {
                    self.groupsProduct.push(new GroupProduct(data[i].id, data[i].name,data[i].transliterationName));
                }
            },
            error: function (data) {
                alert("error! " + data.error);
                console.log('error! Не могу отправить json запрос на получение данных');
                console.log(data);
            }
        });
        jsRoutes.controllers.Admin.attributesJson().ajax({
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function (data) {
                console.log("Успешно обработан json запрос. Записи загружены");
                for (var i = 0; i < data.length; i++) {
                    self.attributes.push(new Attribute(data[i].id, data[i].name));
                }
            },
            error: function (data) {
                alert("error! " + data.error);
                console.log('error! Не могу отправить json запрос на получение данных');
                console.log(data);
            }
        });
    };

    self.reload = function () {
        self.groupsProduct.destroyAll();
        self.attributes.destroyAll();
        self.load();
    };

    self.cleanForm = function () {
        self.name("");
        self.attributeName("");
    };

    self.editGroupProduct = function (groupProduct) {
        console.log(groupProduct);
        self.id(groupProduct.id);
        self.name(groupProduct.name());
    };

    self.editAttribute = function (attribute) {
        console.log(attribute);
        self.attributeId(attribute.id);
        self.attributeName(attribute.name());
    };

    self.removeGroupProduct = function (groupProduct) {
        var jsonData = ko.toJSON(groupProduct);
        console.log(jsonData);
        jsRoutes.controllers.Admin.deleteGroupProductJson().ajax({
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function () {
                console.log("Успешно обработан ajax запрос. Запись удалена");
                self.groupsProduct.remove(groupProduct);
            },
            error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });
    };

    self.removeAttribute = function (attribute) {
        var jsonData = ko.toJSON(attribute);
        console.log(jsonData);
        jsRoutes.controllers.Admin.deleteAttributeJson().ajax({
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function () {
                console.log("Успешно обработан ajax запрос. Запись удалена");
                self.attributes.remove(attribute);
            },
            error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });
    };

    self.saveGroupProduct = function () {
        var groupProduct = new GroupProduct();
        groupProduct.id = self.id();
        groupProduct.name(self.name());
        var jsonData = ko.toJSON(groupProduct);
        console.log(jsonData);

        jsRoutes.controllers.Admin.saveGroupProductJson().ajax({
            dataType: 'json',   //
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function (data) {
                console.log("Успешно обработан ajax запрос. Запись добавлена в DB");
                console.log(data);
                var isNew = false;
                if (groupProduct.id == null)
                    isNew = true;
                if (isNew) { /*создание нового */
                    groupProduct.id = data.id;
                    self.groupsProduct.push(groupProduct);
                } else {
                    //todo редактирование - ищем и обновляем
                    console.log(self.groupsProduct());
                    for (var i = 0; i < self.groupsProduct().length; i++) {
                        if (groupProduct.id == self.groupsProduct()[i].id) {
                            self.groupsProduct()[i].name(data.name);
                            console.log(self.groupsProduct()[i]);
                            break;
                        }
                    }
                }
                //TODO: no reload
                self.reload();
                self.cleanForm();

            },    error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос на добавление группы с продуктами');
                console.log(data);
            }
        });
    };

    self.saveAttribute = function () {
        var attribute = new Attribute();
        attribute.id = self.attributeId();
        attribute.name(self.attributeName());
        var jsonData = ko.toJSON(attribute);
        console.log(jsonData);

        jsRoutes.controllers.Admin.saveAttributeJson().ajax({
            dataType: 'json',   //
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function (data) {
                console.log("Успешно обработан ajax запрос. Запись добавлена в DB");
                console.log(data);
                var isNew = false;
                if (attribute.id == null)
                    isNew = true;
                if (isNew) { /*создание нового */
                    attribute.id = data.id;
                    self.attributes.push(attribute);
                } else {
                    //todo редактирование - ищем и обновляем
                    console.log(self.attributes());
                    for (var i = 0; i < self.attributes().length; i++) {
                        if (attribute.id == self.attributes()[i].id) {
                            self.attributes()[i].name(data.name);
                            console.log(self.attributes()[i]);
                            break;
                        }
                    }
                }

                self.cleanForm();

            },    error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос на добавление атрибута');
                console.log(data);
            }
        });
    };

    self.load();
}
//создаем экземпляр ViewModel
var groupProductViewModel = new GroupsProductViewModel();
//Запускаем Knockout.JS. Организовываем связывание Model с View через ViewModel
ko.applyBindings(groupProductViewModel);