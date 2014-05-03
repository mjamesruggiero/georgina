var Georgina = new Marionette.Application();

Georgina.addRegions({
    mainRegion: "#main-region"
});

Georgina.StaticView = Marionette.ItemView.extend({
    template: "#static-template"
});

Georgina.on("initialize:after", function() {
    var staticView = new Georgina.StaticView();
    Georgina.mainRegion.show(staticView);
    console.log("Georgina has started!");
});
Georgina.start();
