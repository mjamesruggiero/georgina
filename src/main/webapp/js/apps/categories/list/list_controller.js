Georgina.module("CategoriesApp.List", function(List, Georgina, Backbone, Marionette, $, _) {
    List.Controller = {
        listCategories: function() {
            var categories = Georgina.request("category:entities");
            var categoriesListView = new Georgina.CategoriesApp.List.Categories({
                collection: categories
            });
            categoriesListView.on("itemview:category:show", function(childView, model){
                Georgina.CategoriesApp.Show.Controller.showCategory(model);
            });
            Georgina.categoryRegion.show(categoriesListView);
        }
    }
});
