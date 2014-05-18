var Georgina = new Marionette.Application();

Georgina.addRegions({
    mainRegion: "#main-region",
    categoryRegion: "#category-region"
});

Georgina.on("initialize:after", function() {
    Georgina.CategoriesApp.List.Controller.listCategories();
    Georgina.TransactionsApp.List.Controller.listTransactions();
});
