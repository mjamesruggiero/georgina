var Georgina = new Marionette.Application();

Georgina.addRegions({
    mainRegion: "#main-region",
    categoryRegion: "#category-region"
});

Georgina.on("initialize:after", function() {
    var categories = Georgina.request("category:entities");

    var categoryListView = new Georgina.CategoriesView({
        collection: categories
    });

    Georgina.categoryRegion.show(categoryListView);
    Georgina.TransactionsApp.List.Controller.listTransactions();
});
