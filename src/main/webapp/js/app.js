var Georgina = new Marionette.Application();

Georgina.addRegions({
    mainRegion: "#main-region",
    categoryRegion: "#category-region",
    mainChartRegion: "#main-chart-region"
});

Georgina.on("initialize:after", function() {
    Georgina.MainChartApp.Show.Controller.showMainChart();
    Georgina.CategoriesApp.List.Controller.listCategories();
    Georgina.TransactionsApp.List.Controller.listTransactions();
});
