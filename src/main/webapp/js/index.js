Georgina.CategoryItemView = Marionette.ItemView.extend({
    tagName: "li",
    template: "#category-view"
});

Georgina.CategoriesView = Marionette.CollectionView.extend({
    tagName: "ul",
    itemView: Georgina.CategoryItemView
});

Georgina.start();
