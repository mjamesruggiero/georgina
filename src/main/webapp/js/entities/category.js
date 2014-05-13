Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.Category = Backbone.Model.extend({});

    Entities.CategoryCollection = Backbone.Collection.extend({
        model: Entities.Category
    });

    var categories;

    var initializeCategories = function() {
        categories = new Entities.CategoryCollection(getCategories());
    };

    var getCategories = function() {
        var endpoint = "/categories";
        var data = getData(endpoint);
        return data;
    };

    var getData = function(endpoint) {
        var json = $.ajax({
            url: endpoint,
            async: false
        }).responseText;
        data = JSON.parse(json);
        return data;
    };

    var API = {
        getCategoryEntities: function() {
            if (categories === undefined) {
                initializeCategories();
            }
            return categories;
        }
    };

    Georgina.reqres.setHandler("category:entities", function() {
        return API.getCategoryEntities();
    });
});
