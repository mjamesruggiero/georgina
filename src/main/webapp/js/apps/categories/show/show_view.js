Georgina.module("CategoriesApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Category = Marionette.ItemView.extend({
        template: "#summary-view"
    });
});

