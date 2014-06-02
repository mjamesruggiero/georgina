Georgina.module("CategoriesApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Controller = {
        showCategory: function(model) {
            var categoryView = new Show.Category({
                model: model
            });
            Georgina.mainRegion.show(categoryView);
        }
    };
});

