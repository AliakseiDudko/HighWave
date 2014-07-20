App.NewsApp.NewsList = function() {
    var NewsList = {};

    var NewsView = Marionette.ItemView.extend({
        template: Backbone.Marionette.TemplateCache.get("news-list-item-template"),
        className: "list-group-item"
    });

    var NewsListEmptyView = Marionette.ItemView.extend({
        template: Backbone.Marionette.TemplateCache.get("news-list-empty-template"),
        className: "panel-body"
    });

    var NewsListView = Marionette.CompositeView.extend({
        template: Backbone.Marionette.TemplateCache.get("news-list-template"),
        className: "",
        childView: NewsView,
        emptyView: NewsListEmptyView,

        showMessage: function(message) {
            console.log(message);
        },

        initialize: function() {
            _.bindAll(this, "showMessage");
            var self = this;
            App.vent.on("news:error", function() {
                self.showMessage("Error, please retry later :s");
            });
            App.vent.on("news:noResults", function() {
                self.showMessage("No news found")
            });
            App.vent.on("news:fetch:start", function() {
                self.showMessage("Loading news started.");
            });
            App.vent.on("news:fetch:stop", function() {
                self.showMessage("Loading news finished.");
            });
        }
    });

    NewsList.showNews = function(news) {
        var newsListView = new NewsListView({
            collection: news
        });
        App.newsRegion.show(newsListView);

        App.vent.trigger("news:load");
    };

    return NewsList;
}();