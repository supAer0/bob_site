function Group(id, name, transliterationName) {
    var self = this;
    self.name=ko.observable(name);
    self.id = id;
    self.transliterationName = transliterationName;
}

function GroupsViewModel() {
    var self = this;

    self.groups = ko.observableArray([]);

    self.id = ko.observable(null);
    self.transliterationName = ko.observable(null);
    self.name = ko.observable("");

    self.load = function () {
        jsRoutes.controllers.Admin.groupsJson().ajax({
            dataType: 'json',
            success: function (data) {
                console.log("Успешно обработан json запрос. Записи загружены");
                for (var i = 0; i < data.length; i++) {
                    self.groups.push(new Group(data[i].id, data[i].name,data[i].transliterationName));
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
        self.groups.destroyAll();
        self.load();
    };
    self.cleanForm = function () {
        self.name("");
    };
    self.editGroup = function (group) {
        $('#createOrViewGroup').modal();
        console.log(group);
        self.id(group.id);
        self.name(group.name());
    };
    self.removeGroup = function (group) {
        var jsonData = ko.toJSON(group);
        console.log(jsonData);
        jsRoutes.controllers.Admin.deleteGroupJson().ajax({
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function () {
                console.log("Успешно обработан ajax запрос. Запись удалена");
                self.groups.remove(group);
            },
            error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });


    };
    self.saveGroup = function () {
        var group = new Group();
        group.id = self.id();
        group.name(self.name());
        var jsonData = ko.toJSON(group);
        console.log(jsonData);

        jsRoutes.controllers.Admin.saveGroupJson().ajax({
            dataType: 'json',   //
            contentType: 'application/json; charset=utf-8',
            data: jsonData,
            success: function (data) {
                console.log("Успешно обработан ajax запрос. Запись добавлена в DB");
                console.log(data);
                var isNew = false;
                if (group.id == null)
                    var isNew = true;
                if (isNew) { /*создание нового */
                    group.id = data.id;
                    self.groups.push(group);
                } else {
                    //todo редактирование - ищем и обновляем
                    console.log(self.groups());
                    for (i = 0; i < self.groups().length; i++) {
                        var found = false;
                        if (group.id == self.groups()[i].id) {
                            self.groups()[i].name(data.name);

                            console.log(self.groups()[i]);
                            break;
                        }
                    }
                    $('#createOrViewGroup').modal("hide");
                }

                self.cleanForm();
                // TODO: no reload
                self.reload();

                if ($('#checkClose').prop("checked")){
                    $('#createOrViewGroup').modal("hide");
                }

            },    error: function (data) {
                alert(data.responseText);
                console.log('error! Не могу отправить json запрос');
                console.log(data);
            }
        });
    };

    self.load();
}
//создаем экземпляр ViewModel
var groupViewModel = new GroupsViewModel();
//Запускаем Knockout.JS. Организовываем связывание Model с View через ViewModel
ko.applyBindings(groupViewModel);