var Georgina = new Marionette.Application();

Georgina.addRegions({
    mainRegion: "#main-region",
    categoryRegion: "#category-region",
    mainChartRegion: "#main-chart-region",
    categoryListRegion: "#category-filter-region"
});

Georgina.navigate = function(route, options){
    options || (options = {});
    Backbone.history.navigate(route, options);
};

Georgina.getCurrentRoute = function(){
    return Backbone.history.fragment;
};

Georgina.on("initialize:after", function() {
    if(Backbone.history) {
        Backbone.history.start();

        if (this.getCurrentRoute() === "") {
            Georgina.trigger("transactions:list");
        }
    }
    Georgina.MainChartApp.Show.Controller.showMainChart();
    Georgina.CategoriesApp.List.Controller.listCategories();
});
